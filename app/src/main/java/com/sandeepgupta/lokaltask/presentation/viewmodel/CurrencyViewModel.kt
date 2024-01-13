package com.sandeepgupta.lokaltask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandeepgupta.lokaltask.domain.model.CombinedCurrencyList
import com.sandeepgupta.lokaltask.domain.Repository
import com.sandeepgupta.lokaltask.util.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _currencyList = MutableStateFlow<ApiState<CombinedCurrencyList>>(ApiState.Loading())
    val currencyList: StateFlow<ApiState<CombinedCurrencyList>>
        get() = _currencyList

    private var refreshJob: Job? = null // Job for refreshing the currency List

    init {
        getCurrencyList()
        startPeriodicRefresh()
    }

    // To fetch the currency list from the server
    private fun getCurrencyList() = viewModelScope.launch {
        repository.getCombineCurrencyList().collect {
            _currencyList.value = it
        }
    }

    // To refresh the currency List
    fun refreshCurrencyList() = getCurrencyList()

    // To refresh the currency list in every 3 min
    private fun startPeriodicRefresh() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            while (isActive) {
                delay(3 * 60 * 1000L) // Delay for 3 minutes
                refreshCurrencyList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        refreshJob?.cancel() // If viewModel is cleared then cancel the current job
    }
}
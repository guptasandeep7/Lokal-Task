package com.sandeepgupta.lokaltask.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sandeepgupta.lokaltask.domain.model.CombinedCurrencyList
import com.sandeepgupta.lokaltask.presentation.ui.components.CurrencyItem
import com.sandeepgupta.lokaltask.presentation.ui.components.ShimmerCard
import com.sandeepgupta.lokaltask.presentation.viewmodel.CurrencyViewModel
import com.sandeepgupta.lokaltask.util.ApiState
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun HomeScreen(
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {

    val currencyList = currencyViewModel.currencyList.collectAsState()

    when (currencyList.value) {

        is ApiState.Loading -> {
            ShimmerLoadingUI()
        }

        is ApiState.Success -> {
            currencyList.value.data?.let {
                CurrencyListUI(it, currencyViewModel)
            } ?: run {
                currencyViewModel.refreshCurrencyList()
            }
        }

        is ApiState.Error -> {
            ErrorDialog(currencyList, currencyViewModel)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun CurrencyListUI(
    currencyList: CombinedCurrencyList,
    currencyViewModel: CurrencyViewModel
) {

    val isRefreshing by remember {
        mutableStateOf(false)
    }

    val pullRefreshState =
        rememberPullRefreshState(
            isRefreshing,
            { currencyViewModel.refreshCurrencyList() }
        )

    Box(
        Modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ) {

        Column(Modifier.fillMaxSize()) {

            val timestamp = currencyList.timestamp
            val date = Date(timestamp)
            val formattedDate = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date)

            //Last Update Date and Time
            Text(
                text = "Last Update: $formattedDate",
                Modifier.padding(16.dp)
            )

            //Currency List
            LazyColumn(
                modifier = Modifier.pullRefresh(state = pullRefreshState),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = currencyList.currencyDetailsList,
                    key = { it.symbol }) { currency ->
                    CurrencyItem(currency, currencyList.target)
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}


@Composable
private fun ErrorDialog(
    currencyList: State<ApiState<CombinedCurrencyList>>,
    currencyViewModel: CurrencyViewModel
) {
    var showDialog by remember { mutableStateOf(true) }

    Box(Modifier.fillMaxSize()) {
        if (showDialog) {
            AlertDialog(
                shape = RoundedCornerShape(20.dp),
                onDismissRequest = {},//Do not close the dialog box on clicking outside
                text = {
                    Text(currencyList.value.errorMsg.toString())
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        currencyViewModel.refreshCurrencyList()
                    }) {
                        Text("Retry")
                    }
                },
            )
        }
    }

}

@Composable
private fun ShimmerLoadingUI() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(count = 20) {
            ShimmerCard()
        }
    }
}




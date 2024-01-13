package com.sandeepgupta.lokaltask.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sandeepgupta.lokaltask.domain.model.CurrencyDetails

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CurrencyItem(currencyDetails: CurrencyDetails, target: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        AsyncImage(
            model = currencyDetails.icon_url,
            contentDescription = "${currencyDetails.name} icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = currencyDetails.name_full,
                Modifier.basicMarquee(),
                maxLines = 1
            )

            Text(
                text = currencyDetails.max_supply,
                maxLines = 1
            )
        }

        Text(
            text = currencyDetails.rate + " " + target,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
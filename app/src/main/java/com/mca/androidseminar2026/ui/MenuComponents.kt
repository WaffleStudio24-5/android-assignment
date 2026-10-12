package com.mca.androidseminar2026.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.data.MenuItem
import java.text.NumberFormat
import java.util.Locale

/** 제공 코드: 메뉴와 장바구니가 공유하는 과제 2 UI입니다. */
@Composable
fun MenuItemRow(
    menu: MenuItem,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) {
        Row(verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(menu.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.size(8.dp))
                Text(
                    menu.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.size(12.dp))
                Text("${NumberFormat.getNumberInstance(Locale.KOREA).format(menu.price)}원", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(16.dp))
            Image(
                painter = painterResource(menu.imageResId),
                contentDescription = menu.name,
                modifier = Modifier.size(88.dp).aspectRatio(1f).clip(RoundedCornerShape(16.dp)),
            )
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            QuantitySelector(quantity, onIncrease, onDecrease)
        }
    }
}

@Composable
fun QuantitySelector(quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    if (quantity == 0) {
        Button(onClick = onIncrease, modifier = Modifier.width(180.dp).height(48.dp)) { Text("담기") }
    } else {
        Row(
            modifier = Modifier.width(180.dp).height(48.dp)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onDecrease) { Text("−", style = MaterialTheme.typography.titleLarge) }
            Text("${quantity}개")
            IconButton(onClick = onIncrease) { Text("+", style = MaterialTheme.typography.titleLarge) }
        }
    }
}

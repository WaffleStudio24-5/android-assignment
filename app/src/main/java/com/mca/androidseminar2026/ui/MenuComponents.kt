package com.mca.androidseminar2026.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.data.MenuItem

// 메뉴·장바구니에서 같은 함수를 호출하세요. ViewModel 구조는 직접 설계하세요.
// 상태와 콜백을 받는 UI의 예시는 CounterExample.kt를 참고하세요.
@Composable
fun MenuItemRow(
    menu: MenuItem,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    Column(Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(menu.name, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(
                    menu.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(8.dp))
                Text("${menu.price}원", style = MaterialTheme.typography.titleSmall)
            }
            Spacer(Modifier.width(16.dp))
            Image(
                painter = painterResource(menu.imageResId),
                contentDescription = menu.name,
                modifier = Modifier.size(96.dp).clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
            )
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            QuantitySelector(quantity, onIncrease, onDecrease)
        }
        HorizontalDivider(Modifier.padding(top = 12.dp))
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    if (quantity == 0) {
        Button(onClick = onIncrease, modifier = Modifier.width(132.dp)) { Text("담기") }
    } else {
        Row(
            modifier = Modifier.width(132.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(onClick = onDecrease, modifier = Modifier.size(40.dp)) { Text("−") }
            Text("${quantity}개")
            Button(onClick = onIncrease, modifier = Modifier.size(40.dp)) { Text("+") }
        }
    }
}

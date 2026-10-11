package com.mca.androidseminar2026.ui

import androidx.compose.runtime.Composable
import com.mca.androidseminar2026.data.MenuItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow

// 메뉴·장바구니에서 같은 함수를 호출하세요. ViewModel 구조는 직접 설계하세요.
// 상태와 콜백을 받는 UI의 예시는 CounterExample.kt를 참고하세요.
@Composable
fun MenuItemRow(
    // 예시 파라미터로, 수정이나 추가하셔도 됩니다.
    menu: MenuItem,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    // TODO: 왼쪽에 이름·설명·가격, 오른쪽에 둥근 정사각형 이미지를 배치하세요.
    // 설명은 두 줄 이후 말줄임 처리하고 항목 사이에 여백과 구분선을 두세요.
    // 아래쪽 오른편에 QuantitySelector를 배치하세요. 배치는 주어진 예시 이미지를 참고하세요.
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(menu.name)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = menu.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("${menu.price}원")
            }

            Image(
                painter = painterResource(id = menu.imageResId),
                contentDescription = menu.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(108.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            QuantitySelector(
                quantity = quantity,
                onIncrease = onIncrease,
                onDecrease = onDecrease
            )
        }
    }

}

@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    // TODO: 0이면 담기 버튼, 1 이상이면 "−  n개  +" 조작부를 표시하세요.
    // 두 상태의 영역 크기를 유지하고, 이벤트는 전달받은 콜백으로 처리하세요.
    if (quantity == 0) {
        Button(
            onClick = onIncrease,
            modifier = Modifier.width(180.dp)
        ) {
            Text("담기")
        }
    } else {
        Row(
            modifier = Modifier.width(180.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onDecrease) {
                Text("−")
            }

            Text("${quantity}개")

            TextButton(onClick = onIncrease) {
                Text("+")
            }
        }
    }
}

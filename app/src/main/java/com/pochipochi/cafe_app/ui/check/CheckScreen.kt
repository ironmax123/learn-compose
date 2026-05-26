package com.pochipochi.cafe_app.ui.check

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pochipochi.cafe_app.data.model.ProductsModel
import com.pochipochi.cafe_app.ui.check.components.WalletPaymentBottomSheet
import com.pochipochi.cafe_app.ui.check.components.WalletPaymentDialog

@Composable
fun CheckScreen(
    onBack: () -> Unit,
    cartItem: List<ProductsModel>,
    amount: Int,
    onNavigateToShops: () -> Unit,
    shopName: String
) {
    var showWalletSheet by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showPaymentDialog) {
        WalletPaymentDialog(
            onDismiss = {
                showPaymentDialog = false
                Toast.makeText(context, "支払いが完了しました", Toast.LENGTH_SHORT).show()
                onNavigateToShops()
            }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("会計をキャンセルする")
        }
        Text(
            text = shopName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "合計金額  ¥$amount",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(cartItem.size) { index ->
                Text(
                    text = "${cartItem[index].name} | ¥${cartItem[index].price}",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        HorizontalDivider(thickness = 1.dp)
        Button(
            onClick = { showWalletSheet = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ウォレットで支払う")
        }
    }

    if (showWalletSheet) {
        WalletPaymentBottomSheet(
            amount = amount,
            onDismissRequest = { showWalletSheet = false },
            onPayClick = {
                showWalletSheet = false
                showPaymentDialog = true
            }
        )
    }
}

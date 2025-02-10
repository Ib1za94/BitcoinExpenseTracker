package com.example.bitcoinexpensetracker.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontFamily

@Composable
fun MainScreen() {
    var balance by remember { mutableDoubleStateOf(0.0) }
    var showDialog by remember { mutableStateOf(false) }
    val transactions = remember { mutableStateListOf<Transaction>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0))
            .padding(top = 55.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )
            Text(
                text = "BTC/USD: 43,500",
                fontSize = 15.sp,
                color = Color.Black,
                fontFamily = FontFamily.Serif,
                modifier = Modifier
                    .background(Color(0xFFFFF3E0))
                    .padding(horizontal = 8.dp)
            )
        }
        BalanceCard(balance, onAddBalance = { showDialog = true })
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFFFA726), Color(0xFFFFCC80))
                    ),
                    shape = RoundedCornerShape(24.dp)
                ),
            shape = RoundedCornerShape(24.dp),
            elevation = ButtonDefaults.elevation(0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color.White
            )
        ) {
            Text("Add transaction", color = Color.White, fontSize = 20.sp, fontFamily = FontFamily.SansSerif)
        }
        TransactionList(transactions)
    }

    if (showDialog) {
        AddTransactionDialog(onDismiss = { showDialog = false }) { amount ->
            balance += amount
            transactions.add(Transaction(amount, "Deposit"))
        }
    }
}

@Composable
fun BalanceCard(balance: Double, onAddBalance: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFFFA726), Color(0xFFFFCC80))
                ), RoundedCornerShape(44.dp)
            )
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "Bitcoin",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontFamily = FontFamily.SansSerif
                )

                Text(
                    text = "$balance BTC",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            }

            Spacer(modifier = Modifier.height(76.dp))

            FloatingActionButton(
                onClick = onAddBalance,
                backgroundColor = Color.White,
                contentColor = Color(0xFFFFA726),
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.End)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_input_add),
                    contentDescription = "Add Funds"
                )
            }
        }
    }
}

@Composable
fun TransactionList(transactions: List<Transaction>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(transactions) { transaction ->
            TransactionItem(transaction)
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(text = "${transaction.amount} BTC - ${transaction.category}")
        }
    }
}

@Composable
fun AddTransactionDialog(onDismiss: () -> Unit, onAdd: (Double) -> Unit) {
    var amount by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .shadow(8.dp, RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFFFA726), Color(0xFFFFCC80))
                    ), RoundedCornerShape(24.dp)
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add Funds",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        cursorColor = Color(0xFFFFA726),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(24.dp)) {
                        Text("Cancel", color = Color(0xFFFFA726), fontFamily = FontFamily.SansSerif)
                    }
                    Button(onClick = {
                        amount.toDoubleOrNull()?.let {
                            onAdd(it)
                            onDismiss()
                        }
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(24.dp)) {
                        Text("Confirm", color = Color(0xFFFFA726), fontFamily = FontFamily.SansSerif)
                    }
                }
            }
        }
    }
}

data class Transaction(val amount: Double, val category: String)

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen()
}

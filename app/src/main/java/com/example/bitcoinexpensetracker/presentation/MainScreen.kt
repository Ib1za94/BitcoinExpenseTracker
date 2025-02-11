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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.bitcoinexpensetracker.data.model.TransactionEntity
import com.example.bitcoinexpensetracker.presentation.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainScreen(navController: NavController, transactionViewModel: TransactionViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val transactions by transactionViewModel.transactions.collectAsState()
    val bitcoinPrice by transactionViewModel.bitcoinPrice.collectAsState()
    val balance by transactionViewModel.balance.collectAsState()

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
                text = bitcoinPrice?.let { "BTC/USD: $it" } ?: "Loading...",
                fontSize = 15.sp,
                color = Color.Black,
                fontFamily = FontFamily.Serif,
                modifier = Modifier
                    .background(Color(0xFFFFF3E0))
                    .padding(horizontal = 8.dp)
            )
        }

        BalanceCard(balance = balance, onAddBalance = { showDialog = true })
        Button(
            onClick = { navController.navigate("second_screen") },
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
        TransactionList(
            transactions = transactions,
            onScrollToEnd = { transactionViewModel.loadMoreTransactions() }
        )
    }

    if (showDialog) {
        AddTransactionDialog(transactionViewModel, onDismiss = { showDialog = false })
    }
}

@Composable
fun BalanceCard(balance: Double, onAddBalance: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(30.dp))
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
                    fontWeight = FontWeight.Bold,
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
fun TransactionList(
    transactions: List<TransactionEntity>,
    onScrollToEnd: () -> Unit
) {
    val groupedTransactions = transactions.groupBy {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it.timestamp))
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        groupedTransactions.forEach { (date, transactions) ->
            item {
                Text(
                    text = date,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp),
                    fontFamily = FontFamily.SansSerif
                )
            }
            items(transactions) { transaction ->
                TransactionItem(transaction)
            }
        }
        item {
            LaunchedEffect(Unit) {
                onScrollToEnd()
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = transaction.category, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(transaction.timestamp)),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily.SansSerif
                )
            }
            Text(text = "${transaction.amount} BTC", fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
        }
    }
}

@Composable
fun AddTransactionDialog(
    transactionViewModel: TransactionViewModel,
    onDismiss: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Deposit") }

    val isAmountValid = amount.toDoubleOrNull() != null && amount.toDouble() > 0

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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        cursorColor = Color(0xFFFFA726),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    label = { Text("Enter Amount") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (!isAmountValid && amount.isNotEmpty()) {
                    Text(
                        text = "Invalid amount, please enter a positive number",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Cancel", color = Color(0xFFFFA726), fontFamily = FontFamily.SansSerif)
                    }
                    Button(
                        onClick = {
                            if (isAmountValid) {
                                amount.toDoubleOrNull()?.let {
                                    val transaction = TransactionEntity(amount = it, category = category)
                                    transactionViewModel.addTransaction(transaction)
                                    onDismiss()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Confirm", color = Color(0xFFFFA726), fontFamily = FontFamily.SansSerif)
                    }
                }
            }
        }
    }
}
package com.example.bitcoinexpensetracker.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bitcoinexpensetracker.data.model.TransactionEntity
import com.example.bitcoinexpensetracker.presentation.viewmodel.TransactionViewModel

@Composable
fun SecondScreen(navController: NavController, transactionViewModel: TransactionViewModel) {
    var amount by remember { mutableStateOf("") }
    val categories = listOf("Groceries", "Taxi", "Electronics", "Restaurant", "Other")
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionLine("Enter Amount")

        BasicTextField(
            value = amount,
            onValueChange = { amount = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp)
        )

        SectionLine("Select Category")

        Column(modifier = Modifier.padding(top = 16.dp)) {
            categories.chunked(3).forEach { rowCategories ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    rowCategories.forEach { category ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        if (selectedCategory == category)
                                            listOf(Color.LightGray, Color.Gray)
                                        else listOf(Color(0xFFFFA726), Color(0xFFFFCC80))
                                    ),
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .clickable { selectedCategory = category }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(category, color = Color.White, fontSize = 17.sp, fontFamily = FontFamily.Serif)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val transactionAmount = -(amount.toDoubleOrNull() ?: 0.0)
                val transaction = TransactionEntity(
                    amount = transactionAmount,
                    category = selectedCategory,
                    timestamp = System.currentTimeMillis()
                )
                transactionViewModel.addTransaction(transaction)
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFFFA726), Color(0xFFFFCC80))
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("Add", fontSize = 20.sp, fontFamily = FontFamily.Serif, color = Color.White)
            }
        }
    }
}

@Composable
fun SectionLine(title: String) {
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
            text = title,
            fontSize = 20.sp,
            color = Color.Black,
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .background(Color(0xFFFFF3E0))
                .padding(horizontal = 8.dp)
        )
    }
}

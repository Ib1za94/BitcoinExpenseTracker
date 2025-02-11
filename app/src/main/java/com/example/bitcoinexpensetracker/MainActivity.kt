package com.example.bitcoinexpensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bitcoinexpensetracker.data.AppDatabase
import com.example.bitcoinexpensetracker.data.repository.TransactionRepository
import com.example.bitcoinexpensetracker.presentation.MainScreen
import com.example.bitcoinexpensetracker.presentation.SecondScreen
import com.example.bitcoinexpensetracker.presentation.viewmodel.TransactionViewModel
import com.example.bitcoinexpensetracker.presentation.viewmodel.TransactionViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val transactionDao = database.transactionDao()
        val transactionRepository = TransactionRepository(transactionDao)

        val transactionViewModel: TransactionViewModel by viewModels {
            TransactionViewModelFactory(transactionRepository)
        }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "main_screen") {
                composable("main_screen") {
                    MainScreen(navController, transactionViewModel)
                }
                composable("second_screen") {
                    SecondScreen(navController, transactionViewModel)
                }
            }
        }
    }
}

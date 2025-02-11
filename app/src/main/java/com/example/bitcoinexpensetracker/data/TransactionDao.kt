package com.example.bitcoinexpensetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bitcoinexpensetracker.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Locale

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("""
        SELECT * FROM transactions 
        ORDER BY timestamp DESC
    """)
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions 
        ORDER BY timestamp DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getTransactionsPaginated(limit: Int, offset: Int): List<TransactionEntity>
}

fun TransactionEntity.getFormattedDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(timestamp)
}
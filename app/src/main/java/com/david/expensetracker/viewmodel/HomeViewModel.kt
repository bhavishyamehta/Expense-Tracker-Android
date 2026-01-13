package com.david.expensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.david.expensetracker.R
import com.david.expensetracker.Utils
import com.david.expensetracker.data.ExpenseDataBase
import com.david.expensetracker.data.dao.ExpenseDao
import com.david.expensetracker.data.model.ExpenseEntity

class HomeViewModel(val dao: ExpenseDao) : ViewModel() {
    val expenses = dao.getAllExpense()

    //for delete expenses/income call this function im HomeScreen.kt screen
    suspend fun deleteExpense(expenseEntity: ExpenseEntity) : Boolean {
        return try {
            dao.deleteExpense(expenseEntity)
            true
        } catch (ex: Throwable){
            false
        }
    }

    fun getBalance(list: List<ExpenseEntity>): String {
        var balance = 0.0
        for (expense in list) {
            if (expense.type == "Income") {
                balance += expense.amount
            } else {
                balance -= expense.amount
            }
        }
        return "$ ${Utils.formatToDecimalValue(balance)}"
    }

    fun getTotalExpense(list: List<ExpenseEntity>): String {
        var total = 0.0
        for (expense in list) {
            if (expense.type == "Expense") {
                total += expense.amount
            }
        }
        return "$ ${Utils.formatToDecimalValue(total)}"
    }

    fun getTotalIncome(list: List<ExpenseEntity>): String {
        var totalIncome = 0.0
        for (expense in list) {
            if (expense.type == "Income") {
                totalIncome += expense.amount
            }
        }
        return "$ ${Utils.formatToDecimalValue(totalIncome)}"
    }

    fun getItemIcon(item: ExpenseEntity): Int {
        return when (item.category) {
            "Paypal" -> {
                R.drawable.ic_paypal
            }

            "Netflix" -> {
                R.drawable.ic_netflix2
            }

            "Starbucks" -> {
                R.drawable.ic_starbucks
            }

            else -> R.drawable.ic_upwork
        }
    }

}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.david.expensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.david.expensetracker.data.dao.ExpenseDao
import com.david.expensetracker.data.model.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDataBase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        const val DATABASE_NAME = "expense_database"

        @JvmStatic
        fun getDatabase(context: Context): ExpenseDataBase {
            return Room.databaseBuilder(
                context,
                ExpenseDataBase::class.java,
                DATABASE_NAME
            ).addCallback(object  : Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    initBasicData(context)
                }

                fun initBasicData(context: Context){
                   CoroutineScope(Dispatchers.IO).launch {
                        val dao = getDatabase(context).expenseDao()
                        dao.insertExpense(ExpenseEntity(1, "Salary", 5000.40, "2021-08-01", "Salary", type = "Income"))
                        dao.insertExpense(ExpenseEntity(2, "Paypal", 2000.30,  "2021-08-01", "Paypal", type = "Income"))
                        dao.insertExpense(ExpenseEntity(3, "Netflix", 100.00,  "2021-08-01", "Netflix", type = "Expense"))
                        dao.insertExpense(ExpenseEntity(4, "Starbucks", 400.50,  "2021-08-01", "Starbucks", type = "Expense"))
                   }
                }

            }).build()

        }
    }
}
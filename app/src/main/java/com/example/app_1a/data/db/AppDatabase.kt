package com.example.app_1a.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.app_1a.data.db.dao.ProductDao
import com.example.app_1a.data.db.dao.UserDao
import com.example.app_1a.data.db.entity.Product
import com.example.app_1a.data.db.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "level_up_gamer_database"
                )
                    .addCallback(AppDatabaseCallback(CoroutineScope(Dispatchers.IO)))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        // Insertar usuarios predefinidos
                        val userDao = database.userDao()
                        userDao.insertUser(User(email = "admin@gmail.com", pass = "admin123", birthDate = "01-01-2000", role = "admin"))
                        userDao.insertUser(User(email = "user@gmail.com", pass = "123456", birthDate = "01-01-2001", role = "user"))

                        // CORREGIDO: Se vuelven a insertar los productos iniciales del caso.
                        // Ahora se mostrarán tanto estos productos como los que agregue el admin.
                        val productDao = database.productDao()
                        productDao.insertProduct(Product(name = "Catan", price = 29990.0))
                        productDao.insertProduct(Product(name = "Controlador Xbox Series X", price = 59990.0))
                        productDao.insertProduct(Product(name = "PlayStation 5", price = 549990.0))
                        productDao.insertProduct(Product(name = "Silla Gamer Secretlab Titan", price = 349990.0))
                        productDao.insertProduct(Product(name = "Mouse Gamer Logitech G502", price = 49990.0))
                    }
                }
            }
        }
    }
}


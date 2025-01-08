package com.example.garage

import androidx.room.*

@Entity(tableName = "auth_tokens")
data class AuthToken(
    @PrimaryKey val id: Int = 0,
    val token: String
)

@Dao
interface TokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(authToken: AuthToken)

    @Query("SELECT * FROM auth_tokens LIMIT 1")
    suspend fun getToken(): AuthToken?
}

@Database(entities = [AuthToken::class], version = 1)
abstract class TokenDatabase : RoomDatabase() {
    abstract fun tokenDao(): TokenDao

    companion object {
        @Volatile
        private var INSTANCE: TokenDatabase? = null

        fun getInstance(context: android.content.Context): TokenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TokenDatabase::class.java,
                    "token_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

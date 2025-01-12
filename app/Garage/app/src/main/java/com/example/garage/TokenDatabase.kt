package com.example.garage

import androidx.room.*

/**
 * Entity representing the authentication token stored in the local Room database.
 *
 * @param id Primary key (default value of 0 since only one token is stored).
 * @param token The authentication token string.
 */
@Entity(tableName = "auth_tokens")
data class AuthToken(
    @PrimaryKey val id: Int = 0,
    val token: String
)

/**
 * Data Access Object (DAO) interface for interacting with the `auth_tokens` table.
 */
@Dao
interface TokenDao {

    /**
     * Inserts a new authentication token into the database.
     * If a token already exists, it is replaced.
     *
     * @param authToken The `AuthToken` object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(authToken: AuthToken)

    /**
     * Fetches the stored authentication token.
     * Since only one token is stored, this query retrieves a single record.
     *
     * @return The `AuthToken` object or `null` if no token is stored.
     */
    @Query("SELECT * FROM auth_tokens LIMIT 1")
    suspend fun getToken(): AuthToken?

    /**
     * Deletes all tokens from the database.
     * Typically used for logging out the user or resetting the session.
     */
    @Query("DELETE FROM auth_tokens")
    suspend fun deleteAllTokens()
}

/**
 * Room database class for managing authentication tokens.
 *
 * Provides an instance of `TokenDao` for accessing token-related operations.
 */
@Database(entities = [AuthToken::class], version = 1)
abstract class TokenDatabase : RoomDatabase() {

    // Abstract method to get an instance of the `TokenDao`.
    abstract fun tokenDao(): TokenDao

    companion object {
        @Volatile
        private var INSTANCE: TokenDatabase? = null

        /**
         * Returns a singleton instance of the `TokenDatabase`.
         * Ensures only one instance of the database is created for the application.
         *
         * @param context The application context for initializing the database.
         * @return The `TokenDatabase` instance.
         */
        fun getInstance(context: android.content.Context): TokenDatabase {
            // Double-checked locking to ensure thread safety when initializing the database.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TokenDatabase::class.java,
                    "token_database" // Name of the database file.
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
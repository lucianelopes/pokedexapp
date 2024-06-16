package com.example.pokedexapp

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [PokemonEntity::class, UserEntity::class], version = 3, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `user_table` (`username` TEXT NOT NULL, `password` TEXT NOT NULL, PRIMARY KEY(`username`))")
            }
        }

        fun getDatabase(context: Context): PokemonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "pokemon_database"
                )
                    .addMigrations(MIGRATION_1_2) // Adiciona migração aqui
                    .fallbackToDestructiveMigration() // Pode ser removido se não desejar migração destrutiva
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
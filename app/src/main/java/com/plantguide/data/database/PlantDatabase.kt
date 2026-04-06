package com.plantguide.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.plantguide.data.dao.PlantDao
import com.plantguide.data.entity.Plant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Plant::class], version = 2, exportSchema = false)
abstract class PlantDatabase : RoomDatabase() {

    abstract fun plantDao(): PlantDao

    companion object {
        @Volatile
        private var INSTANCE: PlantDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Adiciona a coluna imageUrl
                database.execSQL("ALTER TABLE plants ADD COLUMN imageUrl TEXT NOT NULL DEFAULT ''")

                // Atualiza todas as plantas com as URLs reais
                val urls = mapOf(
                    "plant_sansevieria" to "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fb/Snake_Plant_%28Sansevieria_trifasciata_%27Laurentii%27%29.jpg/800px-Snake_Plant_%28Sansevieria_trifasciata_%27Laurentii%27%29.jpg",
                    "plant_pothos"      to "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Epipremnum_aureum_31082012.jpg/800px-Epipremnum_aureum_31082012.jpg",
                    "plant_orchid"      to "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Orchid_Phalaenopsis.jpg/800px-Orchid_Phalaenopsis.jpg",
                    "plant_echeveria"   to "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Echeveria_lola2.jpg/800px-Echeveria_lola2.jpg",
                    "plant_ficus"       to "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Ficus_lyrata_2019.jpg/800px-Ficus_lyrata_2019.jpg",
                    "plant_christmas_cactus" to "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a3/Schlumbergera_truncata2.jpg/800px-Schlumbergera_truncata2.jpg",
                    "plant_monstera"    to "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f9/Monstera_deliciosa2.jpg/800px-Monstera_deliciosa2.jpg",
                    "plant_lavender"    to "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7a/Lavendel_in_der_Provence.jpg/800px-Lavendel_in_der_Provence.jpg",
                    "plant_zamioculca"  to "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Zamioculcas_zamiifolia1.jpg/800px-Zamioculcas_zamiifolia1.jpg",
                    "plant_rose"        to "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Rosa_rubiginosa_1.jpg/800px-Rosa_rubiginosa_1.jpg",
                    "plant_aloe"        to "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Aloe_vera_flower_inset.png/800px-Aloe_vera_flower_inset.png",
                    "plant_fern"        to "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/Nephrolepis_exaltata_RBGV.jpg/800px-Nephrolepis_exaltata_RBGV.jpg"
                )
                for ((resName, url) in urls) {
                    database.execSQL(
                        "UPDATE plants SET imageUrl = ? WHERE imageResName = ?",
                        arrayOf(url, resName)
                    )
                }
            }
        }

        fun getDatabase(context: Context): PlantDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlantDatabase::class.java,
                    "plant_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(PlantDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class PlantDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.plantDao())
                }
            }
        }

        suspend fun populateDatabase(plantDao: PlantDao) {
            plantDao.insertAll(PlantDataSource.getDefaultPlants())
        }
    }
}

package org.d3if3083.assessment2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ResepEntity::class], version = 1, exportSchema = false)
abstract class ResepDb : RoomDatabase() {

    abstract val resepDao: ResepDao

    companion object {

        @Volatile
        private var INSTANCE: ResepDb? = null

        fun getInstance(context: Context): ResepDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ResepDb::class.java,
                        "resep_db"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
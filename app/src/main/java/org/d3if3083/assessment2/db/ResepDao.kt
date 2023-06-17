package org.d3if3083.assessment2.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResepDao {
    @Insert
    fun insert(recipe: ResepEntity)

    @Delete
    fun delete(recipe: ResepEntity)

    @Query("SELECT * FROM recipe ORDER BY id DESC")
    fun getLastRecipe(): LiveData<List<ResepEntity>>

    @Query("DELETE FROM recipe")
    fun clearData()
}
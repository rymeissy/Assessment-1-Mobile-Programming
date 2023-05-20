package org.d3if3083.assessment2.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class ResepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val namaResep: String,
    val descResep: String,
    val kategori: String,
    val gambar: Int = 0,
    var tanggal: Long = System.currentTimeMillis()
)

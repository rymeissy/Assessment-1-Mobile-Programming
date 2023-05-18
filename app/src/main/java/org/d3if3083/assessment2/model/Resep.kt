package org.d3if3083.assessment2.model

import java.io.Serializable

data class Resep(
    val namaResep: String,
    val descResep: String,
    val kategori: String,
    val gambar: Int = 0,
) : Serializable

package org.d3if3083.assessment2.ui.resep

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3083.assessment2.R
import org.d3if3083.assessment2.db.ResepDao
import org.d3if3083.assessment2.db.ResepEntity
import org.d3if3083.assessment2.model.Resep

class ResepViewModel(private val db: ResepDao) : ViewModel() {

    // mengambil data dari database
    private val data: LiveData<List<ResepEntity>> = db.getLastRecipe()

    // fungsi untuk insert data ke database
    fun insertData(resep: ResepEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            db.insert(resep)
        }
    }

    fun initData() {
        listOf(
            ResepEntity(
                namaResep = "Baso Bakar",
                descResep = "Baso yang dibakar dengan bumbu kacang",
                kategori = "Makanan",
                gambar = R.drawable.bakso
            ),
            ResepEntity(
                namaResep = "Es Jeruk",
                descResep = "Es jeruk segeerrr",
                kategori = "Minuman",
                gambar = R.drawable.es
            ),
            ResepEntity(
                namaResep = "Nasi Goreng Spesial",
                descResep = "Rasanya spesial banget dan nagih",
                kategori = "Makanan",
                gambar = R.drawable.nasgor
            ),
            ResepEntity(
                namaResep = "Mangga Yakult",
                descResep = "Mangga yang dicampur dengan Yakult",
                kategori = "Minuman",
                gambar = R.drawable.mangga
            ),
            ResepEntity(
                namaResep = "Pizza Yummy",
                descResep = "Pizza dengan topping",
                kategori = "Makanan",
                gambar = R.drawable.pizza
            ),
            ResepEntity(
                namaResep = "Soda Ceria",
                descResep = "Soda yang manis",
                kategori = "Minuman",
                gambar = R.drawable.soda
            ),
        ).forEach {
            insertData(it)
        }
    }

    fun getResep(): LiveData<List<ResepEntity>> = data
}
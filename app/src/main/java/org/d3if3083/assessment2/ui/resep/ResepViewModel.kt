package org.d3if3083.assessment2.ui.resep

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3if3083.assessment2.R
import org.d3if3083.assessment2.model.Resep

class ResepViewModel : ViewModel() {

    private val data = MutableLiveData<List<Resep>>()

    init {
        data.value = initData()
    }

    private fun initData(): List<Resep> {
        return listOf(
            Resep("Baso Bakar", "Baso yang dibakar dengan bumbu kacang", "Makanan", R.drawable.bakso),
            Resep("Es Jeruk", "Es jeruk segeerrr", "Minuman", R.drawable.es),
            Resep("Nasi Goreng Spesial", "Rasanya spesial banget dan nagih", "Makanan", R.drawable.nasgor),
            Resep("Mangga Yakult", "Mangga yang dicampur dengan Yakult", "Minuman", R.drawable.mangga),
            Resep("Pizza Yummy", "Pizza dengan topping", "Makanan", R.drawable.pizza),
            Resep("Soda Ceria","Soda yang manis", "Minuman", R.drawable.soda),
        )
    }

    fun getData(): LiveData<List<Resep>> = data
}
package org.d3if3155.hitungbmi.ui.histori

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3083.assessment2.db.ResepDao

class DetailResepViewModelFactory(
    private val db: ResepDao,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailResepViewModel::class.java)) {
            return DetailResepViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

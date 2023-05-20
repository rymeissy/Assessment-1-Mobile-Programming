package org.d3if3083.assessment2.ui.resep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3083.assessment2.db.ResepDao

class ResepViewModelFactory(
    private val db: ResepDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResepViewModel::class.java)) {
            return ResepViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


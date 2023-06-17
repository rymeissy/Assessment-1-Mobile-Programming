package org.d3if3155.hitungbmi.ui.histori

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3083.assessment2.db.ResepDao
import org.d3if3083.assessment2.db.ResepEntity

class DetailResepViewModel(
    private val db: ResepDao,
) : ViewModel() {
    val currentResep = MutableLiveData<ResepEntity>()

    fun deleteResep(resep: ResepEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.delete(resep)
            }
        }
    }
}
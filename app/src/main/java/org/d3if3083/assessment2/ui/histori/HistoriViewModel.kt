package org.d3if3083.assessment2.ui.histori

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3if3083.assessment2.db.ResepDao
import org.d3if3083.assessment2.db.ResepEntity

class HistoriViewModel(
    private val db: ResepDao,
) : ViewModel() {

    fun getResep(): LiveData<List<ResepEntity>> {
        return db.getLastRecipe()
    }
}
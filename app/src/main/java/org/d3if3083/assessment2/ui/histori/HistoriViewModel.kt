package org.d3if3083.assessment2.ui.histori

import androidx.lifecycle.ViewModel
import org.d3if3083.assessment2.db.ResepDao

class HistoriViewModel(db: ResepDao) : ViewModel()  {
    val data = db.getLastRecipe()
}
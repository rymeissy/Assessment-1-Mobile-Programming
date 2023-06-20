package org.d3if3083.assessment2.ui.resep

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3083.assessment2.db.ResepDao
import org.d3if3083.assessment2.db.ResepEntity
import org.d3if3083.assessment2.model.Resep
import org.d3if3083.assessment2.network.UpdateWorker
import org.d3if3083.assessment2.ui.input_resep.InputResepFragment
import org.d3if3083.assessment2.network.ApiStatus
import org.d3if3083.assessment2.network.ResepApi
import java.util.concurrent.TimeUnit

class ResepViewModel(
    private val db: ResepDao,
) : ViewModel() {

    // mengambil data dari API
    private val data = MutableLiveData<List<Resep>>()
    private val status = MutableLiveData<ApiStatus>()

    init {
        retrieveData()
    }

    fun simpanDbResep(resep: ResepEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.insert(resep)
        }
    }

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)
            try {
                data.postValue(ResepApi.service.getResep())
                status.postValue(ApiStatus.SUCCESS)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }
        }
    }

    fun scheduleUpdater(app: Application) {
        val request = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(app).enqueueUniqueWork(
            UpdateWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    suspend fun updateData(fragment: InputResepFragment, resep: Resep): Boolean {
        status.postValue(ApiStatus.LOADING)
        try {
            val list = data.value!!.toMutableList()
            list.add(resep)
            data.postValue(list)

            ResepApi.service.putResep(list.toList())
            simpanDbResep(resep.toResepEntity())
            status.postValue(ApiStatus.SUCCESS)
            return true
        } catch (e: Exception) {
            Log.d("MainViewModel", "Failure: ${e.message}")
            status.postValue(ApiStatus.FAILED)
        }
        return false
    }

    suspend fun deleteResep(resep: Resep): Boolean {
        status.postValue(ApiStatus.LOADING)
        try {
            val list = data.value!!.toMutableList()
            list.remove(resep)
            data.postValue(list)

            ResepApi.service.putResep(list.toList())
            status.postValue(ApiStatus.SUCCESS)
            return true
        } catch (e: Exception) {
            Log.d("MainViewModel", "Failure: ${e.message}")
            status.postValue(ApiStatus.FAILED)
        }
        return false
    }

    fun getData(): LiveData<List<Resep>> = data

    fun getStatus(): LiveData<ApiStatus> = status
}
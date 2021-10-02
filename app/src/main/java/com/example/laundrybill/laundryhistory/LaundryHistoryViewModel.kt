package com.example.laundrybill.laundryhistory

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laundrybill.database.Laundry
import com.example.laundrybill.database.LaundryDao
import kotlinx.coroutines.*

class LaundryHistoryViewModel(val database: LaundryDao, application: Application) : ViewModel() {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private var _collectedLaundryList = MutableLiveData<List<Laundry>>()
    val collectedLaundryList: LiveData<List<Laundry>>
        get() = _collectedLaundryList

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private suspend fun getCollectedList(): List<Laundry>? {
        return withContext(Dispatchers.IO) {
            return@withContext database.getCollectedLaundryItem()
        }
    }

    fun initialize() {
        viewModelScope.launch {
            _collectedLaundryList.value = getCollectedList()
        }
    }

    init {
        initialize()
    }
}
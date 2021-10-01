package com.example.laundrybill.laundryhistory

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.laundrybill.database.LaundryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class LaundryHistoryViewModel(val database: LaundryDao, application: Application) : ViewModel() {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    val laundryList = database.getCollectedLaundryItem()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
}
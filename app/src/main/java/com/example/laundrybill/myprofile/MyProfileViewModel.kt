package com.example.laundrybill.myprofile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laundrybill.database.Laundry
import com.example.laundrybill.database.LaundryDao
import kotlinx.coroutines.*

class MyProfileViewModel(val database: LaundryDao, application: Application) : ViewModel() {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    val laundryList = database.getPendingLaundryItem()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private suspend fun delete(itemId: Long) {
        return withContext(Dispatchers.IO) {
            database.deleteLaundryItem(itemId)
        }
    }

    private suspend fun update(laundry: Laundry) {
        return withContext(Dispatchers.IO) {
            database.updateLaundryItem(laundry)
        }
    }

    fun onDeleteLaundryClicked(itemId: Long) {
        viewModelScope.launch {
            delete(itemId)
        }
    }

    fun onCollectedClicked(laundry: Laundry) {
        viewModelScope.launch {
            laundry.status = "Collected"
            update(laundry)
        }
    }
}
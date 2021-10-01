package com.example.laundrybill.addlaundry

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laundrybill.database.Laundry
import com.example.laundrybill.database.LaundryDao
import kotlinx.coroutines.*

class AddLaundryViewModel(val database: LaundryDao, application: Application) : ViewModel() {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private suspend fun insert(laundry: Laundry) {
        return withContext(Dispatchers.IO) {
            database.insertLaundryItem(laundry)
        }
    }

    private suspend fun update(laundry: Laundry) {
        return withContext(Dispatchers.IO) {
            database.updateLaundryItem(laundry)
        }
    }

    fun onEditClicked(laundry: Laundry) {
        viewModelScope.launch {
            update(laundry)
        }
    }

    fun onAddClicked(laundry: Laundry) {
        viewModelScope.launch {
            insert(laundry)
        }
    }
}
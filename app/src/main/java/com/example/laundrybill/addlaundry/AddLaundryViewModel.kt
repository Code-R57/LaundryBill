package com.example.laundrybill.addlaundry

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laundrybill.database.Laundry
import com.example.laundrybill.database.LaundryDao
import kotlinx.coroutines.*
import java.util.*

class AddLaundryViewModel(val database: LaundryDao, application: Application) : ViewModel() {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val _laundryItem = MutableLiveData<Laundry>()
    val laundryItem: LiveData<Laundry>
    get() = _laundryItem

    private val _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String>
        get() = _currentDate

    init {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        _currentDate.value = year.toString()
        _currentDate.value += if (month < 10) " 0$month" else " $month"
        _currentDate.value += if (day < 10) " 0$day" else " $day"
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

    private suspend fun getItem(itemId: Long): Laundry {
        return withContext(Dispatchers.IO) {
            if(itemId != -1L) {
                return@withContext database.getLaundryItem(itemId)
            }
            else {
                return@withContext Laundry()
            }
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

    fun initialize(itemId: Long) {
        viewModelScope.launch {
            _laundryItem.value = getItem(itemId)
        }
    }
}
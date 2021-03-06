package com.example.laundrybill.addlaundry

import android.app.Application
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
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        _currentDate.value = year.toString()
        _currentDate.value += if (month < 10) " 0$month" else " $month"
        _currentDate.value += if (day < 10) " 0$day" else " $day"
        calendar.set(1, 2, 3)
    }

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

    private fun getItem(itemId: Long): Laundry {
        return runBlocking(Dispatchers.IO) {
            if (itemId != -1L) {
                return@runBlocking database.getLaundryItem(itemId)
            } else {
                return@runBlocking Laundry()
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
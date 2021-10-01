package com.example.laundrybill.myprofile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private var _pendingLaundryList = MutableLiveData<List<Laundry>>()
    val pendingLaundryList: LiveData<List<Laundry>>
        get() = _pendingLaundryList

    private var _pendingAmount = MutableLiveData<Double>()
    val pendingAmount: LiveData<Double>
        get() = _pendingAmount

    private var _totalAmount = MutableLiveData<Double>()
    val totalAmount: LiveData<Double>
        get() = _totalAmount

    var collectedLaundryList : List<Laundry>? = listOf()

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

    private suspend fun getPendingList(): List<Laundry>? {
        return withContext(Dispatchers.IO) {
            var laundryList: List<Laundry>?
            laundryList = database.getPendingLaundryItem()
            return@withContext laundryList
        }
    }

    private suspend fun getCollectedList(): List<Laundry>? {
        return withContext(Dispatchers.IO) {
            return@withContext database.getCollectedLaundryItem()
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

    fun initialize() {
        viewModelScope.launch {
            var total = 0.00
            _pendingLaundryList.value = getPendingList()
            _pendingLaundryList.value?.forEach {
                total += it.totalAmount
            }
            _pendingAmount.value = total
            collectedLaundryList = getCollectedList()
            total = 0.00
            collectedLaundryList?.forEach{
                total += it.totalAmount
            }
            _totalAmount.value = total
        }
    }

    init {
        initialize()
    }

}
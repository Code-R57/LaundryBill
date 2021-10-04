package com.example.laundrybill.myprofile

import android.app.Application
import android.content.res.Configuration
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

    private var _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean>
        get() = _isDarkMode

    var collectedLaundryList: List<Laundry>? = listOf()

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
            return@withContext database.getPendingLaundryItem()
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

    init {
        val uiMode =
            application.applicationContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)
        _isDarkMode.value = uiMode == Configuration.UI_MODE_NIGHT_YES
    }

    fun switchMode() {
        _isDarkMode.value = _isDarkMode.value != true
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
            collectedLaundryList?.forEach {
                total += it.totalAmount
            }
            _totalAmount.value = total
        }
    }

    init {
        initialize()
    }

}
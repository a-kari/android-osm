package jp.neechan.osmtest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.neechan.osmtest.repositories.AddressesRepository

class ViewModelFactory(private val addressesRepository: AddressesRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MapViewModel(addressesRepository) as T
    }
}
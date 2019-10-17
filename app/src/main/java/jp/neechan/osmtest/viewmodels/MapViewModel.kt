package jp.neechan.osmtest.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import jp.neechan.osmtest.models.Address
import jp.neechan.osmtest.repositories.AddressesRepository

class MapViewModel(private val addressesRepository: AddressesRepository) : ViewModel() {

    fun searchAddressByCoordinates(latitude: Double, longitude: Double): Observable<Address> {
        return addressesRepository.searchAddressByCoordinates(latitude, longitude)
    }
}
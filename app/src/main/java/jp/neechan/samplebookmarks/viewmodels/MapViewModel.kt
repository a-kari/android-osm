package jp.neechan.samplebookmarks.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import jp.neechan.samplebookmarks.models.Address
import jp.neechan.samplebookmarks.repositories.AddressesRepository

class MapViewModel(private val addressesRepository: AddressesRepository) : ViewModel() {

    fun searchAddressByCoordinates(latitude: Double, longitude: Double): Observable<Address> {
        return addressesRepository.searchAddressByCoordinates(latitude, longitude)
    }
}
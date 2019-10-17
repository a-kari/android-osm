package jp.neechan.osmtest.repositories

import io.reactivex.Observable
import jp.neechan.osmtest.models.Address
import jp.neechan.osmtest.rest.NominatimService

class AddressesRepository(private val nominatimService: NominatimService) {
    
    fun searchAddressByCoordinates(latitude: Double, longitude: Double): Observable<Address> {
        return nominatimService.searchAddressByCoordinates(latitude, longitude, "json")
    }
}
package jp.neechan.samplebookmarks.repositories

import io.reactivex.Observable
import jp.neechan.samplebookmarks.models.Address
import jp.neechan.samplebookmarks.rest.NominatimService

class AddressesRepository(private val nominatimService: NominatimService) {
    
    fun searchAddressByCoordinates(latitude: Double, longitude: Double): Observable<Address> {
        return nominatimService.searchAddressByCoordinates(latitude, longitude, "json")
    }
}
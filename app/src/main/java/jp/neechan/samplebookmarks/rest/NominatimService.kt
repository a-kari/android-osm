package jp.neechan.samplebookmarks.rest

import io.reactivex.Observable
import jp.neechan.samplebookmarks.models.Address
import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimService {

    @GET("reverse")
    fun searchAddressByCoordinates(@Query("lat")    latitude:  Double,
                                   @Query("lon")    longitude: Double,
                                   @Query("format") format:    String): Observable<Address>
}
package jp.neechan.samplebookmarks.di

import jp.neechan.samplebookmarks.repositories.AddressesRepository
import jp.neechan.samplebookmarks.rest.NominatimService
import jp.neechan.samplebookmarks.viewmodels.ViewModelFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object KoinModule {

    val module = module {

        // Retrofit.Builder
        single {
            Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
        }

        // NominatimService
        single {
            val retrofit = (get() as Retrofit.Builder)
                    .baseUrl("http://my.tile.server/nominatim/")
                    .build()
            retrofit.create(NominatimService::class.java) as NominatimService
        }

        // Repositories
        single { AddressesRepository(get()) }

        // ViewModelFactory
        single { ViewModelFactory(get()) }
    }
}
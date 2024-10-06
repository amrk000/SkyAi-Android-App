package amrk000.skyai.presentation.di.module.retrofit

import amrk000.skyai.data.remote.weatherAPI.IWeatherApiService
import amrk000.skyai.presentation.di.qualifier.WeatherQualifier

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class WeatherRetrofitModule {
    private val WEATHER_BASE_URL = "https://api.tomorrow.io/v4/weather/"

    @Provides
    @Singleton
    @WeatherQualifier
    fun provideRetrofitClient(gson: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(gson)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitClientService(@WeatherQualifier retrofitClient: Retrofit): IWeatherApiService {
        return retrofitClient.create(IWeatherApiService::class.java)
    }
}
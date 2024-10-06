package amrk000.skyai.presentation.di.module.retrofit

import amrk000.skyai.data.remote.geminiAiAPI.IGeminiAiApiService

import amrk000.skyai.presentation.di.qualifier.GeminiAiQualifier

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class GeminiAiRetrofitModule {
    private val AI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/"

    @Provides
    @Singleton
    @GeminiAiQualifier
    fun provideRetrofitClient(gson: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AI_BASE_URL)
            .addConverterFactory(gson)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitClientService(@GeminiAiQualifier retrofitClient: Retrofit): IGeminiAiApiService {
        return retrofitClient.create(IGeminiAiApiService::class.java)
    }
}
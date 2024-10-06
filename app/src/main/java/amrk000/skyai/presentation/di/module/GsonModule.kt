package amrk000.skyai.presentation.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GsonModule {
    @Provides
    fun provideGsonFactory(): GsonConverterFactory  {
        return GsonConverterFactory.create()
    }

    @Provides
    fun providesGson(): Gson {
        return Gson()
    }
}
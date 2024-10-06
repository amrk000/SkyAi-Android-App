package amrk000.skyai.presentation.di.module

import amrk000.skyai.domain.util.SharedPrefManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SharedPrefsModule::class]
)
class SharedPrefManagerTestModule {
    @Provides
    fun provideSharedPreferencesTest(@ApplicationContext context: Context): SharedPreferences {
       return context.getSharedPreferences("prefsTest", MODE_PRIVATE)
    }

    @Provides
    fun provideSharedPrefManagerTest(sharedPreferences: SharedPreferences): SharedPrefManager {
        return SharedPrefManager(sharedPreferences, Gson())
    }
}
package amrk000

import amrk000.skyai.presentation.di.component.SkyAiApplication
import android.app.Application
import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltTestApplication

class TestRunner : AndroidJUnitRunner() {
    @Throws(
        InstantiationException::class,
        IllegalAccessException::class,
        ClassNotFoundException::class
    )
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {

        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }



}
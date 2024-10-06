import com.android.build.api.dsl.Packaging
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "amrk000.skyai"
    compileSdk = 34

    defaultConfig {
        applicationId = "amrk000.skyai"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "amrk000.TestRunner"

        //build config api key
        val properties = Properties()
        properties.load(File("local.properties").inputStream())

        buildConfigField("String", "WEATHER_API_KEY", properties.getProperty("WEATHER_API_KEY"))
        buildConfigField("String", "AI_API_KEY", properties.getProperty("AI_API_KEY"))

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }
    kotlinOptions {
        jvmTarget = "${JavaVersion.VERSION_22}"
    }

    buildFeatures{
        viewBinding = true
        buildConfig = true
    }

    androidResources {
        generateLocaleConfig = true
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }

    //For MockK to work well without jvmti error
    testOptions {
        packaging {
            jniLibs {
                useLegacyPackaging = true
            }
        }
    }

    testOptions {
        animationsDisabled = false
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.location)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.junit)


    //MVVM
    // ViewModel:
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // Saved state module:
    implementation (libs.androidx.lifecycle.viewmodel.savedstate)
    // LiveData:
    implementation (libs.androidx.lifecycle.livedata)

    //Kotlin coroutines
    implementation(libs.kotlinx.coroutines.android)
    testImplementation (libs.kotlinx.coroutines.test)
    androidTestImplementation (libs.kotlinx.coroutines.test)

    //Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    //Splash Screen API
    implementation (libs.androidx.core.splashscreen)

    //viewPager2
    implementation (libs.androidx.viewpager2)

    //Swipe to refresh
    implementation (libs.androidx.swiperefreshlayout)

    //Background Work Manager
    implementation (libs.androidx.work.runtime.ktx)
    implementation (libs.androidx.work.multiprocess)

    //Hilt DI
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)

    //Hilt DI: testing
    testImplementation (libs.hilt.android.testing)
    kaptTest (libs.hilt.android.compiler)

    androidTestImplementation (libs.hilt.android.testing)
    kaptAndroidTest (libs.hilt.android.compiler)

    //Hilt for Worker
    implementation(libs.hilt.work.v100)
    kapt(libs.androidx.hilt.compiler)

    //Testing

    //Junit
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.junit)
    //Test Permission Rule
    androidTestImplementation (libs.androidx.rules)

    //MockK
    testImplementation (libs.mockk)
    testImplementation (libs.mockk.agent)
    androidTestImplementation (libs.mockk.android)
    androidTestImplementation (libs.mockk.agent)

    //Instancio:
    testImplementation (libs.instancio.core)
    androidTestImplementation (libs.instancio.core)

    //ByteBuddy
    androidTestImplementation(libs.byte.buddy.android)

    //Espresso
    androidTestImplementation (libs.androidx.espresso.core)
    androidTestImplementation (libs.androidx.espresso.contrib)
    androidTestImplementation (libs.hamcrest)
    androidTestImplementation (libs.androidx.runner)
    androidTestImplementation (libs.androidx.rules)
}
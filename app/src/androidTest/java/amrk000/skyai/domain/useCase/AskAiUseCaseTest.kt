package amrk000.skyai.domain.useCase

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AskAiUseCaseTest{
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var askAiUseCase: AskAiUseCase

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @Test
    fun invoke() {
        runBlocking{
            val result = askAiUseCase.invoke("what is car?")
            //check request result
            assertEquals(200, result.code)

            //check result data
            assertNotNull(result)

            //check answer text
            assertNotNull(result.candidates?.first()?.content?.parts?.first()?.text)
            assertTrue(result.candidates?.first()?.content?.parts?.first()?.text?.isNotEmpty() == true)

            Log.d(AskAiUseCaseTest::class.simpleName, result.toString())
        }
    }

}
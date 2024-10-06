package amrk000.skyai.data.repository

import amrk000.skyai.domain.useCase.AskAiUseCaseTest
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
class AiRepositoryTest{
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var aiRepository: AiRepository

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @Test
    fun askGeminiAi() {
        runBlocking{
            val result = aiRepository.askGeminiAi("what is car?")
            //check result data
            assertNotNull(result)

            //check answer text
            assertNotNull(result.candidates?.first()?.content?.parts?.first()?.text)
            assertTrue(result.candidates?.first()?.content?.parts?.first()?.text?.isNotEmpty() == true)

            Log.d(AiRepositoryTest::class.simpleName, result.toString())
        }
    }

}
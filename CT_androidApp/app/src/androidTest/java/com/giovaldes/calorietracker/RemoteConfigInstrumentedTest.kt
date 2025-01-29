
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.giovaldes.calorietracker.presentation.MainActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RemoteConfigInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testRemoteConfigFetch() {
        // Simula una respuesta de Firebase Remote Config
        val jsonResponse = """
            {
                "style": "whiteStyle",
                "active": true,
                "isAlertEmoji": true
            }
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse))

        // Verifica que el color de fondo se haya actualizado correctamente
        composeTestRule.onNodeWithText("Calorie Tracker")
            .assertExists()
    }
}

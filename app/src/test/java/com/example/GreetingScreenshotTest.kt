package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.local.PlayerProfileEntity
import com.example.ui.components.HeaderStatusBar
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun header_status_bar_screenshot() {
    composeTestRule.setContent {
      MyApplicationTheme {
        HeaderStatusBar(
          profile = PlayerProfileEntity(
            name = "Don Valentino",
            title = "Street Capo",
            clanName = "Apex Syndicate",
            cash = 50000.0,
            respect = 2500,
            power = 1200
          ),
          cashRate = 125.0,
          respectRate = 15.0,
          powerRate = 8.0
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/header_status_bar.png")
  }
}


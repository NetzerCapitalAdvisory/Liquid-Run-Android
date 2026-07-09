package com.example.liquidrun

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.liquidrun.ui.auth.AuthScreen
import com.example.liquidrun.ui.home.HomeScreen
import com.example.liquidrun.ui.clubs.ClubsScreen
import com.example.liquidrun.ui.tracking.TrackingScreen
import com.example.liquidrun.ui.profile.ProfileScreen

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(Auth)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<Auth> {
          AuthScreen(
            onLoginSuccess = {
              backStack.removeLastOrNull()
              backStack.add(Home)
            }
          )
        }
        entry<Home> {
          HomeScreen(
            onNavigateToTracking = { backStack.add(Tracking) }
          )
        }
        entry<Clubs> {
          ClubsScreen()
        }
        entry<Tracking> {
          TrackingScreen(
            onFinishTracking = { backStack.removeLastOrNull() }
          )
        }
        entry<Profile> {
          ProfileScreen(
            onSignOut = {
              backStack.clear()
              backStack.add(Auth)
            }
          )
        }
      },
  )
}

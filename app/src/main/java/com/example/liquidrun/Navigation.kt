package com.example.liquidrun

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.liquidrun.ui.auth.AuthScreen
import com.example.liquidrun.ui.clubs.ClubsScreen
import com.example.liquidrun.ui.home.HomeScreen
import com.example.liquidrun.ui.profile.ProfileScreen
import com.example.liquidrun.ui.tracking.TrackingScreen

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(Auth)
  val currentScreen = backStack.lastOrNull()

  Scaffold(
    bottomBar = {
      if (currentScreen != Auth && currentScreen != Tracking) {
        NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
          NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentScreen == Home,
            onClick = {
              backStack.clear()
              backStack.add(Home)
            }
          )
          NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Clubs") },
            label = { Text("Clubs") },
            selected = currentScreen == Clubs,
            onClick = {
              backStack.clear()
              backStack.add(Clubs)
            }
          )
          NavigationBarItem(
            icon = { Icon(Icons.Filled.LocationOn, contentDescription = "Karte") },
            label = { Text("Karte") },
            selected = currentScreen == Map,
            onClick = {
              backStack.clear()
              backStack.add(Map)
            }
          )
          NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profil") }, // Need a better icon
            label = { Text("Profil") },
            selected = currentScreen == Profile,
            onClick = {
              backStack.clear()
              backStack.add(Profile)
            }
          )
        }
      }
    }
  ) { innerPadding ->
    NavDisplay(
      backStack = backStack,
      modifier = Modifier.padding(innerPadding),
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
          entry<Map> {
            com.example.liquidrun.ui.map.MapScreen()
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
}

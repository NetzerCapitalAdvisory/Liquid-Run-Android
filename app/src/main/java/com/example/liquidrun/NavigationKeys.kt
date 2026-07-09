package com.example.liquidrun

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Auth : NavKey
@Serializable data object Home : NavKey
@Serializable data object Clubs : NavKey
@Serializable data object Map : NavKey
@Serializable data object Tracking : NavKey
@Serializable data object Profile : NavKey

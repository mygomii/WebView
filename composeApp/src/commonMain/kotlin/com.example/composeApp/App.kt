package com.example.composeApp

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.composeApp.presentations.Main
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Main()
    }

}
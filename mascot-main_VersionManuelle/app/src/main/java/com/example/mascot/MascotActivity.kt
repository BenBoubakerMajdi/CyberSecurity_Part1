package com.example.mascot

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.view.WindowCompat
import com.example.mascot.ui.theme.MascotTheme

class Mascot(val name: String, val representing: String, @DrawableRes val imageRes: Int) {
    override fun toString(): String {
        return "$name ($representing)"
    }
}

class MascotActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MascotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Mascot(
                        mascot = MASCOTS.random(),
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }

    @Composable
    private fun Mascot(mascot: Mascot, modifier: Modifier = Modifier) {
        var response by remember { mutableStateOf(MASCOTS.first()) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.padding(horizontal = 20.dp, vertical = 50.dp)
        ) {
            Column {
                Image(
                    painter = painterResource(id = mascot.imageRes),
                    contentDescription = "Mascotte",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.33f)
                )
                Text(
                    text = "Qui suis-je ?",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MascotDropdown { value -> response = value }

                Button(onClick = {
                    val intent = Intent(baseContext, ResultActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("name", mascot.name)
                    intent.putExtra("representing", mascot.representing)
                    intent.putExtra(
                        "success",
                        mascot.name == response.name && mascot.representing == response.representing
                    )
                    baseContext.startActivity(intent)
                }) {
                    Text("Valider")
                }
            }

        }
    }


    @Composable
    fun MascotDropdown(onChange: (value: Mascot) -> Unit) {
        var expanded by remember { mutableStateOf(false) }
        var selected by remember { mutableStateOf(MASCOTS.first()) }
        var textFieldSize by remember { mutableStateOf(Size.Zero) }

        Column(Modifier.padding(20.dp)) {
            OutlinedTextField(
                value = selected.toString(),
                onValueChange = { },
                readOnly = true,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.LightGray,
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        // This value is used to assign to
                        // the DropDown the same width
                        textFieldSize = coordinates.size.toSize()
                    },
                trailingIcon = {
                    Icon(
                        if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        "Arrow",
                        Modifier.clickable { expanded = !expanded })
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
            ) {
                MASCOTS.forEach { mascot ->
                    DropdownMenuItem(
                        text = { Text(text = mascot.toString()) },
                        onClick = {
                            selected = mascot
                            expanded = false
                            onChange(mascot)
                        }
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun MascotPreview() {
        MascotTheme {
            Mascot(MASCOTS.first())
        }
    }

    companion object {
        // See https://en.wikipedia.org/wiki/List_of_computing_mascots
        val MASCOTS = setOf(
            Mascot(
                "Beastie",
                "BSD",
                R.drawable.bsd
            ),
            Mascot(
                "Bugdroid",
                "Android",
                R.drawable.android
            ),
            Mascot("Keith", "C++", R.drawable.cpp),
            Mascot(
                "Dash",
                "Dart",
                R.drawable.dart
            ),
            Mascot(
                "DotNet Bot",
                ".NET",
                R.drawable.dotnet
            ),
            Mascot(
                "Duke",
                "Java",
                R.drawable.java
            ),
            Mascot(
                "elePHPant",
                "PHP",
                R.drawable.php
            ),
            Mascot(
                "eMule",
                "eMule",
                R.drawable.emule
            ),
            Mascot(
                "Ferris",
                "Rust",
                R.drawable.rust
            ),
            Mascot(
                "GNU",
                "GNU",
                R.drawable.gnu
            ),
            Mascot(
                "GBot",
                "Godot",
                R.drawable.godot
            ),
            Mascot(
                "Gopher",
                "Go",
                R.drawable.go
            ),
            Mascot(
                "Konqi",
                "KDE",
                R.drawable.kde
            ),
            Mascot(
                "Kodee",
                "Kotlin",
                R.drawable.kotlin
            ),
            Mascot(
                "Lisp Alien",
                "Lisp",
                R.drawable.lisp
            ),
            Mascot(
                "Moby Dock",
                "Docker",
                R.drawable.docker
            ),
            Mascot(
                "Mozilla",
                "Mozilla",
                R.drawable.mozilla
            ),
            Mascot(
                "Octocat",
                "Github",
                R.drawable.github
            ),
            Mascot(
                "Slonik",
                "Postgresql",
                R.drawable.postgresql
            ),
            Mascot("Tux", "Linux", R.drawable.linux),
            Mascot(
                "Wilber",
                "Gimp",
                R.drawable.gimp
            ),
            Mascot(
                "Camel",
                "Perl",
                R.drawable.perl
            ),
        ).sortedBy { mascot -> mascot.name.lowercase() }
    }
}
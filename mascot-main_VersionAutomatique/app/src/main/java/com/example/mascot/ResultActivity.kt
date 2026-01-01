package com.example.mascot

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.mascot.ui.theme.MascotTheme

class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = intent.getStringExtra("name") ?: ""
        val representing = intent.getStringExtra("representing") ?: ""
        val success = intent.getBooleanExtra("success", false)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MascotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Result(
                        name,
                        representing,
                        success,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }

    @Composable
    private fun Result(
        name: String,
        representing: String,
        success: Boolean,
        modifier: Modifier = Modifier
    ) {
        val icon = if (success)
            R.drawable.success
        else
            R.drawable.fail

        val message = if (success)
            "Tu as bien deviné !\nC'était $name la mascotte de $representing."
        else
            "Dommage, il se trouve que c'était $name la mascotte de $representing..."

        IllustrationActionColumn(modifier = modifier) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .padding(bottom = 20.dp)
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(onClick = {
                val intent = Intent(baseContext, MascotActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                baseContext.startActivity(intent)
            }) {
                Text("On recommance ?")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun ResultPreview() {
        MascotTheme {
            Result("Tux", "Linux", false)
        }
    }
}
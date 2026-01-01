package com.example.mascot

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mascot.ui.theme.MascotTheme
import android.util.Log
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ========== INJECTION DE LA PROTECTION ==========
        // On exécute les détecteurs IMMÉDIATEMENT au démarrage

        // Log pour le debugging (voir dans Logcat)
        Log.d("Mascot App", "Vérification de sécurité en cours...")

        // Affiche le rapport complet dans les logs
        val report = ThreatDetector.getThreatReport(this)
        Log.d("Mascot App", report)

        // Si une menace est détectée, on tue l'application
        if (ThreatDetector.detectThreats(this)) {
            Log.e("Mascot App", "⚠️ MENACE DÉTECTÉE ! Fermeture de l'application...")
            ThreatDetector.killApplication()
            // Le code ci-dessous ne sera jamais exécuté si une menace existe
        }

        Log.d("Mascot App", "✅ Aucune menace détectée, démarrage normal")
        // ========== FIN DE LA PROTECTION ==========
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MascotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Home(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }

    @Composable
    private fun Home(modifier: Modifier = Modifier) {
        IllustrationActionColumn(
            modifier = modifier
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                val intent = Intent(baseContext, MascotActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                baseContext.startActivity(intent)
            }) {
                Text("Commencer !")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun HomePreview() {
        MascotTheme {
            Home()
        }
    }
}
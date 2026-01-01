package com.example.mascot

import android.content.Context
import android.os.Build
import android.os.Debug
import android.provider.Settings
import java.io.File

/**
 * Classe contenant tous les détecteurs de vecteurs d'attaque
 * Chaque méthode retourne true si le vecteur est détecté
 */
object ThreatDetector {

    /**
     * DÉTECTEUR 1 : Débogueur
     * Détecte si un débogueur est attaché à l'application
     * Méthode : Utilise la fonction native Android Debug.isDebuggerConnected()
     */
    fun isDebuggerDetected(): Boolean {
        return Debug.isDebuggerConnected() || Debug.waitingForDebugger()
    }

    /**
     * DÉTECTEUR 2 : Mode Développeur
     * Détecte si le mode développeur est activé sur l'appareil
     * Méthode : Lit le paramètre système DEVELOPMENT_SETTINGS_ENABLED
     */
    fun isDeveloperModeDetected(context: Context): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
            0
        ) == 1
    }

    /**
     * DÉTECTEUR 3 : Root
     * Détecte si l'appareil est rooté (droits administrateur)
     * Méthode : Vérifie la présence de binaires 'su' et de fichiers système modifiés
     */
    fun isRootDetected(): Boolean {
        // Liste des chemins où 'su' (Super User) peut être présent
        val rootPaths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )

        // Vérifie si un de ces fichiers existe
        for (path in rootPaths) {
            if (File(path).exists()) {
                return true
            }
        }

        // Vérifie si la commande 'su' est exécutable
        return try {
            Runtime.getRuntime().exec("su")
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * DÉTECTEUR 4 : Émulateur
     * Détecte si l'application tourne sur un émulateur
     * Méthode : Analyse les propriétés matérielles et les identifiants du système
     */
    fun isEmulatorDetected(): Boolean {
        // Vérifie les propriétés Build caractéristiques des émulateurs
        val isEmulatorByBuild = (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT)

        // Vérifie si le hardware est de type "goldfish" (émulateur standard Android)
        val isEmulatorByHardware = Build.HARDWARE.contains("goldfish") ||
                Build.HARDWARE.contains("ranchu")

        return isEmulatorByBuild || isEmulatorByHardware
    }

    /**
     * Méthode principale qui exécute tous les détecteurs
     * @return true si au moins un vecteur d'attaque est détecté
     */
    fun detectThreats(context: Context): Boolean {
        return isDebuggerDetected() ||
                isDeveloperModeDetected(context) ||
                isRootDetected() ||
                isEmulatorDetected()
    }

    /**
     * Méthode qui retourne un rapport détaillé des menaces
     * Utile pour le debugging et la compréhension
     */
    fun getThreatReport(context: Context): String {
        val report = StringBuilder()
        report.append("=== RAPPORT DE SÉCURITÉ ===\n")
        report.append("Débogueur détecté : ${isDebuggerDetected()}\n")
        report.append("Mode développeur : ${isDeveloperModeDetected(context)}\n")
        report.append("Root détecté : ${isRootDetected()}\n")
        report.append("Émulateur détecté : ${isEmulatorDetected()}\n")
        return report.toString()
    }

    /**
     * REMÉDIATION : Ferme immédiatement l'application
     * Utilise System.exit() pour terminer le processus
     */
    fun killApplication() {
        // Méthode 1 : Terminer le processus Android
        android.os.Process.killProcess(android.os.Process.myPid())

        // Méthode 2 : Sortie système (backup)
        System.exit(1)
    }
}
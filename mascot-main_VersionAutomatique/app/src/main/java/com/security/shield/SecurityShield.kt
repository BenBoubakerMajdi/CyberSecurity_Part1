package com.security.shield

import android.content.Context
import android.os.Build
import android.os.Debug
import android.provider.Settings
import android.util.Log
import java.io.File

/**
 * Bouclier de sécurité automatique pour applications Android
 * Détecte et bloque les vecteurs d'attaque potentiels
 * 
 * Vecteurs détectés :
 * - Débogueur attaché
 * - Mode développeur activé
 * - Appareil rooté
 * - Exécution sur émulateur
 */
object SecurityShield {

    private const val TAG = "SecurityShield"

    /**
     * Méthode principale à appeler au démarrage de l'application
     * Détecte automatiquement les menaces et applique la remédiation
     * 
     * @param context Le contexte de l'application (généralement 'this' dans une Activity)
     */
    @JvmStatic
    fun protect(context: Context) {
        Log.d(TAG, "🛡️ === INITIALISATION DU BOUCLIER DE SÉCURITÉ ===")
        
        val detectedThreats = mutableListOf<String>()
        
        // ========== EXÉCUTION DES DÉTECTEURS ==========
        
        if (isDebuggerDetected()) {
            detectedThreats.add("Débogueur")
            Log.w(TAG, "⚠️ DÉBOGUEUR DÉTECTÉ")
        }
        
        if (isDeveloperModeDetected(context)) {
            detectedThreats.add("Mode développeur")
            Log.w(TAG, "⚠️ MODE DÉVELOPPEUR DÉTECTÉ")
        }
        
        if (isRootDetected()) {
            detectedThreats.add("Root")
            Log.w(TAG, "⚠️ ROOT DÉTECTÉ")
        }
        
        if (isEmulatorDetected()) {
            detectedThreats.add("Émulateur")
            Log.w(TAG, "⚠️ ÉMULATEUR DÉTECTÉ")
        }
        
        // ========== DÉCISION ET REMÉDIATION ==========
        
        if (detectedThreats.isNotEmpty()) {
            Log.e(TAG, "🚨 MENACES DÉTECTÉES: ${detectedThreats.joinToString(", ")}")
            Log.e(TAG, "🔒 APPLICATION DE LA REMÉDIATION...")
            Log.e(TAG, "💀 FERMETURE IMMÉDIATE DE L'APPLICATION")
            
            // Délai de 100ms pour que les logs s'affichent
            Thread.sleep(100)
            
            // Application de la remédiation
            killApplication()
        } else {
            Log.i(TAG, "✅ Aucune menace détectée")
            Log.i(TAG, "✅ Démarrage de l'application autorisé")
        }
    }

    /**
     * DÉTECTEUR 1 : Débogueur
     * 
     * Principe : Android fournit des méthodes natives pour détecter
     * si un débogueur (Android Studio Debugger, gdb, etc.) est attaché
     * au processus de l'application.
     * 
     * @return true si un débogueur est détecté
     */
    private fun isDebuggerDetected(): Boolean {
        // Méthode 1 : Débogueur actuellement connecté
        val isConnected = Debug.isDebuggerConnected()
        
        // Méthode 2 : Application en attente d'un débogueur
        val isWaiting = Debug.waitingForDebugger()
        
        return isConnected || isWaiting
    }

    /**
     * DÉTECTEUR 2 : Mode Développeur
     * 
     * Principe : Le mode développeur est un paramètre système Android
     * qui active des fonctionnalités de débogage (USB debugging, logs, etc.).
     * On lit ce paramètre via l'API Settings.
     * 
     * @param context Contexte nécessaire pour accéder aux Settings système
     * @return true si le mode développeur est activé
     */
    private fun isDeveloperModeDetected(context: Context): Boolean {
        return try {
            val devMode = Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                0 // Valeur par défaut si le paramètre n'existe pas
            )
            devMode == 1 // 1 = activé, 0 = désactivé
        } catch (e: Exception) {
            // En cas d'erreur (permissions manquantes), on considère non détecté
            Log.d(TAG, "Impossible de lire le mode développeur: ${e.message}")
            false
        }
    }

    /**
     * DÉTECTEUR 3 : Root
     * 
     * Principe : Un appareil rooté possède le binaire 'su' (Super User)
     * qui permet d'obtenir les droits root. On cherche ce binaire dans
     * les emplacements standards et on teste son exécution.
     * 
     * @return true si l'appareil est rooté
     */
    private fun isRootDetected(): Boolean {
        // Liste des chemins où 'su' est généralement installé
        val suspiciousRootPaths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su",
            "/system/xbin/daemonsu",
            "/system/etc/init.d/99SuperSUDaemon",
            "/dev/com.koushikdutta.superuser.daemon/",
            "/system/app/Superuser.apk",
            "/system/etc/init.d/99SuperSUDaemon"
        )

        // Vérification 1 : Chercher les fichiers/dossiers suspects
        for (path in suspiciousRootPaths) {
            try {
                val file = File(path)
                if (file.exists()) {
                    Log.d(TAG, "Fichier root trouvé: $path")
                    return true
                }
            } catch (e: Exception) {
                // Ignorer les erreurs (permissions, etc.)
            }
        }

        // Vérification 2 : Tenter d'exécuter 'su'
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("which", "su"))
            val result = process.inputStream.bufferedReader().readText()
            process.waitFor()
            result.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * DÉTECTEUR 4 : Émulateur
     * 
     * Principe : Les émulateurs Android ont des caractéristiques matérielles
     * et logicielles spécifiques qu'on peut détecter via l'API Build.
     * On analyse les propriétés du système pour identifier un émulateur.
     * 
     * @return true si l'application tourne sur un émulateur
     */
    private fun isEmulatorDetected(): Boolean {
        // Vérification 1 : Analyse du fingerprint système
        val suspiciousFingerprints = Build.FINGERPRINT.let {
            it.startsWith("generic") ||
            it.startsWith("unknown") ||
            it.contains("test-keys") ||
            it.contains("emulator")
        }
        
        // Vérification 2 : Analyse du modèle d'appareil
        val suspiciousModel = Build.MODEL.let {
            it.contains("google_sdk") ||
            it.contains("Emulator") ||
            it.contains("Android SDK built for x86") ||
            it.contains("sdk_gphone")
        }
        
        // Vérification 3 : Analyse du fabricant
        val suspiciousManufacturer = Build.MANUFACTURER.let {
            it.contains("Genymotion") ||
            it.contains("unknown") ||
            it == "Google" && Build.BRAND == "generic"
        }
        
        // Vérification 4 : Analyse du hardware
        val suspiciousHardware = Build.HARDWARE.let {
            it.contains("goldfish") ||  // Émulateur standard Android
            it.contains("ranchu") ||    // Nouvel émulateur Android
            it.contains("vbox")         // VirtualBox
        }
        
        // Vérification 5 : Analyse du produit
        val suspiciousProduct = Build.PRODUCT.let {
            it.contains("sdk") ||
            it.contains("emulator") ||
            it.contains("simulator")
        }
        
        // Vérification 6 : Analyse du board
        val suspiciousBoard = Build.BOARD.let {
            it.contains("goldfish") ||
            it.contains("ranchu")
        }
        
        // Si au moins une vérification est positive, c'est un émulateur
        return suspiciousFingerprints ||
               suspiciousModel ||
               suspiciousManufacturer ||
               suspiciousHardware ||
               suspiciousProduct ||
               suspiciousBoard
    }

    /**
     * REMÉDIATION : Fermeture immédiate de l'application
     * 
     * Principe : On utilise deux méthodes pour garantir la fermeture :
     * 1. killProcess() : Méthode Android qui tue le processus proprement
     * 2. System.exit() : Sortie système de secours
     */
    private fun killApplication() {
        // Méthode 1 : Tuer le processus Android
        android.os.Process.killProcess(android.os.Process.myPid())
        
        // Méthode 2 : Sortie système forcée (au cas où killProcess échoue)
        System.exit(1)
    }
}
# 🛡️ Android Security Injector - Source Shielder

## 📋 Table des matières

- [Présentation](#-présentation)
- [Fonctionnalités](#-fonctionnalités)
- [Architecture](#-architecture)
- [Installation](#-installation)
- [Utilisation](#-utilisation)
- [Vecteurs d'attaque détectés](#-vecteurs-dattaque-détectés)
- [Exemples](#-exemples)
- [Développement](#-développement)
- [Tests](#-tests)


## 🎯 Présentation

**Android Security Injector** est un outil pédagogique développé dans le cadre du cours HAI913I (Évolution et restructuration des logiciels) à l'Université de Montpellier. Il démontre les principes du **blindage d'applications mobiles** (Application Hardening) en détectant et bloquant automatiquement les vecteurs d'attaque potentiels.

### Contexte

Les applications Android sont vulnérables à plusieurs types d'analyses et d'attaques :
- 🔍 Analyse statique (décompilation)
- 🐛 Analyse dynamique (débogage)
- 📱 Environnements compromis (root, émulateurs)
- 🎣 Interception de données (hooking)

Cet outil automatise l'injection d'un système de protection détectant ces menaces au démarrage de l'application.

## ✨ Fonctionnalités

### Protection automatique

- ✅ **Injection automatisée** : Aucune modification manuelle requise
- ✅ **Détection multi-vecteurs** : 4 vecteurs d'attaque détectés
- ✅ **Remédiation immédiate** : Fermeture de l'app si menace détectée
- ✅ **Logs détaillés** : Traçabilité via Logcat
- ✅ **Protection anti-doublon** : Évite les injections multiples
- ✅ **Impact minimal** : < 50 Ko ajoutés à l'APK

### Compatibilité

- 📱 Android API 26+ (Android 8.0 Oreo)
- 🔷 Kotlin et Java (support partiel)
- 🏗️ Projets Android standards (Gradle)
- 🎨 Jetpack Compose et vues classiques

## 🏗️ Architecture

### Vue d'ensemble

```
┌─────────────────────────────────────────────────┐
│         Android Security Injector               │
│                                                  │
│  ┌────────────┐    ┌──────────────┐            │
│  │  Phase 1   │───▶│  Phase 2     │            │
│  │  Copie     │    │  Parse       │            │
│  │  Shield.kt │    │  Manifest    │            │
│  └────────────┘    └──────────────┘            │
│         │                  │                     │
│         ▼                  ▼                     │
│  ┌────────────┐    ┌──────────────┐            │
│  │  Phase 3   │◀───│  Phase 4     │            │
│  │  Localise  │    │  Injection   │            │
│  │  Activité  │    │  Code        │            │
│  └────────────┘    └──────────────┘            │
│                                                  │
└─────────────────────────────────────────────────┘
```

### Structure du projet

```
source-shielder/
├── resources/
│   └── SecurityShield.kt        # Classe de détection des menaces
├── src/
│   ├── lib/
│   │   └── AndroidManifest.ts   # Parser XML pour AndroidManifest
│   └── index.ts                 # Programme principal d'injection
├── dist/                         # Code compilé (généré)
├── package.json
├── tsconfig.json
└── README.md
```

## 🚀 Installation

### Prérequis

- **Node.js** 20.x ou supérieur ([Télécharger](https://nodejs.org/))
- **npm** 10.x ou supérieur (inclus avec Node.js)
- **Android Studio** (pour tester les apps) ([Télécharger](https://developer.android.com/studio))
- **Git** (optionnel) ([Télécharger](https://git-scm.com/))

### Installation rapide

```bash
# Cloner le dépôt
git clone https://github.com/votre-repo/android-security-injector.git
cd android-security-injector

# Installer les dépendances
npm install

# Compiler le projet
npm run build
```

### Vérification de l'installation

```bash
# Afficher l'aide
npm start

# Sortie attendue :
# ❌ Usage: npm start <chemin_application_android>
```

## 💻 Utilisation

### Utilisation basique

```bash
npm start /chemin/vers/votre/projet/Android
```

### Exemple complet

```bash
# 1. Naviguer vers le dossier du projet
cd android-security-injector

# 2. Injecter les protections dans une application
npm start C:\Users\Username\AndroidStudioProjects\MyApp

# 3. Ouvrir l'application dans Android Studio
# 4. Synchroniser le projet (File → Sync Project with Gradle Files)
# 5. Compiler et tester sur un émulateur
```

### Sortie attendue

```
╔════════════════════════════════════════════════════════════╗
║      🛡️  ANDROID SECURITY INJECTOR - Source Shielder     ║
║           Injection automatique de protections            ║
╚════════════════════════════════════════════════════════════╝

📂 Chemin de l'application cible:
   C:\Users\Username\AndroidStudioProjects\MyApp

✅ Projet Android valide détecté

════════════════════════════════════════════════════════════
  PHASE 1 : INJECTION DU FICHIER DE SÉCURITÉ
════════════════════════════════════════════════════════════

🔍 Recherche du fichier SecurityShield.kt...
   ✓ Fichier source trouvé
📁 Création de l'arborescence de packages...
   ✓ Dossiers créés
📄 Copie du fichier...
   ✓ Fichier copié avec succès

════════════════════════════════════════════════════════════
  PHASE 2 : ANALYSE DU MANIFEST
════════════════════════════════════════════════════════════

🔍 Analyse du fichier AndroidManifest.xml...
   ✓ Fichier XML parsé avec succès
🔎 Recherche de l'activité principale (LAUNCHER)...
   ✓ Activité principale trouvée : MainActivity

════════════════════════════════════════════════════════════
  PHASE 3 : LOCALISATION DE L'ACTIVITÉ
════════════════════════════════════════════════════════════

🔍 Recherche du fichier définissant la classe MainActivity...
   ✓ Fichier trouvé

════════════════════════════════════════════════════════════
  PHASE 4 : INJECTION DU CODE DE PROTECTION
════════════════════════════════════════════════════════════

📖 Lecture du fichier de l'activité...
   ✓ Fichier lu
🔧 Injection de l'import...
   ✓ Import ajouté après les imports existants
🔧 Injection de l'appel à protect()...
   ✓ Code injecté après super.onCreate()
💾 Sauvegarde des modifications...
   ✓ Fichier modifié et sauvegardé avec succès

╔════════════════════════════════════════════════════════════╗
║                ✅ INJECTION RÉUSSIE !                      ║
╚════════════════════════════════════════════════════════════╝
```

## 🎯 Vecteurs d'attaque détectés

### 1. 🐛 Débogueur

**Risque** : Analyse du code en temps réel, modification de variables, contournement de sécurités

**Détection** : Utilisation de l'API `Debug.isDebuggerConnected()`

```kotlin
if (Debug.isDebuggerConnected() || Debug.waitingForDebugger()) {
    // Débogueur détecté
}
```

### 2. 🔧 Mode Développeur

**Risque** : Débogage USB activé, logs verbeux, simulation de localisation

**Détection** : Lecture du paramètre système `DEVELOPMENT_SETTINGS_ENABLED`

```kotlin
val devMode = Settings.Global.getInt(
    context.contentResolver,
    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
    0
)
```

### 3. 🔓 Root

**Risque** : Accès administrateur complet, contournement du sandbox Android, lecture de données d'autres apps

**Détection** : Recherche du binaire `su` et tentative d'exécution

```kotlin
val rootPaths = arrayOf(
    "/system/bin/su",
    "/system/xbin/su",
    "/sbin/su",
    // ... 10+ chemins vérifiés
)
```

### 4. 🖥️ Émulateur

**Risque** : Environnement contrôlé par l'attaquant, absence de protections matérielles, facilité d'analyse

**Détection** : Analyse des propriétés système (`Build.FINGERPRINT`, `Build.HARDWARE`)

```kotlin
if (Build.HARDWARE.contains("goldfish") || 
    Build.HARDWARE.contains("ranchu")) {
    // Émulateur détecté
}
```

## 📖 Exemples

### Exemple 1 : Application simple

**Avant injection** (`MainActivity.kt`) :

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                Greeting("Hello World")
            }
        }
    }
}
```

**Après injection** :

```kotlin
import com.security.shield.SecurityShield  // ← Ajouté automatiquement

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // ═════════════════════════════════════════════════
        // INJECTION AUTOMATIQUE - BLINDAGE DE SÉCURITÉ
        // ═════════════════════════════════════════════════
        SecurityShield.protect(this)  // ← Ajouté automatiquement
        // ═════════════════════════════════════════════════
        
        setContent {
            MyAppTheme {
                Greeting("Hello World")
            }
        }
    }
}
```

### Exemple 2 : Logs de détection

Lorsque l'application est lancée sur un **émulateur** :

```
D/SecurityShield: 🛡️ === INITIALISATION DU BOUCLIER DE SÉCURITÉ ===
W/SecurityShield: ⚠️ MODE DÉVELOPPEUR DÉTECTÉ
W/SecurityShield: ⚠️ ÉMULATEUR DÉTECTÉ
E/SecurityShield: 🚨 MENACES DÉTECTÉES: Mode développeur, Émulateur
E/SecurityShield: 🔒 APPLICATION DE LA REMÉDIATION...
E/SecurityShield: 💀 FERMETURE IMMÉDIATE DE L'APPLICATION
```

Lorsque l'application est lancée sur un **appareil physique sans menace** :

```
D/SecurityShield: 🛡️ === INITIALISATION DU BOUCLIER DE SÉCURITÉ ===
I/SecurityShield: ✅ Aucune menace détectée
I/SecurityShield: ✅ Démarrage de l'application autorisé
```

### Exemple 3 : Réinjection (protection anti-doublon)

```bash
# Première injection
npm start /path/to/app
# ✓ Code injecté avec succès

# Deuxième injection sur le même projet
npm start /path/to/app
# ⚠️ Code déjà présent, injection ignorée
```

## 🛠️ Développement

### Scripts disponibles

```json
{
  "scripts": {
    "build": "tsc",                    // Compiler TypeScript → JavaScript
    "start": "npm run build && node dist/index.js",  // Compiler + Exécuter
    "clean": "rimraf dist/",           // Nettoyer le dossier dist
    "dev": "npm run build && npm start" // Mode développement
  }
}
```

### Structure du code

#### `src/index.ts` - Programme principal

```typescript
async function main() {
    // 1. Vérification des arguments
    // 2. Validation du projet Android
    // 3. Exécution des 4 phases
    // 4. Gestion des erreurs
}
```

#### `src/lib/AndroidManifest.ts` - Parser XML

```typescript
export class AndroidManifestParser {
    public parse(path: string): AndroidManifest {
        // Parse le fichier AndroidManifest.xml
        // Retourne un objet typé
    }
}
```

#### `resources/SecurityShield.kt` - Classe de protection

```kotlin
object SecurityShield {
    @JvmStatic
    fun protect(context: Context) {
        // Exécution des détecteurs
        // Application de la remédiation
    }
}
```

### Ajouter un nouveau détecteur

**Étape 1** : Ajouter la méthode dans `SecurityShield.kt`

```kotlin
private fun isNewThreatDetected(): Boolean {
    // Votre logique de détection
    return false
}
```

**Étape 2** : Appeler le détecteur dans `protect()`

```kotlin
if (isNewThreatDetected()) detectedThreats.add("Nouvelle menace")
```

**Étape 3** : Recompiler et tester

```bash
npm run build
npm start /path/to/test/app
```

## 🧪 Tests

### Tests manuels

```bash
# 1. Créer une application Android de test
# 2. Lancer l'injection
npm start /path/to/test/app

# 3. Ouvrir dans Android Studio
# 4. Compiler et lancer sur émulateur
# 5. Vérifier que l'app se ferme immédiatement

# 6. Vérifier les logs
adb logcat -s SecurityShield
```

### Cas de test

| Test | Environnement | Résultat attendu |
|------|---------------|------------------|
| Test 1 | Émulateur Android Studio | App se ferme |
| Test 2 | Appareil physique (mode dev ON) | App se ferme |
| Test 3 | Appareil physique (mode dev OFF) | App démarre |
| Test 4 | Appareil rooté | App se ferme |
| Test 5 | Réinjection | Doublon évité |

### Débogage

**Problème** : L'application ne se ferme pas sur l'émulateur

```bash
# Vérifier que le code a été injecté
cat /path/to/app/app/src/main/java/com/example/app/MainActivity.kt | grep SecurityShield

# Vérifier les logs
adb logcat | grep -i security

# Reconstruire l'application
cd /path/to/app
./gradlew clean
./gradlew build
```

### Recommandations

🚨 **Cet outil est à but pédagogique et ne doit PAS être utilisé en production sans améliorations significatives.**

Pour une application en production :
- ✅ Utilisez une solution commerciale (DexGuard, Arxan, Promon SHIELD)
- ✅ Obfusquez le code de protection
- ✅ Implémentez la vérification d'intégrité de l'APK
- ✅ Ajoutez la détection de hooking (Frida, Xposed)
- ✅ Utilisez du code natif (C/C++) pour les parties critiques

## 👥 Auteurs

- **Majdi Benboubaker** -


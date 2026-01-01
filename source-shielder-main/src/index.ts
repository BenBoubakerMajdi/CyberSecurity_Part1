import { AndroidManifestParser } from "./lib/AndroidManifest.js";
import * as fs from "fs";
import * as path from "path";

/**
 * Programme d'injection automatique de sécurité pour applications Android
 * 
 * Usage: npm start <chemin_vers_application_android>
 * Exemple: npm start C:\AndroidStudioProjects\ToyApp
 */

/**
 * Point d'entrée principal du programme
 */
async function main() {
    console.log("╔════════════════════════════════════════════════════════════╗");
    console.log("║      🛡️  ANDROID SECURITY INJECTOR - Source Shielder     ║");
    console.log("║           Injection automatique de protections            ║");
    console.log("╚════════════════════════════════════════════════════════════╝");
    console.log("");

    // ========== VÉRIFICATION DES ARGUMENTS ==========
    
    if (process.argv.length < 3) {
        console.error("❌ ERREUR : Chemin de l'application manquant\n");
        console.log("📖 Usage:");
        console.log("   npm start <chemin_application_android>");
        console.log("");
        console.log("📝 Exemple:");
        console.log("   npm start C:\\Users\\majdi\\AndroidStudioProjects\\ToyApp");
        console.log("");
        process.exit(1);
    }

    const appPath = process.argv[2];
    
    console.log("📂 Chemin de l'application cible:");
    console.log(`   ${appPath}`);
    console.log("");

    // Vérification de l'existence du chemin
    if (!fs.existsSync(appPath as fs.PathLike)) {
        console.error(`❌ ERREUR : Le chemin n'existe pas`);
        console.error(`   ${appPath}`);
        console.log("");
        console.log("💡 Vérifiez que :");
        console.log("   - Le chemin est correct");
        console.log("   - Vous utilisez des antislash doubles (\\\\) sous Windows");
        console.log("   - Le dossier n'a pas été déplacé ou supprimé");
        console.log("");
        process.exit(1);
    }

    // Vérification que c'est bien un projet Android
    const manifestPath = path.join(appPath as string, "app", "src", "main", "AndroidManifest.xml");
    if (!fs.existsSync(manifestPath)) {
        console.error(`❌ ERREUR : Ce n'est pas un projet Android valide`);
        console.error(`   AndroidManifest.xml introuvable à :`);
        console.error(`   ${manifestPath}`);
        console.log("");
        console.log("💡 Assurez-vous de pointer vers la racine du projet Android");
        console.log("   (le dossier contenant 'app/', 'gradle/', etc.)");
        console.log("");
        process.exit(1);
    }

    console.log("✅ Projet Android valide détecté");
    console.log("");

    // ========== DÉMARRAGE DU PROCESSUS D'INJECTION ==========

    try {
        console.log("═".repeat(60));
        console.log("  PHASE 1 : INJECTION DU FICHIER DE SÉCURITÉ");
        console.log("═".repeat(60));
        console.log("");
        
        await injectSecurityShieldFile(appPath as string);
        
        console.log("");
        console.log("═".repeat(60));
        console.log("  PHASE 2 : ANALYSE DU MANIFEST");
        console.log("═".repeat(60));
        console.log("");
        
        const mainActivity = await findMainActivity(appPath as string);
        
        console.log("");
        console.log("═".repeat(60));
        console.log("  PHASE 3 : LOCALISATION DE L'ACTIVITÉ");
        console.log("═".repeat(60));
        console.log("");
        
        const activityFilePath = await findActivityFile(
			appPath as string,
			mainActivity
		);
        
        console.log("");
        console.log("═".repeat(60));
        console.log("  PHASE 4 : INJECTION DU CODE DE PROTECTION");
        console.log("═".repeat(60));
        console.log("");
        
        await injectProtectionCall(activityFilePath);
        
        console.log("");
        console.log("╔════════════════════════════════════════════════════════════╗");
        console.log("║                ✅ INJECTION RÉUSSIE !                      ║");
        console.log("╚════════════════════════════════════════════════════════════╝");
        console.log("");
        console.log("📱 Prochaines étapes :");
        console.log("   1. Ouvrez l'application dans Android Studio");
        console.log("   2. Synchronisez le projet (File → Sync Project with Gradle Files)");
        console.log("   3. Compilez et testez sur un émulateur");
        console.log("   4. L'application devrait se fermer immédiatement");
        console.log("");
        
    } catch (error) {
        console.log("");
        console.log("╔════════════════════════════════════════════════════════════╗");
        console.log("║                  ❌ ERREUR CRITIQUE                        ║");
        console.log("╚════════════════════════════════════════════════════════════╝");
        console.log("");
        
        if (error instanceof Error) {
            console.error("Message d'erreur :");
            console.error(`   ${error.message}`);
            
            if (error.stack) {
                console.log("");
                console.log("Stack trace (pour debugging) :");
                console.error(error.stack);
            }
        } else {
            console.error("Erreur inconnue :", error);
        }
        
        console.log("");
        process.exit(1);
    }
}

/**
 * PHASE 1 : Copie le fichier SecurityShield.kt dans le projet Android
 */
async function injectSecurityShieldFile(appPath: string): Promise<void> {
    console.log("🔍 Recherche du fichier SecurityShield.kt...");
    
    // Le fichier source est dans resources/ à la racine du projet
    const sourceFile = path.join(process.cwd(), "resources", "SecurityShield.kt");
    
    // Vérifier que le fichier source existe
    if (!fs.existsSync(sourceFile)) {
        throw new Error(
            `Le fichier SecurityShield.kt est introuvable.\n` +
            `   Attendu à : ${sourceFile}\n` +
            `   Assurez-vous que le dossier resources/ existe à la racine du projet.`
        );
    }
    
    console.log(`   ✓ Fichier source trouvé`);
    
    // Créer le chemin de destination selon la nomenclature Java
    // Package: com.security.shield
    // Chemin: app/src/main/java/com/security/shield/SecurityShield.kt
    const destDir = path.join(
        appPath,
        "app", "src", "main", "java",
        "com", "security", "shield"
    );
    const destFile = path.join(destDir, "SecurityShield.kt");
    
    console.log("📁 Création de l'arborescence de packages...");
    console.log(`   ${destDir}`);
    
    // Créer les dossiers si nécessaire (récursivement)
    fs.mkdirSync(destDir, { recursive: true });
    console.log("   ✓ Dossiers créés");
    
    console.log("📄 Copie du fichier...");
    
    // Copier le fichier
    fs.copyFileSync(sourceFile, destFile);
    
    console.log(`   ✓ Fichier copié avec succès`);
    console.log(`   📍 Emplacement : ${destFile}`);
}

/**
 * PHASE 2 : Trouve l'activité principale dans AndroidManifest.xml
 */
async function findMainActivity(appPath: string): Promise<string> {
    const manifestPath = path.join(appPath, "app", "src", "main", "AndroidManifest.xml");
    
    console.log("🔍 Analyse du fichier AndroidManifest.xml...");
    console.log(`   ${manifestPath}`);
    
    // Parser le manifest avec la classe fournie
    const parser = new AndroidManifestParser();
    const manifest = parser.parse(manifestPath);
    
    console.log("   ✓ Fichier XML parsé avec succès");
    
    console.log("🔎 Recherche de l'activité principale (LAUNCHER)...");
    
    // Récupérer les activités
    const activities = manifest.manifest.application.activity ?? [];
    
    if (activities.length === 0) {
        throw new Error("Aucune activité trouvée dans le manifest");
    }
    
    console.log(`   ℹ️  ${activities.length} activité(s) trouvée(s)`);
    
    // Chercher l'activité avec l'intent-filter MAIN + LAUNCHER
    for (const activity of activities) {
        const activityName = activity["@android:name"];
        
        if (!activity["intent-filter"]) continue;
        
        const intentFilters = activity["intent-filter"];
        
        for (const filter of intentFilters) {
            const action = filter.action;
            const category = filter.category;
            
            // Vérifier si c'est l'intent MAIN
            if (action["@android:name"] === "android.intent.action.MAIN") {
                // Extraire le nom de la classe (dernier élément du package)
                const className = activityName.split(".").pop() || activityName;
                
                console.log(`   ✓ Activité principale trouvée : ${activityName}`);
                console.log(`   📌 Nom de la classe : ${className}`);
                
                return className;
            }
        }
    }
    
    throw new Error(
        "Aucune activité principale trouvée dans AndroidManifest.xml\n" +
        "   L'activité principale doit avoir l'intent-filter MAIN"
    );
}

/**
 * PHASE 3 : Trouve le fichier .kt qui contient la classe de l'activité
 */
async function findActivityFile(appPath: string, activityName: string): Promise<string> {
    const javaDir = path.join(appPath, "app", "src", "main", "java");
    
    console.log(`🔍 Recherche du fichier définissant la classe ${activityName}...`);
    console.log(`   Scan du dossier : ${javaDir}`);
    
    if (!fs.existsSync(javaDir)) {
        throw new Error(
            `Le dossier java/ est introuvable :\n` +
            `   ${javaDir}`
        );
    }
    
    /**
     * Fonction récursive pour parcourir tous les fichiers .kt
     */
    function searchKotlinFiles(dir: string, depth: number = 0): string | null {
        const files = fs.readdirSync(dir);
        
        for (const file of files) {
            const filePath = path.join(dir, file);
            const stat = fs.statSync(filePath);
            
            if (stat.isDirectory()) {
                // Recherche récursive dans les sous-dossiers
                const result = searchKotlinFiles(filePath, depth + 1);
                if (result) return result;
            } else if (file.endsWith(".kt")) {
                // Lire le contenu du fichier Kotlin
                const content = fs.readFileSync(filePath, "utf-8");
                
                // Chercher la déclaration de la classe
                // Pattern: class NomActivité : ... ou class NomActivité(...)
                const classRegex = new RegExp(
                    `\\bclass\\s+${activityName}\\s*[:\\(]`,
                    "m"
                );
                
                if (classRegex.test(content)) {
                    return filePath;
                }
            }
        }
        
        return null;
    }
    
    const result = searchKotlinFiles(javaDir);
    
    if (!result) {
        throw new Error(
            `Impossible de trouver le fichier définissant la classe ${activityName}\n` +
            `   Vérifiez que le fichier existe dans app/src/main/java/`
        );
    }
    
    console.log(`   ✓ Fichier trouvé`);
    console.log(`   📍 ${result}`);
    
    return result;
}

/**
 * PHASE 4 : Injecte l'appel à SecurityShield.protect() dans onCreate()
 */
async function injectProtectionCall(activityFilePath: string): Promise<void> {
    console.log("📖 Lecture du fichier de l'activité...");
    
    // Lire le contenu du fichier
    let content = fs.readFileSync(activityFilePath, "utf-8");
    
    console.log("   ✓ Fichier lu");
    
    // Vérifier si l'injection a déjà été faite
    if (content.includes("SecurityShield.protect")) {
        console.log("");
        console.log("⚠️  ATTENTION : Le code de protection est déjà présent dans ce fichier");
        console.log("   L'injection est ignorée pour éviter les doublons");
        console.log("");
        return;
    }
    
    console.log("🔧 Injection de l'import...");
    
    // ========== ÉTAPE 1 : Ajouter l'import de SecurityShield ==========
    
    const importStatement = "import com.security.shield.SecurityShield";
    
    if (!content.includes(importStatement)) {
        // Trouver la dernière ligne d'import existante
        const importRegex = /import\s+[\w.]+/g;
        const imports = content.match(importRegex);
        
        if (imports && imports.length > 0) {
            // Insérer après le dernier import
            const lastImport = imports[imports.length - 1];
            content = content.replace(
                lastImport as string,
                `${lastImport}\n${importStatement}`
            );
            console.log("   ✓ Import ajouté après les imports existants");
        } else {
            // Si pas d'imports, ajouter après le package
            const packageRegex = /package\s+[\w.]+/;
            content = content.replace(
                packageRegex,
                `$&\n\n${importStatement}`
            );
            console.log("   ✓ Import ajouté après la déclaration du package");
        }
    } else {
        console.log("   ℹ️  Import déjà présent");
    }
    
    console.log("🔧 Injection de l'appel à protect()...");
    
    // ========== ÉTAPE 2 : Injecter l'appel dans onCreate() ==========
    
    // Trouver la méthode onCreate
    const onCreateRegex = /(override\s+fun\s+onCreate\s*\([^)]*\)\s*\{)/;
    const onCreateMatch = content.match(onCreateRegex);
    
    if (!onCreateMatch) {
        throw new Error(
            "La méthode onCreate() est introuvable dans le fichier\n" +
            "   Assurez-vous que l'activité hérite de ComponentActivity ou AppCompatActivity"
        );
    }
    
    // Code à injecter
    const injectionCode = `
        // ╔════════════════════════════════════════════════════════════╗
        // ║         INJECTION AUTOMATIQUE - BLINDAGE DE SÉCURITÉ       ║
        // ║  Ce code détecte et bloque les vecteurs d'attaque au      ║
        // ║  démarrage de l'application (émulateur, root, etc.)       ║
        // ╚════════════════════════════════════════════════════════════╝
        SecurityShield.protect(this)
        // ═══════════════ FIN DE L'INJECTION AUTOMATIQUE ═════════════
        `;
    
    // Chercher super.onCreate() pour injecter juste après
    const superOnCreateRegex = /(super\.onCreate\([^)]*\))/;
    
    if (superOnCreateRegex.test(content)) {
        // Injecter après super.onCreate()
        content = content.replace(
            superOnCreateRegex,
            `$1${injectionCode}`
        );
        console.log("   ✓ Code injecté après super.onCreate()");
    } else {
        // Sinon injecter directement après l'accolade de onCreate
        content = content.replace(
            onCreateRegex,
            `$1${injectionCode}`
        );
        console.log("   ✓ Code injecté au début de onCreate()");
    }
    
	console.log("💾 Sauvegarde des modifications...");
    
    // Écrire le fichier modifié
    fs.writeFileSync(activityFilePath, content, "utf-8");
    
    console.log("   ✓ Fichier modifié et sauvegardé avec succès");
}

// ========== LANCEMENT DU PROGRAMME ==========

main();

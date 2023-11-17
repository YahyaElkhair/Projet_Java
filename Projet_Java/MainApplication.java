import java.io.File;
import java.util.*;

public class MainApplication {

    public static void main(String[] args) {
        // Initialisation du scanner pour la saisie utilisateur
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter le chemin du dossier : ");
        
        // Lecture du chemin du dossier fourni par l'utilisateur
        String cheminDossier = sc.nextLine();
        
        // Appel de la fonction pour détecter les fichiers en double
        FichiersDoubles(cheminDossier);
    }

    public static void FichiersDoubles(String cheminDossier) {
        // Création d'un objet File pour représenter le dossier
        File dossier = new File(cheminDossier);

        // Vérification de l'existence et du type du dossier
        if (!dossier.exists() || !dossier.isDirectory()) {
            System.err.println("Le dossier spécifié n'existe pas ou n'est pas un dossier valide.");
            return; // Sortie de la fonction si le dossier n'est pas valide
        }

        // Création d'une Map pour stocker les fichiers par leur nom
        Map<String, List<String>> nomFichier = new HashMap<>();
        
        // Appel de la fonction récursive pour détecter les fichiers en double
        detecterFichiersDoubles(dossier, nomFichier);

        // Affichage des fichiers en double
        System.out.println("Fichiers en double :");
        for (Map.Entry<String, List<String>> entry : nomFichier.entrySet()) {
            List<String> fichiers = entry.getValue();
            if (fichiers.size() > 1) {
                System.out.println("Nom du fichier : " + entry.getKey());
                for (String chemin : fichiers) {
                    System.out.println("   Chemin du fichier : " + chemin);
                }
            }
            System.out.println("------------------------------------");
        }
    }

    // Fonction récursive pour détecter les fichiers en double
    private static void detecterFichiersDoubles(File dossier, Map<String, List<String>> fichiersParNom) {
        // Récupération de la liste des fichiers et sous-dossiers dans le dossier
        File[] fichiersEtDossiers = dossier.listFiles();

        // Vérification si la liste est null (par exemple, si le dossier est vide)
        if (fichiersEtDossiers == null) {
            return;
        }

        // Parcours des fichiers et sous-dossiers
        for (File fichierOuDossier : fichiersEtDossiers) {
            if (fichierOuDossier.isDirectory()) {
                // Si c'est un dossier, récursion pour explorer les sous-dossiers
                detecterFichiersDoubles(fichierOuDossier, fichiersParNom);
            } else {
                // Si c'est un fichier, vérification s'il existe déjà dans la liste
                String nomFichier = fichierOuDossier.getName();
                String cheminFichier = fichierOuDossier.getAbsolutePath();

                // Ajout du chemin du fichier à la liste correspondante dans la Map
                fichiersParNom.computeIfAbsent(nomFichier, k -> new ArrayList<>()).add(cheminFichier);
            }
        }
    }
}

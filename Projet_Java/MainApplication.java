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
        Map<String, List<String>> nomFichier = FichiersDoubles(cheminDossier);
    
        // Vérification si des fichiers sont en double
        boolean fichiersEnDouble = afficherFichiersDoubles(nomFichier);

        // Si aucun fichier en double
        if (!fichiersEnDouble) {
            System.out.println("Aucun fichier en double.");
        } else {
            System.out.println("Entrez 1 pour supprimer un fichier : ");
            System.out.println("Entrez 0 pour quitter : ");
            System.out.print("Choix : ");
            int x = sc.nextInt();
            sc.nextLine();
            switch (x) {
                case 0:
                    break;
                case 1:
                    System.out.print("Entrez le nom du fichier à supprimer : ");
                    String fichierASupprimer = sc.nextLine();
                
                    // Appel de la fonction pour supprimer le fichier
                    supprimerFichierDoublant(fichierASupprimer, nomFichier);
                
                    // Réaffichage après suppression
                    afficherFichiersDoubles(nomFichier);
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
    
    
    public static Map<String, List<String>> FichiersDoubles(String cheminDossier) {
        // Création d'un objet File pour représenter le dossier
        File dossier = new File(cheminDossier);

        // Vérification de l'existence et du type du dossier
        if (!dossier.exists() || !dossier.isDirectory()) {
            System.err.println("Le dossier spécifié n'existe pas ou n'est pas un dossier valide.");
        }

        // Création d'une Map pour stocker les fichiers par leur nom
        Map<String, List<String>> nomFichier = new HashMap<>();
        
        // Appel de la fonction récursive pour détecter les fichiers en double
        detecterFichiersDoubles(dossier, nomFichier);

        return nomFichier;
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

    // Fonction pour afficher les fichiers en double
    private static boolean afficherFichiersDoubles(Map<String, List<String>> nomFichier) {
        boolean fichiersEnDouble = false;
        // Affichage des fichiers en double
        System.out.println("Fichiers en double :");
        for (Map.Entry<String, List<String>> entry : nomFichier.entrySet()) {
            List<String> fichiers = entry.getValue();
            if (fichiers.size() > 1) {
                fichiersEnDouble = true;
                System.out.println("Nom du fichier : " + entry.getKey());
                for (String chemin : fichiers) {
                    System.out.println("   Chemin du fichier : " + chemin);
                }
                System.out.println("------------------------------------");
            }
        }
        return fichiersEnDouble;
    }

    public static void supprimerFichierDoublant(String nomFichier, Map<String, List<String>> fichiersParNom) {
        // Vérification si le fichier existe en doublon
        if (fichiersParNom.containsKey(nomFichier)) {
            List<String> cheminsFichiers = fichiersParNom.get(nomFichier);
    
            // Si le fichier a des doublons
            if (cheminsFichiers.size() > 1) {
                Scanner sc = new Scanner(System.in);
                System.out.println("Les chemins des fichiers doublants pour " + nomFichier + " sont :");
                for (String chemin : cheminsFichiers) {
                    System.out.println(chemin);
                }
    
                System.out.println("Entrez le numéro du fichier que vous voulez supprimer (1 - " + cheminsFichiers.size() + "): ");
                int numeroFichierASupprimer = sc.nextInt();
    
                // Suppression du fichier choisi par l'utilisateur
                String cheminFichierASupprimer = cheminsFichiers.get(numeroFichierASupprimer - 1);
                File fichierASupprimer = new File(cheminFichierASupprimer);
    
                // Vérification et suppression du fichier
                if (fichierASupprimer.exists()) {
                    if (fichierASupprimer.delete()) {
                        System.out.println("Le fichier a été supprimé avec succès.");
                        // Mettre à jour la liste des fichiers dans la Map
                        cheminsFichiers.remove(numeroFichierASupprimer - 1);
                    } else {
                        System.out.println("Erreur lors de la suppression du fichier.");
                    }
                } else {
                    System.out.println("Le fichier n'existe pas.");
                }
            } else {
                System.out.println("Le fichier n'a pas de doublons.");
            }
        } else {
            System.out.println("Ce fichier n'existe pas en doublon dans le dossier.");
        }
    }
}

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;


public class MainApplication {
    public static void main(String[] args) {

        System.out.println();
        System.out.println();

        System.out.println("                           **********************************************************");   
    	System.out.println("                           *          APPLICATEUR POUR DETECTER LES FICHIERS        *");
     	System.out.println("                           *                 DOUBLE DANS UN DOSSIER                 *");
     	System.out.println("                           **********************************************************");   
        System.out.println();
        System.out.println();
        // Initialisation du scanner pour la saisie utilisateur
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter le chemin du dossier A chercher: ");
        
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
            System.out.println();
            System.out.println("Entrez 1 pour supprimer un fichier !!");
            System.out.println("Entrez 0 pour quitter !!");
              System.out.println();
            System.out.print("Choix : ");
            int x = sc.nextInt();
            sc.nextLine();
            switch (x) {
                case 0:
                    break;
                case 1:
                    // Appel de la fonction pour supprimer le fichier
                    sauvegarderEtSupprimerFichierDoublant(nomFichier);
                
                    // Réaffichage après suppression
                    System.out.println("------------------- Mise a jour le dossier -------------------");
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
              System.out.println();
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

        System.out.println();
        // Affichage des fichiers en double
        System.out.println();
        System.out.println("Fichiers en double :");
        System.out.println();
        for (Map.Entry<String, List<String>> entry : nomFichier.entrySet()) {
            List<String> fichiers = entry.getValue();
            if (fichiers.size() > 1) {
                fichiersEnDouble = true;
                  System.out.println();
                System.out.println("Nom du fichier : " + entry.getKey());
                for (String chemin : fichiers) {
                      System.out.println();
                    System.out.println("   Chemin du fichier : " + chemin);
                }
                 System.out.println();
                System.out.println("------------------------------------");
            }
        }
        return fichiersEnDouble;
    }
    
    public static void sauvegarderEtSupprimerFichierDoublant(Map<String, List<String>> fichiersParNom) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("------------------------------------");
            System.out.println();
            System.out.print("Entrez le nom du fichier à sauvegarder et supprimer : ");
            String fichierASauvegarder = sc.nextLine();

            if (fichiersParNom.containsKey(fichierASauvegarder)) {
                List<String> cheminsFichiers = fichiersParNom.get(fichierASauvegarder);

                if (cheminsFichiers.size() > 1) {
                    System.out.println();                  
                    System.out.println("Les chemins des fichiers doublants pour " + fichierASauvegarder + " sont :");
                    System.out.println();
                    for (int i = 0; i < cheminsFichiers.size(); i++) {
                        System.out.println((i + 1) + ". " + cheminsFichiers.get(i));
                    } System.out.println();
                    System.out.println("------------------------------------");
                    System.out.println();
                    System.out.println("Entrez le numéro du fichier que vous voulez supprimer (1 - " + cheminsFichiers.size() + "): ");
                    
                    int numeroFichierASauvegarder = sc.nextInt();
                    sc.nextLine();

                    String cheminFichierASauvegarder = cheminsFichiers.get(numeroFichierASauvegarder - 1);
                    File fichierASupprimer = new File(cheminFichierASauvegarder);
                    System.out.println();
                    System.out.println("Entrer 2 pour arrchiver le fichier supprimer dans un autre dossier.");
                    System.out.println("Entrer 0 pour quitter.");
                    System.out.println();
                    System.out.print("Votre choix : ");

                    int x = sc.nextInt();
                    sc.nextLine();
                    switch (x) {
                        case 2:
                         System.out.println("------------------------------------");
                            System.out.print("Entrez le chemin du dossier de sauvegarde : ");
                            String cheminDossierSauvegarde = sc.nextLine();
                            sauvegarderFichiers(Collections.singletonList(cheminFichierASauvegarder), cheminDossierSauvegarde);
                            break;
                        case 0:
                            break;
                        default:
                            System.out.println("Choix invalid");
                            break;
                    }

                    if (fichierASupprimer.exists() && fichierASupprimer.delete()) {
                         System.out.println();
                        System.out.println("Le fichier a été supprimé avec succès !!");
                        cheminsFichiers.remove(numeroFichierASauvegarder - 1);
                        break;
                    } else {
                        System.out.println();
                        System.out.println("Erreur lors de la suppression du fichier.");
                    }
                } else {
                      System.out.println();
                    System.out.println("Le fichier n'a pas de doublons.");
                }
            } else {
                  System.out.println();
                System.out.println("Ce fichier n'existe pas en doublon dans le dossier.");
            }
        }
    }

    public static void sauvegarderFichiers(List<String> cheminsFichiersASupprimer, String dossierSauvegarde) {
        File dossierBackup = new File(dossierSauvegarde);
        if (!dossierBackup.exists()) {
            dossierBackup.mkdirs();
        }

        for (String chemin : cheminsFichiersASupprimer) {
            File fichierASupprimer = new File(chemin);
            if (fichierASupprimer.exists()) {
                File fichierSauvegarde = new File(dossierBackup, fichierASupprimer.getName());

                try {
                    Files.copy(fichierASupprimer.toPath(), fichierSauvegarde.toPath(), StandardCopyOption.REPLACE_EXISTING);
                      System.out.println();
                    System.out.println("Fichier sauvegardé avec succes : " + fichierSauvegarde.getAbsolutePath());
                } catch (IOException e) {
                      System.out.println();
                    System.err.println("Erreur lors de la sauvegarde du fichier : " + chemin);
                    e.printStackTrace();
                }
            } else {
                  System.out.println();
                System.out.println("Le fichier à sauvegarder n'existe pas : " + chemin);
            }
        }
    }
}

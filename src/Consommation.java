import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Consommation {
    // Attributs de notre classe clients
    Vector<String> vNomP = new Vector<>();
    Vector<String> vPrixP = new Vector<>();
    Vector<String> vStockP = new Vector<>();

    Reservations reservation;
    String prodAchete;
    int quantiteAchetes;

    // Constructeur classe

    public Consommation(Reservations res, String n, int qtt) {
        reservation = res;
        prodAchete = n;
        quantiteAchetes = qtt;
        achat(n);
    }

    public Consommation() {
        creationTables();
    }

    // Methode pour remplir le tableau de nos produits
    public String[][] tableComplete() {
        String[][] resultat = new String[vNomP.size()][3];
        for (int i = 0; i < vNomP.size(); i++) {
            resultat[i][0] = vNomP.get(i).substring(0, 1).toUpperCase() + vNomP.get(i).substring(1).toLowerCase();
            resultat[i][1] = vPrixP.get(i);
            resultat[i][2] = vStockP.get(i);
        }
        return resultat;
    }

    // Methode avec boucle pour reduire le stock quand un produit est achete
    public void achat(String n) {
        int compt = 0;
        // On cree un fichier ainsi qu'un lecteur de fichier
        File fichier = new File("Ressources/Produits.csv");
        try (FileReader reader = new FileReader(fichier)) {
            BufferedReader lecteur = new BufferedReader(reader);
            String ligne;
            // On fait une boucle qui nous permettra de lire jusqu'a qu'on trouver une ligne
            // vide
            int numeroLigne = 0;
            while ((ligne = lecteur.readLine()) != null) {
                // Saut de la premiere ligne
                if (compt != 0) {
                    // Separation du contenu de la ligne par rapport aux virgules separant les
                    // elements
                    String[] casses = ligne.split(",");
                    // on trouve le nom du produit acheté
                    if (casses[0].equals(n)) {
                        // On crée un fichier temporaire ou on ecrira les nouvelles modifications
                        File fichierTemporarire = new File("Ressources/temp.csv");
                        try {
                            // On crée les lecteurs et ecriteurs :
                            FileReader lecteur2 = new FileReader(fichier);
                            BufferedReader bufferedReader = new BufferedReader(lecteur2);
                            FileWriter ecriteur = new FileWriter(fichierTemporarire);
                            String line;
                            int cont = 0;
                            // On lit les elements du fichier original
                            while ((line = bufferedReader.readLine()) != null) {
                                // En arrivant á la ligne( on ajoute 1 á cause de la premiere ligne sauté)
                                if (numeroLigne + 1 == cont) {
                                    String[] values = line.split(",");
                                    // On reduit la quantité acheté du tableau
                                    values[2] = String.valueOf(Integer.valueOf(values[2]) - quantiteAchetes);
                                    // Puis on recrit la ligne avec le tableau (En separant avec des virgules)
                                    line = String.join(",", values);
                                }
                                // En tout cas, quoi qu'il arrive on réecrit la ligne mais sans modifier
                                ecriteur.write(line + "\n");
                                // On continue a compter les lignes du fichier
                                cont++;
                            }
                            // On ferme nos ecriteurs et lecteurs
                            ecriteur.close();
                            bufferedReader.close();
                            lecteur2.close();
                            // On va maintenant passer tout du fichier temporaire au original
                            // Pour cela on va tout effacer du original :
                            BufferedWriter ecrire = new BufferedWriter(new FileWriter(fichier, false));
                            ecrire.write("");// Append = false fait qu'on recrive sur un fichier, non ajouter
                            ecrire.close();
                            // On recrée un lecteur et un ecriteur, mais cette fois dans le sens oppossé
                            BufferedReader lector = new BufferedReader(new FileReader(fichierTemporarire));
                            BufferedWriter ecrire2 = new BufferedWriter(new FileWriter(fichier));
                            String linea;

                            while ((linea = lector.readLine()) != null) {
                                // On parcoure le fichier en ecrivant ce que le lecteur lit
                                ecrire2.write(linea);
                                ecrire2.newLine();
                                // On recopie donc tout le fichier
                            }
                            // On ferme
                            lector.close();
                            ecrire2.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    numeroLigne++;
                } else {
                    compt++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        creationTables();
    }

    public Double prixTotal() {
        creationTables();
        Double resultat = 0.0;
        for (int i = 0; i < vNomP.size(); i++) {
            if (tableComplete()[i][0].equals(prodAchete)) {
                resultat = Double.parseDouble(tableComplete()[i][1]) * quantiteAchetes;
            }
        }
        return resultat;
    }

    public void creationTables() {
        vNomP.clear();
        vPrixP.clear();
        vStockP.clear();
        int compt = 0;
        // On cree un fichier ainsi qu'un lecteur de fichier
        File fichier = new File("Ressources/Produits.csv");
        try (FileReader reader = new FileReader(fichier)) {
            BufferedReader lecteur = new BufferedReader(reader);
            String ligne;
            // On fait une boucle qui nous permettra de lire jusqu'a qu'on trouver une ligne
            // vide
            while ((ligne = lecteur.readLine()) != null) {
                // Saut de la premiere ligne
                if (compt != 0) {
                    // Separation du contenu de la ligne par rapport aux virgules separant les
                    // elements
                    String[] casses = ligne.split(",");
                    // Ajout de la chambre en question á aprtir des casses du fichier (ordre précis)
                    vNomP.add(casses[0]);
                    vPrixP.add(casses[1]);
                    vStockP.add(casses[2]);
                } else {
                    compt++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservations {
    // Attributs des reservations
    int idReservation;
    Date dateDeb;
    Date dateFin;
    Clients client;
    Chambres chambre;
    Sejour sejour;
    Boolean active = false;
    private static String docReservations = "Ressources/Reservations.csv";
    private static String docSejoursTermines = "Ressources/SejoursTermines.csv";
    // On crée un format pour nos dates
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

    // Constructeur classe
    public Reservations(int id, Date date, Date date2, Clients cl, Chambres ch, Boolean a) {
        idReservation = id;
        dateDeb = (Date) date;
        dateFin = (Date) date2;
        client = cl;
        chambre = ch;
        active = a;
        if (a == true) {
            sejour = new Sejour(idReservation, this);
        }

    }

    // Méthodes renvoyant des informations sur la résrvation
    public String info() {
        return "La reservation commence le " + formatDate.format(dateDeb) + " et termine le "
                + formatDate.format(dateFin) +
                ".\nElle est au nom de " + client.prenom + " " + client.nom + ", et les revenus potentielles sont de "
                + revenus() + ". \nEt sera dans la chambre numero " + chambre.noChambre;
    }

    public Double revenus() {
        // On calcul le nombre de nuit qui dure le sejour et on le multiplie au prix par
        // nuit, pour ensuite rajouter la consommation totale par les clients
        java.sql.Date dateD = new java.sql.Date(dateDeb.getTime());
        java.sql.Date dateF = new java.sql.Date(dateFin.getTime());
        if (sejour != null) {
            return chambre.prix * chambre.hotel.obtentionPeriode(dateD, dateF).size() - 1 + sejour.prixTotal();
        }
        return chambre.prix * chambre.hotel.obtentionPeriode(dateD, dateF).size() - 1;
    }

    public Boolean intersect(Date dateD, Date dateF) {
        // On test si une des dates se trouve entre les dates de reservation
        if ((dateD.after(dateDeb) && dateD.before(dateFin)) || (dateF.after(dateDeb) && dateF.before(dateFin))) {
            // Si c'est le cas, nous renvoyons que ces dates ne sont pas disponibles
            return false;
        } else {
            // Si non, on renvoie qu'elles sont disponibles
            return true;
        }

    }

    public void eliminer() {
        try {
            File tempFile = File.createTempFile("tempfile", ".csv");

            // On crée nos leceteurs et ecriteurs
            BufferedReader reader = new BufferedReader(new FileReader(docReservations));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String ligne;
            int compt = 0;
            // On parcours notre fichier
            while ((ligne = reader.readLine()) != null) {
                // On saute la premiere ligne
                if (compt != 0) {
                    int idLigne = Integer.parseInt(ligne.split(",")[0]);
                    // Si l'id de la ligne ne correspond pas á notre resrevation on le laisse = on
                    // le reecrit. si non on l'elimine = on ne le réecrit pas
                    if (idReservation != idLigne) {
                        writer.write(ligne);
                        writer.newLine();
                    }
                } else { // On reecrit la premiere ligne d'abord
                    writer.write(ligne);
                    writer.newLine();
                }
                compt++;
            }
            // On ferme les lecteurs et écriteurs et on remplace nos fichiers
            reader.close();
            writer.close();

            File file = new File(docReservations);
            file.delete();

            tempFile.renameTo(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void modifier(String type, String modif) {
        try {
            File tempFile = File.createTempFile("tempfile", ".csv");

            // On crée nos lecteurs et ecriteurs
            BufferedReader reader = new BufferedReader(new FileReader(docReservations));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String premiereLigne = "";
            String ligne;
            int compt = 0;
            // On parcours notre fichier
            while ((ligne = reader.readLine()) != null) {
                // On saute la prmiere ligne
                if (compt != 0) {
                    int idLigne = Integer.parseInt(ligne.split(",")[0]);
                    // si la reservation ne correspond pas on ne la modifie pas
                    if (idReservation != idLigne) {
                        writer.write(ligne);
                        writer.newLine();
                    } else {// si elle correspond
                        String[] casses1 = premiereLigne.split(",");
                        String[] casses = ligne.split(",");
                        String texte = "";
                        // on parcours les colonnes
                        for (int i = 0; i < casses.length; i++) {
                            // On va reecrire toutes les colonnes pareilles, sauf celle correspondante á la
                            // modification qu'on veut faire
                            if (casses1[i].equals(type) && i != casses.length - 1) {
                                texte += modif + ",";
                            } else if (casses1[i].equals(type)) {
                                // On ecrit la modification qu'on avait donnée en parametre
                                texte += modif;
                            } else if (i != casses.length - 1) {
                                texte += casses[i] + ",";
                            } else {
                                texte += casses[i];
                            }
                        }
                        writer.write(texte);
                        writer.newLine();
                    }

                } else { // Nous allons sauvegarder la premiere ligne
                    premiereLigne = ligne;
                    writer.write(ligne);
                    writer.newLine();
                }
                compt++;
            }
            // On ferme et on remplace nos fichiers
            reader.close();
            writer.close();

            File file = new File(docReservations);
            file.delete();

            tempFile.renameTo(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthodes pour ativer ou terminer une reservation
    public void checkIn() {
        try {
            // On crée un fichier temporaire
            File tempFile = File.createTempFile("tempfile", ".csv");
            // On déclare nos lecteurs(document des reservations) et ecriteurs(fichier
            // temporaire)
            BufferedReader reader = new BufferedReader(new FileReader(docReservations));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String ligne;

            // On parcours chque ligne de notre document
            while ((ligne = reader.readLine()) != null) {
                // On transforme la ligne dans un tableau en utilisant la separation par
                // virgules
                String[] casses = ligne.split(",");
                // On vérifie si la ligne crresponde á notre reservation
                if (casses[0].equals(Integer.toString(idReservation)) == false) {
                    // Si non on écrit telquelle la ligne
                    writer.write(ligne);
                    writer.newLine();
                } else {
                    // Et si c'est le cas on va récreer la ligne mais en modifiant la colonne
                    // correspondant á la situatin de la reservations en la rendant active
                    String newLigne = "";
                    for (int i = 0; i < casses.length - 1; i++) {
                        newLigne += casses[i] + ",";
                    }
                    newLigne += "true";
                    writer.write(newLigne);
                    writer.newLine();
                }
            }
            // On ferme nos lecteurs et écriteurs
            reader.close();
            writer.close();

            // Puis on remplace le fichier original par le temporaire
            File file = new File(docReservations);
            file.delete();
            tempFile.renameTo(file);
        } catch (Exception e) {
        }

    }

    public void checkOut() {
        try {
            // On crée un fichier temporaire
            File tempFile = File.createTempFile("tempfile", ".csv");
            // On déclare nos lecteurs(document des reservations) et ecriteurs(fichier
            // temporaire)
            BufferedReader reader = new BufferedReader(new FileReader(docReservations));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String ligne;

            // On parcours chque ligne de notre document
            while ((ligne = reader.readLine()) != null) {
                // On transforme la ligne dans un tableau en utilisant la separation par
                // virgules
                String[] casses = ligne.split(",");
                // On vérifie si la ligne crresponde á notre reservation
                if (casses[0].equals(Integer.toString(idReservation)) == false) {
                    // Si non on écrit tel quelle la ligne pour eliminer la reservation
                    writer.write(ligne);
                    writer.newLine();
                }
            }
            // On ferme nos lecteurs et écriteurs
            reader.close();
            writer.close();

            // Puis on remplace le fichier original par le temporaire
            File file = new File(docReservations);
            file.delete();
            tempFile.renameTo(file);

            // On ouvre notre fichier avec les reservations terminés
            File document = new File(docSejoursTermines);
            // On declare on nouveau écriteur
            FileWriter writer2 = new FileWriter(document, true);
            // Et on rajoute les informations de notre reservation dans ce fichier
            String text = formatDate.format(dateDeb) + "," + formatDate.format(dateFin) + "," + client.nom + ","
                    + client.prenom + "," + chambre.noChambre + "," + String.valueOf(revenus()) + "\n";
            writer2.append(text);

            // On ferme nos lecteurs et écriteurs
            writer2.flush();
            writer2.close();

        } catch (Exception e) {
        }
    }

}
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

public class Hotel {
    // Attributs hotel
    String nom;
    String adresse;
    int nbrEtages;
    int nbrChambresEtage;
    Vector<Chambres> listeChambres = new Vector<Chambres>();
    Vector<Consommation> listeConsommations = new Vector<Consommation>();
    Vector<Clients> listeClients = new Vector<Clients>();
    SimpleDateFormat formatDates = new SimpleDateFormat("dd/MM/yyyy");
    private static String docChambres = "Ressources/Chambres.csv";
    private static String docClients = "Ressources/Clients.csv";

    // Constructeur classe
    public Hotel(String n, String ad, int et, int nbrChEt) {
        nom = n;
        adresse = ad;
        nbrEtages = et;
        nbrChambresEtage = nbrChEt;
    }

    // Méthodes pour modifications des attributs de l'hotel
    public void ajouterChambre(int no, int et, Boolean d, String type) {
        listeChambres.add(new Chambres(no, et, d, type, this));
    }

    public void supprimerChambre(int no) {
        // On parcours les chambres de l'hotel
        for (int i = 0; i < listeChambres.size(); i++) {
            // On test si la chambre de la liste est celle qu'on cherche
            if (listeChambres.get(i).noChambre == no) {
                // Si c'est le cas on l'enleve de notre liste
                listeChambres.remove(i);
            }
        }
    }

    public void ajouterClients(String nom, String prenom, String ad, String mail, String t) {
        listeClients.add(new Clients(nom, prenom, ad, mail, t, this));
    }

    // Méthodes pour ajout d'attributs dans l'hotel
    public void newChambre(String num, String etage, String type) {
        // ON ouvre le fichier
        File document = new File(docChambres);

        // On crée notre ecriteur
        FileWriter writer;
        try {

            // On ecrit en appends dans le fichier les informations separées entre elles
            writer = new FileWriter(document, true);
            String text = num + "," + etage + "," + type + "\n";
            writer.append(text);

            // On ferme notre ecriteur
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newClient(String nom, String prenom, String adresse, String mail, String telephone) {
        // ON ouvre le fichier
        File document = new File(docClients);

        // On crée notre ecriteur
        FileWriter writer;
        try {
            // On ecrit en appends dans le fichier les informations separées entre elles
            writer = new FileWriter(document, true);
            String text = nom + "," + prenom + "," + adresse + "," + mail + "," + telephone + "\n";
            writer.append(text);

            // On ferme notre ecriteur
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthodes pour renovoyer des informations sur l'hotel
    public Chambres consulterChambre(int no) {
        // On parcours la liste de nos chambres
        for (int i = 0; i < listeChambres.size(); i++) {
            // On test si cette chambre correspondand á celle qu'on cherche
            if (listeChambres.get(i).noChambre == no) {
                // Si c'est le cas on renvoie cette chambre
                return listeChambres.get(i);
            }
        }
        return null;
    }

    public Clients consulterClients(String n, String p) {
        // On procède á la meme méthode pour les clients
        for (int i = 0; i < listeClients.size(); i++) {
            if (listeClients.get(i).nom.equals(n) && listeClients.get(i).prenom.equals(p)) {
                return listeClients.get(i);
            }
        }
        return null;
    }

    public Vector<Date> obtentionPeriode(Date debut, Date fin) {
        // On crée notre vecteur qui va contenir chaque jour entre les dates
        Vector<Date> listeDates = new Vector<>();

        // On utilise la classs calendar qui permet d'augmenter les jours facilement
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(debut);

        // On parcours les dates du début à la fin
        while (calendar.getTime().before(fin) || calendar.getTime().equals(fin)) {
            // On ajoute chaque date à notre liste
            listeDates.add(new java.sql.Date(calendar.getTimeInMillis()));
            // Puis on rajoute un jour pour passer á la date suivante
            calendar.add(Calendar.DATE, 1);
        }

        return listeDates;

    }

    public Vector<Reservations> getReservations() {
        // On crée un vectur pour toutes nos réservations
        Vector<Reservations> resultat = new Vector<Reservations>();

        // Pour chaque chambre on rajoute sa reservation à notre vecteur
        listeChambres.forEach(element -> {
            element.reservations.forEach(el -> {
                resultat.add(el);
            });
        });
        return resultat;
    }

    public Reservations consulterRes(int id) {
        // On parcours nos reservation et on renvoie celle qui correspondant à celle
        // qu'on cherche à partir de son id
        for (int i = 0; i < getReservations().size(); i++) {
            if (getReservations().get(i).idReservation == id) {
                return getReservations().get(i);
            }
        }
        return null;
    }

    public Vector<Sejour> getSejours() {
        Vector<Sejour> resultat = new Vector<Sejour>();
        // On parcours nos reservation et on renvoie celles qui soient actives
        for (int i = 0; i < getReservations().size(); i++) {
            if (getReservations().get(i).active == true) {
                resultat.add(getReservations().get(i).sejour);
            }
        }
        return resultat;
    }

}

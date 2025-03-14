import java.util.Vector;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Chambres {
    // Attributs des reservations
    Hotel hotel;
    int noChambre;
    int etage;
    boolean dispo;
    String typeChambre;
    Vector<Reservations> reservations = new Vector<Reservations>();
    int capacite;
    Double prix;
    private static String docReservations = "Ressources/Reservations.csv";

    // Constructeur classe
    public Chambres(int no, int et, boolean d, String type, Hotel h) {
        hotel = h;
        noChambre = no;
        etage = et;
        dispo = d;
        typeChambre = type;
        // On definie la capacité et le prix en fonction du type
        if (typeChambre.equals("Suit")) {
            capacite = 6;
            prix = 130.0;
        }
        if (typeChambre.equals("Presidentielle")) {
            capacite = 6;
            prix = 200.0;
        }
        if (typeChambre.equals("Double")) {
            capacite = 4;
            prix = 80.0;
        }
        if (typeChambre.equals("Simple")) {
            capacite = 2;
            prix = 70.0;
        }
    }

    // Méthodes de modification des chambres
    public void setHotel(Hotel h) {
        hotel = h;
    }

    public void ajouterReservations(Reservations r) {
        reservations.add(r);
    }

    public void modifierType(String newType) {
        typeChambre = newType;
    }

    // Méthodes pour le renvoie d'informations sur la chambre
    public Boolean disponibilite(Date dateD, Date dateF) {
        // On parcours nos réservations
        for (int i = 0; i < reservations.size(); i++) {
            // On test si les dates sont entre les dates d'au moins une reservation
            if (reservations.get(i).intersect(dateD, dateF) == false) {
                // Si elles le sont pas on peut renvoyer que la chambre est indisponible
                return false;
            }
        }
        // si aucune reservation existe pour les dates en parametre on peut renvoyer que
        // la chambre est disponible
        return true;
    }

    public Sejour getSejour() {
        // On parcours nos réservations
        for (int i = 0; i < reservations.size(); i++) {
            // On test si la résrevation a deja commencé
            if (reservations.get(i).active == true) {
                // On retourne le sejour si elle est active
                return reservations.get(i).sejour;
            }
        }
        return null;
    }

    public String info(Calendar dateA) {
        String texte = "";
        // On test si la chambre á une reservation active pour une date donnée
        if (reservationActuelle(dateA) == null) {
            // On test s'il n'y a pas des prochaines réservations á venir
            if (proxReservation() != null) {
                texte = "La chambre numero " + noChambre + " sera libre jusqu'au "
                        + hotel.formatDates.format(proxReservation().dateDeb)
                        + ".\nCette chambre " + typeChambre + " se trouve au étage numero " + etage
                        + " et peut accueillir jusqu'á "
                        + capacite + " personnes";
            } else {
                texte = "La chambre numero " + noChambre + " n'a aucune reservation encore"
                        + ".\nCette chambre " + typeChambre + " se trouve au étage numero " + etage
                        + " et peut accueillir jusqu'á "
                        + capacite + " personnes";
            }

        } else {
            texte = "La chambre numero " + noChambre + " sera occupée jusqu'au "
                    + hotel.formatDates.format(reservationActuelle(dateA).dateFin)
                    + ".\nCette chambre " + typeChambre + " se trouve au étage numero " + etage
                    + " et peut accueillir jusqu'á "
                    + capacite + " personnes";
        }

        return texte;
    }

    public Reservations reservationActuelle(Calendar date) {
        // On parcours les réservations
        for (int i = 0; i < reservations.size(); i++) {
            java.sql.Date dateA = new java.sql.Date(date.getTimeInMillis());
            // On test si la date en parametre correspondant á la date d'une resrvation
            if (reservations.get(i).intersect(dateA, dateA) == false) {
                // On renvoie la reservation en question si c'est le cas
                return reservations.get(i);
            }
        }
        return null;
    }

    public Reservations proxReservation() {
        Reservations resultat;
        // On test s'il y a ou pas des reservations
        if (reservations.isEmpty() == false) {
            resultat = reservations.get(0);
        } else {
            return null;
        }
        // On parcours nos reservations
        for (int i = 1; i < reservations.size(); i++) {
            // On compare les reservations une á une et on choisis celle qui soit plus
            // proche
            if (reservations.get(i).dateDeb.before(resultat.dateDeb)) {
                resultat = reservations.get(i);
            }
        }
        // On renvoie la résrvation plus proche
        return resultat;
    }

    public void newReservation(int id, Date dateD, Date dateF, String n, String p) {
        // ON ouvre le fichier
        File document = new File(docReservations);
        DateFormat df = new SimpleDateFormat("dd/MM/YYYY");

        // On crée notre ecriteur
        FileWriter writer;
        try {
            // On ecrit en appends dans le fichier les informations separées entre elles
            writer = new FileWriter(document, true);
            String text = Integer.toString(id) + "," + df.format(dateD) + "," + df.format(dateF) + "," + n + "," + p
                    + "," + Integer.toString(noChambre) + "," + false + "\n";
            writer.append(text);

            // On ferme notre ecriteur
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
import java.util.Vector;

public class Clients {

    // Attributs de notre classe clients
    Hotel hotel;
    String nom;
    String prenom;
    String adresse;
    String mail;
    String telephone;
    public Vector<Reservations> listeReservations = new Vector<Reservations>();

    // Constructeur classe
    public Clients(String n, String p, String ad, String m, String t, Hotel h) {
        nom = n;
        prenom = p;
        adresse = ad;
        mail = m;
        telephone = t;
        hotel = h;
    }

    // Méthodes renvoyant des informations sur le client
    public String info() {
        return "Nom du client : " + nom + "\n"
                + "Prenom du client : " + prenom + "\n"
                + "Adresse : " + adresse + "\n"
                + "Mail : " + mail + "\n"
                + "Telephone : " + telephone + "\n"
                + "Numero de reservations : " + listeReservations.size();
    }

}

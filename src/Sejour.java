import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

public class Sejour {
    // Attributs sejour
    int idReservation;
    int idconso;
    Reservations res;
    public Vector<Consommation> listeConsommations = new Vector<Consommation>();
    private String docCoonsommations = "Ressources/consommations.csv";

    // Constructeur classe
    public Sejour(int id, Reservations r) {
        idReservation = id;
        res = r;
    }

    // Méthodes pour modifications des attributs du sejour
    public void ajouterConsommation(String nomProd, int qtt) {
        // On ouvre le document des consommations
        File document = new File(docCoonsommations);
        try {
            // On crée notre écriteur
            FileWriter writer = new FileWriter(document, true);
            // On ecrit l'information
            writer.append(idReservation + "," + nomProd + "," + qtt + "\n");
            // On ferme nos écriteurs
            writer.flush();
            writer.close();
        } catch (Exception e) {
        }
        listeConsommations.add(new Consommation(res, nomProd, qtt));
    }

    // Méthodes pour renvoyer des informations du sejour
    public Double prixTotal() {
        Double resultat = 0.0;
        // On aditionne les prix de toutes les consommations
        for (int i = 0; i < listeConsommations.size(); i++) {
            resultat += listeConsommations.get(i).prixTotal();
        }
        return resultat;
    }
}

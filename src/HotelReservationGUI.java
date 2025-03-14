import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Font;

public class HotelReservationGUI extends JFrame {
    private JPanel ecranAffichage;

    // On crée un hotel
    Hotel hotel = new Hotel("SMVH Resort", "111 rue de la republique", 3, 30);
    private static String docReservations = "Ressources/Reservations.csv";
    private static String docChambres = "Ressources/Chambres.csv";
    private static String docClients = "Ressources/Clients.csv";
    Calendar dateAjd = Calendar.getInstance();
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
    private String docCoonsommations = "Ressources/consommations.csv";
    private String docProduits = "Ressources/Produits.csv";

    public HotelReservationGUI() {
        // On crée notre frame
        setTitle("Logiciel " + hotel.nom);
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // On crée le panel où on ajoutera chaque page
        ecranAffichage = new JPanel();

        // On crée le menu pour notre GUI
        setJMenuBar(createMenuBar());
        add(ecranAffichage);

        // On commence par ajouter nos chambres a notre hotel et ses reservations
        remplirChambres();
        remplirReservations();

        afficher_consultation_chambres();
        setVisible(true);
    }

    // Créer le menu principal
    private JMenuBar createMenuBar() {
        // Creation de la bar du menu, qui contient les subMnus
        JMenuBar menuBar = new JMenuBar();

        // Creation du subMenu pour la gestion de l'Hotel
        JMenu gestionHotel = new JMenu("Gestion de l'Hotel");
        JMenuItem consulterChambreItem = new JMenuItem("Consulter une chambre");
        JMenuItem ajoutChambreItem = new JMenuItem("Ajouter une chambre");
        JMenuItem ajoutConsoItem = new JMenuItem("Ajouter une consommation");
        // Ajout items au subMenu
        gestionHotel.add(consulterChambreItem);
        gestionHotel.add(ajoutChambreItem);
        gestionHotel.add(ajoutConsoItem);
        // Ajout du actionListenner pour afficher ce qu'on veut quand on clic
        consulterChambreItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                afficher_consultation_chambres();
            }
        });
        ajoutChambreItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                afficher_ajout_chambres();
            }
        });
        ajoutConsoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                afficher_ajout_conso();
            }
        });

        // Creation du subMenu pour la gestion des clients
        JMenu gestionClients = new JMenu("Gestion des clients");
        JMenuItem consulterClientItem = new JMenuItem("Consulter un client");
        JMenuItem ajouterClientItem = new JMenuItem("Ajouter un client");
        // Ajout items au subMenu
        gestionClients.add(consulterClientItem);
        gestionClients.add(ajouterClientItem);
        // Ajout du actionListenner pour afficher ce qu'on veut quand on clic
        consulterClientItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                afficher_consultation_client();
            }
        });
        ajouterClientItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                afficher_ajout_client();
            }
        });
        // Creation du subMenu "gestionReservations" et ses items
        JMenu gestionReservations = new JMenu("Gestion des reservations");
        JMenuItem checkItem = new JMenuItem("Check-in & Check-out");
        JMenuItem ajoutReservationItem = new JMenuItem("Ajouter des reservations");
        JMenuItem consulterReservationItem = new JMenuItem("Consulter une reservations");
        // Ajout items au subMenu
        gestionReservations.add(checkItem);
        gestionReservations.add(ajoutReservationItem);
        gestionReservations.add(consulterReservationItem);
        // Ajout du actionListenner pour afficher ce qu'on veut quand on clic
        checkItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                afficher_check_outIn();
            }
        });
        ajoutReservationItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                afficher_ajout_reservations();
            }
        });
        consulterReservationItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                afficher_consulter_reservations();
            }
        });

        // Creation du subMenu "gestionProduits" et ses items
        JMenu gestionProduits = new JMenu("Gestion des produits");
        JMenuItem listeProduits = new JMenuItem("Liste des produits");
        JMenuItem listeConsommations = new JMenuItem("Liste des consommations");
        // Ajout items au subMenu
        gestionProduits.add(listeProduits);
        gestionProduits.add(listeConsommations);
        // Ajout du actionListenner pour afficher ce qu'on veut quand on clic
        listeProduits.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                afficher_liste_listeProduits();
            }
        });
        listeConsommations.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                afficher_liste_listeConsommations();
            }
        });

        // Creation du subMenu "configuration" et ses items
        JMenu parametres = new JMenu("Parametres");
        JMenuItem paraDate = new JMenuItem("Parametres");
        parametres.add(paraDate);
        paraDate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                afficher_parametres_date();
            }
        });

        // Ajouter tout au barMenu
        menuBar.add(gestionHotel);
        menuBar.add(gestionReservations);
        menuBar.add(gestionClients);
        menuBar.add(gestionProduits);
        menuBar.add(parametres);
        return menuBar;
    }

    // Differentes pages de l'interphace
    public void afficher_consultation_chambres() {
        // On nettoie le panel
        ecranAffichage.removeAll();
        ecranAffichage.setLayout(new FlowLayout());
        // On crée une grille
        GridBagConstraints grille = new GridBagConstraints();
        GridBagLayout gridBagLayout = new GridBagLayout();
        ecranAffichage.setLayout(gridBagLayout);

        // On etabli une marge de separation entre les casses
        grille.insets = new Insets(60, 20, 20, 20);

        // On gere les coordonnées
        grille.gridx = 0;
        grille.gridy = 0;
        grille.fill = GridBagConstraints.CENTER;
        grille.weightx = 0.5;

        // Titre du panel
        JLabel titrePanel = new JLabel("Consultation chambre");
        titrePanel.setFont(new Font("Arial", Font.BOLD, 20));
        titrePanel.setForeground(Color.WHITE);
        titrePanel.setBackground(Color.BLACK);
        titrePanel.setOpaque(true);
        titrePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        ecranAffichage.add(titrePanel, grille);

        // On crée une nouvelle grille pour le petit panel
        GridBagConstraints grilleR = new GridBagConstraints();
        JPanel recherche = new JPanel(new GridBagLayout());
        grilleR.insets = new Insets(10, 5, 10, 5);
        grilleR.gridx = 0;
        grilleR.gridy = 0;
        grilleR.fill = GridBagConstraints.NONE;
        grilleR.weightx = 0.5;

        // On crée les elements et on les ajoute en changean les cordoonnées
        JLabel texte = new JLabel("Numero de chambre: ");
        JTextField noChChamp = new JTextField(5);
        JLabel texteEnter = new JLabel("(ENTER pour avoir les informations)");
        recherche.add(texte, grilleR);
        grilleR.gridx = 1;
        grilleR.gridy = 0;
        recherche.add(noChChamp, grilleR);
        grilleR.gridx = 0;
        grilleR.gridy = 1;
        grilleR.gridwidth = 2;
        grilleR.fill = GridBagConstraints.NONE;
        grilleR.anchor = GridBagConstraints.PAGE_END;
        recherche.add(texteEnter, grilleR);

        grille.gridx = 0;
        grille.gridy = 1;
        ecranAffichage.add(recherche, grille);
        noChChamp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nochSelect = noChChamp.getText();
                String message = hotel.consulterChambre(Integer.parseInt(noChChamp.getText())).info(dateAjd);
                // On affiche un message avec les informations
                JOptionPane.showMessageDialog(ecranAffichage, message, "Information chambre " + nochSelect,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        grille.gridx = 1;
        grille.gridy = 0;
        grille.gridheight = 2;
        grille.insets = new Insets(20, 20, 20, 20);

        ecranAffichage.add(carteHotel(), grille);
        ecranAffichage.revalidate();
        ecranAffichage.repaint();
    }

    public void afficher_ajout_chambres() {
        ecranAffichage.removeAll();
        ecranAffichage.setLayout(new FlowLayout());

        // Création d'une grille et on l'attribue à notre panel
        GridBagConstraints grille = new GridBagConstraints();
        GridBagLayout gridBagLayout = new GridBagLayout();
        ecranAffichage.setLayout(gridBagLayout);

        grille.insets = new Insets(60, 0, 20, 0);
        grille.gridx = 0;
        grille.gridy = 0;
        grille.fill = GridBagConstraints.CENTER;
        grille.weightx = 0.5;

        // On crée nos components et on les rajoute
        JLabel titrePanel = new JLabel("Ajouter une chambre");
        titrePanel.setFont(new Font("Arial", Font.BOLD, 20));
        titrePanel.setForeground(Color.WHITE);
        titrePanel.setBackground(Color.darkGray);
        titrePanel.setOpaque(true);
        titrePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        ecranAffichage.add(titrePanel, grille);

        GridBagConstraints grilleR = new GridBagConstraints();
        JPanel recherche = new JPanel(new GridBagLayout());
        grilleR.insets = new Insets(10, 5, 10, 5);
        grilleR.gridx = 0;
        grilleR.gridy = 0;
        grilleR.fill = GridBagConstraints.NONE;
        grilleR.weightx = 0.5;

        JLabel texte = new JLabel("Numero etage: ");
        JTextField etageChamp = new JTextField(5);
        JLabel texteEnter = new JLabel("(ENTER pour valider le numero d'etage)");
        recherche.add(texte, grilleR);
        grilleR.gridx = 1;
        grilleR.gridy = 0;
        recherche.add(etageChamp, grilleR);
        grilleR.gridx = 0;
        grilleR.gridy = 1;
        grilleR.gridwidth = 2;

        recherche.add(texteEnter, grilleR);

        grille.gridx = 0;
        grille.gridy = 1;
        ecranAffichage.add(recherche, grille);
        etageChamp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // On obtient l'etage selectionné
                int etageSelect = Integer.parseInt(etageChamp.getText());

                // On créera une nouvelle fenetre avec une nouvelle grille
                JFrame f = new JFrame();
                JDialog creationChamDialog = new JDialog(f, "creation chambre", true);
                creationChamDialog.setLayout(new GridBagLayout());
                creationChamDialog.setLocationRelativeTo(null);
                creationChamDialog.setSize(250, 200);

                // On rajoute les components nécessaires
                JButton buttonAuto = new JButton("Créer automatiquement");
                JButton buttonManuel = new JButton("Créer manuellement");

                GridBagConstraints gbcAuto = new GridBagConstraints();
                gbcAuto.gridx = 0;
                gbcAuto.gridy = 0;
                gbcAuto.weighty = 0.5;
                gbcAuto.insets = new Insets(10, 10, 10, 10);

                buttonAuto.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent es) {
                        // On va créer une liste de chambres
                        Vector<Chambres> listeChEtage = new Vector<>();
                        // On parcours nos chambres
                        for (int i = 0; i < hotel.listeChambres.size(); i++) {
                            // On test si cette chambre se trouve dans l'etage selecionnées
                            if (hotel.listeChambres.get(i).etage == etageSelect) {
                                // Si c'est le cas on la rajoute dans notre liste de chambres par étage
                                listeChEtage.add(hotel.listeChambres.get(i));
                            }
                        }

                        // On ajoute notre chambre avec :
                        // un numero de chambre superieur á la dernière chambre
                        // L'etage selectionnée
                        // Et un type de chambre égale à la dernière
                        hotel.newChambre(String.valueOf(listeChEtage.get(listeChEtage.size() - 1).noChambre + 1),
                                String.valueOf(etageSelect),
                                listeChEtage.get(listeChEtage.size() - 1).typeChambre);

                        creationChamDialog.setVisible(false);
                        afficher_ajout_chambres();
                    }
                });
                creationChamDialog.add(buttonAuto, gbcAuto);

                GridBagConstraints gbcManuel = new GridBagConstraints();
                gbcManuel.gridx = 0;
                gbcManuel.gridy = 1;
                gbcManuel.weighty = 0.5;
                // On crée un button pour un ajout manuel
                buttonManuel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent es) {
                        creationChamDialog.setVisible(false);
                        // On crée notre dialogue
                        JDialog creationChamManuel = new JDialog(f, "creation chambre", true);

                        // Nos components
                        JLabel numeroText = new JLabel("Entrez le numero :");
                        JLabel typeText = new JLabel("Entrez le type :");
                        JComboBox<String> typeOptions = new JComboBox<>(
                                new String[] { "Simple", "Double", "Suit", "Presidentielle" });

                        JTextField numeroSelect = new JTextField(10);
                        JButton creer = new JButton("Créer");
                        creer.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent ee) {
                                String typeSelect = (String) typeOptions.getSelectedItem();
                                hotel.newChambre(numeroSelect.getText(), String.valueOf(etageSelect), typeSelect);
                                remplirChambres();
                                afficher_ajout_chambres();
                                creationChamManuel.setVisible(false);
                            }
                        });

                        // On rajoute nos components dans notre grille
                        creationChamManuel.setLayout(new GridBagLayout());
                        GridBagConstraints gbcManuel = new GridBagConstraints();
                        gbcManuel.insets = new Insets(20, 20, 20, 20);
                        gbcManuel.gridx = 0;
                        gbcManuel.gridy = 0;
                        gbcManuel.weightx = 0.5;
                        creationChamManuel.add(numeroText, gbcManuel);
                        gbcManuel.gridx = 1;
                        gbcManuel.gridy = 0;
                        gbcManuel.weightx = 0.5;
                        creationChamManuel.add(numeroSelect, gbcManuel);
                        gbcManuel.gridx = 0;
                        gbcManuel.gridy = 1;
                        gbcManuel.weighty = 0.5;
                        creationChamManuel.add(typeText, gbcManuel);
                        gbcManuel.gridx = 1;
                        gbcManuel.gridy = 1;
                        gbcManuel.weighty = 0.5;
                        creationChamManuel.add(typeOptions, gbcManuel);
                        gbcManuel.gridx = 0;
                        gbcManuel.gridy = 2;
                        gbcManuel.weighty = 0.5;
                        gbcManuel.gridwidth = 2;
                        creationChamManuel.add(creer, gbcManuel);

                        creationChamManuel.setLocationRelativeTo(null);
                        creationChamManuel.setSize(350, 300);
                        creationChamManuel.setVisible(true);
                    }
                });
                creationChamDialog.add(buttonManuel, gbcManuel);

                creationChamDialog.setVisible(true);

            }
        });
        grille.gridx = 1;
        grille.gridy = 0;
        grille.gridheight = 2;
        grille.insets = new Insets(20, 0, 20, 0);
        ecranAffichage.add(carteHotel(), grille);
        ecranAffichage.revalidate();
        ecranAffichage.repaint();
    }

    public void afficher_ajout_conso() {
        ecranAffichage.removeAll();
        ecranAffichage.setLayout(new FlowLayout());
        ecranAffichage.setLayout(new GridLayout(2, 2, 100, 100));

        JLabel titrePanel = new JLabel("Ajouter une consommation");
        titrePanel.setFont(new Font("Arial", Font.BOLD, 20));
        titrePanel.setForeground(Color.BLACK);
        titrePanel.setOpaque(true);
        titrePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // On crée notre grille et les differents compenents
        GridBagConstraints grilleR = new GridBagConstraints();
        JPanel recherche = new JPanel(new GridBagLayout());
        grilleR.insets = new Insets(10, 5, 10, 5);
        grilleR.gridx = 0;
        grilleR.gridy = 0;
        grilleR.weightx = 0.5;

        JLabel texte = new JLabel("Selectionnez une chambre: ");
        recherche.add(texte, grilleR);
        JTextField chambreChamp = new JTextField(5);
        grilleR.gridx = 1;
        grilleR.gridy = 0;
        recherche.add(chambreChamp, grilleR);
        JLabel texteEnter = new JLabel("(ENTER pour valider)");
        grilleR.gridx = 0;
        grilleR.gridy = 1;
        grilleR.gridwidth = 2;
        grilleR.fill = GridBagConstraints.NONE;
        grilleR.anchor = GridBagConstraints.PAGE_END;
        recherche.add(texteEnter, grilleR);

        chambreChamp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // On va obtenir la chambre selectionnée et on va créer un dialogue
                int chambreSelect = Integer.parseInt(chambreChamp.getText());
                JFrame f = new JFrame();
                JDialog consoDialog = new JDialog(f, "Creation consommation chambre " + chambreSelect, true);
                consoDialog.setLayout(new GridBagLayout());
                consoDialog.setLocationRelativeTo(null);
                consoDialog.setSize(400, 200);

                // Pour ce dialogue on crée une grille et nos components
                GridBagConstraints grille = new GridBagConstraints();
                grille.gridx = 0;
                grille.gridy = 0;
                grille.weighty = 0.5;
                grille.insets = new Insets(10, 10, 10, 10);

                JLabel textProd = new JLabel("Entrez le nom du produit :");
                consoDialog.add(textProd, grille);

                JTextField champProduit = new JTextField(10);
                grille.gridx = 1;
                grille.gridy = 0;
                consoDialog.add(champProduit, grille);

                JLabel textQtt = new JLabel("Entrez la quantité acheté :");
                grille.gridx = 0;
                grille.gridy = 1;
                consoDialog.add(textQtt, grille);

                JTextField champQtt = new JTextField(10);
                grille.gridx = 1;
                grille.gridy = 1;
                consoDialog.add(champQtt, grille);

                JButton creerConso = new JButton("Ajouter la consommation");
                creerConso.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evenement) {
                        // á partir des informations on crée notre consommation dans la chambre choisie
                        hotel.consulterChambre(chambreSelect).getSejour().ajouterConsommation(champProduit.getText(),
                                Integer.valueOf(champQtt.getText()));
                        consoDialog.setVisible(false);
                        afficher_ajout_conso();
                    }
                });
                grille.gridx = 0;
                grille.gridy = 2;
                grille.gridwidth = 2;
                consoDialog.add(creerConso, grille);
                // On rajoute une condition pour tester si la chambre choisie existe
                if (hotel.consulterChambre(chambreSelect) != null) {
                    consoDialog.setVisible(true);
                }
            }
        });

        // On va ceréer une table á deux dimensions pour montrer la liste de produits
        DefaultTableModel tableModel = new DefaultTableModel(new Consommation().tableComplete(),
                new String[] { "Nom du produit", "Prix", "Quantité de Stock" });
        JTable table = new JTable(tableModel);

        // On crée un panel avec un scroll verticall
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // On va créer les vecteurs nécessaires pour une table avec les revenus en
        // fonction de la chambre
        Vector<String> revenus = new Vector<>();
        Vector<String> chambres = new Vector<>();
        hotel.listeChambres.forEach(element -> {
            element.reservations.forEach(el -> {
                if (el.active == true) {
                    revenus.add(String.valueOf(el.revenus()));
                    chambres.add(String.valueOf(element.noChambre));
                }
            });
        });
        // On transforme les vecteurs en tableaux et on les colle
        String[][] gainsParCh = { revenus.toArray(new String[revenus.size()]),
                chambres.toArray(new String[chambres.size()]) };

        ecranAffichage.add(titrePanel);
        ecranAffichage.add(scrollPane);
        ecranAffichage.add(recherche);
        // on créer notre histogramme á partir de la table 2 x 2
        ecranAffichage.add(
                new Histogramm("Consommation chambres", "Numero de chambre", "$ Depensée", gainsParCh).chartPanel);

        ecranAffichage.revalidate();
        ecranAffichage.repaint();
    }

    public void afficher_check_outIn() {
        ecranAffichage.removeAll();
        remplirChambres();
        remplirClients();
        remplirReservations();
        ecranAffichage.setLayout(new GridLayout(2, 1, 100, 0));

        // Nous allons créer un panel pour les check out et un pour les check in
        JPanel partieIn = new JPanel(new GridLayout(1, 1, 0, 0));
        // Notre premier panel va donc contenir les reservations qui commencent ce jour
        Vector<Reservations> resAjdIn = new Vector<Reservations>();
        // On va donc créer un vecteur contenant toutes les reservations pour ce jour
        for (int i = 0; i < hotel.getReservations().size(); i++) {
            Reservations res = hotel.getReservations().get(i);
            // On teste si la reservation commence ce jour et si elle n'a pas encore debutée
            if (formatDate.format(res.dateDeb).equals(formatDate.format(dateAjd.getTime())) == true
                    && res.active == false) {
                // On le rajoute
                resAjdIn.add(res);
            }
        }

        // Puis on crée un vecteur contenant les sejours qui se terminent aujourd'hui
        Vector<Reservations> resAjdOut = new Vector<Reservations>();
        for (int i = 0; i < hotel.getSejours().size(); i++) {
            // On teste si le sejour termine ce jour
            if (formatDate.format(hotel.getSejours().get(i).res.dateFin)
                    .equals(formatDate.format(dateAjd.getTime())) == true) {
                // On le rajoute
                resAjdOut.add(hotel.getSejours().get(i).res);
            }
        }
        // On va donc créer une table des reservations
        String[][] tableRes = new String[resAjdIn.size()][4];
        for (int i = 0; i < tableRes.length; i++) {
            tableRes[i][0] = String.valueOf(resAjdIn.get(i).idReservation);
            tableRes[i][1] = formatDate.format(resAjdIn.get(i).dateFin);
            tableRes[i][2] = resAjdIn.get(i).client.prenom + " " + resAjdIn.get(i).client.nom;
            tableRes[i][3] = String.valueOf(resAjdIn.get(i).chambre.noChambre);
        }
        // Pour ensuite la rajouter á un panel avec un scroll vertical
        DefaultTableModel tableModel = new DefaultTableModel(tableRes,
                new String[] { "Id Reservation", "Date fin", "Client  ( PRENOM,NOM )", "No chambre" });
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        partieIn.add(scrollPane);

        // On va créer une partie pour rechercher la reservation ou le sejour
        JPanel partieRecherche = new JPanel(new GridBagLayout());
        // On rajoute nos components
        GridBagConstraints grilleR = new GridBagConstraints();
        grilleR.insets = new Insets(10, 10, 10, 10);
        grilleR.gridwidth = 2;
        partieRecherche.add(new JLabel("Rechercher une reservation par :"), grilleR);
        grilleR.gridwidth = 1;
        grilleR.gridx = 0;
        grilleR.gridy = 1;
        // On donne l'option de trouver la reservation par le client ou par l'id de
        // reservation
        partieRecherche.add(new JLabel("Id de la reservation"), grilleR);
        grilleR.gridy = 2;
        partieRecherche.add(new JLabel("Nom et prenom (NOM,PRENOM)"), grilleR);
        grilleR.gridx = 1;
        grilleR.gridy = 1;
        JTextField champId = new JTextField(10);
        partieRecherche.add(champId, grilleR);
        grilleR.gridy = 2;
        JTextField champclient = new JTextField(15);
        partieRecherche.add(champclient, grilleR);
        champId.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                for (int i = 0; i < hotel.getReservations().size(); i++) {
                    Reservations res = hotel.getReservations().get(i);
                    // On test si la reservation correspond bien au id recerchée
                    if (String.valueOf(res.idReservation).equals(champId.getText()) == true) {
                        // On test si on doit faire un chek in ou check out
                        if (resAjdIn.contains(res)) {
                            // On demande une confirmation de checck-in á l'utilisateur
                            int reponse = JOptionPane.showConfirmDialog(null,
                                    "La reservation numero : " + res.idReservation + "\n" +
                                            "Au nom de : M/Mme " + res.client.nom + " sera validé comme debuté",
                                    "Valider check-in",
                                    JOptionPane.YES_NO_OPTION);
                            if (reponse == JOptionPane.YES_OPTION) {
                                // On fait un check in le cas consecant
                                remplirReservations();
                                res.checkIn();
                                afficher_check_outIn();
                                remplirReservations();
                            }
                        } else if (resAjdOut.contains(res)) {
                            // On fait le meme processus si on doit faire un check out
                            int reponse = JOptionPane.showConfirmDialog(null,
                                    "La reservation numero : " + res.idReservation + "\n" +
                                            "Au nom de : M/Mme " + res.client.nom + " sera validé comme terminée",
                                    "Valider check-in",
                                    JOptionPane.YES_NO_OPTION);
                            if (reponse == JOptionPane.YES_OPTION) {
                                remplirReservations();
                                res.checkOut();
                                remplirReservations();
                                afficher_check_outIn();

                            } else {
                                JOptionPane.showMessageDialog(null, "reservation non trouvée");
                            }
                        }
                    }
                }
                for (int i = 0; i < hotel.listeChambres.size(); i++) {
                    for (int a = 0; a < hotel.listeChambres.get(i).reservations.size(); a++) {
                        Reservations res = hotel.listeChambres.get(i).reservations.get(a);
                        if (String.valueOf(res.idReservation).equals(champId.getText()) == true) {
                            if (resAjdIn.contains(res)) {
                                int reponse = JOptionPane.showConfirmDialog(null,
                                        "La reservation numero : " + res.idReservation + "\n" +
                                                "Au nom de : M/Mme " + res.client.nom + " sera validé comme debuté",
                                        "Valider check-in",
                                        JOptionPane.YES_NO_OPTION);
                                if (reponse == JOptionPane.YES_OPTION) {
                                    remplirReservations();
                                    res.checkIn();
                                    afficher_check_outIn();
                                    remplirReservations();
                                } else {
                                }
                            } else if (resAjdOut.contains(res)) {
                                int reponse = JOptionPane.showConfirmDialog(null,
                                        "La reservation numero : " + res.idReservation + "\n" +
                                                "Au nom de : M/Mme " + res.client.nom + " sera validé comme terminée",
                                        "Valider check-in",
                                        JOptionPane.YES_NO_OPTION);
                                if (reponse == JOptionPane.YES_OPTION) {
                                    remplirReservations();
                                    res.checkOut();
                                    remplirReservations();
                                    afficher_check_outIn();

                                } else {
                                    JOptionPane.showMessageDialog(null, "reservation non trouvée");
                                }
                            }
                        }
                    }
                }
            }
        });
        champclient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // On fait le meme processus que pour l'id mais en fonction du client
                String[] casses = champclient.getText().split(",");
                String nom = casses[0];
                String prenom = casses[1];

                for (int i = 0; i < hotel.listeChambres.size(); i++) {
                    for (int a = 0; a < hotel.listeChambres.get(i).reservations.size(); a++) {
                        Reservations res = hotel.listeChambres.get(i).reservations.get(a);
                        if (res.client.nom.equals(nom) && res.client.prenom.equals(prenom)
                                && formatDate.format(res.dateDeb).equals(formatDate.format(dateAjd.getTime()))) {
                            if (resAjdIn.contains(res)) {
                                int reponse = JOptionPane.showConfirmDialog(null,
                                        "La reservation numero : " + res.idReservation + "\n" +
                                                "Au nom de : M/Mme " + res.client.nom + " sera validé comme debuté",
                                        "Valider check-in",
                                        JOptionPane.YES_NO_OPTION);
                                if (reponse == JOptionPane.YES_OPTION) {
                                    remplirReservations();
                                    res.checkIn();
                                    afficher_check_outIn();
                                    remplirReservations();
                                } else {
                                }
                            } else if (resAjdOut.contains(res)) {
                                int reponse = JOptionPane.showConfirmDialog(null,
                                        "La reservation numero : " + res.idReservation + "\n" +
                                                "Au nom de : M/Mme " + res.client.nom + " sera validé comme terminée",
                                        "Valider check-in",
                                        JOptionPane.YES_NO_OPTION);
                                if (reponse == JOptionPane.YES_OPTION) {
                                    remplirReservations();
                                    res.checkOut();
                                    remplirReservations();
                                    afficher_check_outIn();

                                } else {
                                    JOptionPane.showMessageDialog(null, "reservation non trouvée");
                                }
                            }
                        }
                    }
                }
            }
        });
        grilleR.gridy = 3;
        grilleR.gridwidth = 1;
        grilleR.gridx = 0;
        grilleR.insets = new Insets(50, 10, 0, 10);

        // on crée nos butons pour changer entre les sejours et reservations
        JButton reservations = new JButton("Liste reservations d'aujourd'hui");
        reservations.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                DefaultTableModel tableModel = new DefaultTableModel(tableRes,
                        new String[] { "Id Reservation", "Date fin", "Client  ( PRENOM,NOM )", "No chambre" });
                JTable table = new JTable(tableModel);
                // en changeant on va seulement modifier le contenu de la table de nos panels
                scrollPane.setViewportView(table);

                ecranAffichage.revalidate();
                ecranAffichage.repaint();
            }
        });

        JButton sejours = new JButton("Liste sejours d'aujourd'hui");
        sejours.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String[][] tableSej = new String[resAjdOut.size()][4];
                for (int i = 0; i < tableSej.length; i++) {
                    tableSej[i][0] = Integer.toString(resAjdOut.get(i).idReservation);
                    tableSej[i][1] = String.valueOf(resAjdOut.get(i).revenus());
                    tableSej[i][2] = resAjdOut.get(i).client.prenom + ","
                            + resAjdOut.get(i).client.nom;
                    tableSej[i][3] = Integer.toString(resAjdOut.get(i).chambre.noChambre);

                }
                DefaultTableModel tableModel = new DefaultTableModel(tableSej,
                        new String[] { "Id Reservation", "Revenus", "Client  ( PRENOM,NOM )", "No chambre" });
                JTable table = new JTable(tableModel);
                // en changeant on va seulement modifier le contenu de la table de nos panels
                scrollPane.setViewportView(table);

                ecranAffichage.revalidate();
                ecranAffichage.repaint();
            }
        });
        grilleR.gridx = 0;
        partieRecherche.add(reservations, grilleR);
        grilleR.gridx = 1;
        partieRecherche.add(sejours, grilleR);

        ecranAffichage.add(partieRecherche);
        ecranAffichage.add(partieIn);

        ecranAffichage.revalidate();
        ecranAffichage.repaint();
    };

    public void afficher_ajout_reservations() {
        ecranAffichage.removeAll();
        ecranAffichage.setLayout(new GridBagLayout());
        // Pour les reservations au lieu de faire plusieurs dialogues , nous allons
        // faire un panel interactif qui permettra une meilleure et simple utilisation

        // On va donc détailler chaque étape
        // On crée notre grille
        GridBagConstraints grille = new GridBagConstraints();
        grille.gridx = 0;
        grille.gridy = 0;
        grille.insets = new Insets(20, 20, 20, 20);
        // On va utiliser JCalendar, une classe qui permet d'avoir des calendriers dans
        // notre interface graphique. Cette classe doit etre telerchargée et se trouve
        // dans notre librairie
        JCalendar calendrier = new JCalendar();
        calendrier.setCalendar(dateAjd);

        JLabel titrePanel = new JLabel("Selectionnez la date d'arrivée: ");
        titrePanel.setFont(new Font("Arial", Font.BOLD, 20));
        titrePanel.setForeground(Color.WHITE);
        titrePanel.setBackground(Color.BLACK);
        titrePanel.setOpaque(true);
        titrePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton buttonDate = new JButton("Confirmer");
        buttonDate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // On va importer la date selectionée
                Date dateArrive = new java.sql.Date(calendrier.getDate().getTime());
                // On va donc faire la meme chose mais pour la date de départs
                ecranAffichage.removeAll();
                grille.gridx = 0;
                grille.gridy = 0;
                titrePanel.setText("Selectionnez la date de départ: ");
                ecranAffichage.add(titrePanel, grille);
                grille.gridx = 0;
                grille.gridy = 1;
                // Cette fois ci le calendrier commencera avec la date selecionnée auparavant
                calendrier.setDate(dateArrive);
                ecranAffichage.add(calendrier, grille);
                grille.gridy = 2;
                JButton buttonDepart = new JButton("Confirmer");
                ecranAffichage.add(buttonDepart, grille);

                // Nous allons également rajouter l'information sur la date d'arrivee
                grille.gridy = 3;
                ecranAffichage.add(new JLabel("Date d'arrivée : " + formatDate.format(dateArrive)), grille);

                buttonDepart.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // On importe la date de depart selectionnée
                        Date dateDepart = new java.sql.Date(calendrier.getDate().getTime());
                        ecranAffichage.removeAll();

                        grille.gridx = 0;
                        grille.gridy = 0;
                        grille.gridheight = 3;
                        // rajoutons également un calendrier colorée avec les dates selectionnées
                        // Pour cela on utilise la methode panelInfoRes
                        // Ce panel va donc ocupper 3 casses d'hauteur
                        ecranAffichage.add(panelInfoRes(dateArrive, dateDepart), grille);

                        JLabel titre2 = new JLabel("Entrez les informations du client ");
                        titre2.setFont(new Font("Arial", Font.BOLD, 20));
                        titre2.setForeground(Color.WHITE);
                        titre2.setBackground(Color.BLACK);
                        titre2.setOpaque(true);
                        titre2.setBorder(new EmptyBorder(10, 10, 10, 10));

                        // On rajoute le titre occupant 2 casses horizontales
                        grille.gridheight = 1;
                        grille.gridwidth = 2;
                        grille.gridx = 1;
                        grille.gridy = 0;
                        ecranAffichage.add(titre2, grille);

                        // Les componants suivants occupent uniquement 1 casse
                        JLabel nomText = new JLabel("Nom ");
                        JLabel prenomText = new JLabel("Prenom ");
                        JTextField nomField = new JTextField(10);
                        JTextField prenomField = new JTextField(10);
                        prenomField.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                // on importe le nom et prenom du client
                                String nomSelected = nomField.getText();
                                String prenomSelected = prenomField.getText();
                                remplirClients();
                                // On va donc avoir deux cas: le client existe ou le client n'existe pas
                                if (hotel.consulterClients(nomSelected, prenomSelected) == null) {
                                    // Si le client n'existe pas on va l'ajouter en demandant ses informations
                                    JFrame f = new JFrame();
                                    // On fait donc un panel de dialogue dans une nouvelle frame et on le rajoute
                                    JDialog dialog = new JDialog(f, "Ajout client", true);
                                    dialog.setLayout(new GridBagLayout());
                                    dialog.setLocationRelativeTo(null);
                                    dialog.setSize(500, 400);
                                    GridBagConstraints gbc = new GridBagConstraints();
                                    gbc.gridx = 0;
                                    gbc.gridy = 0;
                                    gbc.insets = new Insets(10, 10, 10, 10);

                                    JLabel texte = new JLabel(
                                            "Le client n'existe pas dans la base de donnés");

                                    JLabel adresseText = new JLabel("Entrez l'addresse du client");
                                    JTextField adresse = new JTextField(10);

                                    JLabel teleText = new JLabel("Entrez le telephone du client");
                                    JTextField telephone = new JTextField(10);

                                    JLabel mailText = new JLabel("Entrez le mail du client");
                                    JTextField mail = new JTextField(10);

                                    JButton ajoutClient = new JButton("Ajouter");

                                    ajoutClient.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent ev) {
                                            hotel.newClient(nomSelected, prenomSelected, adresse.getText(),
                                                    mail.getText(),
                                                    telephone.getText());
                                            dialog.setVisible(false);
                                        }
                                    });

                                    gbc.gridwidth = 2;
                                    dialog.add(texte, gbc);

                                    gbc.gridwidth = 1;
                                    gbc.gridx = 0;
                                    gbc.gridy = 1;
                                    dialog.add(adresseText, gbc);
                                    gbc.gridx = 1;
                                    gbc.gridy = 1;
                                    dialog.add(adresse, gbc);
                                    gbc.gridx = 0;
                                    gbc.gridy = 2;
                                    dialog.add(teleText, gbc);
                                    gbc.gridx = 1;
                                    gbc.gridy = 2;
                                    dialog.add(telephone, gbc);

                                    gbc.gridx = 0;
                                    gbc.gridy = 3;
                                    dialog.add(mailText, gbc);
                                    gbc.gridx = 1;
                                    gbc.gridy = 3;
                                    dialog.add(mail, gbc);

                                    gbc.gridx = 0;
                                    gbc.gridy = 4;
                                    gbc.gridwidth = 2;
                                    dialog.add(ajoutClient, gbc);
                                    dialog.setVisible(true);
                                }
                                ;
                                // Une fois rajouté, ou simplement si le client existais on fait un nouveau
                                // dialogue
                                JFrame f = new JFrame();
                                JDialog dialog = new JDialog(f, "Information supplementaires", true);
                                dialog.setLocationRelativeTo(null);
                                dialog.setSize(500, 400);

                                // Ici on uniquement demander le nombre de personnes, pour pouvoir filtrer les
                                // chambres á offrir á notre client
                                JPanel resultat = new JPanel(new GridBagLayout());
                                GridBagConstraints grille = new GridBagConstraints();
                                grille.insets = new Insets(10, 10, 10, 10);
                                grille.gridx = 0;
                                grille.gridy = 0;
                                resultat.add(new JLabel("Entrez le nombre de personnes"), grille);
                                grille.gridy = 1;

                                JTextField champPers = new JTextField(5);
                                resultat.add(champPers, grille);

                                champPers.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent ev) {
                                        resultat.removeAll();
                                        resultat.setLayout(new BorderLayout());
                                        // On importe le nombre de personnes
                                        int personnes = Integer.valueOf(champPers.getText());
                                        JPanel recherchePanel = new JPanel();
                                        // On va donc donner les chambres disponibles et qui suivent ce que le client
                                        // recherche
                                        JLabel nomPtext = new JLabel("Entrez le numero de chambre choissie");
                                        recherchePanel.add(nomPtext);

                                        JTextField chambreF = new JTextField(10);
                                        chambreF.addActionListener(new ActionListener() {
                                            public void actionPerformed(ActionEvent e) {
                                                // On crée notre resrvation avec les informations données
                                                int chambreselect = Integer.valueOf(chambreF.getText());
                                                // Pour l'id un utilise le chambre, le nombre de personnes et un entier
                                                // aléatoire entre 0 et 100
                                                hotel.consulterChambre(chambreselect)
                                                        .newReservation(chambreselect * 1000 + personnes * 100
                                                                + (int) (Math.random() * 100),
                                                                dateArrive, dateDepart,
                                                                nomSelected, prenomSelected);
                                                dialog.setVisible(false);
                                                afficher_ajout_reservations();
                                            }
                                        });
                                        recherchePanel.add(chambreF);

                                        // Nous allons également donner l'option de filtrer les chambres en fonction de
                                        // leur type

                                        // Cela permet de savoir ulterieurement le type de chambres qui sont dispónibles
                                        // sans passer par des messages pour indiquer qu'il n'y a pas de telle ou telle
                                        // chambre
                                        JComboBox<String> typeOptions = new JComboBox<>(
                                                new String[] { "Tout", "Simple", "Double", "Suit", "Presidentielle" });
                                        // Par defaut il apparaisseront toutes le chambres
                                        typeOptions.setSelectedItem("Tout");
                                        // On etabli les dimensions
                                        typeOptions.setPreferredSize(new Dimension(100, 25));
                                        recherchePanel.add(typeOptions);

                                        // Puis on va créer notre table de chambres disponibles
                                        Vector<Vector<String>> tableDispo = new Vector<Vector<String>>();
                                        Vector<String> noCh = new Vector<>();
                                        Vector<String> type = new Vector<>();
                                        Vector<String> prixTot = new Vector<>();
                                        tableDispo.add(noCh);
                                        tableDispo.add(type);
                                        tableDispo.add(prixTot);
                                        remplirChambres();
                                        remplirReservations();
                                        for (int i = 0; i < hotel.listeChambres.size(); i++) {
                                            // On parcours nos chambres et on test sa disponibilitée, sa bonne capacitée
                                            // et son type
                                            if (hotel.listeChambres.get(i).disponibilite(dateDepart,
                                                    dateArrive) == true
                                                    && hotel.listeChambres.get(i).capacite >= personnes
                                                    && (hotel.listeChambres.get(i).typeChambre
                                                            .equals(typeOptions.getSelectedItem()) ||
                                                            typeOptions.getSelectedItem().equals("Tout"))) {
                                                // ON rajoute les informations de la chambre á notre vecteur 2 x 2 si
                                                // elles remplissent les conditions
                                                tableDispo.get(0)
                                                        .add(String.valueOf(hotel.listeChambres.get(i).noChambre));
                                                tableDispo.get(1)
                                                        .add(String.valueOf(hotel.listeChambres.get(i).typeChambre));
                                                tableDispo.get(2).add(String.valueOf(hotel.listeChambres.get(i).prix
                                                        * hotel.obtentionPeriode(dateArrive, dateDepart).size() - 1));
                                            }
                                        }

                                        // On transformt notre vecteur dans une table de string( nécessaire pour créer
                                        // une Jtable)
                                        String[][] dispoTab = new String[tableDispo.get(0).size()][3];
                                        for (int i = 0; i < tableDispo.size(); i++) {
                                            for (int a = 0; a < tableDispo.get(i).size(); a++) {
                                                dispoTab[a][i] = tableDispo.get(i).get(a);
                                            }
                                        }
                                        // On crée nos tables et le panel avec scroll vertical
                                        DefaultTableModel tableModel = new DefaultTableModel(dispoTab,
                                                new String[] { "Numero de chambre", "Type", "Prix du sejour" });
                                        JTable table = new JTable(tableModel);
                                        JScrollPane scrollPane = new JScrollPane(table);
                                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                                        typeOptions.addActionListener(new ActionListener() {
                                            public void actionPerformed(ActionEvent ev) {
                                                // Si le type est changée on fait le meme processus pour remplir nos
                                                // chambres
                                                Vector<Vector<String>> tableDispo = new Vector<Vector<String>>();
                                                Vector<String> noCh = new Vector<>();
                                                Vector<String> type = new Vector<>();
                                                Vector<String> prixTot = new Vector<>();
                                                tableDispo.add(noCh);
                                                tableDispo.add(type);
                                                tableDispo.add(prixTot);
                                                remplirChambres();
                                                remplirReservations();
                                                for (int i = 0; i < hotel.listeChambres.size(); i++) {
                                                    if (hotel.listeChambres.get(i).disponibilite(dateDepart,
                                                            dateArrive) == true
                                                            && hotel.listeChambres.get(i).capacite >= personnes
                                                            && (hotel.listeChambres.get(i).typeChambre
                                                                    .equals(typeOptions.getSelectedItem()) ||
                                                                    typeOptions.getSelectedItem().equals("Tout"))) {

                                                        tableDispo.get(0)
                                                                .add(String
                                                                        .valueOf(hotel.listeChambres.get(i).noChambre));
                                                        tableDispo.get(1)
                                                                .add(String.valueOf(
                                                                        hotel.listeChambres.get(i).typeChambre));
                                                        tableDispo.get(2)
                                                                .add(String.valueOf(hotel.listeChambres.get(i).prix
                                                                        * hotel.obtentionPeriode(dateArrive, dateDepart)
                                                                                .size()
                                                                        - 1));
                                                    }
                                                }
                                                String[][] dispoTab = new String[tableDispo.get(0).size()][3];

                                                for (int i = 0; i < tableDispo.size(); i++) {
                                                    for (int a = 0; a < tableDispo.get(i).size(); a++) {
                                                        dispoTab[a][i] = tableDispo.get(i).get(a);
                                                    }
                                                }
                                                DefaultTableModel tableModel = new DefaultTableModel(dispoTab,
                                                        new String[] { "Numero de chambre", "Type", "Prix du sejour" });
                                                JTable table = new JTable(tableModel);
                                                // On modifie le contenu de notre panel avec la nouvelle table
                                                scrollPane.setViewportView(table);

                                                resultat.revalidate();
                                                resultat.repaint();
                                            }
                                        });

                                        resultat.add(recherchePanel, BorderLayout.NORTH);
                                        resultat.add(scrollPane, BorderLayout.CENTER);

                                        resultat.revalidate();
                                        resultat.repaint();
                                        dialog.add(resultat);
                                    }
                                });

                                dialog.add(resultat);
                                dialog.setVisible(true);

                                ecranAffichage.revalidate();
                                ecranAffichage.repaint();
                            }
                        });

                        grille.gridwidth = 1;
                        grille.gridx = 1;
                        grille.gridy = 1;
                        ecranAffichage.add(nomText, grille);
                        grille.gridx = 2;
                        ecranAffichage.add(nomField, grille);

                        grille.gridx = 1;
                        grille.gridy = 2;
                        ecranAffichage.add(prenomText, grille);
                        grille.gridx = 2;
                        ecranAffichage.add(prenomField, grille);

                        ecranAffichage.revalidate();
                        ecranAffichage.repaint();
                    }
                });
                ecranAffichage.revalidate();
                ecranAffichage.repaint();
            }
        });

        ecranAffichage.add(titrePanel, grille);
        grille.gridy = 1;
        ecranAffichage.add(calendrier, grille);
        grille.gridy = 2;
        ecranAffichage.add(buttonDate, grille);

        ecranAffichage.revalidate();
        ecranAffichage.repaint();

    };

    public void afficher_consulter_reservations() {
        ecranAffichage.removeAll();
        remplirChambres();
        remplirClients();
        remplirReservations();
        ecranAffichage.setLayout(new GridLayout(2, 1, 100, 0));

        JPanel panelReservations = new JPanel(new GridLayout(1, 1, 0, 0));
        Vector<Reservations> reservations = hotel.getReservations();

        // On crée notre table pour les informations des reservations
        String[][] tableRes = new String[reservations.size()][4];
        for (int i = 0; i < tableRes.length; i++) {
            tableRes[i][0] = String.valueOf(reservations.get(i).idReservation);
            tableRes[i][1] = formatDate.format(reservations.get(i).dateDeb) + " - "
                    + formatDate.format(reservations.get(i).dateFin);
            tableRes[i][2] = reservations.get(i).client.prenom + "," + reservations.get(i).client.nom;
            tableRes[i][3] = String.valueOf(reservations.get(i).chambre.noChambre);
        }

        // On crée la JTable et on la rajoute á un panel avec scroll vertical
        DefaultTableModel tableModel = new DefaultTableModel(tableRes,
                new String[] { "Id Reservation", "Periode", "Client  ( PRENOM,NOM )", "No chambre" });
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelReservations.add(scrollPane);

        // On crée également un panel pour les recherches
        JPanel partieRecherche = new JPanel(new GridBagLayout());
        GridBagConstraints grilleR = new GridBagConstraints();
        grilleR.insets = new Insets(10, 10, 10, 10);
        grilleR.gridwidth = 2;
        partieRecherche.add(new JLabel("Rechercher une reservation par :"), grilleR);
        grilleR.gridwidth = 1;
        grilleR.gridx = 0;
        grilleR.gridy = 1;
        // On donne l'option de trouver une reservation par id pour client
        partieRecherche.add(new JLabel("Id de la reservation"), grilleR);
        grilleR.gridy = 2;
        partieRecherche.add(new JLabel("Nom et prenom (Prenom,Nom)"), grilleR);
        grilleR.gridx = 1;
        grilleR.gridy = 1;
        JTextField champId = new JTextField(10);
        partieRecherche.add(champId, grilleR);
        grilleR.gridy = 2;
        JTextField champclient = new JTextField(15);
        partieRecherche.add(champclient, grilleR);

        champId.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                remplirReservations();
                // on importe l'id de reservation
                int idRes = Integer.parseInt(champId.getText());
                JFrame f = new JFrame();
                // On crée le dialogue et ses componenets
                JDialog dialogue = new JDialog(f, "Options pour reservation");
                dialogue.setSize(new Dimension(300, 250));
                dialogue.setLayout(new GridBagLayout());

                GridBagConstraints grille = new GridBagConstraints();
                grille.insets = new Insets(10, 10, 10, 10);

                grille.gridy = 0;
                JButton info = new JButton("Information sur la reservation");
                dialogue.add(info, grille);

                grille.gridy = 1;
                JButton annuler = new JButton("Annuler la reservation");
                dialogue.add(annuler, grille);

                grille.gridy = 2;
                JButton modifier = new JButton("Modifier la reservation");
                dialogue.add(modifier, grille);

                info.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        JOptionPane.showMessageDialog(ecranAffichage,
                                hotel.consulterRes(idRes).info(),
                                "Information reservation " + champId.getText(),
                                JOptionPane.INFORMATION_MESSAGE);
                        dialogue.setVisible(false);
                    }
                });

                annuler.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        hotel.consulterRes(idRes).eliminer();
                        dialogue.setVisible(false);
                        afficher_consulter_reservations();
                    }
                });

                modifier.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        dialogue.setVisible(false);
                        // On va créer un dialogue pour demander les modifications á faire
                        JFrame f = new JFrame();
                        JDialog dialogue = new JDialog(f, "Options ");
                        dialogue.setSize(new Dimension(500, 400));
                        dialogue.setLayout(new GridBagLayout());

                        // On crée la grille et ses components egalement
                        GridBagConstraints grille = new GridBagConstraints();
                        grille.insets = new Insets(10, 10, 10, 10);

                        grille.gridwidth = 2;

                        dialogue.add(
                                new JLabel(
                                        "Selectionnez un parametre á changer ainsi que la modification dessirée"),
                                grille);

                        grille.gridwidth = 1;
                        JComboBox<String> typeOptions = new JComboBox<String>(
                                new String[] { "Id reservation", "Numero de chambre", "Date debut",
                                        "Date fin" });
                        grille.gridy = 1;
                        dialogue.add(typeOptions, grille);

                        JTextField champMod = new JTextField(15);
                        champMod.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent ev) {
                                // On modifie notre reservation avec la methode modifier
                                hotel.consulterRes(idRes).modifier((String) typeOptions.getSelectedItem(),
                                        champMod.getText());
                                dialogue.setVisible(false);
                                afficher_consulter_reservations();
                            }
                        });
                        grille.gridx = 1;
                        dialogue.add(champMod, grille);

                        dialogue.setLocationRelativeTo(null);
                        dialogue.setVisible(true);
                    }
                });
                // On verifie si la resergvation existe
                if (hotel.consulterRes(Integer.parseInt(champId.getText())) != null) {
                    dialogue.setLocationRelativeTo(null);
                    dialogue.setVisible(true);
                }

            }
        });
        champclient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // Dans le cas où on cherche la reservation par client, on va uniquement
                // modifier la table pour que l'utilisateur puisse observer toutes les
                // reservationos du client en question
                String[] casses = champclient.getText().split(",");

                // On va créer un vecteur contenant les reservation du client recherché
                Vector<Reservations> newReservations = new Vector<Reservations>();
                reservations.forEach(element -> {
                    if (element.client.nom.equals(casses[1]) && element.client.prenom.equals(casses[0])) {
                        newReservations.add(element);
                    }
                });

                // On recrée le tableau des reservations mais uniquement avec les reservations
                // du client en question
                String[][] newTableRes = new String[newReservations.size()][4];
                for (int i = 0; i < newTableRes.length; i++) {
                    newTableRes[i][0] = String.valueOf(newReservations.get(i).idReservation);
                    newTableRes[i][1] = formatDate.format(newReservations.get(i).dateDeb) + " - "
                            + formatDate.format(newReservations.get(i).dateFin);
                    newTableRes[i][2] = newReservations.get(i).client.prenom + "," + newReservations.get(i).client.nom;
                    newTableRes[i][3] = String.valueOf(newReservations.get(i).chambre.noChambre);
                }

                // on crée la table et on modifie notre panel avec scroll verticale
                DefaultTableModel newTableModel = new DefaultTableModel(newTableRes,
                        new String[] { "Id Reservation", "Periode", "Client  ( PRENOM,NOM )", "No chambre" });
                JTable newTable = new JTable(newTableModel);
                scrollPane.setViewportView(newTable);
                ecranAffichage.revalidate();
                ecranAffichage.repaint();
            }
        });
        grilleR.gridy = 3;
        grilleR.gridwidth = 1;
        grilleR.gridx = 0;

        ecranAffichage.add(partieRecherche);
        ecranAffichage.add(panelReservations);

        ecranAffichage.revalidate();
        ecranAffichage.repaint();
    };

    public void afficher_consultation_client() {
        ecranAffichage.removeAll();
        // On utlise un Border Layout puisque on n'a que deux composants
        ecranAffichage.setLayout(new BorderLayout());

        remplirClients();
        // Nous importons la lsite des clients pour plus de facilité
        Vector<Clients> lClients = hotel.listeClients;

        // On crée le tableau contenant les informations des clients
        String[][] tableClients = new String[lClients.size()][6];
        for (int i = 0; i < tableClients.length; i++) {
            tableClients[i][0] = String.valueOf(i + 1);
            tableClients[i][1] = lClients.get(i).nom;
            tableClients[i][2] = lClients.get(i).prenom;
            tableClients[i][3] = lClients.get(i).adresse;
            tableClients[i][4] = lClients.get(i).mail;
            tableClients[i][5] = lClients.get(i).telephone;
        }

        // Nous créons la Jtable et le panel vertical
        DefaultTableModel tableModel = new DefaultTableModel(tableClients,
                new String[] { "Indice", "Nom", "Prenom", "Adresse", "Mail", "Telephone" });

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        ecranAffichage.add(scrollPane, BorderLayout.CENTER);

        // Nous créon un panel de recherche
        JPanel recherchePan = new JPanel();
        recherchePan.add(new JLabel("Entrez l'indice du client"));
        JTextField champInd = new JTextField(5);
        champInd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // Nous importons l'indice entrée par le client
                int indice = Integer.valueOf(champInd.getText());
                Clients client = lClients.get(Integer.valueOf(champInd.getText()) - 1);
                JFrame f = new JFrame();
                JDialog dialogue = new JDialog(f, "Options pour le client ");
                dialogue.setSize(new Dimension(300, 250));
                dialogue.setLayout(new GridBagLayout());

                GridBagConstraints grille = new GridBagConstraints();
                grille.insets = new Insets(10, 10, 10, 10);

                JButton info = new JButton("Information sur le client");
                info.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        // On renvoie un dialogue avec les informations du client
                        JOptionPane.showMessageDialog(null, client.info());
                        dialogue.setVisible(false);
                        afficher_consultation_client();
                    }
                });

                JButton reservations = new JButton("Reservations client");
                reservations.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        // Dans un messsageDialog on renvoie la date du debut de la prochaine
                        // reservation du client
                        remplirReservations();
                        remplirClients();
                        String texte = "";
                        for (int i = 0; i < hotel.listeChambres.size(); i++) {
                            for (int a = 0; a < hotel.listeChambres.get(i).reservations.size(); a++) {
                                if (hotel.listeChambres.get(i).reservations.get(a).client.nom.equals(client.nom) &&
                                        hotel.listeChambres.get(i).reservations.get(a).client.prenom
                                                .equals(client.prenom)) {
                                    texte += formatDate.format(hotel.listeChambres.get(i).reservations.get(a).dateDeb)
                                            + "\n";
                                }
                            }
                        }
                        JOptionPane.showMessageDialog(null, texte);
                        dialogue.setVisible(false);
                        afficher_consultation_client();
                    }
                });

                JButton eliminer = new JButton("Eliminer le client");
                eliminer.addActionListener(new ActionListener() {
                    Boolean eliminer = true;

                    public void actionPerformed(ActionEvent ev) {
                        // On test si le client a des reservations en cours
                        hotel.listeChambres.forEach(element -> {
                            element.reservations.forEach(el -> {
                                if (el.client.nom.equals(client.nom) && el.client.prenom.equals(client.prenom)) {
                                    // Si le client a des reservation on bloque l'elimination
                                    eliminer = false;
                                }
                            });
                        });
                        if (eliminer == true) {
                            // Pour eliminer on utilise notre methode eliminerInfo
                            eliminerInfo(docClients, Integer.valueOf(champInd.getText()) + 1);
                            dialogue.setVisible(false);
                            afficher_consultation_client();
                        } else {
                            // Si le client a des reservation on renvoie qu'il est impossible á eliminer
                            JOptionPane.showMessageDialog(null,
                                    "Le client ne peut pas être eliminé puisque il a des reservations en cours");
                            dialogue.setVisible(false);
                            afficher_consultation_client();
                        }
                    }
                });

                JButton modifier = new JButton("Modifer infos client");
                modifier.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        // On crée un dialogue et ses compossants nécessaires pour la modification
                        // d'information d'un client
                        dialogue.setVisible(false);
                        JFrame f = new JFrame();
                        JDialog dialogue = new JDialog(f, "Options pour le client ");
                        dialogue.setSize(new Dimension(500, 400));
                        dialogue.setLayout(new GridBagLayout());

                        GridBagConstraints grille = new GridBagConstraints();
                        grille.insets = new Insets(10, 10, 10, 10);

                        grille.gridwidth = 2;

                        dialogue.add(
                                new JLabel("Selectionnez un parametre á changer ainsi que la modification dessirée"),
                                grille);

                        grille.gridwidth = 1;
                        JComboBox<String> typeOptions = new JComboBox<String>(
                                new String[] { "Nom", "Prenom", "Adresse", "Mail", "Telephone" });
                        grille.gridy = 1;
                        dialogue.add(typeOptions, grille);

                        JTextField champMod = new JTextField(15);
                        champMod.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent ev) {
                                // On utilise la methode modifier info pour modifier le client
                                modifierInfo(docClients, indice, typeOptions.getSelectedIndex(),
                                        champMod.getText());
                                dialogue.setVisible(false);
                                afficher_consultation_client();
                            }
                        });
                        grille.gridx = 1;
                        dialogue.add(champMod, grille);

                        dialogue.setLocationRelativeTo(null);
                        dialogue.setVisible(true);
                    }
                });

                grille.gridy = 0;
                dialogue.add(info, grille);
                grille.gridy = 1;
                dialogue.add(reservations, grille);
                grille.gridy = 2;
                dialogue.add(eliminer, grille);
                grille.gridy = 3;
                dialogue.add(modifier, grille);

                dialogue.setLocationRelativeTo(null);
                dialogue.setVisible(true);
            }
        });

        recherchePan.add(champInd);
        recherchePan.add(new JLabel("OU"));

        // Nous donnons également l'option de rechercher un client á aprtir de son nom
        // et prenom
        JButton rechercheManuel = new JButton("Rechercher manuelment");
        recherchePan.add(rechercheManuel);
        rechercheManuel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // Nous créons le dialogue permettant de demander le nom et prenom
                JFrame f = new JFrame();
                JDialog dialogue = new JDialog(f, "Recherche client ");
                dialogue.setSize(new Dimension(500, 250));
                dialogue.setLayout(new GridBagLayout());

                GridBagConstraints grille = new GridBagConstraints();
                grille.insets = new Insets(10, 10, 10, 10);
                grille.gridy = 0;
                dialogue.add(new JLabel("Nom du client"), grille);
                grille.gridx = 1;

                JTextField nomRech = new JTextField(15);
                dialogue.add(nomRech, grille);
                grille.gridy = 1;
                JTextField prenomRech = new JTextField(15);

                prenomRech.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        // Nous cherchons l'indice du client á partir de son nom et prenom, on va donc
                        // parcourir notre tableau
                        for (int i = 0; i < tableClients.length; i++) {
                            // on test si on a trouvé le client
                            if (tableClients[i][1].equals(nomRech.getText())
                                    && tableClients[i][2].equals(prenomRech.getText())) {
                                // Le cas écheant, nous ecrivons son indice dans le champ et nous modifions la
                                // table des clients afin de laisser uniquement ce dernier : au cas où besoin
                                // d'informations rapides
                                champInd.setText(tableClients[i][0]);
                                JOptionPane.showMessageDialog(null, "L'indice du client est : " + tableClients[i][0]);
                                // Cette fois ci on modifie la table en eliminant le panel et en créant un
                                // nouveau (Pas mieux, juste pour tester)
                                ecranAffichage.remove(scrollPane);
                                dialogue.setVisible(false);
                                String[][] newTable = new String[1][6];

                                newTable[0] = tableClients[i];

                                DefaultTableModel tableModel = new DefaultTableModel(newTable,
                                        new String[] { "Indice", "Nom", "Prenom", "Adresse", "Mail", "Telephone" });

                                JTable table = new JTable(tableModel);
                                JScrollPane scrollPane = new JScrollPane(table);
                                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                                ecranAffichage.add(scrollPane, BorderLayout.CENTER);
                                ecranAffichage.revalidate();
                                ecranAffichage.repaint();
                            }
                        }
                    }
                });

                dialogue.add(prenomRech, grille);
                grille.gridy = 1;
                grille.gridx = 0;
                dialogue.add(new JLabel("Prenom du client"), grille);

                dialogue.setLocationRelativeTo(null);
                dialogue.setVisible(true);

            }
        });

        ecranAffichage.add(recherchePan, BorderLayout.NORTH);
        ecranAffichage.revalidate();
        ecranAffichage.repaint();
    }

    public void afficher_ajout_client() {
        // L'ajout du client est simple, on a besoin de toutes les informations et on
        // crée donc une grille et les ocmposants nécessaires pour avoir ces
        // informations
        ecranAffichage.removeAll();
        ecranAffichage.setLayout(new GridBagLayout());
        GridBagConstraints grille = new GridBagConstraints();
        grille.insets = new Insets(10, 10, 10, 10);

        grille.gridx = 0;
        ecranAffichage.add(new JLabel("Nom du client"), grille);
        grille.gridx = 1;
        ecranAffichage.add(new JLabel("Prenom du client"), grille);
        grille.gridx = 2;
        ecranAffichage.add(new JLabel("Adresse du client"), grille);
        grille.gridx = 3;
        ecranAffichage.add(new JLabel("Mail du client"), grille);
        grille.gridx = 4;
        ecranAffichage.add(new JLabel("Telephone du client"), grille);

        grille.gridy = 1;
        grille.gridx = 0;
        JTextField nom = new JTextField(10);
        ecranAffichage.add(nom, grille);
        grille.gridx = 1;
        JTextField prenom = new JTextField(10);
        ecranAffichage.add(prenom, grille);
        grille.gridx = 2;
        JTextField adresse = new JTextField(15);
        ecranAffichage.add(adresse, grille);
        grille.gridx = 3;
        JTextField mail = new JTextField(10);
        ecranAffichage.add(mail, grille);
        grille.gridx = 4;
        JTextField telephone = new JTextField(10);
        ecranAffichage.add(telephone, grille);

        grille.gridy = 2;
        grille.gridx = 0;
        grille.gridwidth = 5;
        grille.insets = new Insets(100, 10, 20, 10);
        JButton valider = new JButton("Cliquez pour valider les informations");
        ecranAffichage.add(valider, grille);

        // Une fois les informations validées :
        valider.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // On rajoute á notre fichier le client qu'on a crée
                hotel.newClient(nom.getText(), prenom.getText(), adresse.getText(), mail.getText(),
                        telephone.getText());
                JOptionPane.showMessageDialog(null,
                        prenom.getText() + " " + nom.getText() + " a été bien ajouté au système.");
                afficher_ajout_client();
            }
        });

        ecranAffichage.revalidate();
        ecranAffichage.repaint();
    }

    public void afficher_liste_listeProduits() {
        ecranAffichage.removeAll();
        // On utilise un BorderLayour puisqu'on a que deux composants
        ecranAffichage.setLayout(new BorderLayout());
        // Nous créeons la table de produits ainsi qu'un panel de recherche
        String[][] tableProd = new Consommation().tableComplete();
        JPanel recherchePanel = new JPanel();

        JLabel nomPtext = new JLabel("Entrez le nom du produit pour le rechercher");
        JTextField nomPchamp = new JTextField(20);

        nomPchamp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String prodCherche = nomPchamp.getText();
                for (int i = 0; i < tableProd.length; i++) {
                    if (tableProd[i][0].equals(prodCherche)) {
                        // Nous créons un dialogue pour les options á faire sur le produit
                        int ligne = i + 1;
                        JFrame f = new JFrame();
                        JDialog dialogue = new JDialog(f, "Options pour le produit ");
                        dialogue.setSize(new Dimension(300, 250));
                        dialogue.setLayout(new GridBagLayout());
                        GridBagConstraints grille = new GridBagConstraints();
                        grille.insets = new Insets(10, 10, 10, 10);
                        // On peut renvoyer l'information du client
                        JButton infoProd = new JButton("Informations sur : " + prodCherche);
                        infoProd.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent ev) {
                                JOptionPane.showMessageDialog(ecranAffichage,
                                        "Le prix de ce produit est de " + tableProd[ligne - 2][1] + " euros" + "\n"
                                                + "Et en stock il en reste "
                                                + tableProd[ligne - 2][2] + " unités",
                                        "Information sur " + prodCherche,
                                        JOptionPane.INFORMATION_MESSAGE);
                                dialogue.setLocationRelativeTo(null);
                                dialogue.setVisible(false);
                            }
                        });

                        dialogue.add(infoProd, grille);

                        // ON peut modifier les informations du produit
                        JButton modifProd = new JButton("Modifier les informations du produit");
                        modifProd.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent ev) {
                                // Nous créoons un dialogue qui demande les informations pour modifier le
                                // produit
                                dialogue.setVisible(false);
                                JFrame f = new JFrame();
                                JDialog dialogue = new JDialog(f, "Modification des informations du produit ");
                                dialogue.setSize(new Dimension(500, 150));
                                dialogue.setLayout(new GridBagLayout());
                                GridBagConstraints grille = new GridBagConstraints();
                                grille.insets = new Insets(10, 10, 10, 10);

                                JComboBox<String> options = new JComboBox<>(
                                        new String[] { "Nom du produit", "Prix", "Stock" });
                                dialogue.add(options, grille);

                                JTextField champMod = new JTextField(15);
                                grille.gridx = 2;
                                dialogue.add(champMod, grille);
                                champMod.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent ev) {
                                        // On modifie notre produit grace á modifierInfo
                                        modifierInfo(docProduits, ligne, options.getSelectedIndex(),
                                                champMod.getText());
                                        dialogue.setVisible(false);
                                        afficher_liste_listeProduits();
                                    }
                                });

                                dialogue.setLocationRelativeTo(null);
                                dialogue.setVisible(true);
                            }
                        });

                        grille.gridy = 1;
                        dialogue.add(modifProd, grille);

                        dialogue.setLocationRelativeTo(null);
                        dialogue.setVisible(true);
                    }
                }
            }
        });

        recherchePanel.add(nomPtext);
        recherchePanel.add(nomPchamp);

        // Nous créons la JTable et notre scrollPane á partir de cette derniere
        DefaultTableModel tableModel = new DefaultTableModel(tableProd,
                new String[] { "Nom du produit", "Prix", "Quantité de Stock" });
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Nous rajoutons les composants
        ecranAffichage.add(recherchePanel, BorderLayout.NORTH);
        ecranAffichage.add(scrollPane, BorderLayout.CENTER);

        ecranAffichage.revalidate();
        ecranAffichage.repaint();
    };

    public void afficher_liste_listeConsommations() {
        remplirReservations();
        ecranAffichage.removeAll();
        // On utilise un BorderLayour puisqu'on a que deux composants
        ecranAffichage.setLayout(new BorderLayout());
        // On utilise la même configuiration de recherche panel + tableau

        Vector<String> tableConso = new Vector<>();
        // On parcours nos reservations
        for (int i = 0; i < hotel.getReservations().size(); i++) {
            // On test si la reservation a un sejour = active
            if (hotel.getReservations().get(i).active == true) {
                Reservations res = hotel.getReservations().get(i);
                // si c'est le cas, on rajoute les consommations
                res.sejour.listeConsommations.forEach(el -> {
                    // Pour les rajouter au vecteur, on sépare les informations par un virgule pour
                    // ensuite pouvoir créer le tableau 2 x 2
                    tableConso.add(
                            res.idReservation + "," + el.prodAchete + "," + el.quantiteAchetes + "," + el.prixTotal());
                });
            }
        }

        // Nous créons et nous remplisons le tableau á deux dimensions
        String[][] tableConsommations = new String[tableConso.size()][4];
        for (int i = 0; i < tableConso.size(); i++) {
            String[] casses = tableConso.get(i).split(",");
            tableConsommations[i] = casses;
        }
        // On crée le panel de recherche pour rechercher les consommations en fonction
        // de l'id de reservations
        JPanel recherchePanel = new JPanel();
        JLabel nomPtext = new JLabel(
                "Entrez le id de reservation pour avoir ses consommations ");

        JTextField IdChamp = new JTextField(20);

        // Nous créons la JTable et le scrollPane
        DefaultTableModel tableModel = new DefaultTableModel(tableConsommations,
                new String[] { "Id de reservation", "Nom du produit", "Quantité achété", "Prix (TTC)" });
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        IdChamp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(IdChamp.getText());
                Vector<String> tableConso = new Vector<>();
                // On parcours nos reservations
                for (int i = 0; i < hotel.getReservations().size(); i++) {
                    // On test si la reservation a un sejour = active
                    if (hotel.getReservations().get(i).active == true
                            && hotel.getReservations().get(i).idReservation == id) {
                        Reservations res = hotel.getReservations().get(i);
                        // si c'est le cas, on rajoute les consommations
                        res.sejour.listeConsommations.forEach(el -> {
                            // Pour les rajouter au vecteur, on sépare les informations par un virgule pour
                            // ensuite pouvoir créer le tableau 2 x 2
                            tableConso.add(
                                    res.idReservation + "," + el.prodAchete + "," + el.quantiteAchetes + ","
                                            + el.prixTotal());
                        });
                    }
                }

                // Nous créons et nous remplisons le tableau á deux dimensions
                String[][] tableConsommations = new String[tableConso.size()][4];
                for (int i = 0; i < tableConso.size(); i++) {
                    String[] casses = tableConso.get(i).split(",");
                    tableConsommations[i] = casses;
                }

                DefaultTableModel tableModel = new DefaultTableModel(tableConsommations,
                        new String[] { "Id de reservation", "Nom du produit", "Quantité achété", "Prix (TTC)" });
                JTable table = new JTable(tableModel);

                scrollPane.setViewportView(table);
                afficher_liste_listeConsommations();
                ecranAffichage.revalidate();
                ecranAffichage.repaint();
            }
        });

        recherchePanel.add(nomPtext);
        recherchePanel.add(IdChamp);

        // On rajoute nos compossants
        ecranAffichage.add(recherchePanel, BorderLayout.NORTH);
        ecranAffichage.add(scrollPane, BorderLayout.CENTER);

        ecranAffichage.revalidate();
        ecranAffichage.repaint();
    };

    public void afficher_parametres_date() {
        ecranAffichage.removeAll();
        ecranAffichage.setLayout(new GridBagLayout());

        GridBagConstraints grille = new GridBagConstraints();
        grille.gridx = 0;
        grille.gridy = 0;
        grille.insets = new Insets(10, 20, 50, 20);

        JLabel titrePanel = new JLabel("Selectionnez la date");
        titrePanel.setFont(new Font("Arial", Font.BOLD, 20));
        titrePanel.setForeground(Color.WHITE);
        titrePanel.setBackground(Color.BLACK);
        titrePanel.setOpaque(true);
        titrePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JCalendar calendrier = new JCalendar();
        calendrier.setCalendar(dateAjd);
        JButton button = new JButton("Modifier la date");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Calendar selectDate = calendrier.getCalendar();
                dateAjd = selectDate;
                JOptionPane.showMessageDialog(ecranAffichage,
                        "La date a été modifié au : " + formatDate.format(dateAjd.getTime()),
                        "Modification date",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        grille.gridx = 0;
        grille.gridy = 0;
        ecranAffichage.add(titrePanel, grille);
        grille.gridx = 0;
        grille.gridy = 1;
        grille.insets = new Insets(50, 20, 100, 20);
        ecranAffichage.add(calendrier, grille);
        grille.gridx = 0;
        grille.gridy = 2;
        grille.insets = new Insets(0, 20, 10, 20);
        ecranAffichage.add(button, grille);

        ecranAffichage.revalidate();
        ecranAffichage.repaint();
    }

    // Methodes pour creation de classes á partir de fichier texte
    public void remplirChambres() {
        hotel.listeChambres.clear();
        // On crée un compteur et on l'initialise : ils nous servira pour sauter la
        // premiere ligne du fichier( correspondant a l'en tete)
        int compt = 0;
        // On cree un fichier ainsi qu'un lecteur de fichier
        File fichier = new File(docChambres);
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
                    hotel.ajouterChambre(Integer.valueOf(casses[0]), Integer.valueOf(casses[1]), true, casses[2]);
                } else {
                    compt++;
                }
            }
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public void remplirClients() {
        hotel.listeClients.clear();
        // On crée un compteur et on l'initialise : ils nous servira pour sauter la
        // premiere ligne du fichier( correspondant a l'en tete)
        int compt = 0;
        File fichier = new File(docClients);
        // On cree un fichier ainsi qu'un lecteur de fichier
        try (FileReader reader = new FileReader(fichier)) {
            BufferedReader lecteur = new BufferedReader(reader);
            String ligne;
            // On fait une boucle qui nous permettra de lire jusqu'a qu'on trouver une ligne
            // vide
            while ((ligne = lecteur.readLine()) != null) {
                // Saut de la premiere ligne
                if (compt != 0) {
                    String[] casses = ligne.split(",");
                    // Ajout de la chambre en question á aprtir des casses du fichier (ordre précis)
                    hotel.ajouterClients(casses[0], casses[1], casses[2], casses[3], casses[4]);
                } else {
                    compt++;
                }
            }
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public void remplirReservations() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        remplirChambres();
        remplirClients();
        for (int i = 0; i < hotel.listeChambres.size(); i++) {
            hotel.listeChambres.get(i).reservations.clear();
        }
        // On crée un compteur et on l'initialise : ils nous servira pour sauter la
        // premiere ligne du fichier( correspondant a l'en tete)
        int compt = 0;
        File fichier = new File(docReservations);
        // On cree un fichier ainsi qu'un lecteur de fichier
        try (FileReader reader = new FileReader(fichier)) {
            BufferedReader lecteur = new BufferedReader(reader);
            String ligne;
            // On fait une boucle qui nous permettra de lire jusqu'a qu'on trouver une ligne
            // vide
            while ((ligne = lecteur.readLine()) != null) {
                // Saut de la premiere ligne
                if (compt != 0) {
                    String[] casses = ligne.split(",");
                    /*
                     * On crée un boucle quicherche la chambre en fnct du numero de la premiere
                     * reservation. Puis, dés qu'elle trouve la chambre, on créera la reservation
                     * avec les
                     * informations du fichier
                     */
                    for (int i = 0; i < hotel.listeChambres.size(); i++) {
                        if (hotel.listeChambres.get(i).noChambre == Integer.parseInt(casses[5])) {
                            hotel.listeChambres.get(i)
                                    .ajouterReservations(new Reservations(Integer.parseInt(casses[0]),
                                            format.parse(casses[1]), format.parse(casses[2]),
                                            hotel.consulterClients(casses[3], casses[4]), hotel.listeChambres.get(i),
                                            Boolean.parseBoolean(casses[6])));
                        }
                    }
                } else {
                    compt++;
                }

            }
            remplirConsommations();
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void remplirConsommations() {
        // On crée un compteur et on l'initialise : ils nous servira pour sauter la
        // premiere ligne du fichier( correspondant a l'en tete)
        int compt = 0;
        File fichier = new File(docCoonsommations);
        // On cree un fichier ainsi qu'un lecteur de fichier
        try (FileReader reader = new FileReader(fichier)) {
            BufferedReader lecteur = new BufferedReader(reader);
            String ligne;
            // On fait une boucle qui nous permettra de lire jusqu'a qu'on trouver une ligne
            // vide
            while ((ligne = lecteur.readLine()) != null) {
                // Saut de la premiere ligne
                if (compt != 0) {
                    String[] casses = ligne.split(",");
                    hotel.getReservations().forEach(el -> {
                        if (el.idReservation == Integer.parseInt(casses[0]) && el.active == true) {
                            el.sejour.listeConsommations
                                    .add(new Consommation(el, casses[1], Integer.parseInt(casses[2])));
                        }
                    });
                } else {
                    compt++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthodes pour l'affichage de compenents complexes
    public JPanel carteHotel() {
        remplirChambres();
        remplirReservations();
        GridBagConstraints grilleRes = new GridBagConstraints();
        /*
         * Avec cette methode on va representer eles chambres de l'hotel
         * D'abord un va créer notre panel ou il y aura tous nos resultats
         */
        JPanel resultat = new JPanel();
        /*
         * On établie une grille de 3 ligne et 1 colonne ce qui nous permet
         * de positionner tous les elements de maniere ordonnée
         */
        resultat.setLayout(new GridBagLayout());
        /*
         * On commence par construir les etages qu'on a le choiox ainsi que le numero de
         * chambres par etage
         */
        String[] etageChoix = new String[hotel.nbrEtages];
        Vector<Integer> chambresParEtage = new Vector<>();
        for (int i = 1; i <= hotel.nbrEtages; i++) {
            etageChoix[i - 1] = String.valueOf(i);
            int compt = 0;// compteur qui nous permettra de savoir les chambres par etage
            for (int a = 0; a < hotel.listeChambres.size(); a++) {
                if (hotel.listeChambres.get(a).etage == i) {
                    compt++;
                }
            }
            chambresParEtage.add(compt);
        }
        // titre
        JLabel titre = new JLabel("Selectionnez un étage pour le visualizer");
        grilleRes.gridy = 0;
        resultat.add(titre, grilleRes);
        /*
         * On cree un panel et on rajoute pas directement le petit menu car
         * cela nous permet d'etablir des vraies dimensions pour le menu
         */
        JPanel selectionPanel = new JPanel(new FlowLayout());
        // JCombobox nous permet de faire une petit menu
        JComboBox<String> optionsEtage = new JComboBox<>();
        // Ce menu portera chaque etage qu'on puisse choisir
        optionsEtage.setModel(new DefaultComboBoxModel<>(etageChoix));
        optionsEtage.setSelectedItem("1");
        // On etabli les dimensions
        optionsEtage.setPreferredSize(new Dimension(100, 25));
        selectionPanel.add(optionsEtage);
        grilleRes.insets = new Insets(20, 0, 0, 0);
        grilleRes.gridy = 1;
        resultat.add(selectionPanel, grilleRes);

        // Ici on crée le panel oú il y aura la carte du hotel
        JPanel carte = new JPanel();
        // on importe l'option choisisse
        int etageSelect = Integer.valueOf((String) optionsEtage.getSelectedItem());
        /*
         * On divise notre panel en deux colonnes et en des lignes contenant chaqu'une
         * deux chambres
         */
        GridBagConstraints grille = new GridBagConstraints();
        carte.setLayout(new GridBagLayout());
        /*
         * On remplie notre carte, on met en couleur rouge les chambres occupés et en
         * vert les disponibles
         */
        int x = 0;
        int y = 0;
        int larg = 0;
        for (int i = 0; i < chambresParEtage.get(etageSelect - 1); i++) {
            if (i != 0 && i % 16 == 0) {
                y = 0;
                x += 2;
                larg += 40;
            }
            if (x == 0 || x % 2 == 0) {
                grille.insets = new Insets(5, 5 + larg, 5, 5);
            } else {
                grille.insets = new Insets(5, 5, 5, 5);
            }
            grille.gridx = x;
            grille.gridy = y;
            // Le panel pour chaque chambre sera une casse
            JPanel chambre = new JPanel(new GridBagLayout());
            // On etabli donc un contour noir
            chambre.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            // On met le numero de la chambre
            JLabel noCh = new JLabel(String.valueOf(etageSelect * 100 + i), JLabel.CENTER);
            chambre.add(noCh);
            // ON colore les casses en fonction de sa disponibilité
            Date dateA = new java.sql.Date(dateAjd.getTimeInMillis());
            if (hotel.consulterChambre(etageSelect * 100 + i).disponibilite(dateA, dateA) == false) {
                chambre.setBackground(Color.RED);
            } else if (hotel.consulterChambre(etageSelect * 100 + i).proxReservation() != null) {
                chambre.setBackground(Color.GREEN);
            } else {
                chambre.setBackground(Color.GRAY);
            }
            chambre.setBorder(new EmptyBorder(5, 5, 5, 5));
            carte.add(chambre, grille);
            if (x == 1 || x % 2 != 0) {
                y++;
                x -= 1;
            } else if (x == 0 || x % 2 == 0) {
                x++;
            }

        }
        grilleRes.gridy = 2;
        grilleRes.insets = new Insets(50, 0, 20, 0);
        resultat.add(carte, grilleRes);

        // On rajoute un action listenner pour actualiser la page
        // Si on change de selection la carte se met a jour
        optionsEtage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // on enleve tout et on le refait un par un
                resultat.removeAll();
                grilleRes.insets = new Insets(0, 0, 0, 0);
                grilleRes.gridy = 0;
                resultat.add(titre, grilleRes);
                grilleRes.insets = new Insets(20, 0, 0, 0);
                grilleRes.gridy = 1;
                resultat.add(selectionPanel, grilleRes);
                GridBagConstraints grilleRes = new GridBagConstraints();
                JPanel carte = new JPanel();
                int etageSelect = Integer.valueOf((String) optionsEtage.getSelectedItem());
                GridBagConstraints grille = new GridBagConstraints();
                carte.setLayout(new GridBagLayout());
                int x = 0;
                int y = 0;
                int larg = 0;
                for (int i = 0; i < chambresParEtage.get(etageSelect - 1); i++) {
                    if (i != 0 && i % 16 == 0) {
                        y = 0;
                        x += 2;
                        larg += 40;
                    }
                    if (x == 0 || x % 2 == 0) {
                        grille.insets = new Insets(5, 5 + larg, 5, 5);
                    } else {
                        grille.insets = new Insets(5, 5, 5, 5);
                    }
                    grille.gridx = x;
                    grille.gridy = y;
                    // Le panel pour chaque chambre sera une casse
                    JPanel chambre = new JPanel(new GridBagLayout());
                    // On etabli donc un contour noir
                    chambre.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    // On met le numero de la chambre
                    JLabel noCh = new JLabel(String.valueOf(etageSelect * 100 + i), JLabel.CENTER);
                    chambre.add(noCh);
                    // ON colore les casses en fonction de sa disponibilité
                    Date dateA = new java.sql.Date(dateAjd.getTimeInMillis());
                    if (hotel.consulterChambre(etageSelect * 100 + i).disponibilite(dateA, dateA) == false) {
                        chambre.setBackground(Color.RED);
                    } else if (hotel.consulterChambre(etageSelect * 100 + i).proxReservation() != null) {
                        chambre.setBackground(Color.GREEN);
                    } else {
                        chambre.setBackground(Color.GRAY);
                    }
                    chambre.setBorder(new EmptyBorder(5, 5, 5, 5));
                    carte.add(chambre, grille);

                    if (x == 1 || x % 2 != 0) {
                        y++;
                        x -= 1;
                    } else if (x == 0 || x % 2 == 0) {
                        x++;
                    }
                }
                grilleRes.gridy = 2;
                grilleRes.insets = new Insets(50, 0, 20, 0);
                resultat.add(carte, grilleRes);

                grilleRes.insets = new Insets(5, 5, 5, 5);
                grilleRes.gridy = 3;
                resultat.add(new JLabel("Vert  : A une reservation dans le futur et libre "), grilleRes);
                grilleRes.gridy = 4;
                resultat.add(new JLabel("Gris  : Aucune reservation dans le futur et libre"), grilleRes);
                grilleRes.gridy = 5;
                resultat.add(new JLabel("Vert  : Occupée"), grilleRes);
                resultat.revalidate();
                resultat.repaint();
            }
        });

        grilleRes.insets = new Insets(5, 5, 5, 5);
        grilleRes.gridy = 3;
        resultat.add(new JLabel("VERT  : A une reservation dans le futur et libre "), grilleRes);
        grilleRes.gridy = 4;
        resultat.add(new JLabel("GRIS  : Aucune reservation dans le futur et libre"), grilleRes);
        grilleRes.gridy = 5;
        resultat.add(new JLabel("ROUGE  : La chambre est ccupée"), grilleRes);
        return resultat;
    }

    public JPanel panelInfoRes(Date dArrive, Date dDepart) {
        // D'abord on crée le panel à renvoyer
        JPanel resultat = new JPanel(new GridBagLayout());

        // On crée une grille et les elements pour le panel
        GridBagConstraints grille = new GridBagConstraints();
        grille.insets = new Insets(10, 10, 10, 10);
        grille.gridy = 0;
        resultat.add(new JLabel("Dates :"), grille);
        // On va simplement rajouter la date de depart et d'arivée
        grille.gridy = 1;
        resultat.add(new JLabel("La date d'arrivée est le : " + formatDate.format(dArrive)), grille);

        grille.gridy = 2;
        resultat.add(new JLabel("La date de depart est le : " + formatDate.format(dDepart)), grille);

        // On crée un vecteur contenant toutes les dates entre la date d'arrivée et de
        // départ
        Vector<Date> datesColorer = hotel.obtentionPeriode(dArrive, dDepart);

        // Nous créons notre calendrier et on le met dans la date d'arrivée
        JCalendar calendar = new JCalendar();
        calendar.setDate(dArrive);

        // On donc transformer la cuadricule contenant les jours en panel, cela nous
        // permettra de pouvoir moodifier le couleur de chaque jour. On commence donc
        // par obtenir la cuadricule par la methode getDaychooser puis par getDayPanel
        // on la trasnforme en panel
        JDayChooser dayChooser = calendar.getDayChooser();
        JPanel dayPanel = dayChooser.getDayPanel();

        // Nous créons une date de la classe Calendar, cette classe nous permet
        // d'augmenter les jours d'une manière simple
        Calendar cal = Calendar.getInstance();
        cal.setTime(dArrive);
        // Cette date correspondra au jour numero 1 du mois de la date d'arrivée
        cal.set(Calendar.DAY_OF_MONTH, 1);

        datesColorer.forEach(date -> {
            // Pour chaque jour on crée une date avec Calendrier
            Calendar dateEnCalendar = Calendar.getInstance();
            dateEnCalendar.setTime(date);

            // On obtient le numero du jour d'arrivée
            int jourArrivee = dateEnCalendar.get(Calendar.DAY_OF_MONTH);

            // On obtient le numero de la journée par laquelle comence le mois
            int jourPremiereSemaine = cal.get(Calendar.DAY_OF_WEEK);

            // On obtient l'indice du composant de la manieère suivant :
            // On constate qu'il y a toujours 5 composants avant le premier jour du mois
            // On constate qu'il faut rajouter les composants correspondant aux premiere
            // dates inexistantes du mois (jourPremiereSemaine)
            int indiceJour = jourArrivee + 5 + jourPremiereSemaine;

            // On déclare notre compossant
            Component component = dayPanel.getComponent(indiceJour);
            // On test si on se trouve dans le bon mois
            if (dateEnCalendar.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
                // si c'est le cas on colorie le composant en rouge
                component.setBackground(Color.RED);
            }
        });

        // On bloque les modifications du calendrier
        JMonthChooser monthChooser = calendar.getMonthChooser();
        JYearChooser yearChooser = calendar.getYearChooser();
        monthChooser.setVisible(false);
        yearChooser.setVisible(false);

        grille.gridy = 3;
        resultat.add(calendar, grille);

        // on renvoie notre panel contenant tout
        return resultat;

    }

    // Méthode pour modifier fichiers
    private void eliminerInfo(String nomFichier, int ligneDelete) {
        try {
            // On crée le fichier á partir du parametre
            File tempFile = File.createTempFile("tempfile", ".csv");

            // On crée nos lecteurs et ecriteurs
            BufferedReader reader = new BufferedReader(new FileReader(nomFichier));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String ligne;

            // On crée un indicateur de la liggne
            int indiceLigne = 1;
            // On parcours notre fichier
            while ((ligne = reader.readLine()) != null) {
                // si on ne se trouve pas dans la ligne á eliminer on réecrit l'information de
                // cette ligne. Si non, on ecrit rien = elimination de la ligne
                if (indiceLigne != ligneDelete) {
                    writer.write(ligne);
                    writer.newLine();
                }
                indiceLigne++;
            }
            // On ferme nos lecteurs et ecriteurs
            reader.close();
            writer.close();

            // On remplace nos fichiers
            File file = new File(nomFichier);
            file.delete();
            tempFile.renameTo(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void modifierInfo(String nomFichier, int noLigne, int colonne, String changement) {
        try {
            // On crée notre fichier á aprtir du paramètre
            File tempFile = File.createTempFile("tempfile", ".csv");

            // On crée le lecteur et écriteur
            BufferedReader reader = new BufferedReader(new FileReader(nomFichier));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String ligne;

            // On crée un indicateur de la ligne où on se trouve
            int indiceLigne = 0;
            // PArcours du fichier
            while ((ligne = reader.readLine()) != null) {
                // On réecrit la ligne normalement si les indices sont differents
                if (indiceLigne != noLigne) {
                    writer.write(ligne);
                    writer.newLine();
                } else { // Si non on modifie la ligne
                    String[] casses = ligne.split(",");
                    // Á partir de la colonne donnée on modifie la ligne
                    casses[colonne] = changement;

                    // On redifinie notre ligne puis on la reecrit
                    String newLigne = "";
                    for (int i = 0; i < casses.length - 1; i++) {
                        newLigne += casses[i] + ",";
                    }
                    newLigne += casses[casses.length - 1];
                    writer.write(newLigne);
                    writer.newLine();

                }
                indiceLigne++;
            }
            // On ferme nos lecteurs et ecriteurs et on remplaces les fichiers
            reader.close();
            writer.close();

            File file = new File(nomFichier);
            file.delete();
            tempFile.renameTo(file);

        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        new HotelReservationGUI();
    }
}

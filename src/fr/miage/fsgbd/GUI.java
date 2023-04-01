package fr.miage.fsgbd;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Galli Gregory, Mopolo Moke Gabriel
 */
public class GUI extends JFrame implements ActionListener {
    TestInteger testInt = new TestInteger();
    BTreePlus<Integer> bInt;
    private JButton buttonClean, buttonRemove, buttonLoad, buttonSave, buttonAddMany, buttonAddItem, buttonRefresh, buttonGenererLignesFichier, buttonRecherche100;
    private JTextField txtNbreItem, txtNbreSpecificItem, txtU, txtFile, removeSpecific;
    private final JTree tree = new JTree();
    String dataFileName = "data.txt";

    public GUI() {
        super();
        build();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonLoad || e.getSource() == buttonClean || e.getSource() == buttonSave || e.getSource() == buttonRefresh) {
            if (e.getSource() == buttonLoad) {
                BDeserializer<Integer> load = new BDeserializer<Integer>();
                bInt = load.getArbre("arbre.abr");
                if (bInt == null)
                    System.out.println("Echec du chargement.");

            } else if (e.getSource() == buttonClean) {
                if (Integer.parseInt(txtU.getText()) < 2)
                    System.out.println("Impossible de cr?er un arbre dont le nombre de cl?s est inf?rieur ? 2.");
                else
                    bInt = new BTreePlus<Integer>(Integer.parseInt(txtU.getText()), testInt);
            } else if (e.getSource() == buttonSave) {
                BSerializer<Integer> save = new BSerializer<Integer>(bInt, txtFile.getText());
            }else if (e.getSource() == buttonRefresh) {
                tree.updateUI();
            }
        } else {
            if (bInt == null)
                bInt = new BTreePlus<Integer>(Integer.parseInt(txtU.getText()), testInt);

            if (e.getSource() == buttonAddMany) {
                for (int i = 0; i < Integer.parseInt(txtNbreItem.getText()); i++) {
                    int valeur = (int) (Math.random() * 10 * Integer.parseInt(txtNbreItem.getText()));
                    boolean done = bInt.addValeur(valeur, 0);

					/*
					  On pourrait forcer l'ajout mais on risque alors de tomber dans une boucle infinie sans "r?gle" faisant sens pour en sortir

					while (!done)
					{
						valeur =(int) (Math.random() * 10 * Integer.parseInt(txtNbreItem.getText()));
						done = bInt.addValeur(valeur);
					}
					 */
                }

            } else if (e.getSource() == buttonAddItem) {
                if (!bInt.addValeur(Integer.parseInt(txtNbreSpecificItem.getText()), 0))
                    System.out.println("Tentative d'ajout d'une valeur existante : " + txtNbreSpecificItem.getText());
                txtNbreSpecificItem.setText(
                        String.valueOf(
                                Integer.parseInt(txtNbreSpecificItem.getText()) + 2
                        )
                );

            } else if (e.getSource() == buttonRemove) {
                bInt.removeValeur(Integer.parseInt(removeSpecific.getText()));
            } else if(e.getSource() == buttonGenererLignesFichier){
                this.generer10kLignes();
            }else if(e.getSource() == buttonRecherche100){
                this.lancerLesRecherches();
            }
        }

        tree.setModel(new DefaultTreeModel(bInt.bArbreToJTree()));
        for (int i = 0; i < tree.getRowCount(); i++)
            tree.expandRow(i);

        tree.updateUI();
    }

    private void build() {
        setTitle("Indexation - B Arbre");
        setSize(760, 760);
        setLocationRelativeTo(this);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(buildContentPane());
    }

    private JPanel buildContentPane() {
        GridBagLayout gLayGlob = new GridBagLayout();

        JPanel pane1 = new JPanel();
        pane1.setLayout(gLayGlob);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 5, 2, 0);

        JLabel labelU = new JLabel("Nombre max de cl?s par noeud (2m): ");
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        pane1.add(labelU, c);

        txtU = new JTextField("4", 7);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 2;
        pane1.add(txtU, c);

        JLabel labelBetween = new JLabel("Nombre de clefs ? ajouter:");
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        pane1.add(labelBetween, c);

        txtNbreItem = new JTextField("10000", 7);
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        pane1.add(txtNbreItem, c);


        buttonAddMany = new JButton("Ajouter n ?l?ments al?atoires ? l'arbre");
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonAddMany, c);

        JLabel labelSpecific = new JLabel("Ajouter une valeur sp?cifique:");
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(labelSpecific, c);

        txtNbreSpecificItem = new JTextField("50", 7);
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(txtNbreSpecificItem, c);

        buttonAddItem = new JButton("Ajouter l'?l?ment");
        c.gridx = 2;
        c.gridy = 3;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonAddItem, c);

        JLabel labelRemoveSpecific = new JLabel("Retirer une valeur sp?cifique:");
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(labelRemoveSpecific, c);

        removeSpecific = new JTextField("54", 7);
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(removeSpecific, c);

        buttonRemove = new JButton("Supprimer l'?l?ment n de l'arbre");
        c.gridx = 2;
        c.gridy = 4;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonRemove, c);

        JLabel labelFilename = new JLabel("Nom de fichier : ");
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(labelFilename, c);

        txtFile = new JTextField("arbre.abr", 7);
        c.gridx = 1;
        c.gridy = 5;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(txtFile, c);

        buttonSave = new JButton("Sauver l'arbre");
        c.gridx = 2;
        c.gridy = 5;
        c.weightx = 0.5;
        c.gridwidth = 1;
        pane1.add(buttonSave, c);

        buttonLoad = new JButton("Charger l'arbre");
        c.gridx = 3;
        c.gridy = 5;
        c.weightx = 0.5;
        c.gridwidth = 1;
        pane1.add(buttonLoad, c);

        buttonClean = new JButton("Reset");
        c.gridx = 2;
        c.gridy = 6;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonClean, c);

        buttonRefresh = new JButton("Refresh");
        c.gridx = 2;
        c.gridy = 7;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonRefresh, c);

        buttonGenererLignesFichier = new JButton("Générer 10k lignes");
        c.gridx = 2;
        c.gridy = 8;
        c.weightx = 1;
        c.gridwidth = 2;
        buttonGenererLignesFichier.setBackground(Color.RED);
        buttonGenererLignesFichier.setOpaque(true);
        pane1.add(buttonGenererLignesFichier, c);

        buttonRecherche100 = new JButton("Lancer 100 recherches");
        c.gridx = 2;
        c.gridy = 9;
        c.weightx = 1;
        c.gridwidth = 2;
        buttonRecherche100.setBackground(Color.RED);
        buttonRecherche100.setOpaque(true);
        pane1.add(buttonRecherche100, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 400;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.gridwidth = 4;   //2 columns wide
        c.gridx = 0;
        c.gridy = 10;

        JScrollPane scrollPane = new JScrollPane(tree);
        pane1.add(scrollPane, c);

        tree.setModel(new DefaultTreeModel(null));
        tree.updateUI();

        txtNbreItem.addActionListener(this);
        buttonAddItem.addActionListener(this);
        buttonAddMany.addActionListener(this);
        buttonLoad.addActionListener(this);
        buttonSave.addActionListener(this);
        buttonRemove.addActionListener(this);
        buttonClean.addActionListener(this);
        buttonRefresh.addActionListener(this);
        buttonGenererLignesFichier.addActionListener(this);
        buttonRecherche100.addActionListener(this);

        return pane1;
    }

    /*
        Lancer les recherches séquentielles et par index avec chacune 100 reherches diffférentes
     */
    private void lancerLesRecherches(){
        Rechercher rechercherParIndex = new Rechercher("par index");
        Rechercher rechercherSequentiellement = new Rechercher("sequentielle");

        System.out.println("*************************************************************************************");
        System.out.println("------------Recherche séquentielle | 100 recherches------------");
        System.out.println("Total de num sécurité social trouvé: "+rechercherSequentiellement.totalRechercheTrouvee);
        System.out.println("Temps minimum: "+rechercherSequentiellement.tempsMinimum+ " nanosecondes");
        System.out.println("Temps moyen: "+rechercherSequentiellement.tempsMoyen+ " nanosecondes");
        System.out.println("Temps maximum: "+rechercherSequentiellement.tempsMaximum+ " nanosecondes");

        System.out.println("------------Recherche par index | 100 recherches------------");
        System.out.println("Total de num sécurité social trouvé: "+rechercherParIndex.totalRechercheTrouvee);
        System.out.println("Temps minimum: "+rechercherParIndex.tempsMinimum+ " nanosecondes");
        System.out.println("Temps moyen: "+rechercherParIndex.tempsMoyen+ " nanosecondes");
        System.out.println("Temps maximum: "+rechercherParIndex.tempsMaximum+ " nanosecondes");
    }

    /*
        Effectue 100 rehcerches avec 2 approches différentes au choix:
        • Recherche séquentielle dans le fichier
        • Recherche depuis l’index
     */
    class Rechercher{
        long tempsMinimum;
        long tempsMoyen;
        long tempsMaximum;

        int totalRechercheTrouvee = 0;

        public Rechercher(String typeDeRecherche) {
            int[] numSecuriteSocialPourRecherche = new int[]{
                    20320486, 65380448, 67944205, 46926050, 16286298, 10464349, 17137479, 47261175, 90217792, 18695255, 81572731,
                    11551076, 97327439, 58746461, 23442148, 75997700, 87256814, 14016531, 88407582, 93681617, 20741097, 11760021,
                    21141738, 16294355, 35893089, 72750662, 65972821, 11943701, 26123940, 57107477, 66954394, 20452774, 35271164,
                    25686541, 11320601, 49745189, 94371411, 17892575, 27998962, 71284306, 57307798, 23111694, 54922419, 77385850,
                    86574829, 18672328, 25252005, 93866638, 19062500, 84531254, 81147430, 20025577, 12917357, 18316090, 49639199,
                    45305947, 12307260, 64153967, 93491792, 56350530, 30126134, 58072934, 97758962, 47671501, 33279884, 26767528,
                    27809731, 33945016, 11107916, 54020441, 81694366, 49849538, 82399843, 16404945, 29500382, 13806806, 25639940,
                    96784322, 10992598, 70667614, 48557315, 13266671, 10962226, 40395937, 99169179, 51674831, 68529676, 88726172,
                    47725441, 21794792, 11930718, 15420461, 34411660, 13159534, 34066739, 22893109, 56866296, 22709283, 89199682,
                    72664557, 16663667
            };



            for (int i=0; i < numSecuriteSocialPourRecherche.length ; i++){
                long debut = System.nanoTime();
                if(typeDeRecherche.equals("par index")){
                    if(this.rechercheParIndex(numSecuriteSocialPourRecherche[i])){
                        totalRechercheTrouvee++;
                    }
                } else if (typeDeRecherche.equals("sequentielle")) {
                    if(this.rechercheSequentielleDansLeFichier(numSecuriteSocialPourRecherche[i])){
                        totalRechercheTrouvee++;
                    }
                }
                long fin = System.nanoTime();
                long temps = fin-debut;
                if(i == 0 ) { tempsMaximum = temps; tempsMinimum = temps ;}

                if(temps > tempsMaximum ) tempsMaximum = temps ;

                if(temps < tempsMinimum ) tempsMinimum = temps;
            }

            tempsMoyen = ( tempsMinimum + tempsMaximum) / 2 ;
        }

        private boolean rechercheParIndex(Integer clef){
            return bInt.rechercheParIndex(clef);
        }

        private boolean rechercheSequentielleDansLeFichier(Integer clef){
            boolean ligneTrouvee = false;

            try {
                // Prepare to read input file
                File file = new File(dataFileName);
                Scanner sc = new Scanner(file);

                int positionLigne = 0;
                //Parcourir toutes les lignes
                while (sc.hasNextLine()) {
                    positionLigne++;
                    String ligne = sc.nextLine().replace(" ", "");
                    Personne personne = Personne.convertirLigneToPersonne(ligne);
                    if(clef.equals(personne.getNumerosSecuriteSocial())){ ligneTrouvee = true; }
                }

            } catch (FileNotFoundException e) {
                System.err.println(e);
            }
            return ligneTrouvee;
        }
    }

    private void generer10kLignes(){

        // lire le fihier
        try {
            // Prepare to read input file
            File file = new File(dataFileName);
            Scanner sc = new Scanner(file);

            int positionLigne = 0;
            //Parcourir toutes les lignes
            while (sc.hasNextLine()) {
                positionLigne++;
                String ligne = sc.nextLine().replace(" ", "");
                Personne personne = Personne.convertirLigneToPersonne(ligne);
                bInt.addValeur(personne.getNumerosSecuriteSocial(), positionLigne);
            }

        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }
}


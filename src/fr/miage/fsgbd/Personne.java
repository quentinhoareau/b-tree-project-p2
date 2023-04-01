package fr.miage.fsgbd;

public class Personne {
    private String nom;
    private String prenom;
    private Integer numerosSecuriteSocial;

    public Personne(Integer numerosSecuriteSocial, String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.numerosSecuriteSocial = numerosSecuriteSocial;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Integer getNumerosSecuriteSocial() {
        return numerosSecuriteSocial;
    }

    public void setNumerosSecuriteSocial(Integer numerosSecuriteSocial) {
        this.numerosSecuriteSocial = numerosSecuriteSocial;
    }


    static Personne convertirLigneToPersonne(String ligne){
        String[] donnesLigne = ligne.split("[(,)]"); //Séparer chaque valeur dans un tableau ([0]-> Numéros de sécruté social , [1]-> nom , [2]-> prénom)

        return new Personne(Integer.parseInt(donnesLigne[0]),donnesLigne[1],donnesLigne[2]);
    }

}

package model;
public class Enfant {
    public int id;
    public String nom;
    public int age;
    public String allergies;
    public String historiqueMedical;
    public int idEnseignat;

    public Enfant(String name, int age, String allergies, String medicalHistory, int educatorId) {
        this.nom = name;
        this.age = age;
        this.allergies = allergies;
        this.historiqueMedical = medicalHistory;
        this.idEnseignat = educatorId;
    }
}

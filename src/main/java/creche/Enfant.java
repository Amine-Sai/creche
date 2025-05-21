package creche;
public class Enfant {
    public int id;
    public String nom;
    public int age;
    public String allergies;
    public String historiqueMedical;
    public int idEnseignat;

    public Enfant(int id, String nom, int age, String allergies, String medicalHistory, int educatorId) {
        this.id = id;
        this.nom = nom;
        this.age = age;
        this.allergies = allergies;
        this.historiqueMedical = medicalHistory;
        this.idEnseignat = educatorId;
    }

    public Enfant(String nom, int age, String allergies, String medicalHistory, int educatorId) {
        this(-1, nom, age, allergies, medicalHistory, educatorId); 
    }
}
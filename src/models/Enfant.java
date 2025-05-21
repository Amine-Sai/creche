package model;
public class Child {
    public int id;
    public String name;
    public int age;
    public String allergies;
    public String medicalHistory;
    public int educatorId;

    public Child(String name, int age, String allergies, String medicalHistory, int educatorId) {
        this.name = name;
        this.age = age;
        this.allergies = allergies;
        this.medicalHistory = medicalHistory;
        this.educatorId = educatorId;
    }
}

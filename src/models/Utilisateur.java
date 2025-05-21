// Utilisateur.java
public class Utilisateur {
    private int idUtilisateur;
    private String nomUtilisateur;
    private String motDePasse;
    private String role;
    private String dateCreation;

    public Utilisateur(int idUtilisateur, String nomUtilisateur, String motDePasse, String role, String dateCreation) {
        this.idUtilisateur = idUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.role = role;
        this.dateCreation = dateCreation;
    }

    // Getters et setters
    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
               "idUtilisateur=" + idUtilisateur +
               ", nomUtilisateur='" + nomUtilisateur + '\'' +
               ", role='" + role + '\'' +
               ", dateCreation='" + dateCreation + '\'' +
               '}';
    }
}
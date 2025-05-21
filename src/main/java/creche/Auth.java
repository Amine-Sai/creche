package creche;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

public class Auth {

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public Auth(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public boolean inscrireUtilisateur(String nomUtilisateur, String motDePasse, String role) {
    try (Connection conn = getConnection();
         PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM utilisateurs WHERE nom_utilisateur = ?")) {
        checkStmt.setString(1, nomUtilisateur);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            System.out.println("Nom d'utilisateur déjà existant: " + nomUtilisateur);
            return false; // User already exists
        }

        String hashedPassword = BCrypt.hashpw(motDePasse, BCrypt.gensalt());
        String sql = "INSERT INTO utilisateurs (nom_utilisateur, mot_de_passe, role, actif) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomUtilisateur);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, role);
            pstmt.setBoolean(4, false);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de l'inscription de l'utilisateur: " + e.getMessage());
        return false;
    }
}

    public Utilisateur authentifierUtilisateur(String nomUtilisateur, String motDePasse) {
        String sql = "SELECT id_utilisateur, nom_utilisateur, mot_de_passe, role, date_creation, actif FROM utilisateurs WHERE nom_utilisateur = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomUtilisateur);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String hashedPasswordFromDB = rs.getString("mot_de_passe");
                if (BCrypt.checkpw(motDePasse, hashedPasswordFromDB)) {
                    return new Utilisateur(
                            rs.getInt("id_utilisateur"),
                            rs.getString("nom_utilisateur"),
                            hashedPasswordFromDB,
                            rs.getString("role"),
                            rs.getString("date_creation"),
                            rs.getBoolean("actif")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification: " + e.getMessage());
        }
        return null;
    }
}
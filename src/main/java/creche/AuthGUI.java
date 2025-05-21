import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.mindrot.jbcrypt.BCrypt;

public class AuthGUI extends JFrame implements ActionListener {

    private UtilisateurService userService;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Panneau d'inscription
    private JPanel signupPanel;
    private JTextField signupNomUtilisateurField;
    private JPasswordField signupMotDePasseField;
    private JComboBox<String> signupRoleComboBox;
    private JButton signupButton;
    private JButton goToLoginFromSignupButton;
    private JTextArea signupOutputArea;

    // Panneau de connexion
    private JPanel loginPanel;
    private JTextField loginNomUtilisateurField;
    private JPasswordField loginMotDePasseField;
    private JButton loginButton;
    private JButton goToSignupFromLoginButton;
    private JTextArea loginOutputArea;

    public AuthGUI(UtilisateurService service) {
        this.userService = service;
        setTitle("Authentification");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialisation du panneau d'inscription
        signupPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        signupPanel.add(new JLabel("Nom d'utilisateur:"));
        signupNomUtilisateurField = new JTextField();
        signupPanel.add(signupNomUtilisateurField);
        signupPanel.add(new JLabel("Mot de passe:"));
        signupMotDePasseField = new JPasswordField();
        signupPanel.add(signupMotDePasseField);
        signupPanel.add(new JLabel("Rôle:"));
        String[] roles = {"educateur", "cuisine", "parent"};
        signupRoleComboBox = new JComboBox<>(roles);
        signupPanel.add(signupRoleComboBox);
        signupButton = new JButton("S'inscrire");
        signupButton.addActionListener(this);
        signupPanel.add(signupButton);
        goToLoginFromSignupButton = new JButton("Se connecter");
        goToLoginFromSignupButton.addActionListener(this);
        signupPanel.add(goToLoginFromSignupButton);
        signupOutputArea = new JTextArea(3, 20);
        signupOutputArea.setEditable(false);
        signupPanel.add(new JScrollPane(signupOutputArea));
        mainPanel.add(signupPanel, "signup");

        // Initialisation du panneau de connexion
        loginPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        loginPanel.add(new JLabel("Nom d'utilisateur:"));
        loginNomUtilisateurField = new JTextField();
        loginPanel.add(loginNomUtilisateurField);
        loginPanel.add(new JLabel("Mot de passe:"));
        loginMotDePasseField = new JPasswordField();
        loginPanel.add(loginMotDePasseField);
        loginButton = new JButton("Se connecter");
        loginButton.addActionListener(this);
        loginPanel.add(loginButton);
        goToSignupFromLoginButton = new JButton("S'inscrire");
        goToSignupFromLoginButton.addActionListener(this);
        loginPanel.add(goToSignupFromLoginButton);
        loginOutputArea = new JTextArea(3, 20);
        loginOutputArea.setEditable(false);
        loginPanel.add(new JScrollPane(loginOutputArea));
        mainPanel.add(loginPanel, "login");

        add(mainPanel);
        cardLayout.show(mainPanel, "login"); 
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signupButton) {
            String nom = signupNomUtilisateurField.getText();
            String motDePasse = new String(signupMotDePasseField.getPassword());
            String role = (String) signupRoleComboBox.getSelectedItem();
            if (!nom.isEmpty() && !motDePasse.isEmpty()) {
                if (userService.inscrireUtilisateur(nom, motDePasse, role)) {
                    signupOutputArea.setText("Inscription réussie. Veuillez attendre la validation de l'administrateur.");
                    clearSignupFields();
                } else {
                    signupOutputArea.setText("Échec de l'inscription. Le nom d'utilisateur existe peut-être déjà.");
                }
            } else {
                signupOutputArea.setText("Veuillez remplir tous les champs.");
            }
        } else if (e.getSource() == loginButton) {
            String nom = loginNomUtilisateurField.getText();
            String motDePasse = new String(loginMotDePasseField.getPassword());
            Utilisateur utilisateur = userService.authentifierUtilisateur(nom, motDePasse);
            if (utilisateur != null) {
                if (utilisateur.getRole().equals("administrateur")) {
                    loginOutputArea.setText("Connexion réussie en tant qu'administrateur.");
                    // Ici, vous ouvririez l'interface d'administration
                    ouvrirInterfacePrincipale(utilisateur);
                    dispose(); // Fermer l'écran de connexion
                } else if (utilisateur.isActif()) {
                    loginOutputArea.setText("Connexion réussie en tant que " + utilisateur.getRole() + ".");
                    // Ici, vous ouvririez l'interface utilisateur standard
                    ouvrirInterfacePrincipale(utilisateur);
                    dispose(); // Fermer l'écran de connexion
                } else {
                    loginOutputArea.setText("Votre compte n'a pas encore été validé par l'administrateur.");
                }
            } else {
                loginOutputArea.setText("Nom d'utilisateur ou mot de passe incorrect.");
            }
        } else if (e.getSource() == goToSignupFromLoginButton) {
            cardLayout.show(mainPanel, "signup");
            loginOutputArea.setText("");
        } else if (e.getSource() == goToLoginFromSignupButton) {
            cardLayout.show(mainPanel, "login");
            signupOutputArea.setText("");
        }
    }

    private void ouvrirInterfacePrincipale(Utilisateur utilisateur) {
        // Ici, vous instancierez et afficherez l'interface principale de l'application
        // en passant l'objet utilisateur connecté si nécessaire.
        System.out.println("Ouverture de l'interface principale pour: " + utilisateur.getNomUtilisateur() + " (" + utilisateur.getRole() + ")");
        // Par exemple: new MainAppGUI(utilisateur);
    }

    private void clearSignupFields() {
        signupNomUtilisateurField.setText("");
        signupMotDePasseField.setText("");
    }

    private void clearLoginFields() {
        loginNomUtilisateurField.setText("");
        loginMotDePasseField.setText("");
    }

    public static void main(String[] args) {
        String dbUrl = "jdbc:mysql://localhost:3306/creche?serverTimezone=UTC";
        String dbUser = "javauser";
        String dbPassword = "your_java_password";
        UtilisateurService userService = new UtilisateurService(dbUrl, dbUser, dbPassword);
        SwingUtilities.invokeLater(() -> new AuthGUI(userService));
    }
}

class Utilisateur {
    private int idUtilisateur;
    private String nomUtilisateur;
    private String motDePasse;
    private String role;
    private String dateCreation;
    private boolean actif;

    public Utilisateur(int idUtilisateur, String nomUtilisateur, String motDePasse, String role, String dateCreation, boolean actif) {
        this.idUtilisateur = idUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.role = role;
        this.dateCreation = dateCreation;
        this.actif = actif;
    }

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

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
               "idUtilisateur=" + idUtilisateur +
               ", nomUtilisateur='" + nomUtilisateur + '\'' +
               ", role='" + role + '\'' +
               ", dateCreation='" + dateCreation + '\'' +
               ", actif=" + actif +
               '}';
    }
}

class UtilisateurService {

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public UtilisateurService(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public boolean inscrireUtilisateur(String nomUtilisateur, String motDePasse, String role) {
        String hashedPassword = BCrypt.hashpw(motDePasse, BCrypt.gensalt());
        String sql = "INSERT INTO utilisateurs (nom_utilisateur, mot_de_passe, role, actif) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomUtilisateur);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, role);
            pstmt.setBoolean(4, false);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
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

    // Méthodes de gestion des utilisateurs (lire, modifier, supprimer, activer)
    public Utilisateur lireUtilisateur(String nomUtilisateur) {
        String sql = "SELECT id_utilisateur, nom_utilisateur, mot_de_passe, role, date_creation, actif FROM utilisateurs WHERE nom_utilisateur = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomUtilisateur);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom_utilisateur"),
                        rs.getString("mot_de_passe"),
                        rs.getString("role"),
                        rs.getString("date_creation"),
                        rs.getBoolean("actif")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la lecture de l'utilisateur: " + e.getMessage());
        }
        return null;
    }

    public boolean modifierUtilisateur(int idUtilisateur, String nouveauMotDePasse, String nouveauRole, Boolean actif) {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE utilisateurs SET ");
        boolean setClauseAdded = false;

        if (nouveauMotDePasse != null && !nouveauMotDePasse.isEmpty()) {
            String hashedPassword = BCrypt.hashpw(nouveauMotDePasse, BCrypt.gensalt());
            sqlBuilder.append("mot_de_passe = ?");
            setClauseAdded = true;
        }

        if (nouveauRole != null && !nouveauRole.isEmpty()) {
            if (setClauseAdded) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append("role = ?");
            setClauseAdded = true;
        }

        if (actif != null) {
            if (setClauseAdded) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append("actif = ?");
            setClauseAdded = true;
        }

        if (!setClauseAdded) {
            System.out.println("Aucune information à modifier fournie.");
            return false;
        }

        sqlBuilder.append(" WHERE id_utilisateur = ?");
        String sql = sqlBuilder.toString();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int parameterIndex = 1;
            if (nouveauMotDePasse != null && !nouveauMotDePasse.isEmpty()) {
                pstmt.setString(parameterIndex++, BCrypt.hashpw(nouveauMotDePasse, BCrypt.gensalt()));
            }
            if (nouveauRole != null && !nouveauRole.isEmpty()) {
                pstmt.setString(parameterIndex++, nouveauRole);
            }
            if (actif != null) {
                pstmt.setBoolean(parameterIndex++, actif);
            }
            pstmt.setInt(parameterIndex, idUtilisateur);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de l'utilisateur: " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerUtilisateur(int idUtilisateur) {
        String sql = "DELETE FROM utilisateurs WHERE id_utilisateur = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUtilisateur);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur: " + e.getMessage());
        }
        return false;
    }

    // Nouvelle méthode pour activer/désactiver un utilisateur par l'admin
    public boolean activerUtilisateur(int idUtilisateur, boolean actif) {
        String sql = "UPDATE utilisateurs SET actif = ? WHERE id_utilisateur = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, actif);
            pstmt.setInt(2, idUtilisateur);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'activation/désactivation de l'utilisateur: " + e.getMessage());
            return false;
        }
    }
}

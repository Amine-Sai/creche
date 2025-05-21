// creche/EnfantCRUDGUI.java
package creche;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class EnfantGUI extends JFrame implements ActionListener {

    private DefaultListModel<String> enfantListModel;
    private JList<String> enfantList;
    private JTextField nomField;
    private JTextField ageField;
    private JTextField allergiesField;
    private JTextField historiqueMedicalField;
    private JTextField idEnseignatField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton logoutButton; // New logout button
    private List<Enfant> enfants;
    private int selectedEnfantId = -1;
    private final String dbUrl = "jdbc:mysql://localhost:3306/creche?serverTimezone=UTC";
    private final String dbUser = "javauser";
    private final String dbPassword = "your_java_password";

    public EnfantGUI() {
        setTitle("Enfant CRUD Operations");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450); // Increased height to accommodate the new button
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        enfants = new ArrayList<>();
        enfantListModel = new DefaultListModel<>();
        enfantList = new JList<>(enfantListModel);
        enfantList.addListSelectionListener(e -> {
            int selectedIndex = enfantList.getSelectedIndex();
            if (selectedIndex != -1) {
                Enfant selectedEnfant = enfants.get(selectedIndex);
                selectedEnfantId = selectedEnfant.id;
                displayEnfantDetails(selectedEnfant);
            } else {
                selectedEnfantId = -1;
                clearForm();
            }
        });

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        listPanel.add(new JLabel("Liste des Enfants:"), BorderLayout.NORTH);
        listPanel.add(new JScrollPane(enfantList), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("Nom:"));
        nomField = new JTextField();
        inputPanel.add(nomField);
        inputPanel.add(new JLabel("Âge:"));
        ageField = new JTextField();
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Allergies:"));
        allergiesField = new JTextField();
        inputPanel.add(allergiesField);
        inputPanel.add(new JLabel("Historique Médical:"));
        historiqueMedicalField = new JTextField();
        inputPanel.add(historiqueMedicalField);
        inputPanel.add(new JLabel("ID Enseignant:"));
        idEnseignatField = new JTextField();
        inputPanel.add(idEnseignatField);
        inputPanel.add(new JLabel()); // Empty space

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Ajouter");
        addButton.addActionListener(this);
        updateButton = new JButton("Modifier");
        updateButton.addActionListener(this);
        deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(this);
        logoutButton = new JButton("Déconnexion"); // Create logout button
        logoutButton.addActionListener(this); // Add action listener for logout
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(logoutButton); // Add logout button to the panel

        add(listPanel, BorderLayout.WEST);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadEnfantsFromDatabase();
        setVisible(true);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    private void loadEnfantsFromDatabase() {
        enfants.clear();
        enfantListModel.clear();
        String sql = "SELECT id_enfant, nom, age, allergies, historique_medical, id_enseignat FROM enfants";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Enfant enfant = new Enfant(
                        rs.getInt("id_enfant"),
                        rs.getString("nom"),
                        rs.getInt("age"),
                        rs.getString("allergies"),
                        rs.getString("historique_medical"),
                        rs.getInt("id_enseignat")
                );
                enfants.add(enfant);
                enfantListModel.addElement(enfant.nom + " (ID: " + enfant.id + ")");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des enfants: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateEnfantListDisplay() {
        enfantListModel.clear();
        for (Enfant enfant : enfants) {
            enfantListModel.addElement(enfant.nom + " (ID: " + enfant.id + ")");
        }
    }

    private void displayEnfantDetails(Enfant enfant) {
        nomField.setText(enfant.nom);
        ageField.setText(String.valueOf(enfant.age));
        allergiesField.setText(enfant.allergies);
        historiqueMedicalField.setText(enfant.historiqueMedical);
        idEnseignatField.setText(String.valueOf(enfant.idEnseignat));
    }

    private Enfant getFormData(int id) {
        String nom = nomField.getText();
        int age = Integer.parseInt(ageField.getText());
        String allergies = allergiesField.getText();
        String historiqueMedical = historiqueMedicalField.getText();
        int idEnseignat = Integer.parseInt(idEnseignatField.getText());
        Enfant enfant = new Enfant(id, nom, age, allergies, historiqueMedical, idEnseignat);
        return enfant;
    }

    private void clearForm() {
        nomField.setText("");
        ageField.setText("");
        allergiesField.setText("");
        historiqueMedicalField.setText("");
        idEnseignatField.setText("");
        enfantList.clearSelection();
        selectedEnfantId = -1;
    }

    private void addEnfantToDatabase() {
        Enfant newEnfant = getFormData(-1);
        String sql = "INSERT INTO enfants (nom, age, allergies, historique_medical, id_enseignat) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, newEnfant.nom);
            pstmt.setInt(2, newEnfant.age);
            pstmt.setString(3, newEnfant.allergies);
            pstmt.setString(4, newEnfant.historiqueMedical);
            pstmt.setInt(5, newEnfant.idEnseignat);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    newEnfant.id = generatedKeys.getInt(1);
                    enfants.add(newEnfant);
                    updateEnfantListDisplay();
                    clearForm();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'enfant: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateEnfantInDatabase() {
        if (selectedEnfantId != -1) {
            Enfant updatedEnfant = getFormData(selectedEnfantId);
            String sql = "UPDATE enfants SET nom = ?, age = ?, allergies = ?, historique_medical = ?, id_enseignat = ? WHERE id_enfant = ?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, updatedEnfant.nom);
                pstmt.setInt(2, updatedEnfant.age);
                pstmt.setString(3, updatedEnfant.allergies);
                pstmt.setString(4, updatedEnfant.historiqueMedical);
                pstmt.setInt(5, updatedEnfant.idEnseignat);
                pstmt.setInt(6, selectedEnfantId);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    for (int i = 0; i < enfants.size(); i++) {
                        if (enfants.get(i).id == selectedEnfantId) {
                            enfants.set(i, updatedEnfant);
                            break;
                        }
                    }
                    updateEnfantListDisplay();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun enfant mis à jour.", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour de l'enfant: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enfant à modifier.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteEnfantFromDatabase() {
        if (selectedEnfantId != -1) {
            int choice = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet enfant ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM enfants WHERE id_enfant = ?";
                try (Connection conn = getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, selectedEnfantId);
                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        enfants.removeIf(enfant -> enfant.id == selectedEnfantId);
                        updateEnfantListDisplay();
                        clearForm();
                    } else {
                        JOptionPane.showMessageDialog(this, "Impossible de supprimer l'enfant.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'enfant: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enfant à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void logout() {
    this.dispose();
    String dbUrl = "jdbc:mysql://localhost:3306/creche?serverTimezone=UTC";
    String dbUser = "javauser";
    String dbPassword = "your_java_password";
    Auth authService = new Auth(dbUrl, dbUser, dbPassword); 
    SwingUtilities.invokeLater(() -> new AuthGUI(authService));
}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addEnfantToDatabase();
        } else if (e.getSource() == updateButton) {
            updateEnfantInDatabase();
        } else if (e.getSource() == deleteButton) {
            deleteEnfantFromDatabase();
        } else if (e.getSource() == logoutButton) {
            logout();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EnfantGUI::new);
    }
}
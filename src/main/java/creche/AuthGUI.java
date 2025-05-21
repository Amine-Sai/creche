package creche;

// mvn clean install
// mvn exec:java -Dexec.mainClass="creche.AuthGUI"

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import javax.swing.border.EmptyBorder;

public class AuthGUI extends JFrame implements ActionListener {

    private Auth userService;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Signup panel components
    private JPanel signupPanel;
    private JTextField signupNomUtilisateurField;
    private JPasswordField signupMotDePasseField;
    private JComboBox<String> signupRoleComboBox;
    private JButton signupButton;
    private JButton goToLoginFromSignupButton;
    private JTextArea signupOutputArea;

    // Login panel components
    private JPanel loginPanel;
    private JTextField loginNomUtilisateurField;
    private JPasswordField loginMotDePasseField;
    private JButton loginButton;
    private JButton goToSignupFromLoginButton;
    private JTextArea loginOutputArea;

    // Common styles
    private Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
    private Font buttonFont = new Font("Segoe UI", Font.BOLD, 16);
    private Color primaryColor = new Color(63, 81, 181); // Indigo
    private Color secondaryColor = new Color(156, 39, 176); // Purple
    private Color errorTextColor = new Color(220, 53, 69); // Danger red
    private Color successTextColor = new Color(25, 135, 84); // Success green

    public AuthGUI(Auth service) {
        this.userService = service;
        setTitle("Authentification");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.WHITE);

        // Initialize Signup Panel
        signupPanel = new JPanel(new GridBagLayout());
        signupPanel.setBackground(Color.WHITE);
        signupPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbcSignup = new GridBagConstraints();
        gbcSignup.insets = new Insets(10, 10, 10, 10);
        gbcSignup.fill = GridBagConstraints.HORIZONTAL;
        gbcSignup.weightx = 1.0;
        gbcSignup.gridwidth = GridBagConstraints.REMAINDER;

        JLabel signupTitle = new JLabel("Inscription");
        signupTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        signupTitle.setForeground(primaryColor);
        gbcSignup.gridx = 0;
        gbcSignup.gridy = 0;
        signupPanel.add(signupTitle, gbcSignup);

        JLabel signupNomLabel = new JLabel("Nom d'utilisateur:");
        signupNomLabel.setFont(labelFont);
        gbcSignup.gridy++;
        signupPanel.add(signupNomLabel, gbcSignup);
        signupNomUtilisateurField = new JTextField();
        signupNomUtilisateurField.setFont(labelFont);
        signupPanel.add(signupNomUtilisateurField, gbcSignup);

        JLabel signupMotDePasseLabel = new JLabel("Mot de passe:");
        signupMotDePasseLabel.setFont(labelFont);
        gbcSignup.gridy++;
        signupPanel.add(signupMotDePasseLabel, gbcSignup);
        signupMotDePasseField = new JPasswordField();
        signupMotDePasseField.setFont(labelFont);
        signupPanel.add(signupMotDePasseField, gbcSignup);

        JLabel signupRoleLabel = new JLabel("Rôle:");
        signupRoleLabel.setFont(labelFont);
        gbcSignup.gridy++;
        signupPanel.add(signupRoleLabel, gbcSignup);
        String[] roles = {"educateur", "cuisine", "parent"};
        signupRoleComboBox = new JComboBox<>(roles);
        signupRoleComboBox.setFont(labelFont);
        signupPanel.add(signupRoleComboBox, gbcSignup);

        signupButton = createStyledButton("S'inscrire", primaryColor);
        signupButton.addActionListener(this);
        gbcSignup.gridy++;
        signupPanel.add(signupButton, gbcSignup);

        goToLoginFromSignupButton = createStyledButton("Se connecter", secondaryColor);
        goToLoginFromSignupButton.addActionListener(this);
        gbcSignup.gridy++;
        signupPanel.add(goToLoginFromSignupButton, gbcSignup);

        signupOutputArea = new JTextArea(3, 20);
        signupOutputArea.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        signupOutputArea.setEditable(false);
        signupOutputArea.setLineWrap(true);
        signupOutputArea.setWrapStyleWord(true);
        gbcSignup.gridy++;
        signupPanel.add(new JScrollPane(signupOutputArea), gbcSignup);

        mainPanel.add(signupPanel, "signup");

        // Initialize Login Panel
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbcLogin = new GridBagConstraints();
        gbcLogin.insets = new Insets(10, 10, 10, 10);
        gbcLogin.fill = GridBagConstraints.HORIZONTAL;
        gbcLogin.weightx = 1.0;
        gbcLogin.gridwidth = GridBagConstraints.REMAINDER;

        JLabel loginTitle = new JLabel("Connexion");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        loginTitle.setForeground(primaryColor);
        gbcLogin.gridx = 0;
        gbcLogin.gridy = 0;
        loginPanel.add(loginTitle, gbcLogin);

        JLabel loginNomLabel = new JLabel("Nom d'utilisateur:");
        loginNomLabel.setFont(labelFont);
        gbcLogin.gridy++;
        loginPanel.add(loginNomLabel, gbcLogin);
        loginNomUtilisateurField = new JTextField();
        loginNomUtilisateurField.setFont(labelFont);
        loginPanel.add(loginNomUtilisateurField, gbcLogin);

        JLabel loginMotDePasseLabel = new JLabel("Mot de passe:");
        loginMotDePasseLabel.setFont(labelFont);
        gbcLogin.gridy++;
        loginPanel.add(loginMotDePasseLabel, gbcLogin);
        loginMotDePasseField = new JPasswordField();
        loginMotDePasseField.setFont(labelFont);
        loginPanel.add(loginMotDePasseField, gbcLogin);

        loginButton = createStyledButton("Se connecter", primaryColor);
        loginButton.addActionListener(this);
        gbcLogin.gridy++;
        loginPanel.add(loginButton, gbcLogin);

        goToSignupFromLoginButton = createStyledButton("S'inscrire", secondaryColor);
        goToSignupFromLoginButton.addActionListener(this);
        gbcLogin.gridy++;
        loginPanel.add(goToSignupFromLoginButton, gbcLogin);

        loginOutputArea = new JTextArea(3, 20);
        loginOutputArea.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        loginOutputArea.setEditable(false);
        loginOutputArea.setLineWrap(true);
        loginOutputArea.setWrapStyleWord(true);
        gbcLogin.gridy++;
        loginPanel.add(new JScrollPane(loginOutputArea), gbcLogin);

        mainPanel.add(loginPanel, "login");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(buttonFont);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signupButton) {
            String nom = signupNomUtilisateurField.getText().trim();
            String motDePasse = new String(signupMotDePasseField.getPassword());
            String role = (String) signupRoleComboBox.getSelectedItem();
            if (!nom.isEmpty() && !motDePasse.isEmpty()) {
                if (userService.inscrireUtilisateur(nom, motDePasse, role)) {
                    signupOutputArea.setForeground(successTextColor);
                    signupOutputArea.setText("Inscription réussie. Veuillez attendre la validation de l'administrateur.");
                    clearSignupFields();
                } else {
                    signupOutputArea.setForeground(errorTextColor);
                    signupOutputArea.setText("Échec de l'inscription. Le nom d'utilisateur existe peut-être déjà.");
                }
            } else {
                signupOutputArea.setForeground(errorTextColor);
                signupOutputArea.setText("Veuillez remplir tous les champs.");
            }
        } else if (e.getSource() == loginButton) {
            String nom = loginNomUtilisateurField.getText().trim();
            String motDePasse = new String(loginMotDePasseField.getPassword());
            Utilisateur utilisateur = userService.authentifierUtilisateur(nom, motDePasse);
            if (utilisateur != null) {
                if (utilisateur.getRole().equals("administrateur")) {
                    loginOutputArea.setForeground(successTextColor);
                    loginOutputArea.setText("Connexion réussie en tant qu'administrateur.");
                    ouvrirInterfacePrincipale(utilisateur);
                    dispose();
                } else if (utilisateur.isActif()) {
                    loginOutputArea.setForeground(successTextColor);
                    loginOutputArea.setText("Connexion réussie en tant que " + utilisateur.getRole() + ".");
                    ouvrirInterfacePrincipale(utilisateur);
                    dispose();
                } else {
                    loginOutputArea.setForeground(errorTextColor);
                    loginOutputArea.setText("Votre compte n'a pas encore été validé par l'administrateur.");
                }
            } else {
                loginOutputArea.setForeground(errorTextColor);
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

// In AuthGUI.java

private void ouvrirInterfacePrincipale(Utilisateur utilisateur) {
    System.out.println("Bienvenue " + utilisateur.getNomUtilisateur() + "!");
    // TODO: Launch the main app GUI here based on the user's role
    if (utilisateur.getRole().equals("administrateur")) {
        SwingUtilities.invokeLater(EnfantGUI::new);
        this.dispose(); // Close the authentication window
    } else {
        // Open a different interface for other roles
        System.out.println("Bienvenue " + utilisateur.getNomUtilisateur() + " (" + utilisateur.getRole() + ")!");
    }
}

    private void clearSignupFields() {
        signupNomUtilisateurField.setText("");
        signupMotDePasseField.setText("");
    }

    public static void main(String[] args) {
        String dbUrl = "jdbc:mysql://localhost:3306/creche";
        String dbUser = "javauser";
        String dbPassword = "your_java_password";
        Auth userService = new Auth(dbUrl, dbUser, dbPassword);
        SwingUtilities.invokeLater(() -> new AuthGUI(userService));
    }
}
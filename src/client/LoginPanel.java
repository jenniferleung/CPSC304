package client;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel {

	private JTextField userNameField;
	private JPasswordField passwordField;
	private JFrame loginFrame;
	private JButton loginButton;
	private JLabel usernameLabel;
	private boolean loggedIn;
	private JLabel passwordLabel;

	public LoginPanel() {

		setLoggedIn(false);
		initLoginFrame();

		try {
			// Load the Oracle JDBC driver
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			System.exit(-1);
		}
		loginFrame.setVisible(true);
	}

	/**
	 * initialize login window
	 */
	public void initLoginFrame() {
		loginFrame = (new JFrame("Oracle User Login"));
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		usernameLabel = new JLabel("Enter username: ");
		passwordLabel = new JLabel("Enter password: ");
		userNameField = (new JTextField(10));
		passwordField = (new JPasswordField(10));
		passwordField.setEchoChar('*');
		loginButton = (new JButton("Log In"));
		JPanel contentPane = new JPanel();
		loginFrame.setContentPane(contentPane);

		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		contentPane.setLayout(gb);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// place the username label
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 0);
		gb.setConstraints(usernameLabel, c);
		contentPane.add(usernameLabel);

		// place the text field for the username
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 0, 5, 10);
		gb.setConstraints(userNameField, c);
		contentPane.add(userNameField);

		// place password label
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 10, 0);
		gb.setConstraints(passwordLabel, c);
		contentPane.add(passwordLabel);

		// place the password field
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 10, 10);
		gb.setConstraints(passwordField, c);
		contentPane.add(passwordField);

		// place the login button
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 10, 10, 10);
		c.anchor = GridBagConstraints.CENTER;
		gb.setConstraints(loginButton, c);
		contentPane.add(loginButton);

		loginFrame.pack();

		// center the frame
		Dimension d = loginFrame.getToolkit().getScreenSize();
		Rectangle r = loginFrame.getBounds();
		loginFrame.setLocation((d.width - r.width) / 2,
				(d.height - r.height) / 2);

		// place the cursor in the text field for the username
		userNameField.requestFocus();
	}

	/**
	 * 
	 */
	public void addListener(ActionListener e) {
		loginButton.addActionListener(e);
		passwordField.addActionListener(e);
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public char[] getPassword() {
		return passwordField.getPassword();
	}

	public void dispose() {
		loginFrame.dispose();
	}

	public void ClearPasswordField() {
		passwordField.setText("");

	}

	public String getUserName() {
		return userNameField.getText();
	}

}

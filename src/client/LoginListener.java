package client;

import java.sql.*;
//for the login window
import javax.swing.*;
import java.awt.event.*;

public class LoginListener implements ActionListener {

	private LoginPanel loginPanel;
	private int loginAttempts;
	private StartPagePanel startPagePanel;
	private MainPanel mainPanel;
	private Connection con;

	public LoginListener() {

		loginAttempts = 0;
		loginPanel = new LoginPanel();

		loginPanel.addListener(this);
		loginPanel.setVisible(true);

	}

	/*
	 * connects to Oracle database named ug using user supplied username and
	 * password
	 */
	private boolean connect(String username, String password) {
		String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";

		try {
			con = DriverManager.getConnection(connectURL, username, password);
			System.out.println("\nSuccessfully Connected to Oracle!");
			mainPanel = new MainPanel(con);
			startPagePanel = new StartPagePanel(mainPanel);
			return true;
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			return false;
		}
	}

	/*
	 * event handler
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		connectToOracle();
	}

	/**
	 * 
	 */
	public void connectToOracle() {
		if (connect(loginPanel.getUserName(),
				String.valueOf(loginPanel.getPassword()))) {

			loginPanel.setLoggedIn(true);
			loginPanel.dispose();
			startPagePanel.repaint();
			startPagePanel.setVisible(true);

			// login unsuccessful
		} else {
			loginAttempts++;

			// exceeded maximum login attempts
			if (loginAttempts >= 3) {
				JOptionPane.showMessageDialog(new JFrame(),
						"Exceeded maximum attempts. Please try again later.",
						"Oops!", JOptionPane.ERROR_MESSAGE);

				loginPanel.dispose();
				System.exit(-1);

				// prompt for retry.
			} else {
				loginPanel.ClearPasswordField();
				JOptionPane.showMessageDialog(new JFrame(),
						"Invalid username/password. Please try again.",
						"Oops!", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

}

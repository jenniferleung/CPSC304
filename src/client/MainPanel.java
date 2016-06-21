package client;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import operationPanels.AbstractOperationPanel;

import tables.AbstractDatabaseTable;

/**
 * 
 * @author dongningli
 */
@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	// GUI
	private JLabel greetingsLabel;
	private JLabel userTypeLabel;
	private JPanel userTypePanel;
	private JList userTypeList;
	private JButton nextBtn;
	private JLabel selectedUserLabel;

	private JPanel dataTablePanel;
	private JLabel dataTableLabel;
	private JList dataTableList;
	private JButton viewBtn;
	private JButton goBackBtn;
	private JButton showTableBtn;

	private JPanel transPanel;
	private JList transList;
	private JButton goTransBtn;
	private JLabel transLabel;

	// database user information
	private DatabaseUser currentUser;
	private DatabaseUser borrowerUser;
	private DatabaseUser clerkUser;
	private DatabaseUser librUser;

	private final int BORROWER_INDEX = 0;
	private final int CLERK_INDEX = 1;
	private final int LIBR_INDEX = 2;
	private Connection con;
	private GridBagConstraints mainConstraint;

	/**
	 * Constructor.
	 */
	public MainPanel(Connection con) {

		borrowerUser = new DatabaseUser(BORROWER_INDEX, con);
		clerkUser = new DatabaseUser(CLERK_INDEX, con);
		librUser = new DatabaseUser(LIBR_INDEX, con);
		// database user is a clerk by default
		setCurrentUser(1);
		this.con = con;
		initComponents();
		showSelectUserTypePanel();
		this.repaint();
		this.setVisible(true);
	}

	/**
	 * initalize components on startPanel.
	 */
	private void initComponents() {
		selectedUserLabel = new JLabel("");

		greetingsLabel = new JLabel("     Welcome to Your Library Database");
		Font greetingsFont = new Font("Arial", Font.BOLD, 18);
		// Set JLabel font using new created font
		greetingsLabel.setFont(greetingsFont);

		this.setLayout(new GridBagLayout());
		mainConstraint = new GridBagConstraints();
		mainConstraint.fill = GridBagConstraints.HORIZONTAL;
		mainConstraint.anchor = GridBagConstraints.PAGE_START;
		mainConstraint.gridx = 1;
		mainConstraint.gridy = 0;
		this.add(greetingsLabel, mainConstraint);

		nextBtn = new JButton("Next");
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (userTypeList.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(new JFrame(),
							"You need to select a user type.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					setCurrentUser(userTypeList.getSelectedIndex());
					showSelectTransPanel();
				}
			}
		});
		// select user type panel
		userTypePanel = new JPanel();
		userTypeLabel = new JLabel("Select a user type");
		Font newLabelFont = new Font(userTypeLabel.getFont().getName(),
				Font.BOLD, 14);
		userTypeLabel.setFont(newLabelFont);
		userTypeList = new JList();
		userTypeList.setAutoscrolls(true);
		userTypeList.setModel(new AbstractListModel() {
			String[] strings = { "Borrower", "Clerk ", "Librarian" };

			public int getSize() {
				return strings.length;
			}

			public Object getElementAt(int i) {
				return strings[i];
			}
		});

		// collect user type panel components
		userTypePanel.setLayout(new GridBagLayout());
		GridBagConstraints usrTypeConstraint = new GridBagConstraints();
		userTypePanel.add(userTypeLabel, usrTypeConstraint);
		usrTypeConstraint.insets = new Insets(10, 0, 0, 0);
		usrTypeConstraint.gridy = 3;
		userTypePanel.add(userTypeList, usrTypeConstraint);

		// select data table panel
		dataTableLabel = new JLabel("Select a data table");
		Font newLabelFont1 = new Font(dataTableLabel.getFont().getName(),
				Font.BOLD, 12);
		dataTableLabel.setFont(newLabelFont1);
		dataTablePanel = new JPanel();
		dataTableList = new JList();
		dataTableList.setAutoscrolls(true);

		// collect dataTablePanel components
		dataTablePanel.setLayout(new GridBagLayout());
		GridBagConstraints dataTblConstraint = new GridBagConstraints();
		dataTblConstraint.gridy = 0;
		dataTablePanel.add(dataTableLabel, dataTblConstraint);
		dataTblConstraint.insets = new Insets(10, 0, 0, 0);
		dataTblConstraint.gridy = 3;
		dataTablePanel.add(dataTableList, dataTblConstraint);
		goBackBtn = new JButton("Go back");
		goBackBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showSelectUserTypePanel();
			}

		});
		viewBtn = new JButton("view table");
		viewBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (dataTableList.getSelectedValue() == null) {
					// error msg window
					JOptionPane.showMessageDialog(new JFrame(),
							"You haven't selected any table.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					openSelectedDataTable(dataTableList.getSelectedValue());
				}
			}

		});

		// select trasaction panel components
		transLabel = new JLabel("Select a transaction");
		Font transLabelFont = new Font(transLabel.getFont().getName(),
				Font.BOLD, 14);
		transLabel.setFont(transLabelFont);
		transList = new JList();
		transList.setAutoscrolls(true);
		transPanel = new JPanel();
		transPanel.setLayout(new GridBagLayout());
		GridBagConstraints transConstraint = new GridBagConstraints();
		transPanel.add(transLabel, transConstraint);
		transConstraint.insets = new Insets(10, 0, 0, 0);
		transConstraint.gridy = 3;
		transPanel.add(transList, usrTypeConstraint);

		showTableBtn = new JButton("view tables..");
		showTableBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// showTransSelectionPanel();
				showSelectTablePanel();
			}
		});
		goTransBtn = new JButton("start!");
		goTransBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (transList.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(new JFrame(),
							"You must select a transaction before proceed.",
							"Error", JOptionPane.ERROR_MESSAGE);
				} else {
					openTransactionPanel();
				}
			}
		});
	}

	public void showSelectTransPanel() {
		selectedUserLabel.setText("user: " + currentUser.getUserTypeToString());
		hideUsrTypeSelectionPanel();

		transList.setModel(new AbstractListModel() {
			String[] strings = currentUser.getTransString();

			public int getSize() {
				return strings.length;
			}

			public Object getElementAt(int i) {
				return strings[i];
			}
		});

		mainConstraint.fill = GridBagConstraints.HORIZONTAL;
		mainConstraint.anchor = GridBagConstraints.CENTER;
		mainConstraint.insets = new Insets(10, 0, 0, 0);
		mainConstraint.gridx = 0;
		mainConstraint.gridy = 1;
		this.add(goBackBtn, mainConstraint);

		mainConstraint.fill = GridBagConstraints.HORIZONTAL;
		mainConstraint.anchor = GridBagConstraints.CENTER;
		mainConstraint.insets = new Insets(10, 0, 0, 0);
		mainConstraint.gridx = 1;
		mainConstraint.gridy = 1;
		mainConstraint.gridheight = 6;
		this.add(transPanel, mainConstraint);

		mainConstraint.anchor = GridBagConstraints.CENTER;
		mainConstraint.ipady = 0;
		mainConstraint.gridx = 2;
		mainConstraint.gridy = 3;
		this.add(showTableBtn, mainConstraint);

		mainConstraint.anchor = GridBagConstraints.BASELINE;
		mainConstraint.ipady = 0;
		mainConstraint.gridx = 2;
		mainConstraint.gridy = 4;
		this.add(goTransBtn, mainConstraint);

		mainConstraint.anchor = GridBagConstraints.PAGE_START;
		mainConstraint.gridx = 0;
		mainConstraint.gridy = 1;
		this.add(selectedUserLabel, mainConstraint);

		this.updateUI();
	}

	public void hideTransSelectionPanel() {
		this.remove(transPanel);
		this.remove(goTransBtn);
		this.remove(showTableBtn);
	}

	/**
	 * Open the appropriate data table
	 * 
	 * @param selectedTable
	 */
	private void openSelectedDataTable(Object selectedTable) {
		System.out.println("selected table:" + selectedTable);
		for (AbstractDatabaseTable table : currentUser.getViewableTables()) {

			if (table.getName().equalsIgnoreCase(selectedTable.toString())) {
				if (table.isDisplayed()) {
					JOptionPane.showMessageDialog(new JFrame(), table.getName()
							+ " is already open.", "Message",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					table.displayTablePanel();
				}
			}
		}
	}

	public void showSelectUserTypePanel() {
		hideDataTblSelectionPanel();
		hideTransSelectionPanel();
		mainConstraint.fill = GridBagConstraints.HORIZONTAL;
		mainConstraint.anchor = GridBagConstraints.CENTER;
		mainConstraint.insets = new Insets(10, 0, 0, 0);
		mainConstraint.gridx = 1;
		mainConstraint.gridy = 1;
		mainConstraint.gridheight = 6;
		this.add(userTypePanel, mainConstraint);

		mainConstraint.fill = GridBagConstraints.HORIZONTAL;
		mainConstraint.anchor = GridBagConstraints.PAGE_END;
		mainConstraint.insets = new Insets(10, 0, 0, 0);
		mainConstraint.gridx = 2;
		mainConstraint.gridy = 3; // 3rd row
		this.add(nextBtn, mainConstraint);
		this.updateUI();
	}

	public void showSelectTablePanel() {
		// updates the selected user type label
		selectedUserLabel.setText("user: " + currentUser.getUserTypeToString());
		hideUsrTypeSelectionPanel();
		hideTransSelectionPanel();
		// Obtain viewable tables of current user, then extract these table
		// names
		Collection<AbstractDatabaseTable> tables = getCurrentUser()
				.getViewableTables();
		String[] tableNames = new String[tables.size()];
		int i = 0;
		for (AbstractDatabaseTable a : tables) {
			tableNames[i] = a.getName();
			i++;
		}
		final String[] listContents = tableNames;

		dataTableList.setModel(new AbstractListModel() {
			String[] strings = listContents;

			public int getSize() {
				return strings.length;
			}

			public Object getElementAt(int i) {
				return strings[i];
			}
		});

		mainConstraint.fill = GridBagConstraints.HORIZONTAL;
		mainConstraint.anchor = GridBagConstraints.CENTER;
		mainConstraint.insets = new Insets(10, 0, 0, 0);
		mainConstraint.gridx = 0;
		mainConstraint.gridy = 1;

		this.add(goBackBtn, mainConstraint);

		mainConstraint.fill = GridBagConstraints.HORIZONTAL;
		mainConstraint.anchor = GridBagConstraints.CENTER;
		mainConstraint.insets = new Insets(10, 0, 0, 0);
		mainConstraint.gridx = 1;
		mainConstraint.gridy = 1;
		mainConstraint.gridheight = 6;
		this.add(dataTablePanel, mainConstraint);

		mainConstraint.anchor = GridBagConstraints.CENTER;
		mainConstraint.ipady = 0;
		mainConstraint.gridx = 2;
		mainConstraint.gridy = 3;
		this.add(viewBtn, mainConstraint);

		mainConstraint.anchor = GridBagConstraints.PAGE_START;
		mainConstraint.gridx = 0;
		mainConstraint.gridy = 1;
		this.add(selectedUserLabel, mainConstraint);

		this.updateUI();
	}

	/**
	 * Removes the userTypePanel and the nextBtn from mainframe
	 */
	public void hideUsrTypeSelectionPanel() {
		this.remove(userTypePanel);
		this.remove(nextBtn);
	}

	/**
	 * Remove the dataTablePanel, goBtn and goBackBtn from UI
	 */
	public void hideDataTblSelectionPanel() {
		this.remove(dataTablePanel);
		this.remove(selectedUserLabel);
		this.remove(viewBtn);
		this.remove(goBackBtn);
		this.remove(showTableBtn);
	}

	public DatabaseUser getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(int userType) {

		switch (userType) {
		case BORROWER_INDEX:
			currentUser = borrowerUser;
			break;
		case CLERK_INDEX:
			currentUser = clerkUser;
			break;
		case LIBR_INDEX:
			currentUser = librUser;
			break;

		default:
			System.out.println("no user type was selected.");
		}
	}

	public DatabaseUser getBorrowerUser() {
		return borrowerUser;
	}

	public DatabaseUser getClerkUser() {
		return clerkUser;
	}

	public DatabaseUser getLibrUser() {
		return librUser;
	}

	public int getBORROWER_INDEX() {
		return BORROWER_INDEX;
	}

	public int getCLERK_INDEX() {
		return CLERK_INDEX;
	}

	public int getLIBR_INDEX() {
		return LIBR_INDEX;
	}

	public Connection getConnection() {
		return this.con;
	}

	/**
	 * display panel for selected transacton.
	 */
	public void openTransactionPanel() {
		for (AbstractOperationPanel aop : currentUser
				.getAuthorizedTransactions()) {
			if (transList.getSelectedIndex() == aop.getOpIndex()) {

				if (aop.isDisplayed()) {

					JOptionPane.showMessageDialog(new JFrame(),
							"Selected transaction is already displayed.",
							"Error", JOptionPane.ERROR_MESSAGE);
				} else {
					aop.displayOperationPanel();
				}
			}
		}
	}

}

package tables;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BorrowerType extends AbstractDatabaseTable {
	private String param_type = "BORROWERTYPE_TYPE";
	private String param_btl = "BORROWERTYPE_BOOKTIMELIMIT";

	public BorrowerType(Connection con) {
		super(con);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void defineTableIdentity() {
		tableName = "BorrowerType";
		tableIndex = BORROWERTYPE_INDEX;

	}

	@Override
	public void insert() {
		// TODO Auto-generated method stub

		final JFrame insertFrame = new JFrame(
				"Enter values for the new tuple in " + tableName);
		JPanel insertContentPane = new JPanel();
		insertFrame.setContentPane(insertContentPane);

		JLabel typeLabel = new JLabel(param_type + ": ");
		JLabel btlLabel = new JLabel(param_btl + ": ");

		final JTextField typeField = new JTextField(15);
		final JTextField btlField = new JTextField(15);
		JButton insertBtn = new JButton("insert");
		insertBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String type = typeField.getText();
				String bookTimeLimitTemp = btlField.getText();
				int btl;
				PreparedStatement ps;

				try {
					ps = con.prepareStatement("INSERT INTO borrowerType VALUES (?,?)");
					ps.setString(1, type);

					if (bookTimeLimitTemp.length() == 0) {
						ps.setNull(2, java.sql.Types.INTEGER);
					} else {
						btl = Integer.parseInt(bookTimeLimitTemp);
						ps.setInt(2, btl);
					}

					ps.executeUpdate();
					con.commit();
					ps.close();
					insertFrame.dispose();
				} catch (SQLException ex) {
					System.out.println("Message: " + ex.getMessage());
					JOptionPane.showMessageDialog(new JFrame(),
							ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

					try {
						// undo the insert
						con.rollback();
					} catch (SQLException ex2) {

						JOptionPane.showMessageDialog(new JFrame(),
								ex2.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);

						System.out.println("Message: " + ex2.getMessage());
						System.exit(-1);
					}
				} finally {
					show();
				}
			}

		});

		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		insertContentPane.setLayout(gb);
		insertContentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,
				10));

		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 0);
		gb.setConstraints(typeLabel, c);
		insertContentPane.add(typeLabel);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 0, 5, 10);
		gb.setConstraints(typeField, c);
		insertContentPane.add(typeField);

		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 10, 0);
		gb.setConstraints(btlLabel, c);
		insertContentPane.add(btlLabel);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 10, 10);
		gb.setConstraints(btlField, c);
		insertContentPane.add(btlField);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 10, 10, 10);
		c.anchor = GridBagConstraints.CENTER;
		gb.setConstraints(insertBtn, c);
		insertContentPane.add(insertBtn);

		insertFrame.pack();
		insertFrame.setVisible(true);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		JFrame deleteFrame = new JFrame("Delete from " + tableName);
		JPanel deleteContentPanel = new JPanel();

		deleteFrame.setContentPane(deleteContentPanel);
		final JTextField bTypeField = new JTextField(15);
		final JTextField btlField = new JTextField(15);

		JButton deleteButton = new JButton("Delete");

		deleteContentPanel.add(bTypeField);
		deleteContentPanel.add(btlField);
		deleteContentPanel.add(deleteButton);

		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String borrowerType;
				int t;
				PreparedStatement ps;
				try {
					ps = con.prepareStatement("DELETE FROM BORROWERTYPE WHERE BORROWERTYPE_TYPE = ?");
					borrowerType = bTypeField.getText();
					System.out.println(borrowerType);
					ps.setString(1, borrowerType);
					int rowCount = ps.executeUpdate();
					System.out.println("row count is: " + rowCount);
					if (rowCount == 0) {
						JOptionPane.showMessageDialog(new JFrame(),
								"row count is 0", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

					con.commit();
					ps.close();

				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(new JFrame(),
							e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					try {
						con.rollback();
					} catch (SQLException e2) {
						JOptionPane.showMessageDialog(new JFrame(),
								e2.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
						System.exit(-1);
					}
				}

			}

		});

		deleteFrame.pack();
		deleteFrame.setVisible(true);

	}

	@Override
	public void showTable() {
		String btype;
		String timeLimit;
		Statement stmt;
		ResultSet rs;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM borrowertype");
			// get info on ResultSet
			ResultSetMetaData rsmd = rs.getMetaData();
			// get number of columns
			int numCols = rsmd.getColumnCount();
			System.out.println(" ");
			// display column names;
			for (int i = 0; i < numCols; i++) {
				// get column name and print it
				System.out.printf("%-15s", rsmd.getColumnName(i + 1));
			}
			System.out.println(" ");
			while (rs.next()) {

				btype = rs.getString("borrowertype_type");
				System.out.printf("%-10.10s", btype);

				timeLimit = rs.getString("borrowertype_booktimelimit");
				if (rs.wasNull()) {
					System.out.printf("%-15.15s\n", " ");
				} else {
					System.out.printf("%-15.15s\n", timeLimit);
				}
			}
			stmt.close();
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

	}
}
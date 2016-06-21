package tables;

//We need to import the java.sql package to use JDBC
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
//for reading from the command line
//for the login window

public class Fine extends AbstractDatabaseTable {

	public Fine(Connection con) {
		super(con);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void defineTableIdentity() {
		tableName = "Fine";
		tableIndex = FINE_INDEX;

	}

	@Override
	public void insert() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {

		JFrame updateFrame = new JFrame("update fine");
		JPanel contentPane = new JPanel();
		updateFrame.setContentPane(contentPane);
		JLabel fidLabel = new JLabel("fid: ");
		JLabel paidYear = new JLabel("paid year: () ");
		JLabel paidMonth = new JLabel("paid month(01~12):");
		JLabel paidDay = new JLabel("paid date(0~31): ");

		final JTextField fidTextField = new JTextField(10);
		final JTextField paidYearTextField = new JTextField(10);
		final JTextField paidMonthTextField = new JTextField(10);
		final JTextField paidDayTextField = new JTextField(10);

		JButton payBtn = new JButton("pay");

		payBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					int fidTemp = Integer.parseInt(fidTextField.getText());
					String fid = "'" + fidTemp + "'";
					String paidDate = "";
					PreparedStatement ps;
					String paid_year = paidYearTextField.getText();
					String paid_month = paidMonthTextField.getText();
					String paid_day = paidDayTextField.getText();

					// apend paid date
					if (paid_year.length() == 0 || paid_month.length() == 0
							|| paid_day.length() == 0) {
						paidDate = "";
					} else {
						paidDate = "'" + paid_year + "-" + paid_month + "-"
								+ paid_day + "'";
					}

					String statement = "UPDATE Fine SET fine_paiddate = "
							+ paidDate + " WHERE fine_fid = " + fid;

					ps = con.prepareStatement(statement);

					int rowCount = ps.executeUpdate();
					if (rowCount == 0) {
						JOptionPane.showMessageDialog(new JFrame(),
								"given fid doesn't exist", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					con.commit();
					ps.close();

					JOptionPane.showMessageDialog(new JFrame(),
							"You have successfully paid for your fine. Payment date is "
									+ paidDate + ".", "Payment successful",
							JOptionPane.INFORMATION_MESSAGE);
					tableFrame.dispose();
					setDisplayed(false);

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(new JFrame(),
							ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

					try {
						con.rollback();
					} catch (SQLException ex2) {
						JOptionPane.showMessageDialog(new JFrame(),
								ex.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);

						System.exit(-1);
					}
				}
			}
		});

		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// row 0
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(fidLabel, c);
		c.gridx = 1;
		c.insets = new Insets(0, 5, 0, 0);
		contentPane.add(fidTextField, c);
		// row 1
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(paidYear, c);
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 5, 0, 0);
		contentPane.add(paidYearTextField, c);
		// row 2
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(paidMonth, c);
		c.gridx = 1;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(paidMonthTextField, c);
		// row 3
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(paidDay, c);
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(paidDayTextField, c);

		// row 4
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 4;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(25, 0, 0, 0);
		contentPane.add(payBtn, c);
		updateFrame.setSize(300, 250);
		updateFrame.setVisible(true);
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showTable() {

		// TODO Auto-generated method stub

	}

	public int[] searchByBid(String bid) {

		ResultSet rs;
		Statement stmt;
		int fid = -1;
		int amount = -1;
		try {
			stmt = con.createStatement();

			String tempStatement = "  SELECT * FROM FINE, "
					+ "BORROWING WHERE BORROWING_BID ="
					+ "'"
					+ bid
					+ "'"
					+ " AND FINE_BORID = BORROWING_BORID AND FINE_PAIDDATE IS NULL";
			rs = stmt.executeQuery(tempStatement);
			rs.next();
			// get info on ResultSet
			fid = rs.getInt("FINE_FID");
			amount = rs.getInt("FINE_AMOUNT");
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(new JFrame(), ex.getMessage()
					+ " No outstanding fine for the given bid", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return new int[] { fid, amount };
	}
}

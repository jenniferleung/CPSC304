package tables;

//We need to import the java.sql package to use JDBC
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//for reading from the command line
//for the login window

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 *
 */
public class HoldRequest extends AbstractDatabaseTable {

	public HoldRequest(Connection con) {
		super(con);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void defineTableIdentity() {
		tableName = "HoldRequest";
		tableIndex = HOLDREQUEST_INDEX;

	}

	@Override
	public void insert() {
		// TODO Auto-generated method stub

		JFrame updateFrame = new JFrame("Place a hold request");
		JPanel contentPane = new JPanel();
		updateFrame.setContentPane(contentPane);
		JLabel bidLabel = new JLabel("bid: ");
		JLabel callNumLabel = new JLabel("Call #: ");
		JLabel issueYear = new JLabel("Issue year: () ");
		JLabel issueMonth = new JLabel("Issue month(01~12):");
		JLabel issueDay = new JLabel("Issue date(01~31): ");

		final JTextField bidTextField = new JTextField(10);
		final JTextField callNumTextField = new JTextField(10);
		final JTextField issueYearTextField = new JTextField(10);
		final JTextField issueMonthTextField = new JTextField(10);
		final JTextField issueDayTextField = new JTextField(10);

		JButton holdBtn = new JButton("Hold");
		final BookCopy bc = new BookCopy(con);

		holdBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					boolean is_all_out = bc.isAllBookOut(callNumTextField
							.getText());
					if (!is_all_out) {
						// cannot hold
						JOptionPane
						.showMessageDialog(
								new JFrame(),
								"At least one copies of the book is currently in the library",
								"Cannot place the hold request.",
								JOptionPane.ERROR_MESSAGE);
					} else {
						String issuedDate = "";
						String bid;
						String callNum;
						PreparedStatement ps;
						String year = issueYearTextField.getText();
						String month = issueMonthTextField.getText();
						String day = issueDayTextField.getText();

						ps = con.prepareStatement("INSERT INTO holdRequest VALUES (hold_request_counter.nextval,?,?,?)");

						bid = bidTextField.getText();
						ps.setString(1, bid);
						callNum = callNumTextField.getText();
						ps.setString(2, callNum);

						// append issue date
						if (year.length()==0 || month.length()==0 ||day.length()==0) {
							ps.setString(3, null);
						} 
						else {
							issuedDate = year + "-" + month + "-" + day;
							SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
							java.util.Date utilDate = null; 
							try { 
								utilDate = fm.parse(issuedDate); } catch (ParseException e) {
									// TODO Auto-generated catch block e.printStackTrace();
								} 
							java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
							ps.setDate(3,sqlDate); }

						
						int rowCount = ps.executeUpdate();
						if (rowCount == 0) {
							JOptionPane.showMessageDialog(new JFrame(),
									"given callnumber doesn't exist", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
						con.commit();
						ps.close();

						JOptionPane.showMessageDialog(new JFrame(),
								"You have successfully hold the request for the book with callNo. "
										+ callNum + ".",
										"Hold request successful",
										JOptionPane.INFORMATION_MESSAGE);
						tableFrame.dispose();
						setDisplayed(false);

					}
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(new JFrame(),
							ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

					try {
						con.rollback();
					} catch (SQLException ex2) {
						JOptionPane.showMessageDialog(new JFrame(),
								ex2.getMessage(), "Error",
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
		contentPane.add(bidLabel, c);
		c.gridx = 1;
		c.insets = new Insets(0, 5, 0, 0);
		contentPane.add(bidTextField, c);
		// row 1
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(callNumLabel, c);
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 5, 0, 0);
		contentPane.add(callNumTextField, c);
		// row 2
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(issueYear, c);
		c.gridx = 1;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(issueYearTextField, c);
		// row 3
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(issueMonth, c);
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(issueMonthTextField, c);
		// row 4
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(issueDay, c);
		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(issueDayTextField, c);
		// row 5
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 5;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(25, 0, 0, 0);
		contentPane.add(holdBtn, c);
		updateFrame.setSize(300, 250);
		updateFrame.setVisible(true);

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showTable() {
		// TODO Auto-generated method stub

	}
}
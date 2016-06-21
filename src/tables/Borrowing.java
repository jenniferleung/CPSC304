package tables;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Borrowing extends AbstractDatabaseTable {

	public Borrowing(Connection con) {
		super(con);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void defineTableIdentity() {
		tableName = "Borrowing";
		tableIndex = BORROWING_INDEX;
	}

	@Override
	public void insert() {

		final JFrame insertFrame = new JFrame("Check out");
		JPanel contentPane = new JPanel();
		insertFrame.setContentPane(contentPane);
		insertFrame.setSize(300, 400);
		JLabel bidLabel = new JLabel("bid: ");
		final JTextField bidTextField = new JTextField(10);

		JLabel callNo1 = new JLabel("Call # 1:");
		final JTextField callNo1TextField = new JTextField(10);

		JLabel callNo2 = new JLabel("Call # 2:");
		final JTextField callNo2TextField = new JTextField(10);

		JLabel callNo3 = new JLabel("Call # 3:");
		final JTextField callNo3TextField = new JTextField(10);

		JLabel outYear = new JLabel("Out year: ");
		JLabel outMonth = new JLabel("Out month(01~12):");
		JLabel outDay = new JLabel("Out date(01~31): ");
		final JTextField outYearTextField = new JTextField(10);
		final JTextField outMonthTextField = new JTextField(10);
		final JTextField outDayTextField = new JTextField(10);

		JButton checkOutBtn = new JButton("checkout");
		checkOutBtn.addActionListener(new ActionListener() {
			String[] reportString = new String[3];

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				String outDate = "";

				if (outDayTextField.getText().length() == 0
						|| outMonthTextField.getText().length() == 0
						|| outDayTextField.getText().length() == 0) {

				} else {
					outDate = outYearTextField.getText() + "-"
							+ outMonthTextField.getText() + "-"
							+ outDayTextField.getText();
				}
				if (callNo1TextField.getText().length() > 0) {
					try {
						executeInsert(bidTextField.getText(),
								callNo1TextField.getText(), outDate, 0);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				if (callNo2TextField.getText().length() > 0) {
					try {
						executeInsert(bidTextField.getText(),
								callNo2TextField.getText(), outDate, 1);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				if (callNo3TextField.getText().length() > 0) {
					try {
						executeInsert(bidTextField.getText(),
								callNo3TextField.getText(), outDate, 2);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				JFrame reportFrame = new JFrame("check out successful.");
				JPanel reportcp = new JPanel();
				reportFrame.setContentPane(reportcp);
				JTextArea reportTextArea = new JTextArea();
				reportTextArea.setText("Item1: " + reportString[0] + "\n"
						+ "item2: " + reportString[1] + "\n" + "item3: "
						+ reportString[2]);
				reportcp.add(reportTextArea);
				reportFrame.setSize(200, 200);
				reportFrame.setVisible(true);

			}

			private void executeInsert(String inputBid, String bookCallNo,
					String checkedOutDate, int index) throws ParseException {
				int bid = 0;
				String callNumber;
				String copyNoTemp = "";
				int copyNo = 0;
				String type = "";
				int bookTimeLimit = 0;
				PreparedStatement ps;
				PreparedStatement ps2;
				Statement stmt;
				ResultSet rs;

				try {
					System.out.println(checkedOutDate);
					ps = con.prepareStatement("INSERT INTO borrowing VALUES (borrowing_counter.nextval,?,?,?,?,?)");
					ps2 = con
							.prepareStatement("UPDATE bookCopy SET bookCopy_status = 'out' "
									+ "WHERE bookCopy_callNumber = ? AND bookCopy_copyNo = ?");

					String bidTemp = inputBid;
					if (bidTemp.length() == 0) {
						ps.setNull(1, java.sql.Types.INTEGER);
					} else {
						bid = Integer.parseInt(bidTemp);
						ps.setInt(1, bid);
					}

					callNumber = bookCallNo;
					if (callNumber.length() == 0) {
						ps.setString(2, null);
					} else {
						ps.setString(2, callNumber);
						ps2.setString(1, callNumber);
					}

					stmt = con.createStatement();
					String string = "SELECT * FROM BOOKCOPY WHERE BOOKCOPY_CALLNUMBER = '"
							+ bookCallNo + "' AND BOOKCOPY_STATUS = 'in'";
					rs = stmt.executeQuery(string);

					while (rs.next()) {
						copyNoTemp = rs.getString("BOOKCOPY_COPYNO");
					}

					if (copyNoTemp.length() == 0) {
						ps.setInt(3, copyNo);
						ps2.setInt(2, copyNo);
						JOptionPane.showMessageDialog(new JFrame(), callNumber
								+ " doesn't have any available copy", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						copyNo = Integer.parseInt(copyNoTemp);
						ps.setInt(3, copyNo);
						ps2.setInt(2, copyNo);
					}

					if (checkedOutDate.length() == 0) {
						JOptionPane.showMessageDialog(new JFrame(),
								"Please preform transaction on " + bookCallNo
										+ "again and insert correct Date",
								"Error", JOptionPane.ERROR_MESSAGE);
						ps.setString(4, null);
						ps.setString(5, null);
					} else {
						SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
						java.util.Date utilDate = fm.parse(checkedOutDate);
						java.sql.Date sqlDate = new java.sql.Date(utilDate
								.getTime());
						ps.setDate(4, sqlDate);
						ps.setString(5, null);
						if (copyNo != 0) {

							System.out.println("why did you execute!!!"
									+ copyNo);
							ps.executeUpdate();
							ps2.executeUpdate();

							stmt = con.createStatement();
							rs = stmt.executeQuery("SELECT * "
									+ "FROM borrower "
									+ "WHERE borrower.borrower_bid=" + bid);
							while (rs.next()) {
								type = rs.getString("borrower_type");
								// System.out.println("type= " + type);
							}
							stmt.close();

							// get bookTimeLimit
							// TODO

							stmt = con.createStatement();
							String temp = "SELECT borrowertype_booktimelimit "
									+ "FROM borrowerType "
									+ "WHERE borrowerType.borrowerType_type='"
									+ type + "'";
							rs = stmt.executeQuery(temp);
							while (rs.next()) {
								bookTimeLimit = rs
										.getInt("borrowertype_booktimelimit");
								// System.out.println("bookTimeLimit= " +
								// bookTimeLimit);
							}

							stmt.close();

							String dueDay = checkedOutDate.substring(8, 10);
							System.out.println("Day: " + dueDay);
							String dueMonth = checkedOutDate.substring(5, 7);
							System.out.println("Month: " + dueMonth);
							String dueYear = checkedOutDate.substring(0, 4);
							System.out.println("Year: " + dueYear);
							String dueDate = dueYear + "-" + dueMonth + "-"
									+ dueDay;
							System.out.println("DueDate: " + dueDate);
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd");
							Calendar cal = Calendar.getInstance();
							cal.setTime(sdf.parse(dueDate));
							cal.add(Calendar.DATE, bookTimeLimit);
							dueDate = sdf.format(cal.getTime());
							String dueDateReport = dueDate.toString();
							System.out.println("Report: " + dueDateReport);

							reportString[index] = dueDateReport;

						}
					}

					// commit work
					con.commit();
					ps.close();
					ps2.close();
					insertFrame.dispose();
					setDisplayed(false);

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(new JFrame(), ex.getMessage()
							+ "test1", "Error", JOptionPane.ERROR_MESSAGE);
					try {
						// undo the insert
						con.rollback();
					} catch (SQLException ex2) {
						JOptionPane.showMessageDialog(new JFrame(),
								ex2.getMessage() + "test2", "Error",
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
		contentPane.add(callNo1, c);
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 5, 0, 0);
		contentPane.add(callNo1TextField, c);
		// row 2
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(callNo2, c);
		c.gridx = 1;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(callNo2TextField, c);
		// row 3
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(callNo3, c);
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(callNo3TextField, c);
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(outYear, c);
		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(outYearTextField, c);

		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(outMonth, c);
		c.gridx = 1;
		c.gridy = 5;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(outMonthTextField, c);

		c.gridx = 0;
		c.gridy = 6;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(outDay, c);
		c.gridx = 1;
		c.gridy = 6;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(outDayTextField, c);

		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 7;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(25, 0, 0, 0);
		contentPane.add(checkOutBtn, c);
		insertFrame.setVisible(true);

	}

	@Override
	public void update() {
		final JFrame updateFrame = new JFrame("update borrowing");
		JPanel contentPane = new JPanel();
		updateFrame.setContentPane(contentPane);

		JLabel callNoLabel = new JLabel("call #: ");
		JLabel copyNo = new JLabel("Copy #:");
		JLabel inYear = new JLabel("in year: ");
		JLabel inMonth = new JLabel("in month(01~12):");
		JLabel inDay = new JLabel("in date(01~31): ");
		final JTextField inYearTextField = new JTextField(10);
		final JTextField inMonthTextField = new JTextField(10);
		final JTextField inDayTextField = new JTextField(10);
		final JTextField callNoTextField = new JTextField(10);
		final JTextField copyNoField = new JTextField(10);

		JButton returnBtn = new JButton("return book");

		returnBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int borid = 0;
				String inDate;
				String dueDate = null;
				String inDateComp = null;
				int bid = 0;
				String type = "";
				String callNumber;
				int copyNo;
				int bookTimeLimit = 0;
				PreparedStatement ps;
				PreparedStatement ps2;
				PreparedStatement ps3;

				// fine
				int amount = 0;
				Statement stmt;
				ResultSet rs;
				try {
					ps = con.prepareStatement("UPDATE borrowing SET borrowing_inDate = ? WHERE borrowing_borid = ?");
					ps2 = con
							.prepareStatement("UPDATE bookCopy SET bookCopy_status = 'in' WHERE bookCopy_callNumber = ? AND bookCopy_copyNo = ?");
					ps3 = con
							.prepareStatement("INSERT INTO fine VALUES (fine_counter.nextval,?,?,?,?)");

					System.out.print("\nCallNumber: ");
					callNumber = callNoTextField.getText();
					ps2.setString(1, callNumber);

					System.out.print("\nCopyNumber: ");
					String copyNoTemp = copyNoField.getText();
					copyNo = Integer.parseInt(copyNoTemp);
					ps2.setInt(2, copyNo);

					String year = inYearTextField.getText();
					String month = inMonthTextField.getText();
					String day = inDayTextField.getText();
					if (year.length() == 0 || month.length() == 0
							|| day.length() == 0) {
						ps.setString(1, null);
					} else {
						inDate = year + "-" + month + "-" + day;
						inDateComp = inDate;
						// TODO
						System.out.println("inDate: " + inDate);
						// System.out.println("inDateComp: " + inDateComp);
						SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
						java.util.Date utilDate = fm.parse(inDate);
						java.sql.Date sqlDate = new java.sql.Date(utilDate
								.getTime());
						ps.setDate(1, sqlDate);
						ps3.setDate(2, sqlDate);
					}

					// getting borid given callNumber and copyNo
					stmt = con.createStatement();
					String temp1 = "SELECT * " + "FROM borrowing "
							+ "WHERE borrowing.borrowing_callNumber='"
							+ callNumber + "' AND borrowing.borrowing_copyNo='"
							+ copyNo + "'";
					rs = stmt.executeQuery(temp1);

					while (rs.next()) {
						String boridTemp = rs.getString("borrowing_borid");
						borid = Integer.parseInt(boridTemp);
						ps.setInt(2, borid);
						ps3.setInt(4, borid);
					}
					stmt.close();

					// getting DueDate for a certain borid
					stmt = con.createStatement();
					rs = stmt.executeQuery("SELECT * " + "FROM borrowing "
							+ "WHERE borrowing.borrowing_borid=" + borid);

					while (rs.next()) {
						dueDate = rs.getString("borrowing_outDate");
						bid = rs.getInt("borrowing_bid");
					}
					stmt.close();
					// get borrower type
					stmt = con.createStatement();
					rs = stmt.executeQuery("SELECT * " + "FROM borrower "
							+ "WHERE borrower.borrower_bid=" + bid);
					while (rs.next()) {
						type = rs.getString("borrower_type");
						// System.out.println("type= " + type);
					}
					stmt.close();
					stmt = con.createStatement();
					String temp = "SELECT borrowertype_booktimelimit "
							+ "FROM borrowerType "
							+ "WHERE borrowerType.borrowerType_type='" + type
							+ "'";
					rs = stmt.executeQuery(temp);
					while (rs.next()) {
						bookTimeLimit = rs.getInt("borrowertype_booktimelimit");
					}

					stmt.close();

					String dueDay = dueDate.substring(0, 2);
					String dueMonth = dueDate.substring(3, 6);
					if (dueMonth.equals("JAN"))
						dueMonth = "01";
					else if (dueMonth.equals("FEB"))
						dueMonth = "02";
					else if (dueMonth.equals("MAR"))
						dueMonth = "03";
					else if (dueMonth.equals("APR"))
						dueMonth = "04";
					else if (dueMonth.equals("MAY"))
						dueMonth = "05";
					else if (dueMonth.equals("JUN"))
						dueMonth = "06";
					else if (dueMonth.equals("JUL"))
						dueMonth = "07";
					else if (dueMonth.equals("AUG"))
						dueMonth = "08";
					else if (dueMonth.equals("SEP"))
						dueMonth = "09";
					else if (dueMonth.equals("OCT"))
						dueMonth = "10";
					else if (dueMonth.equals("NOV"))
						dueMonth = "11";
					else if (dueMonth.equals("DEC"))
						dueMonth = "12";
					String dueYear = dueDate.substring(7, 9);
					dueDate = "20" + dueYear + "-" + dueMonth + "-" + dueDay;

					// String dueDateComp = dueDate.toString();
					// System.out.println(dueDate);

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					cal.setTime(sdf.parse(dueDate));
					cal.add(Calendar.DATE, bookTimeLimit);
					dueDate = sdf.format(cal.getTime());
					String dueDateComp = dueDate.toString();

					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
					java.util.Date date1 = sdf1.parse(dueDateComp);
					java.util.Date date2 = sdf1.parse(inDateComp);

					System.out.println(sdf.format(date1));
					System.out.println(sdf.format(date2));

					if (date1.compareTo(date2) < 0) {
						System.out.println("Overdue");
						int diffInDays = (int) ((date2.getTime() - date1
								.getTime()) / (1000 * 60 * 60 * 24));
						System.out.println(diffInDays);

						amount = diffInDays;
						ps3.setInt(1, amount);
						ps3.setString(3, null);
						ps3.executeUpdate();

					} else if (date1.compareTo(date2) >= 0) {
						System.out.println("not overdue");
					}

					int rowCount = ps.executeUpdate();
					ps2.executeUpdate();
					if (rowCount == 0) {
						System.out.println("\nBorrowing " + borid
								+ " does not exist!");
					}

					con.commit();

					ps.close();
					ps2.close();
					ps3.close();

					JOptionPane.showMessageDialog(new JFrame(),
							"you have successfully returned a copy of book. You book is overdue for "
									+ amount + " days" + callNumber + ".",
							"Successfully returned",
							JOptionPane.INFORMATION_MESSAGE);

					updateFrame.dispose();
					setDisplayed(false);

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
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// row 0
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(callNoLabel, c);
		c.gridx = 1;
		c.insets = new Insets(0, 5, 0, 0);
		contentPane.add(callNoTextField, c);
		// row 1
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(copyNo, c);
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 5, 0, 0);
		contentPane.add(copyNoField, c);

		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(inYear, c);
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(inYearTextField, c);

		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(inMonth, c);
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(inMonthTextField, c);

		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(inDay, c);
		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(inDayTextField, c);

		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 5;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(25, 0, 0, 0);
		contentPane.add(returnBtn, c);
		updateFrame.setSize(300, 250);
		updateFrame.setVisible(true);

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

    @Override
	public void showTable() {
		JLabel sendEmailLabel = new JLabel(
                                           "send email to brrower with overdue items");
		JButton sendEmailBtn = new JButton("send email");
		final JFrame insertFrame = new JFrame("List of Overdue Items");
		insertFrame.setSize(500, 250);
		JPanel insertContentPane = new JPanel();
		insertFrame.setContentPane(insertContentPane);
        
		Statement stmt1;
		Statement stmt2;
		Statement stmt3;
		Statement stmt4;
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		ResultSet rs4;
		String msg = "";
		try {
			stmt1 = con.createStatement();
			String temp1 = "SELECT * " + "FROM Borrowing "
            + "WHERE Borrowing_inDate is null";
			rs1 = stmt1.executeQuery(temp1);
            
			while (rs1.next()) {
				String outDate = rs1.getString("borrowing_outDate");
				String bid = rs1.getString("borrowing_bid");
				String callNumber = rs1.getString("borrowing_callNumber");
				stmt2 = con.createStatement();
				String temp2 = "SELECT * " + "FROM Borrower "
                + "WHERE Borrower_bid = '" + bid + "'";
				rs2 = stmt2.executeQuery(temp2);
				rs2.next();
				String type = rs2.getString("Borrower_type");
				String email = rs2.getString("Borrower_emailAddress");
                
				stmt3 = con.createStatement();
				String temp = "SELECT borrowertype_booktimelimit "
                + "FROM borrowerType " + "WHERE borrowerType_type='"
                + type + "'";
				rs3 = stmt3.executeQuery(temp);
				rs3.next();
				int bookTimeLimit = rs3.getInt("borrowertype_booktimelimit");
				stmt4 = con.createStatement();
				String temp4 = "SELECT * " + "FROM Book "
                + "WHERE Book_callNumber = '" + callNumber + "'";
				rs4 = stmt4.executeQuery(temp4);
                
				rs4.next();
                
				String title = rs4.getString("Book_title");
                
				String dueDay = outDate.substring(0, 2);
				String dueMonth = outDate.substring(3, 6);
				if (dueMonth.equals("JAN"))
					dueMonth = "01";
				else if (dueMonth.equals("FEB"))
					dueMonth = "02";
				else if (dueMonth.equals("MAR"))
					dueMonth = "03";
				else if (dueMonth.equals("APR"))
					dueMonth = "04";
				else if (dueMonth.equals("MAY"))
					dueMonth = "05";
				else if (dueMonth.equals("JUN"))
					dueMonth = "06";
				else if (dueMonth.equals("JUL"))
					dueMonth = "07";
				else if (dueMonth.equals("AUG"))
					dueMonth = "08";
				else if (dueMonth.equals("SEP"))
					dueMonth = "09";
				else if (dueMonth.equals("OCT"))
					dueMonth = "10";
				else if (dueMonth.equals("NOV"))
					dueMonth = "11";
				else if (dueMonth.equals("DEC"))
					dueMonth = "12";
				String dueYear = outDate.substring(7, 9);
				outDate = "20" + dueYear + "-" + dueMonth + "-" + dueDay;
                
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.setTime(sdf.parse(outDate));
				cal.add(Calendar.DATE, bookTimeLimit);
				String dueDate = sdf.format(cal.getTime());
                
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date1 = sdf1.parse(dueDate);
				java.util.Date date2 = sdf1.parse("2013-04-03");
                
				if (date1.compareTo(date2) < 0) {
					System.out.println("Overdue");
					System.out.println("Account with email address " + email
                                       + " has " + title + " overdue");
					String message = "Account with email address " + email
                    + " has " + title + " overdue";
					msg = msg + "\n" + message;
                    
				}
			}
            
			JTextArea text = new JTextArea();
			text.setText(msg);
			insertContentPane.add(text);
            
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		sendEmailBtn.addActionListener(new ActionListener() {
            
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(new JFrame(),
                                              "Emails sent successfully.", "successful",
                                              JOptionPane.INFORMATION_MESSAGE);
                
				insertFrame.dispose();
			}
		});
        
		GridBagConstraints c = new GridBagConstraints();
		contentPane.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
        
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.insets = new Insets(30, 0, 0, 0);
		c.anchor = GridBagConstraints.CENTER;
		insertContentPane.add(sendEmailLabel, c);
        
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(20, 0, 0, 0);
		insertContentPane.add(sendEmailBtn, c);
        
		insertFrame.setVisible(true);
        
	}

}
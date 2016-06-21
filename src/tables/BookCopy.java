package tables;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class BookCopy extends AbstractDatabaseTable {
	
	public BookCopy(Connection con) {
		super(con);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void defineTableIdentity() {
		tableName = "BookCopy";
		tableIndex = BOOKCOPY_INDEX;
		
	}
	
	@Override
	public void insert() {
		final JFrame insertFrame = new JFrame("Add a new book copy");
		insertFrame.setSize(400, 250);
		JPanel contentPane = new JPanel();
		insertFrame.setContentPane(contentPane);
		JLabel callNoLabel = new JLabel("Call #: ");
		final JTextField callNoField = new JTextField(10);
		
		JButton checkBtn = new JButton("Continue");
		
		checkBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BookCopy bc = new BookCopy(con);
				int numOfCopies = bc.searchByCallNo(callNoField.getText());
				System.out.println("num of copies: " + numOfCopies);
				int newCopyNo = numOfCopies + 1;
				
				if (numOfCopies == 0) {
					// TODO: create a book
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(
																	 tableFrame, "Specified call# has NOT been found. "
																	 + "Would you like to add a new book?",
																	 "Are you sure?", dialogButton);
					// execute upon "YES"
					if (dialogResult == JOptionPane.YES_OPTION) {
						Book book = new Book(con);
						book.insert();
					}
				} else {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane
					.showConfirmDialog(
									   tableFrame,
									   "Specified call# has been found. "
									   + "Would you like to add a book copy with copy No. equal to "
									   + newCopyNo + "?", "Are you sure?",
									   dialogButton);
					// execute upon "YES"
					if (dialogResult == JOptionPane.YES_OPTION) {
						PreparedStatement ps;
						try {
							ps = con.prepareStatement("INSERT INTO bookCopy VALUES (?,?,'in')");
							ps.setString(1, callNoField.getText());
							ps.setInt(2, newCopyNo);
							ps.executeUpdate();
							// commit work
							con.commit();
							ps.close();
							JOptionPane
							.showMessageDialog(new JFrame(),
											   "Book copy has been successfully created for "
											   + callNoField.getText()
											   + " with copy # "
											   + newCopyNo + ".",
											   "Successful",
											   JOptionPane.INFORMATION_MESSAGE);
							insertFrame.dispose();
							setDisplayed(false);
							
						} catch (SQLException ex) {
							JOptionPane.showMessageDialog(new JFrame(),
														  ex.getMessage(), "Error",
														  JOptionPane.ERROR_MESSAGE);
							
							try {
								// undo the insert
								con.rollback();
							} catch (SQLException ex2) {
								JOptionPane.showMessageDialog(new JFrame(),
															  ex2.getMessage(), "Error",
															  JOptionPane.ERROR_MESSAGE);
								System.exit(-1);
							}
							
						}
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
		contentPane.add(callNoLabel, c);
		c.gridx = 1;
		c.insets = new Insets(0, 5, 0, 0);
		contentPane.add(callNoField, c);
		c.gridx = 2;
		c.insets = new Insets(0, 5, 0, 0);
		contentPane.add(checkBtn, c);
		
		insertFrame.setVisible(true);
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
		
	}
	
	public void showCheckedOutItems() throws ParseException {
		
		JFrame showTableFrame = new JFrame("checked out items");
		JPanel contentPane = new JPanel();
		showTableFrame.setContentPane(contentPane);
		// JTextArea resultArea = new JTextArea();
		
		showTableFrame.setSize(300, 1000);
		
		JScrollPane scrollPane;
		String columnNames[] = { "Checked out date", "Due Date", "Call number",
			"copy No", "over due" };
		
		// [column][row]
		String[][] resultString = new String[10][5];
		
		// int bid;
		String out = "out";
		String checkedOutDate = "00-JAN-00";
		int bookTimeLimit = 0;
		Statement stmt;
		Statement stmt1;
		Statement stmt2;
		
		ResultSet rs;
		ResultSet rs1;
		ResultSet rs2;
		
		try {
			stmt = con.createStatement();
			String temp = "SELECT *" + "FROM bookCopy "
			+ "WHERE BookCopy_status = '" + out + "'";
			rs = stmt.executeQuery(temp);
			String overDue = "no";
			
			int index = 0;
			while (rs.next() && index < 10) {
				String callNumber = rs.getString("bookcopy_callNumber");
				String copyNo = rs.getString("bookCopy_copyNo");
				resultString[index][2] = callNumber;
				resultString[index][3] = copyNo;
				index++;
			}
			stmt.close();
			
			while (index > 0) {
				stmt = con.createStatement();
				String temp1 = "SELECT *" + "FROM borrowing "
				+ "WHERE borrowing_callNumber = '"
				+ resultString[index][2] + "' AND borrowing_copyNo = '"
				+ resultString[index][3] + "'";
				rs = stmt.executeQuery(temp1);
				
				while (rs.next()) {
					checkedOutDate = rs.getString("borrowing_outDate");
					System.out.println("Check out date: " + checkedOutDate);
					resultString[index][0] = checkedOutDate;
					String bidTemp = rs.getString("borrowing_bid");
					int bid = Integer.parseInt(bidTemp);
					
					stmt1 = con.createStatement();
					rs1 = stmt1.executeQuery("SELECT * " + "FROM borrower "
											 + "WHERE borrower.borrower_bid=" + bid);
					while (rs1.next()) {
						String type = rs1.getString("borrower_type");
						stmt2 = con.createStatement();
						String temp2 = "SELECT borrowertype_booktimelimit "
						+ "FROM borrowerType "
						+ "WHERE borrowerType.borrowerType_type='"
						+ type + "'";
						rs2 = stmt2.executeQuery(temp2);
						while (rs2.next()) {
							bookTimeLimit = rs2
							.getInt("borrowertype_booktimelimit");
						}
						
						stmt2.close();
					}
					
					stmt1.close();
				}
				stmt.close();
				
				String dueDay = checkedOutDate.substring(0, 2);
				String dueMonth = checkedOutDate.substring(3, 6);
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
				String dueYear = checkedOutDate.substring(7, 9);
				String dueDate = "20" + dueYear + "-" + dueMonth + "-" + dueDay;
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.setTime(sdf.parse(dueDate));
				cal.add(Calendar.DATE, bookTimeLimit);
				dueDate = sdf.format(cal.getTime());
				String dueDateComp = dueDate.toString();
				
				System.out.println("due date : " + dueDate);
				
				resultString[index][1] = dueDateComp;
				
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date1 = sdf1.parse(dueDateComp);
				java.util.Date date2 = sdf1.parse("2013-04-03");
				
				if (date1.compareTo(date2) < 0) {
					overDue = "yes";
				}
				resultString[index][4] = overDue;
				overDue = "no";
				
				index--;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JTable resultTable = new JTable(resultString, columnNames);
		scrollPane = new JScrollPane(resultTable);
		
		GridBagConstraints c = new GridBagConstraints();
		contentPane.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(scrollPane, c);
		showTableFrame.pack();
		showTableFrame.setVisible(true);
	}
	
	public int searchByCallNo(String callNum) {
		
		System.out.println("call num: " + callNum);
		
		ResultSet rs;
		
		Statement stmt;
		String numRows = null;
		try {
			stmt = con.createStatement();
			String tempStatement = "SELECT COUNT(*) FROM BOOKCOPY WHERE BOOKCOPY_CALLNUMBER ='"
			+ callNum + "'";
			rs = stmt.executeQuery(tempStatement);
			rs.next();
			numRows = rs.getString("COUNT(*)");
			System.out.printf(numRows);
			stmt.close();
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(),
										  "Error", JOptionPane.ERROR_MESSAGE);
		}
		return Integer.parseInt(numRows);
	}
	
	public boolean isAllBookOut(String callNumber) {
		boolean is_all_out = true;
		String status;
		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = con.createStatement();
			String temp = "SELECT bookCopy_status FROM bookCopy where bookcopy_callnumber = '"
			+ callNumber + "'";
			rs = stmt.executeQuery(temp);
			while (rs.next()) {
				status = rs.getString("bookCopy_status");
				if (status.equals("in")) {
					System.out.println("caught one 'in'");
					is_all_out = false;
				}
			}
			stmt.close();
			
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(),
										  "Error", JOptionPane.ERROR_MESSAGE);
			
			try {
				con.rollback();
			} catch (SQLException ex2) {
				JOptionPane.showMessageDialog(new JFrame(), ex2.getMessage(),
											  "Error", JOptionPane.ERROR_MESSAGE);
				
				System.exit(-1);
			}
		}
		return is_all_out;
	}
	
}

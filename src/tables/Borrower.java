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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
//for reading from the command line
//for the login window

public class Borrower extends AbstractDatabaseTable {
    
	public Borrower(Connection con) {
		super(con);
		// TODO Auto-generated constructor stub
	}
    
	@Override
	public void defineTableIdentity() {
		tableName = "Borrower";
		tableIndex = BORROWER_INDEX;
        
	}
    
	@Override
	public void insert() {
		JLabel password = new JLabel("Password: ");
		JLabel name = new JLabel("Name: ");
		JLabel address = new JLabel("Address: ");
		JLabel phone = new JLabel("Phone: ");
		JLabel emailAddress = new JLabel("Email address: ");
		JLabel sinOrStNo = new JLabel("Sin or Student#: ");
		JLabel expiryDate = new JLabel("Expiry date: ");
		JLabel borrowerType = new JLabel("Borrower type: ");
        
		final JTextField passwordField = new JTextField(10);
		final JTextField nameField = new JTextField(10);
		final JTextField addressField = new JTextField(10);
		final JTextField phoneField = new JTextField(10);
		final JTextField emailAddressField = new JTextField(10);
		final JTextField sinOrStNoField = new JTextField(10);
		final JTextField expiryDateField = new JTextField(10);
		String[] comboBoxList = { "STUDENT", "FACULTY", "STAFF" };
		final JComboBox borrowerTypeComboBox = new JComboBox(comboBoxList);
		borrowerTypeComboBox.setSelectedIndex(0);
        
		JButton createBtn = new JButton("create");
		final JFrame insertFrame = new JFrame(
                                              "insert a new row into Borrower.. ");
		insertFrame.setSize(300, 400);
		JPanel contentPane = new JPanel();
		insertFrame.setContentPane(contentPane);
        
		createBtn.addActionListener(new ActionListener() {
            
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int bid;
				String pw;
				String name;
				String addr;
				int phone;
				String email;
				int sinOrStNo;
				String expiryDate;
				String type;
				PreparedStatement ps;
				try {
					ps = con.prepareStatement("INSERT INTO borrower VALUES (borrower_counter.nextval,?,?,?,?,?,?,?,?)");
                    
					pw = passwordField.getText();
					if (pw.length() == 0) {
						ps.setString(1, null);
					} else {
						ps.setString(1, pw);
					}
                    
					name = nameField.getText();
					if (name.length() == 0) {
						ps.setString(2, null);
					} else {
						ps.setString(2, name);
					}
                    
					addr = addressField.getText();
					if (addr.length() == 0) {
						ps.setString(3, null);
					} else {
						ps.setString(3, addr);
					}
                    
					String phoneTemp = phoneField.getText();
					if (phoneTemp.length() == 0) {
						ps.setNull(4, java.sql.Types.INTEGER);
					} else {
						phone = Integer.parseInt(phoneTemp);
						ps.setInt(4, phone);
					}
                    
					email = emailAddressField.getText();
					if (email.length() == 0) {
						ps.setString(5, null);
					} else {
						ps.setString(5, email);
					}
                    
					String numTemp = sinOrStNoField.getText();
					if (numTemp.length() == 0) {
						ps.setNull(6, java.sql.Types.INTEGER);
					} else {
						sinOrStNo = Integer.parseInt(numTemp);
						ps.setInt(6, sinOrStNo);
					}
					expiryDate = expiryDateField.getText();
					if (name.length() == 0) {
						ps.setString(7, null);
					} else {
						ps.setString(7, expiryDate);
					}
					type = borrowerTypeComboBox.getSelectedItem().toString();
					ps.setString(8, type);
					ps.executeUpdate();
					con.commit();
					ps.close();
					JOptionPane
                    .showMessageDialog(new JFrame(),
                                       "Successfully created borrower account for "
                                       + name, "Successful",
                                       JOptionPane.INFORMATION_MESSAGE);
					insertFrame.dispose();
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(new JFrame(),
                                                  ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
		});
        
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// row 0
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(name, c);
		c.gridx = 1;
		c.insets = new Insets(0, 5, 0, 0);
		c.gridy = 0;
		contentPane.add(nameField, c);
		// row 1
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(password, c);
		c.gridx = 1;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(passwordField, c);
		// row 2
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(address, c);
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(addressField, c);
		// row 3
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(phone, c);
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(phoneField, c);
		// row 4
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(emailAddress, c);
		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(emailAddressField, c);
		// row 5
		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(sinOrStNo, c);
		c.gridx = 1;
		c.gridy = 5;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(sinOrStNoField, c);
		// row 6
		c.gridx = 0;
		c.gridy = 6;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(expiryDate, c);
		c.gridx = 1;
		c.gridy = 6;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(expiryDateField, c);
		// row 7
		c.gridx = 0;
		c.gridy = 7;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(borrowerType, c);
		c.gridx = 1;
		c.gridy = 7;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(borrowerTypeComboBox, c);
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(25, 0, 0, 0);
		contentPane.add(createBtn, c);
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
		JButton checkBtn = new JButton("Check");
		JLabel bid = new JLabel("bid: ");
		final JTextField bidField = new JTextField(10);
        
        
        
		final JFrame insertFrame = new JFrame("Please enter your bid");
		insertFrame.setSize(300,300);
		JPanel contentPane = new JPanel();
        
		insertFrame.setContentPane(contentPane);
        
		checkBtn.addActionListener(new ActionListener() {
            
			public void actionPerformed(ActionEvent arg0){
				int bid;
				Statement stmt1;
				Statement stmt2;
				Statement stmt3;
				Statement stmt4;
				Statement stmt5;
				Statement stmt6;
                
				ResultSet rs1;
				ResultSet rs2;
				ResultSet rs3;
				ResultSet rs4;
				ResultSet rs5;
				ResultSet rs6;
				bid = Integer.parseInt(bidField.getText());
                
                
				String titles = "";
				String holds = "";
				int totalFine = 0;
				try {
					stmt1 = con.createStatement();
					System.out.println(bid);
					String temp1 = "SELECT *" +
                    "FROM Borrowing " +
                    "WHERE Borrowing_bid = '" + bid + "'";
					rs1 = stmt1.executeQuery(temp1);
                    
					System.out.println("first");
					while(rs1.next()){
						// titles
						System.out.println("processing books.....");
						String callNumber = rs1.getString("borrowing_callNumber");
						String copyNo = rs1.getString("borrowing_copyNo");
						System.out.println(callNumber);
						System.out.println(copyNo);
                        
						stmt2 = con.createStatement();
						String temp2 = "SELECT * " +
                        "FROM BookCopy " +
                        "WHERE BookCopy_callNumber = '" + callNumber + "'" +
                        "AND BookCopy_copyNo = '" + copyNo + "'";
						rs2 = stmt2.executeQuery(temp2);
						rs2.next();
						String status = rs2.getString("bookCopy_status");
						System.out.println(status);
                        
						if(status.equals("out")){
							stmt3 = con.createStatement();
							String temp3 = "SELECT * " +
                            "FROM Book " +
                            "WHERE Book_callNumber = '" + callNumber + "'";
							rs3 = stmt3.executeQuery(temp3);
							rs3.next();
							String title = rs3.getString("book_title");
							titles = titles + "\n" + title;
						}
                        
						//fines
						String borid = rs1.getString("Borrowing_borid");
						String temp4 = "SELECT * " +
                        "FROM Fine " +
                        "WHERE Fine_borid = '" + borid +"'" +
                        " AND Fine_paidDate is null";
						stmt4= con.createStatement();
						rs4 = stmt4.executeQuery(temp4);
						while(rs4.next()){
                            
							int fine = Integer.parseInt(rs4.getString("fine_amount"));
							totalFine = totalFine + fine;
                            
						}
						
                    }
                    
                    
					//HoldRequests
					String temp5 = "SELECT * " +
                    "FROM HoldRequest " +
                    "WHERE HoldRequest_bid = '" + bid + "'";
                    
					stmt5 = con.createStatement();
					rs5 = stmt5.executeQuery(temp5);
					while(rs5.next()){
						String callNumber1 = rs5.getString("HoldRequest_callNumber");
						String issuedDate = rs5.getString("holdRequest_issuedDate");
						System.out.println(callNumber1);
						System.out.println(issuedDate);
						String temp6 = "SELECT * " +
                        "FROM Book " +
                        "WHERE Book_callNumber = '" + callNumber1 + "'";
						stmt6 = con.createStatement();
						rs6 = stmt6.executeQuery(temp6);
						rs6.next();
						String holdTitle = rs6.getString("Book_title");
						System.out.println(holdTitle);
						String holdTitleAndIssuedDate = holdTitle + " " + issuedDate;
						System.out.println(holdTitleAndIssuedDate);
						holds = holds + "\n" + holdTitleAndIssuedDate;
						System.out.println(holds);
                        
					}
                    
					final JFrame infoFrame = new JFrame("Information of your account");
					infoFrame.setSize(500,500);
					JPanel contentPane = new JPanel();
					JTextArea text = new JTextArea();
					text.setText("Books not returned:" + titles + "\n" +
                                 "Total fines: " + totalFine +
                                 "\nHold Request made: " + holds + "\n");
					contentPane.add(text);
					infoFrame.setContentPane(contentPane);
					infoFrame.setVisible(true);
                    
                    
                    
                    
                    
                    
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                
                
                
                
			}
		});
        
		GridBagConstraints c = new GridBagConstraints();
		contentPane.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(bid, c);
		c.gridx = 1;
		c.insets = new Insets(0,5,0,0);
		contentPane.add(bidField, c);
        
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 2;
        
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(25, 0, 0, 0);
		contentPane.add(checkBtn, c);
		insertFrame.setVisible(true);
        
	}
}
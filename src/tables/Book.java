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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Book extends AbstractDatabaseTable {

	public Book(Connection con) {
		super(con);
	}

	@Override
	public void defineTableIdentity() {
		tableName = "Book";
		tableIndex = BOOK_INDEX;

	}

	@Override
	public void insert() {
		JLabel callNo = new JLabel("Call #: ");
		JLabel isbn = new JLabel("ISBN: ");
		JLabel title = new JLabel("Book title: ");
		JLabel mainAuth = new JLabel("Main author: ");
		JLabel publisher = new JLabel("Publisher: ");
		JLabel year = new JLabel("Year: ");
		JLabel allAuth = new JLabel("Name of all authors: ");
		JLabel subject = new JLabel("Subject: ");

		final JTextField callNoField = new JTextField(10);
		final JTextField isbnField = new JTextField(10);
		final JTextField titleField = new JTextField(10);
		final JTextField mainAuthField = new JTextField(10);
		final JTextField publisherField = new JTextField(10);
		final JTextField yearField = new JTextField(10);
		final JTextField allAuthField = new JTextField(10);
		final JTextField subjectField = new JTextField(10);

		JButton createBtn = new JButton("create");
		final JFrame insertFrame = new JFrame(
				"insert a new row into BOOK, HASAUTHOR, HASSUBJECT.. ");
		insertFrame.setSize(300, 400);
		JPanel contentPane = new JPanel();
		insertFrame.setContentPane(contentPane);

		createBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String callNumber;
				String isbn;
				String title;
				String mainAuthor;
				String publisher;
				int year;
				String subject;
				String name;
				PreparedStatement ps;
				PreparedStatement ps2;
				PreparedStatement ps3;
				PreparedStatement ps4;

				try {
					ps = con.prepareStatement("INSERT INTO book VALUES (?,?,?,?,?,?)");
					callNumber = callNoField.getText();
					ps.setString(1, callNumber);

					isbn = isbnField.getText();
					if (isbn.length() == 0) {
						ps.setString(2, null);
					} else {
						ps.setString(2, isbn);
					}

					title = titleField.getText();
					if (title.length() == 0) {
						ps.setString(3, null);
					} else {
						ps.setString(3, title);
					}

					mainAuthor = mainAuthField.getText();
					if (mainAuthor.length() == 0) {
						ps.setString(4, null);
					} else {
						ps.setString(4, mainAuthor);
					}

					publisher = publisherField.getText();
					if (publisher.length() == 0) {
						ps.setString(5, null);
					} else {
						ps.setString(5, publisher);
					}

					String yearTemp = yearField.getText();
					if (yearTemp.length() == 0) {
						ps.setNull(6, java.sql.Types.INTEGER);
					} else {
						year = Integer.parseInt(yearTemp);
						ps.setInt(6, year);
					}
					ps.executeUpdate();

					ps2 = con
							.prepareStatement("INSERT INTO bookCopy VALUES (?,1,'in')");
					ps2.setString(1, callNoField.getText());
					ps2.executeUpdate();

					ps3 = con
							.prepareStatement("INSERT INTO hasSubject VALUES (?,?)");
					ps3.setString(1, callNumber);
					subject = subjectField.getText();
					ps3.setString(2, subject);
					ps3.executeUpdate();

					ps4 = con
							.prepareStatement("INSERT INTO hasAuthor VALUES (?,?)");
					callNumber = callNoField.getText();
					ps4.setString(1, callNumber);
					name = allAuthField.getText();
					ps4.setString(2, name);

					ps4.executeUpdate();

					// commit work
					con.commit();
					ps.close();
					ps2.close();
					ps3.close();
					ps4.close();

					JOptionPane.showMessageDialog(new JFrame(),
							"Successfully created a book record for call number "
									+ callNumber, "Successful",
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
		contentPane.add(callNo, c);
		c.gridx = 1;
		c.insets = new Insets(0, 5, 0, 0);
		c.gridy = 0;
		contentPane.add(callNoField, c);
		// row 1
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(isbn, c);
		c.gridx = 1;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(isbnField, c);
		// row 2
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(title, c);
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(titleField, c);
		// row 3
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(mainAuth, c);
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(mainAuthField, c);
		// row 4
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(publisher, c);
		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(publisherField, c);
		// row 5
		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(year, c);
		c.gridx = 1;
		c.gridy = 5;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(yearField, c);
		// row 6
		c.gridx = 0;
		c.gridy = 6;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(allAuth, c);
		c.gridx = 1;
		c.gridy = 6;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(allAuthField, c);
		// row 7
		c.gridx = 0;
		c.gridy = 7;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(subject, c);
		c.gridx = 1;
		c.gridy = 7;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(subjectField, c);
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

		// TODO Auto-generated method stub

	}

	public void searchBooks() {
		// TODO Auto-generated method stub
		final JFrame insertFrame = new JFrame(
				"Let's search for some good books");
		insertFrame.setSize(400, 250);
		final JPanel contentPane = new JPanel();
		insertFrame.setContentPane(contentPane);
		JLabel titleLabel = new JLabel("Title:");
		final JTextField titleField = new JTextField(10);
		JLabel authorLabel = new JLabel("Author:");
		final JTextField authorField = new JTextField(10);
		JLabel subjectLabel = new JLabel("Subject:");
		final JTextField subjectField = new JTextField(10);

		JButton searchBtn = new JButton("search");

		searchBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame tableFrame = new JFrame("Result");
				tableFrame.setSize(600, 400);
				String[][] resultString = new String[10][3];
				JScrollPane scrollPane;
				String columnNames[] = { "Book title", "inCount", "outCount" };

				String author = authorField.getText();
				if (author.length() != 0)
					author = "%" + author + "%";
				String title = titleField.getText();
				if (title.length() != 0)
					title = "%" + title + "%";
				String subject = subjectField.getText();
				if (subject.length() != 0)
					subject = "%" + subject + "%";

				String temp = "SELECT distinct (book_callnumber) FROM Book, HasAuthor, HasSubject "
						+ "Where (HasSubject_subject LIKE '"
						+ subject
						+ "' OR HasAuthor_name like '"
						+ author
						+ "' OR book_title like '"
						+ title
						+ "') AND HasAuthor_callnumber = HasSubject_callnumber AND HasSubject_callnumber= Book_callnumber";

				String temp1 = "SELECT distinct (book_title) FROM Book, HasAuthor, HasSubject "
						+ "Where (HasSubject_subject LIKE '"
						+ subject
						+ "' OR HasAuthor_name like '"
						+ author
						+ "' OR book_title like '"
						+ title
						+ "') AND HasAuthor_callnumber = HasSubject_callnumber AND HasSubject_callnumber= Book_callnumber";

				System.out.println(temp);

				try {
					Statement stmt = con.createStatement();
					Statement stmt1 = con.createStatement();
					Statement stmt2 = con.createStatement();
					ResultSet rs;
					ResultSet rs8;
					rs = stmt.executeQuery(temp);
					rs8 = stmt.executeQuery(temp1);

					String titleName = "";
					String inCount = "";
					String outCount = "";
					int index = 0;

					while (rs.next() && index < 10) {
						String callNumber = rs.getString("book_callnumber");

						System.out.println(callNumber);

						titleName = rs8.getString("book_title");
						resultString[index][0] = titleName;
						System.out.println(titleName);
						ResultSet rs1;
						ResultSet rs2;
						String inTemp = "SELECT COUNT(*) AS inCount FROM BOOKCOPY WHERE bookcopy_callnumber = '"
								+ callNumber + "' AND bookcopy_status = 'in'";
						rs1 = stmt1.executeQuery(inTemp);
						String outTemp = "SELECT COUNT(*) AS outCount FROM BOOKCOPY WHERE bookcopy_callnumber = '"
								+ callNumber + "' AND bookcopy_status = 'out'";
						rs2 = stmt2.executeQuery(outTemp);

						rs1.next();
						inCount = rs1.getString(inCount);
						resultString[index][1] = inCount;
						System.out.println(inCount);
						rs2.next();
						outCount = rs2.getString(outCount);
						resultString[index][2] = outCount;
						System.out.println(outCount);
						index++;
					}
					stmt.close();
					stmt1.close();
					stmt2.close();

					JTable resultTable = new JTable(resultString, columnNames);
					scrollPane = new JScrollPane(resultTable);

					GridBagConstraints c = new GridBagConstraints();
					JPanel contentPane = new JPanel();
					tableFrame.setContentPane(contentPane);
					contentPane.setLayout(new GridBagLayout());
					c.fill = GridBagConstraints.HORIZONTAL;
					c.anchor = GridBagConstraints.CENTER;
					c.gridx = 0;
					c.gridy = 0;
					contentPane.add(scrollPane, c);
					tableFrame.pack();
					tableFrame.setVisible(true);

				}

				catch (SQLException e) {
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
		contentPane.add(titleLabel, c);
		c.gridx = 1;
		c.insets = new Insets(0, 5, 0, 0);
		contentPane.add(titleField, c);
		// row 1
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(authorLabel, c);
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 5, 0, 0);
		contentPane.add(authorField, c);
		// row 2
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(subjectLabel, c);
		c.gridx = 1;
		c.insets = new Insets(5, 5, 0, 0);
		contentPane.add(subjectField, c);
		// row 3
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(25, 0, 0, 0);
		contentPane.add(searchBtn, c);
		insertFrame.setSize(300, 250);
		insertFrame.setVisible(true);
	}
}
package operationPanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import tables.Book;
import tables.BookCopy;

public class SearchPanel extends AbstractOperationPanel {

	private JLabel lb1;
	private JButton searchBtn;

	public SearchPanel(final Connection con) {
		super(con);
		lb1 = new JLabel(
				"<html><p>Search for books using keyword search on titles, authors and subjects.</p></html> ");
		searchBtn = new JButton("search");
		searchBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Book b = new Book(con);
				b.searchBooks();
				// opFrame.dispose();
				// setDisplayed(false);
			}
		});

		GridBagConstraints c = new GridBagConstraints();
		contentPane.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.insets = new Insets(10, 10, 0, 0); // top padding
		contentPane.add(lb1, c);
		c.insets = new Insets(10, 10, 0, 0);

		c.gridy = 1;
		contentPane.add(searchBtn, c);
	}

	@Override
	public void identifyOperation() {
		setOpIndex(SEARCH_INDEX);
		setOpDescription("Search");
	}

}

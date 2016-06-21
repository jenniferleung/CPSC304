package operationPanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import tables.BookCopy;

public class AddNewBookPanel extends AbstractOperationPanel {

	private JLabel lb1;
	private JButton addBtn;
	private BookCopy bookCopy = new BookCopy(con);
	private JTextField textField;

	public AddNewBookPanel(final Connection con) {
		super(con);
		lb1 = new JLabel(
				"<html><p>Add a new copy of an existing book into the library data base."
						+ " If the given call number doens't exist, "
						+ "you will first need to create a book before adding the copy.</p></html> ");

		addBtn = new JButton("add a new book/copy");
		addBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				BookCopy bc = new BookCopy(con);
				bc.insert();
//				opFrame.dispose();
//				setDisplayed(false);
			}
		});

		GridBagConstraints c = new GridBagConstraints();
		contentPane.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(lb1, c);
		c.insets = new Insets(10, 0, 0, 0); // top padding
		c.gridy = 1;
		contentPane.add(addBtn, c);
	}

	@Override
	public void identifyOperation() {
		// TODO Auto-generated method stub
		setOpIndex(ADDNEWBOOK_INDEX);
		setOpDescription("Add new book");
	}

}

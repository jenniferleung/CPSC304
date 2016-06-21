package operationPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.ParseException;

import javax.swing.JButton;

import tables.BookCopy;

public class ShowCheckedOutBooksPanel extends AbstractOperationPanel {

	private JButton btn;

	public ShowCheckedOutBooksPanel(final Connection con) {
		super(con);
		btn = new JButton("list checked out books");
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				BookCopy bc = new BookCopy(con);
				try {
					bc.showCheckedOutItems();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		contentPane.add(btn);
	}

	@Override
	public void identifyOperation() {
		setOpIndex(SHOWCHECKEDOUTBOOKS_INDEX);
		setOpDescription("List checked out books");
	}

}

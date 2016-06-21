package operationPanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import tables.BookCopy;
import tables.Borrower;
import tables.Fine;
import tables.HoldRequest;

import client.MainPanel;

public class PlaceHoldPanel extends AbstractOperationPanel {

	private JLabel lb1;
	private JButton startBtn;
	
	public PlaceHoldPanel(final Connection con) {
		super(con);
		lb1 = new JLabel(
				"<html><p>You can only place a hold request for a book where all its copies are currently "
						+ "not in the library.</p></html>");

		startBtn = new JButton("Place a hold request");
		startBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				HoldRequest hr= new HoldRequest(con);
				hr.insert();
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
		contentPane.add(startBtn, c);
	}
	@Override
	public void identifyOperation() {
		setOpIndex(PLACEHOLD_INDEX);
		setOpDescription("Place a hold request");		
	}

}

package operationPanels;

import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;

import tables.Borrower;
import tables.Borrowing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ViewAccountPanel extends AbstractOperationPanel {
	private JButton checkBtn;
	private JLabel lb1;
	public ViewAccountPanel(final Connection con) {
		super(con);
		lb1 = new JLabel(
				"<html><p>Please have your bid (BorrowerID) ready before you check your account.</p></html> ");
		checkBtn = new JButton("Check Account");
		checkBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Borrower borrower = new Borrower(con);
				borrower.showTable();

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
		contentPane.add(checkBtn, c);
		
	}

	@Override
	public void identifyOperation() {

		setOpIndex(VIEWACCOUNTPANEL_INDEX);
		setOpDescription("View my account");
	}

}

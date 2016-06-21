package operationPanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import tables.Borrower;

public class AddNewBorrowerPanel extends AbstractOperationPanel {

	private JLabel lb1;
	private JLabel lb2;
	private JButton startBtn;
	private Borrower borrower = new Borrower(con);

	// private JPanel reportPanel;
	// private String reportString;
	// private JTextArea reportTextArea;

	public AddNewBorrowerPanel(final Connection con) {
		super(con);
		lb1 = new JLabel(
				"<html><p>In order to add a new borrower, you need to have his/her name, password, "
						+ "address, phone, email address, and sin/student number ready.</p></html>");

		lb2 = new JLabel("New Borrower:");
		startBtn = new JButton("add a new Borrower...");
		startBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Borrower borrower = new Borrower(con);
				borrower.insert();
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
		setOpIndex(ADDNEWBORROWER_INDEX);
		setOpDescription("Add a new borrower");
	}

}

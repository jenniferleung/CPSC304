package operationPanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;

import tables.Borrowing;

public class CheckOutPanel extends AbstractOperationPanel {

	private JLabel lb1;
	private JButton goBtn;

	public CheckOutPanel(Connection con) {
		super(con);

		lb1 = new JLabel(
				"<html><p>Please have your call numbers ready. "
						+ "In this program, maximum allowed check-out items is 3 at a time.</p></html>");
		goBtn = new JButton("Check out ..");
		final Borrowing borrowing = new Borrowing(con);
	
		goBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				borrowing.insert();
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
		contentPane.add(goBtn, c);

	}

	@Override
	public void identifyOperation() {
		setOpIndex(CHECKOUT_INDEX);
		setOpDescription("Check out");
	}

}

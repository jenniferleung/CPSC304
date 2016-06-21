package operationPanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import tables.Fine;

public class PayFinePanel extends AbstractOperationPanel {

	private JLabel lb1;
	private JTextField bidField;
	private JButton luBtn;
	Fine fine = new Fine(con);

	public PayFinePanel(Connection con) {
		super(con);
		lb1 = new JLabel("<html><p>Enter your bid.</p></html>");
		luBtn = new JButton("Lookup outstanding fine");
		bidField = new JTextField(10);
		luBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] result = fine.searchByBid(bidField.getText());
				int fid = result[0];
				int amount = result[1];
				if (fid == -1) {
					opFrame.dispose();
					setDisplayed(false);
				} else {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane
							.showConfirmDialog(
									opFrame,
									"You have an outstanding fine of "
											+ +amount
											+ "dollars. The fid is "
											+ fid
											+ ". Would You Like to pay for this fine now?",
									"Continue?", dialogButton);
					// execute upon "YES"
					if (dialogResult == JOptionPane.YES_OPTION) {
						fine.update();
					}
				}
			}
		});

		GridBagConstraints c = new GridBagConstraints();
		contentPane.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(lb1, c);
		c.gridy = 1;
		c.insets = new Insets(5, 0, 0, 0);
		contentPane.add(bidField, c);

		c.insets = new Insets(10, 0, 0, 0); // top padding
		c.gridy = 2;
		contentPane.add(luBtn, c);
	}

	@Override
	public void identifyOperation() {
		setOpIndex(PAYFINE_INDEX);
		setOpDescription("Pay fine");
	}

}

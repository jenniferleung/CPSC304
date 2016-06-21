package operationPanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;

import tables.Borrowing;

public class ShowOverDueItemPanel extends AbstractOperationPanel {
	private JButton startBtn;
	private JLabel lb1;

	public ShowOverDueItemPanel(final Connection con) {
		super(con);

		lb1 = new JLabel("List all overdue items in database");
		startBtn = new JButton("continue");
		startBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Borrowing borrowering = new Borrowing(con);
				borrowering.showTable();

			}
		});
		GridBagConstraints c = new GridBagConstraints();
		contentPane.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(lb1, c);
		c.gridx = 0;
		c.gridy = 1;
		contentPane.add(startBtn, c);
	}

	@Override
	public void identifyOperation() {
		setOpIndex(SHOWOVERDUEITEM_INDEX);
		setOpDescription("List overdue items");
	}

}

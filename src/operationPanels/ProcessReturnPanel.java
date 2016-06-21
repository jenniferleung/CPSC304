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

public class ProcessReturnPanel extends AbstractOperationPanel {

	private JLabel lb1;
	private JButton btn;

	public ProcessReturnPanel(final Connection con) {
		super(con);

		lb1 = new JLabel("Process a return.");
		btn = new JButton("go!");
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Borrowing b = new Borrowing(con);
				b.update();

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
		contentPane.add(btn, c);
	}

	@Override
	public void identifyOperation() {
		setOpIndex(PROCESSRETURN_INDEX);
		setOpDescription("Process return");
	}

}

package operationPanels;

import java.sql.Connection;

import javax.swing.JPanel;

import client.MainPanel;

public class ShowPopularBookPanel extends AbstractOperationPanel {

	public ShowPopularBookPanel(Connection con) {
		super(con);
	}

	@Override
	public void identifyOperation() {
		setOpIndex(SHOWPOPULARBOOK_INDEX);
		setOpDescription("List most popular books of the year");
	}

}

package operationPanels;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class AbstractOperationPanel extends JPanel {

	protected Connection con;
	// index value of the JList diplayed on UI
	private int opIndex;
	private boolean isDisplayed;
	private String opDescription;

	protected final int ADDNEWBOOK_INDEX = 0;
	protected final int SHOWCHECKEDOUTBOOKS_INDEX = 1;
	protected final int SHOWPOPULARBOOK_INDEX = 2;

	protected final int VIEWACCOUNTPANEL_INDEX = 0;
	protected final int SEARCH_INDEX = 1;
	protected final int PLACEHOLD_INDEX = 2;
	protected final int PAYFINE_INDEX = 3;

	protected final int ADDNEWBORROWER_INDEX = 0;
	protected final int CHECKOUT_INDEX = 1;
	protected final int PROCESSRETURN_INDEX = 2;
	protected final int SHOWOVERDUEITEM_INDEX = 3;

	protected JFrame opFrame;
	protected JPanel contentPane;

	public AbstractOperationPanel(Connection con) {
		identifyOperation();
		initComponents();
		this.con = con;
		setDisplayed(false);
	}

	public abstract void identifyOperation();

	public void initComponents() {

		opFrame = new JFrame(getOpDescription());
		opFrame.setSize(300, 250);
		opFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				setDisplayed(false);
			}
		});
		contentPane = new JPanel();
		opFrame.setContentPane(contentPane);

	}

	public boolean isDisplayed() {
		return isDisplayed;
	}

	public void setDisplayed(boolean isDisplayed) {
		this.isDisplayed = isDisplayed;
	}

	public int getOpIndex() {
		return opIndex;
	}

	public void setOpIndex(int opIndex) {
		this.opIndex = opIndex;
	}

	public void displayOperationPanel() {
		opFrame.setVisible(true);
		setDisplayed(true);
	}

	public String getOpDescription() {
		return opDescription;
	}

	public void setOpDescription(String opDescription) {
		this.opDescription = opDescription;
	}
}

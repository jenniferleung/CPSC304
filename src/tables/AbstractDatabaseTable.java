package tables;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;

public abstract class AbstractDatabaseTable extends JPanel {

	protected final int BOOK_INDEX = 0;
	protected final int BOOKCOPY_INDEX = 1;
	protected final int BORROWER_INDEX = 2;
	protected final int BORROWERTYPE_INDEX = 3;
	protected final int BORROWING_INDEX = 4;
	protected final int FINE_INDEX = 5;
	protected final int HASAUTHOR_INDEX = 6;
	protected final int HASSUBJECT_INDEX = 7;
	protected final int HOLDREQUEST_INDEX = 8;

	protected final int INSERT_OP_INDEX = 10;
	protected final int UPDATE_OP_INDEX = 11;
	protected final int DELETE_OP_INDEX = 12;
	protected final int SHOW_TB_INDEX = 13;

	protected int tableIndex;
	protected String tableName;
	protected Connection con;
	protected Collection<String> param;

	protected JFrame tableFrame;
	protected JPanel contentPane;
	protected JMenuBar menubar;
	protected JMenu opMenu;
	protected JMenuItem insertMenuItem;
	protected JMenuItem deleteMenuItem;
	protected JMenuItem updateMenuItem;
	protected JTable dataJTable;
	private boolean isDisplayed;

	public AbstractDatabaseTable(Connection con) {
		defineTableIdentity();
		this.con = con;
		setDisplayed(false);
		initComponents();
	}

	/**
	 * 
	 */
	public void initComponents() {
		tableFrame = new JFrame(this.tableName);
		contentPane = new JPanel();
		tableFrame.setContentPane(contentPane);
		menubar = new JMenuBar();
		opMenu = new JMenu("operation");

		insertMenuItem = new JMenuItem("insert new tuple");
		insertMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				insert();
			}
		});
		updateMenuItem = new JMenuItem("update tuple");
		updateMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		deleteMenuItem = new JMenuItem("delete tuple");
		deleteMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});

		opMenu.add(insertMenuItem);
		opMenu.add(updateMenuItem);
		opMenu.add(deleteMenuItem);
		menubar.add(opMenu);
		tableFrame.setJMenuBar(menubar);
		tableFrame.setSize(350, 500);
		tableFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setDisplayed(false);
			}

			@Override
			public void windowOpened(WindowEvent e) {
				showTable();
			}
		});

		Dimension d = tableFrame.getToolkit().getScreenSize();
		Rectangle r = tableFrame.getBounds();
		tableFrame.setLocation((d.width - r.width) / 2,
				(d.height - r.height) / 2);
	}

	public String getName() {
		return tableName;
	}

	public int getTableIndex() {
		return tableIndex;
	}

	public abstract void defineTableIdentity();

	public abstract void insert();

	public abstract void update();

	public abstract void delete();

	public abstract void showTable();

	public void displayTablePanel() {
		tableFrame.setVisible(true);
		isDisplayed = true;
	}

	public boolean isDisplayed() {
		return isDisplayed;
	}

	public void setDisplayed(boolean isDisplayed) {
		this.isDisplayed = isDisplayed;
	}

}

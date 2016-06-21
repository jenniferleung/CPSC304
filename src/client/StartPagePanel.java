package client;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * 
 * @author dongningli
 */
@SuppressWarnings("serial")
public class StartPagePanel extends JPanel {

	// Variables declaration - do not modify
	private JFrame mainFrame;
	private JMenuBar mainMenuBar;
	private JPanel contentPane;

	// user type menu
	private JMenu userTypeMenu;
	private JMenuItem usrt_ClerkMItem;
	private JMenuItem usrt_LibrarianMItem;
	private JMenuItem usrt_borrowerMItem;

	private JMenu quitMenu;
	private JMenuItem quitMenuItem;

	private MainPanel mainPanel;

	/**
	 * Creates new MainPanel
	 */
	public StartPagePanel(MainPanel mp) {
		this.mainPanel = mp;
		initComponents();

		quitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				System.exit(0);
			}
		});
	}

	/**
	 * initialize all the conponents of the main panel
	 */
	private void initComponents() {

		mainFrame = new JFrame("Library Database");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainMenuBar = new JMenuBar();
		contentPane = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		contentPane.setLayout(gb);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainFrame.getContentPane().add(mainPanel);

		// quit menu
		quitMenu = new JMenu("Library Database");
		quitMenuItem = new JMenuItem("Quit database");

		quitMenu.add(quitMenuItem);
		// user type menu
		usrt_borrowerMItem = new JMenuItem("Borrower");

		usrt_borrowerMItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.setCurrentUser(mainPanel.getBORROWER_INDEX());
				// mainPanel.showSelectTablePanel();
				mainPanel.showSelectTransPanel();
			}

		});

		usrt_ClerkMItem = new JMenuItem("Clerk");
		usrt_ClerkMItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.setCurrentUser(mainPanel.getCLERK_INDEX());
				// mainPanel.showSelectTablePanel();
				mainPanel.showSelectTransPanel();

			}

		});

		usrt_LibrarianMItem = new JMenuItem("Librarian");
		usrt_LibrarianMItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mainPanel.setCurrentUser(mainPanel.getLIBR_INDEX());
				// mainPanel.showSelectTablePanel();
				mainPanel.showSelectTransPanel();

			}

		});

		userTypeMenu = new JMenu("User type");
		JMenuItem[] userTypeMItems = { usrt_borrowerMItem, usrt_ClerkMItem,
				usrt_LibrarianMItem };

		for (JMenuItem e : userTypeMItems) {
			userTypeMenu.add(e);
		}
		contentPane.add(mainPanel);

		// assemble
		mainFrame.setSize(650, 300);
		mainFrame.setContentPane(contentPane);
		mainMenuBar.add(quitMenu);

		mainMenuBar.add(userTypeMenu);
		mainFrame.setJMenuBar(mainMenuBar);
		// mainMenuBar.add(tableMenu);

		// center the frame
		Dimension d = mainFrame.getToolkit().getScreenSize();
		Rectangle r = mainFrame.getBounds();
		mainFrame.setLocation((d.width - r.width) / 2,
				(d.height - r.height) / 2);
	}

	@Override
	public void setVisible(boolean b) {
		mainFrame.setVisible(b);
	}

}

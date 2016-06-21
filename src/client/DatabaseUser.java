package client;

import java.sql.Connection;
import java.util.Collection;
import java.util.LinkedList;

import operationPanels.AbstractOperationPanel;
import operationPanels.AddNewBookPanel;
import operationPanels.AddNewBorrowerPanel;
import operationPanels.CheckOutPanel;
import operationPanels.PayFinePanel;
import operationPanels.PlaceHoldPanel;
import operationPanels.ProcessReturnPanel;
import operationPanels.SearchPanel;
import operationPanels.ShowCheckedOutBooksPanel;
import operationPanels.ShowOverDueItemPanel;
import operationPanels.ShowPopularBookPanel;
import operationPanels.ViewAccountPanel;

import tables.Book;
import tables.BookCopy;
import tables.Borrower;
import tables.BorrowerType;
import tables.Borrowing;
import tables.AbstractDatabaseTable;
import tables.Fine;
import tables.HasAuthor;
import tables.HasSubject;
import tables.HoldRequest;

/**
 * Database user class. One of such object is created upon user selecting user
 * type from the UI for the first time, and user type will change dynamically.
 * It also specifies which database tables are authorized to be viewed by a
 * given user type
 *
 * @author dongningli
 *
 */
public class DatabaseUser {

	private static final String[] TRANSSTRING_LIBR = { "add new book or copy",
			"show checked out books", "show most popular books of the year" };
	private static final String[] TRANSSTRING_BORROWER = { "view my account",
			"search", "place hold request", "pay fine" };
	private static final String[] TRANSSTRING_CLERK = { "Add new borrower",
			"check-out ", "process return", "list overdue items" };

	public static final int VIEW_TABLE = 13;
	private Connection con;
	private int userType;
	private final int BORROWER = 0;
	private final int CLERK = 1;
	private final int LIBR = 2;

	private String userTypeString;
	private final String BORROWER_STRING = "Borrower";
	private final String CLERK_STRING = "Clerk";
	private final String LIBRARIAN_STRING = "Librarian";

	private Collection<AbstractDatabaseTable> viewableTables;

	private Collection<AbstractOperationPanel> authorizedTransactions;

	public DatabaseUser(int userType, Connection con) {
		viewableTables = new LinkedList<AbstractDatabaseTable>();
		authorizedTransactions = new LinkedList<AbstractOperationPanel>();
		this.setUserType(userType);
		this.con = con;
		setViewableTbls(userType);
		setAuthorizedTransactions(userType);

	}

	private void setViewableTbls(int userType) {
		// Defines viewable tables for Librarian
		if (userType == LIBR) {
			viewableTables = new LinkedList<AbstractDatabaseTable>();
			viewableTables.add(new Book(con));
			viewableTables.add(new BookCopy(con));
			viewableTables.add(new Borrowing(con));
			viewableTables.add(new HasAuthor(con));
			viewableTables.add(new HasSubject(con));
		}

		// Defines viewable tables for Clerk
		else if (userType == CLERK) {
			viewableTables = new LinkedList<AbstractDatabaseTable>();
			viewableTables.add(new Book(con));
			viewableTables.add(new BookCopy(con));
			viewableTables.add(new Borrower(con));
			viewableTables.add(new BorrowerType(con));
			viewableTables.add(new Borrowing(con));
			viewableTables.add(new Fine(con));
			viewableTables.add(new HasAuthor(con));
			viewableTables.add(new HasSubject(con));
			viewableTables.add(new HoldRequest(con));
		}

		// Defines viewable tables for borrower
		else if (userType == BORROWER) {
			viewableTables = new LinkedList<AbstractDatabaseTable>();
			viewableTables.add(new Book(con));
			viewableTables.add(new BookCopy(con));
			viewableTables.add(new HasAuthor(con));
			viewableTables.add(new HasSubject(con));
			// viewableTables.add(new HoldRequest(con));
		} else {
			System.out.println("User type undefined");
		}
	}

	public Collection<AbstractDatabaseTable> getViewableTables() {
		return viewableTables;
	}

	/**
	 * checks whether this user can view table t.
	 *
	 * @param t
	 * @return
	 */
	public boolean canViewTable(AbstractDatabaseTable t) {
		return viewableTables.contains(t) ? true : false;

	}

	public int getUserType() {
		return userType;
	}

	/**
	 * set the user type and also update the viewable table
	 *
	 * @param userType2
	 * @throws IllegalArgumentException
	 */
	public void setUserType(int userType2) throws IllegalArgumentException {
		if (!(userType2 == BORROWER) && userType2 == CLERK && userType2 == LIBR) {
			throw new IllegalArgumentException(
					"Invalid userType.Type should be one of Borrower, Clerk, and Library.");
		} else {
			this.userType = userType2;

			switch (userType) {
			case BORROWER:
				setUserTypeString(BORROWER_STRING);
				break;
			case CLERK:
				setUserTypeString(CLERK_STRING);
				break;
			case LIBR:
				setUserTypeString(LIBRARIAN_STRING);
				break;
			}
			setViewableTbls(userType2);
		}
	}

	public String getUserTypeToString() {
		return userTypeString;
	}

	public void setUserTypeString(String userTypeString) {
		this.userTypeString = userTypeString;
	}

	public Collection<AbstractOperationPanel> getAuthorizedTransactions() {
		return authorizedTransactions;
	}

	public void setAuthorizedTransactions(int userType) {
		if (userType == BORROWER) {
			authorizedTransactions.add(new ViewAccountPanel(con));
			authorizedTransactions.add(new SearchPanel(con));
			authorizedTransactions.add(new PlaceHoldPanel(con));
			authorizedTransactions.add(new PayFinePanel(con));
		}

		else if (userType == CLERK) {
			authorizedTransactions.add(new AddNewBorrowerPanel(con));
			authorizedTransactions.add(new CheckOutPanel(con));
			authorizedTransactions.add(new ProcessReturnPanel(con));
			authorizedTransactions.add(new ShowOverDueItemPanel(con));
		}

		else if (userType == LIBR) {
			authorizedTransactions.add(new ShowPopularBookPanel(con));
			authorizedTransactions.add(new AddNewBookPanel(con));
			authorizedTransactions.add(new ShowCheckedOutBooksPanel(con));
		}
	}

	public String[] getTransString() {
		String[] returnString = {};
		if (userType == CLERK) {
			returnString = TRANSSTRING_CLERK;
		} else if (userType == BORROWER) {
			returnString = TRANSSTRING_BORROWER;
		} else {
			returnString = TRANSSTRING_LIBR;
		}
		return returnString;
	}
}

package tables;

//We need to import the java.sql package to use JDBC
import java.sql.*;

//for reading from the command line
import java.io.*;

//for the login window
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * 
 */
public class HasSubject extends AbstractDatabaseTable {

	public HasSubject(Connection con) {
		super(con);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void defineTableIdentity() {
		tableName = "HasSubject";
		tableIndex = HASSUBJECT_INDEX;
	}

	@Override
	public void insert() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showTable() {

		// TODO Auto-generated method stub

	}
}
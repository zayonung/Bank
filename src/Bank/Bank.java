package Bank;

import java.sql.SQLException;

public interface Bank {
	public void createUser();
	public int loginUser() throws SQLException;
	public void findId();
	
	
	Account createAccount();
	boolean deposit();
	boolean withDraw();

	


}
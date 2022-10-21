package Bank;

public class Account {
	private String accountNo;
	private int ac_pw;
	private String user_id;
	private static String ac_name;
	private int balance;
	
		public Account(String accountNo, int ac_pw, String user_id, String ac_name, int balance) {
		super();
		this.accountNo = accountNo;
		this.ac_pw = ac_pw;
		this.user_id = user_id;
		this.ac_name = ac_name;
		this.balance = balance;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public int getAc_pw() {
		return ac_pw;
	}

	public void setAc_pw(int ac_pw) {
		this.ac_pw = ac_pw;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public static String getAc_name() {
		return ac_name;
	}

	public void setAc_name(String ac_name) {
		this.ac_name = ac_name;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

}

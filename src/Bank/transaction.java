package Bank;

public class transaction {
	private String accountNo;
	private String reAccountNo;
	private String transType; // 입금 or 출금	
	private int amount; // 거래금액
	private int lastbalance;
	private String transactionTime; // 거래시간	
	
	public transaction(String accountNo, String reAccountNo, String transType, int amount, int lastbalance,
			String transactionTime) {
		super();
		this.accountNo = accountNo;
		this.reAccountNo = reAccountNo;
		this.transType = transType;
		this.amount = amount;
		this.lastbalance = lastbalance;
		this.transactionTime = transactionTime;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getReAccountNo() {
		return reAccountNo;
	}
	public void setReAccountNo(String reAccountNo) {
		this.reAccountNo = reAccountNo;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getLastbalance() {
		return lastbalance;
	}
	public void setLastbalance(int lastbalance) {
		this.lastbalance = lastbalance;
	}
	public String getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	
}

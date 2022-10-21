package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BankApp implements Bank {
//	static Account[] accountArray = new Account[100];
	static List<Account> accountArray;
	static Account account;
	static int num = 0;
	static List<UserInfo> userArr;
	public static UserInfo userInfo = new UserInfo();
	static List<transaction> transArr;
	static transaction transaction;
	private static Scanner sc = new Scanner(System.in);
	private Connection conn;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	
	
	public BankApp() {
		accountArray = new ArrayList<>();
		userArr = new ArrayList<>();
		transArr = new ArrayList<>();
		
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/practice", "root", "admin");
			}catch( ClassNotFoundException | SQLException e)
				{ e.printStackTrace();}
			}

	public static void main(String[] args) throws SQLException {
		BankApp bankApp = new BankApp();
		boolean run = true;
				
		do {
			System.out.println("\t ######  ### ###  ######    #####   ##  ###  ### ###  \r\n"
					+ "\t   ###   ### ###  ### ###  ### ###  ### ###  ### ###  \r\n"
					+ "\t   ###   ### ###  ### ###  ### ###  #######  ### ##   \r\n"
					+ "\t   ###    #####   ######   #######  #######  #####    \r\n"
					+ "\t   ###     ###    ### ###  ### ###  ### ###  ### ##   \r\n"
					+ "\t## ###     ###    ### ###  ### ###  ### ###  ### ###  \r\n"
					+ "\t #####     ###    ######   ### ###  ### ###  ### ###  ");
			
			try {
				System.out.println("---------------------------------------------------------------------------------- " );
				System.out.println("\t\t1.회원가입 | 2.로그인 | 3. ID찾기 | 4. PW변경 | 5. BANK 종료");
				System.out.println("---------------------------------------------------------------------------------- " );
				System.out.print("선택>");
	
				int select = sc.nextInt();
				if (select == 1) {
					bankApp.createUser();
				} else if (select == 2) {
					 int result = bankApp.loginUser();
					while(result == 0) {	
						bankApp.loginUser();
					};
						if (result == 1) {
							while (run) {
								System.out.println("================================================================================= " );
								System.out.println("\t1.계좌 생성 | 2.MY 계좌 조회 | 3.입금 | 4.출금 | 5.계좌 송금 | 6.거래 내역 | 7. 종료");
								System.out.println("---------------------------------------------------------------------------------- " );
								System.out.print("선택>");
								int selectNo = sc.nextInt();
								switch (selectNo) {
								case 1:
									bankApp.createAccount(); break;
								case 2:
									bankApp.chkAccount();	break;
								case 3:
									bankApp.deposit();	break;
								case 4:
									bankApp.withDraw();	break;
								case 5:
									bankApp.transfer();	break;
								case 6:
									bankApp.transferList();	break;
								case 7:
									System.out.println("Bank 어플이 종료됩니다.");
									run = false;	break;
								}
							}
							}else if(result == 2) {
								while(run) {
									System.out.println("================================================================================== " );
									System.out.println("\t\t\t\t관리자 메뉴 " );
									System.out.println("---------------------------------------------------------------------------------- " );
									System.out.println("\t\t1.전체 회원 조회 | 2.전체 계좌 조회 | 3. 계좌 삭제 | 4. BANK 종료 ");
									System.out.println("---------------------------------------------------------------------------------- " );
									System.out.print("선택>"); 
									  int selectNo = sc.nextInt(); 
									  switch (selectNo) {
									  	  case 1:
											  bankApp.allUserList(); 
											  break; 
										  case 2:
											  bankApp.allAccountList(); 
											  break; 
										  case 3: 
											  bankApp.deleteInfo(); 
											  break;
										  case 4: 
											  System.out.println("Bank 어플이 종료됩니다."); 
											  run = false; 
											  break;
									  	}
									}
							}
					
				} else if (select == 3) {
					bankApp.findId();
	
				} else if (select == 4) {
					bankApp.findPw();
	
				} else if (select == 5) {
					run = false;
					System.out.println("Bank 어플이 종료됩니다.");
				} else {
					System.out.println(" 메뉴를 다시 골라주세요 ");
					run = true;
				}
			 } catch(InputMismatchException e) {
		         System.out.println("숫자를 입력하지 않았습니다 숫자를 입력해주세요 " + e.getMessage());
		         int run1 = sc.nextInt();
				//다시 돌아가기
		    } catch(Exception e) {
		         System.out.println("오류 " + e.getMessage());
	
				//다시 돌아가기
		}
		}while (run);	  
	
	}

	public void createUser() {
		// 회원가입
		System.out.println("================================================================================== " );
		System.out.println("\t\t\t\t회원가입");
		System.out.println("---------------------------------------------------------------------------------- " );
		
		try {
			System.out.println("아이디를 입력하세요");
			String id = sc.next();
			
			System.out.println("비밀번호를 입력하세요");
			String pw = sc.next();

			System.out.println("닉네임을 입력하세요");
			String name = sc.next();
						
			pstmt = conn.prepareStatement("insert into userinfo values(?, ?, ?, now())");
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			int result = pstmt.executeUpdate();
			String msg = result > -1 ? "회원가입이 완료되었습니다." : "회원가입이 실패했습니다";
			System.out.println(msg);
			}catch(SQLException e) {
				System.out.println("ID가 중복되었습니다.");
			}finally {
				try {
					if(pstmt != null) pstmt.close();
				}catch(SQLException e) {}
			}
		}


	public int loginUser() throws SQLException {

		System.out.println("================================================================================== " );
		System.out.println("\t\t\t\t로그인");
		System.out.println("---------------------------------------------------------------------------------- " );
		
		System.out.println("아이디를 입력하세요");
		System.out.print("ID  : ");
		String id = sc.next();
	
		System.out.println("비밀번호를 입력하세요");
		System.out.print("PW  : ");
		String password = sc.next();
		
		try {
			String sql = "SELECT * FROM USERINFO WHERE USER_ID=? AND USER_PW=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(id.equals("admin") ){
					System.out.println("관리자님 로그인 되었습니다.");
					return 2;
					
				}else{
					System.out.println(rs.getString("user_name") + " 님 로그인 되었습니다.");
					userInfo.setUser_id(rs.getString(1));
					userInfo.setUser_pw(rs.getString(2));
					userInfo.setUser_name(rs.getString(3));
					userInfo.setUser_time(rs.getString(4));
					return 1;
				}
			}else {
				System.out.println("로그인 정보가 옳지 않습니다. 다시 확인 해주세요");
				return 0;
			}
			}catch ( Exception e ) {
				e.printStackTrace();
			}finally{
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
				}catch(SQLException e) {}
			}
			return 0;
	
		} 
	
		public void findId() {

			System.out.println("================================================================================== " );
		System.out.println("\t\t\t\tID 찾기");
		System.out.println("---------------------------------------------------------------------------------- " );
		System.out.println("이름을 입력하세요");
		System.out.print("이름 : ");
		String findId = sc.next();
			try {
				String sql = "SELECT USER_ID FROM USERINFO WHERE USER_NAME = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, findId);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					System.out.println("고객님의 ID 는 " + rs.getString("user_id") + "입니다");
					System.out.println();
				}else {
					System.out.println("입력하신 정보는 존재하지 않습니다.");
				return;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(pstmt != null ) pstmt.close();
					if(rs != null) rs.close();
			}catch(Exception e) {}; 
			}
		}

	public void findPw() {
		String sql = null;

		System.out.println("================================================================================== " );
		System.out.println("\t\t\t\tPassword 변경");
		System.out.println("---------------------------------------------------------------------------------- " );
		System.out.println("ID를 입력하세요");
		System.out.print("ID : ");
		String findPwId= sc.next();
		System.out.println("이름를 입력하세요");
		System.out.print("이름 : ");
		String findPwName = sc.next();
			try {
				sql = "SELECT USER_PW FROM USERINFO WHERE USER_ID = ? AND USER_NAME = ?  ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, findPwId);
				pstmt.setString(2, findPwName);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					pstmt.close();
					rs.close();
					System.out.println("바꾸실 비밀번호를 입력해주세요");
					System.out.print(">>");
					String changePw  = sc.next();
					sql = "UPDATE USERINFO SET USER_PW = '" + changePw + "'WHERE USER_ID = '" + findPwId + "'AND USER_NAME = '" + findPwName + "'" ;
					pstmt = conn.prepareStatement(sql);
					pstmt.execute();
					int result = pstmt.executeUpdate();
					String msg = result > -1 ? "비밀번호가 변경되었습니다." : "비밀번호 변경이 완료 되지 않았습니다.";
					System.out.println(msg);
					System.out.println();
				}else {
					System.out.println("입력하신 정보는 존재하지 않습니다.");
				return;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(pstmt != null ) pstmt.close();
					if(rs != null) rs.close();
			}catch(Exception e) {}; 
			}
		}
	
//--------------------------------계좌
	@Override
	public Account createAccount() {

		String sql = null;
		
		// 계좌 생성
		Random randNo = new Random();
		String accountNo = Integer.toString(randNo.nextInt(8) + 1); // 8가지 숫자 랜덤
		System.out.println("================================================================================== " );
		System.out.println("\t\t\t\t계좌 생성");
		System.out.println("---------------------------------------------------------------------------------- " );

		try {
			//계좌 생성
			String userid = userInfo.getUser_id();
			System.out.print("계좌 번호 : ");
	
			for (int i = 0; i < 7; i++) {
				accountNo += Integer.toString(randNo.nextInt(9));
			}
			for (Account element : accountArray) {
				if (element.getAccountNo().equals(accountNo)) {
					return null;
				}
			}
			System.out.println(accountNo);
	
			System.out.print("계좌 비밀번호 : ");
			String pw = sc.next();
	
			System.out.print("계좌주 (이름) : ");
			String owner = userInfo.getUser_name();
			System.out.println(owner);
	
			System.out.print("초기 입금 액 :  ");
			int balance = sc.nextInt();
			sql = "insert into account values(?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, accountNo);
			pstmt.setString(2, pw);
			pstmt.setString(3, userid);
			pstmt.setString(4, owner);
			pstmt.setInt(5, balance);
			int result = pstmt.executeUpdate();
			String msg = result > -1 ? "계좌가 생성되었습니다." : "계좌 생성이 실패하였습니다.";
			System.out.println(msg);
			pstmt.close();
			
			
			sql = "INSERT INTO TRANSACTION values(?, ?, ?, ?, ?, now())";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, accountNo);
			pstmt.setString(2, accountNo);
			pstmt.setString(3, "계좌 생성");
			pstmt.setInt(4, balance);
			pstmt.setInt(5, balance);
			pstmt.executeUpdate();
			}catch(SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					if(pstmt != null) pstmt.close();
				}catch(SQLException e) {}
			}
		return null;


	}

	public void chkAccount() {
		String sql=null;

		System.out.println("================================================================================== " );
		System.out.println("\t\t\tMY 계좌 확인");
		System.out.println("---------------------------------------------------------------------------------- " );
	
		System.out.println("아이디를 입력하세요");
		System.out.print("ID  : ");
		String id = sc.next();

		try { 
			sql = "SELECT * FROM ACCOUNT WHERE USER_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			System.out.println("---------------------------------------------------------------------------------- " );
			System.out.println("   |   ID   |   계좌 번호    |    계좌 잔액   ");
			System.out.println("---------------------------------------------------------------------------------- " );
			while(rs.next()) {
				Account ac = new Account(sql, 0, sql, sql, 0);
				ac.setAccountNo(rs.getString("AccountNo"));
				ac.setAc_pw(rs.getInt("ac_pw"));
				ac.setUser_id(rs.getString("user_id"));
				ac.setAc_name(rs.getString("ac_name"));
				ac.setBalance(rs.getInt("balance"));
				accountArray.add(ac);
				System.out.println("\t" + ac.getUser_id() + "\t" + ac.getAccountNo() + "\t" + ac.getBalance());
			}
			pstmt.close();


			
			Account ac_new = retryAccountNumber(id);	//id가 존재하는지 여부
			if(ac_new != null) {
				sql = "SELECT * FROM ACCOUNT WHERE USER_ID = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				rs=pstmt.executeQuery();
			}else {
				System.out.println("계좌가 존재하지 않습니다.");
				}
		}catch ( Exception e ) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			}catch(SQLException e) {}
		}

	}

	private Account retryAccountNumber(String id) {
		//보유 계좌 조회
		for (Account ac_new : accountArray) {
			if (ac_new.getUser_id().equals(id)) {
				return ac_new;
			}
		}
		return null;
	}

	@Override
	public boolean deposit() {
		String sql = null;
		int balance = 0;
		
		System.out.println("================================================================================== " );
		System.out.println("\t\t\t\t입금");
		System.out.println("---------------------------------------------------------------------------------- " );
		System.out.print("입금하실 계좌 번호를 입력해주세요 >>");
		String accountNo = sc.next();
		System.out.print("계좌 비밀번호를 입력해주세요 >>");
		String ac_Pw = sc.next();
		
		try { 
			//조회하기
			sql = "SELECT BALANCE FROM ACCOUNT WHERE ACCOUNTNO=? AND AC_PW=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, accountNo);
			pstmt.setString(2, ac_Pw);
			rs = pstmt.executeQuery();
			if(rs.next()==false) {
				System.out.println("정보가 없습니다.");
				return false;
			}
			balance = rs.getInt(1);	
			rs.close();	pstmt.close();
			
			System.out.print("입금하실 금액을 입력해주세요. >>");
			int money = sc.nextInt();
				if(money <= 0 ) {
					// 입금
					System.out.println("금액을 다시 입력하세요. ");
					return false;
					}
				
			sql = "UPDATE ACCOUNT SET BALANCE = ? WHERE ACCOUNTNO = " + accountNo;
			int sum = balance + money;
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sum);
			pstmt.execute();
			pstmt.close();
			
			
			sql = "INSERT INTO TRANSACTION values(?, ?, ?, ?, ?, default)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, accountNo);
			pstmt.setString(2, accountNo);
			pstmt.setString(3, "입금");
			pstmt.setInt(4, money);
			pstmt.setInt(5, sum);
			pstmt.executeUpdate();
			
			System.out.println("입금이 완료되었습니다.");
			System.out.println("잔고는 " + sum + "입니다");
			

		}catch ( Exception e ) {
			e.printStackTrace();
		}finally{
			
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			}catch(SQLException e) {}
		}
		return false;
	}

	@Override
	public boolean withDraw() {
		//출금
		String sql = null;
		int balance = 0;
		
		System.out.println("================================================================================== " );
		System.out.println("\t\t\t\t출금");
		System.out.println("---------------------------------------------------------------------------------- " );
		System.out.print("출금하실 계좌 번호를 입력해주세요 >>");
		String accountNo = sc.next();
		System.out.print("계좌 비밀번호를 입력해주세요 >>");
		String ac_Pw = sc.next();

		try { 
			//조회하기
			sql = "SELECT BALANCE FROM ACCOUNT WHERE ACCOUNTNO=? AND AC_PW=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, accountNo);
			pstmt.setString(2, ac_Pw);
			rs = pstmt.executeQuery();
			if(rs.next()==false) {
				System.out.println("정보가 없습니다.");
				return false;
			}
			balance = rs.getInt(1);	
			rs.close();	pstmt.close();
			
			System.out.print("출금하실 금액을 입력해주세요. >>");
			int money = sc.nextInt();
			
			if( money > balance ) {
				System.out.println("잔액이 부족합니다.");
				return false;
				
			}else if(money <= 0) {
				System.out.println("0원 및 마이너스 단위는 출금하실 수 없습니다.");
				return false;
				
			} else {
			sql = "UPDATE ACCOUNT SET BALANCE = ? WHERE ACCOUNTNO = " + accountNo;
			int minus = balance - money;
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, minus);
			pstmt.execute();
			pstmt.close();
			
			sql = "INSERT INTO TRANSACTION values(?, ?, ?, ?, ?, now())";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, accountNo);
			pstmt.setString(2, accountNo);
			pstmt.setString(3, "출금");
			pstmt.setInt(4, money);
			pstmt.setInt(5, minus);
			pstmt.executeUpdate();
			
			System.out.println("출금이 완료되었습니다.");
			System.out.println("잔고는 " + minus + "입니다");
				
			}
		}catch ( Exception e ) {
			e.printStackTrace();
		}finally{
			
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			}catch(SQLException e) {}
		}
		return false;
	}

	public boolean transfer() {
		//계좌 송금

		String sql = null;
		int sendBalance = 0;
		int receiveBalance = 0;
		
		System.out.println("================================================================================== " );
		System.out.println("\t\t\t\t계좌 이체");
		System.out.println("---------------------------------------------------------------------------------- " );
		System.out.print("이체하실 계좌 번호를 입력해주세요. >>");
		String sendAccount = sc.next();
		System.out.print("계좌 비밀번호를 입력해주세요 >>");
		String ac_Pw = sc.next();
		
		
		try { 
			//계좌 조회하기
			sql = "SELECT BALANCE FROM ACCOUNT WHERE ACCOUNTNO=? AND AC_PW=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sendAccount);
			pstmt.setString(2, ac_Pw);
			rs = pstmt.executeQuery();
						
			if(rs.next()==false) {
				System.out.println("정보가 없습니다.");
				return false;
			}
			sendBalance = rs.getInt(1);
			rs.close();	pstmt.close();
			
			System.out.print("입금받으실 계좌 번호를 입력해주세요. >>");
			String receiveAccount = sc.next();	
			
			sql = "SELECT BALANCE FROM ACCOUNT WHERE ACCOUNTNO=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, receiveAccount);
			rs = pstmt.executeQuery();
			rs.next();
			receiveBalance = rs.getInt(1);
			pstmt.close();
			
			//출금 및 입금
			sql = "UPDATE ACCOUNT SET BALANCE = ? WHERE ACCOUNTNO = " + sendAccount;
			System.out.print("출금하실 금액을 입력해주세요. >>");
			int sendmoney = sc.nextInt();
			
			if( sendmoney > sendBalance ) {
				System.out.println("잔액이 부족합니다.");
				return false;
				
			}else if(sendmoney <= 0) {
				System.out.println("0원 및 마이너스 단위는 출금하실 수 없습니다.");
				return false;
			
			} else {
				int minus = sendBalance - sendmoney;
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, minus);
				pstmt.execute();
				pstmt.close();
				
				sql = "UPDATE ACCOUNT SET BALANCE = ? WHERE ACCOUNTNO = " + receiveAccount;
				int sum = receiveBalance + sendmoney; 
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, sum);
				pstmt.execute();
				pstmt.close();
				
				//거래 내역 저장
				sql = "INSERT INTO TRANSACTION values(?, ?, ?, ?, ?, now())";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, sendAccount);
				pstmt.setString(2, receiveAccount);
				pstmt.setString(3, "송금-출금");
				pstmt.setInt(4, sendBalance);
				pstmt.setInt(5, minus);
				pstmt.executeUpdate();
				pstmt.close();
				
				sql = "INSERT INTO TRANSACTION values(?, ?, ?, ?, ?, now())";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, receiveAccount);
				pstmt.setString(2, sendAccount);
				pstmt.setString(3, "송금-입금");
				pstmt.setInt(4, receiveBalance);
				pstmt.setInt(5, sum);
				pstmt.executeUpdate();
				pstmt.close();
				
				System.out.println("출금이 완료되었습니다.");
				System.out.println("잔고는 " + minus + "입니다");
			}
			
		}catch ( Exception e ) {
			e.printStackTrace();
		}finally{
			
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			}catch(SQLException e) {}
		}
		return false;
	}
	
	
	private void transferList() {

		System.out.println("================================================================================== " );
		System.out.println("\t\t\t\t계좌 거래 내역");
		System.out.println("---------------------------------------------------------------------------------- " );
		System.out.print("조회하실 계좌 번호를 입력해주세요. >>");
		String viewAccount = sc.next();
		
		System.out.println("---------------------------------------------------------------------------------- " );
		System.out.println("  계좌  번호   |   거래계좌번호   |    거래  타입    ||  거래금액  |  잔 액   |     거래 시간  " );

		try { 
			String sql = "SELECT * FROM transaction WHERE AccountNo = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, viewAccount);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				transaction tc = new transaction(sql, sql, sql, 0, 0, sql);
				tc.setAccountNo(rs.getString("AccountNo"));
				tc.setReAccountNo(rs.getString("reAccountNo"));
				tc.setTransType(rs.getString("transType"));
				tc.setAmount(rs.getInt("amount"));
				tc.setLastbalance(rs.getInt("lastbalance"));
				tc.setTransactionTime(rs.getString("transactionTime"));
				transArr.add(tc);

				System.out.println( " "+tc.getAccountNo() +"\t"+ tc.getReAccountNo() +"\t"+ tc.getTransType() +"\t\t"+ tc.getAmount() +"\t  "+ tc.getLastbalance() +"\t    "+ tc.getTransactionTime());
			}	

		}catch ( Exception e ) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			}catch(SQLException e) {}
		}

	}	
	
	//--------------------------------관리자
	private void allUserList() {
		
		System.out.println("================================================================================== " );
		System.out.println("\t\t\t\t전체 회원 조회 ");
		System.out.println("---------------------------------------------------------------------------------- " );
		System.out.println("   \t|   ID   |   이름  |   가입 시간   ");
		System.out.println("---------------------------------------------------------------------------------- " );
		try { 
			String sql = "SELECT * FROM USERINFO";
			pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				UserInfo ui = new UserInfo();
				ui.setUser_id(rs.getString("user_id"));
				ui.setUser_name(rs.getString("user_name"));
				ui.setUser_time(rs.getString("user_time"));
				userArr.add(ui);
				System.out.println("\t" + ui.getUser_id() + "\t"+ ui.getUser_name() + "\t" + ui.getUser_time());
			}	
		}catch ( Exception e ) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			}catch(SQLException e) {}
		}

	}	

	private void allAccountList() {
	
		System.out.println("================================================================================== " );
		System.out.println("\t\t\t\t전체 계좌 조회 ");
		System.out.println("---------------------------------------------------------------------------------- " );
		System.out.println("   \t|   ID   |   이름  |   계좌 번호   |   계좌 잔액   ");
		System.out.println("---------------------------------------------------------------------------------- " );
		try { 
			String sql = "SELECT * FROM ACCOUNT ORDER BY USER_ID ASC;";
			pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				Account ac = new Account(sql, 0, sql, sql, 0);
				ac.setAccountNo(rs.getString("AccountNo"));
				ac.setAc_pw(rs.getInt("ac_pw"));
				ac.setUser_id(rs.getString("user_id"));
				ac.setAc_name(rs.getString("ac_name"));
				ac.setBalance(rs.getInt("balance"));
				accountArray.add(ac);
				System.out.println("\t" + ac.getUser_id() + "   \t"+ ac.getAc_name() + "\t" + ac.getAccountNo() + "\t" + ac.getBalance());
			}	
		}catch ( Exception e ) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			}catch(SQLException e) {}
		}

	}	
	private void deleteInfo() {
		String sql = null;

		System.out.println("================================================================================== " );
		System.out.println("\t\t\t\t계좌 삭제 ");
		System.out.println("---------------------------------------------------------------------------------- " );
		System.out.print("삭제할 계좌번호 입력하세요. >>");
		int deleteAccount = sc.nextInt();
		
			try { 
				sql = "DELETE FROM transaction WHERE ACCOUNTNO = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, deleteAccount);
				pstmt.executeUpdate();
				pstmt.close();
				
				sql = "DELETE FROM ACCOUNT WHERE ACCOUNTNO = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, deleteAccount);
				int result = pstmt.executeUpdate(); //성공하면 1을 반환
				String msg = result > -1 ? "계좌가 삭제되었습니다." : "계좌 삭제 실패하였습니다.";
				System.out.println(msg);
			}catch ( Exception e ) {
				e.printStackTrace();
			}finally{
				try {
					if(pstmt != null) pstmt.close();
				}catch(SQLException e) {}
			}

	}	
}

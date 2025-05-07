package chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.regex.*;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;


/**** CLIENT TABLE
+----------+--------------+------+-----+---------+-------+
| Field    | Type         | Null | Key | Default | Extra |
+----------+--------------+------+-----+---------+-------+
| name     | varchar(20)  | YES  |     | NULL    |       |
| birth      | date         | YES  |     | NULL    |       |
| email     | varchar(50)  | YES  |     | NULL    |       |
| addr      | varchar(150) | YES  |     | NULL    |       |
| phone   | varchar(15)  | YES  |     | NULL    |       |
| id           | varchar(20)  | NO   | PRI | NULL    |       |
| password | varchar(20)  | YES  |     | NULL    |       |
| image    | blob         | YES  |     | NULL    |       |
+----------+--------------+------+-----+---------+-------+
 *******/
public class JoinButtonActionListener implements ActionListener {
	static String c_name, c_birth, c_email, c_address, c_phone, c_id, c_password = null;
	static boolean RuleOfId = false;
	static boolean RuleOfPw = false;

	static PreparedStatement pstmt = null;
	static Connection mysqlConn = null;

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (ChatJoin.IdCheck == false) {
			JOptionPane.showConfirmDialog(null, "아이디 중복 확인 해주세요");
		} else {
			c_name = ChatJoin.txtName.getText().trim();
			c_email = ChatJoin.txtEmail.getText().trim() + "@";
			c_address = ChatJoin.txtBunji1.getText() + "-";
			c_address += ChatJoin.txtBunji2.getText() + " ";
			c_address += ChatJoin.txtAddress2.getText();
			ChatJoin.s_birth = ChatJoin.cmbYEAR.getSelectedItem().toString()
					+ ChatJoin.cmbMONTH.getSelectedItem().toString() + ChatJoin.cmbDAY.getSelectedItem().toString();
			c_phone = ChatJoin.txtP1.getText() + ChatJoin.txtP2.getText() + ChatJoin.txtP3.getText();
			
			/******* 필수 입력 사항 체크 ******/
			if ((!(c_name.equals(""))) && (!(c_email.equals(""))) && (!(c_address.equals("")))
					&& (!(ChatJoin.s_birth.equals("")))) {
				c_email += ChatJoin.cmbEMAIL.getSelectedItem().toString().trim();
				c_id = ChatJoin.txtID.getText();
				char[] password = ChatJoin.pwField1.getPassword();
				c_password = String.valueOf(password);

				Condition.main(null);
				if ((RuleOfId) && (RuleOfPw)) {
					DriverConnect();
				} else {
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "* 필수사항을 입력해주세요 *", "✖ ︎ERROR ✖", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/***** Driver Connect **********/
	public void DriverConnect() {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");

			/*****	Date FORMAT	*********/
			SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
			ChatJoin.c_birth = format1.parse(ChatJoin.s_birth);
			// Date�� c_birth String�� s_birth
			c_birth = new SimpleDateFormat("yyyy-MM-dd").format(ChatJoin.c_birth);
			// Date�������� -> String�� ���ֱ� = format
			// String�� date ������ = parse

			File file = new File("/Users/eunae/Library/Mobile Documents/com~apple~CloudDocs/eclipse-workspace/eunae-workspace/ChattinProgramm/BasicProfile.png");
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			while (true) {
				int x = fis.read();
				if (x == -1)
					break;
				bos.write(x);
			}
			fis.close();
			bos.close();
			
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

			String sql;
			sql = "INSERT INTO CLIENT(NAME, BIRTH, EMAIL, ADDR, PHONE, ID, PASSWORD, IMAGE) ";
			sql += "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			
			pstmt = mysqlConn.prepareStatement(sql);

			pstmt.setString(1, c_name);
			System.out.println(c_name);
			pstmt.setString(2, c_birth);
			System.out.println(c_birth);
			pstmt.setString(3, c_email);
			System.out.println(c_email);
			pstmt.setString(4, c_address);
			System.out.println(c_address);
			pstmt.setString(5, c_phone);
			System.out.println(c_phone);
			pstmt.setString(6, c_id);
			System.out.println(c_id);
			pstmt.setString(7, c_password);
			System.out.println(c_password);
			pstmt.setBinaryStream(8, bis, bos.size());
			System.out.println(bis);
			pstmt.executeUpdate();
			System.out.println("�ѹ� ����");
			
			sql = "INSERT INTO ROOMCHAT(ID, ROOM1, ROOM2, ROOM3)";
			sql += "VALUES (?, ?, ?, ?)";
			
			pstmt = mysqlConn.prepareStatement(sql);
			
			pstmt.setString(1, c_id);
			pstmt.setBinaryStream(2, null);
			pstmt.setBinaryStream(3, null);
			pstmt.setBinaryStream(4, null);
			System.out.println(c_id + " : ROOM 개설 완료");
			
			pstmt.executeUpdate();
			
			// DB�� ����
			ChatJoin.btnJoin.setEnabled(false);
			ChatJoin.JoinFrame.setVisible(false);

		} catch (ClassNotFoundException cnfe) {
			System.out.println("JOIN ERROR : ClassNotFound" + cnfe.getMessage());
		} catch (Exception e) {
			System.out.println("JOIN ERROR : " + e.getMessage());
		}

		finally {
			try {
				pstmt.close();
				mysqlConn.close();
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}

		}
	}

	/************ ���̵�, ��й�ȣ ��Ģ Ŭ���� ****************/
	public static class Condition {
		static String chkId;
		static String chkPw;
		static boolean match_id;
		static boolean match_pw1;
		static boolean match_pw2;

		public static void main(String args[]) {

			chkId = JoinButtonActionListener.c_id;
			System.out.println(" ID : " + chkId);
			chkPw = JoinButtonActionListener.c_password;
			System.out.println(" PW : " + chkPw);

			try {
				System.out.println("* id_Condition ȣ��");
				id_Condition();
				System.out.println("* ���̵� ��Ģ Ȯ�� *");
				System.out.println("* pw_Condition ȣ��");
				pw_Condition();
				System.out.println("* ��й�ȣ ��Ģ Ȯ�� *");
			} catch (Exception e) {
				System.out.println("ID ���� : " + e.getMessage());
			}
		}


		public static void id_Condition() {

			String idCond_symbol = "([a-z].*[0-9])";

			Pattern pattern_symbol = Pattern.compile(idCond_symbol);

			Matcher matcher_symbol = pattern_symbol.matcher(chkId);
			match_id = matcher_symbol.find();

			System.out.println("matcher_symbol : " + match_id);

			if (match_id) {
				RuleOfId = true;
			} else {
				JOptionPane.showMessageDialog(null, "* 영소문자&숫자 조합으로 입력해주세요.");
				ChatJoin.txtID.setText("");
			}
			System.out.println();
		}

		public static void pw_Condition() {

			String pwCond_symbol = "([0-9].*[!,@,#,^,&,*,(,)])|([!,@,#,^,&,*,(,)].*[0-9])";
			String pwCond_alpha = "([a-z].*[A-Z])|([A-Z],*[a-z])";

			Pattern pattern_symbol = Pattern.compile(pwCond_symbol);
			Pattern pattern_alpha = Pattern.compile(pwCond_alpha);
			// ���� ǥ���� ������

			Matcher matcher_symbol = pattern_symbol.matcher(chkPw);
			Matcher matcher_alpha = pattern_alpha.matcher(chkPw);
			// ���� ��Ī

			match_pw1 = matcher_symbol.find();
			match_pw2 = matcher_alpha.find();

			System.out.println("matcher_symbol : " + match_pw1);
			System.out.println("matcher_alpha : " + match_pw2);

			if (match_pw1 && match_pw2) {
				System.out.println("��й�ȣ�� �����մϴ�");
				RuleOfPw = true;
			} else {
				JOptionPane.showMessageDialog(null,
						"* 비밀번호 확인 불일치, ���빮��, ����, Ư������ �����̿��� �մϴ�.\n (특수문자 : !, @, #, ^, &, * , ',')");
				ChatJoin.pwField1.setText("");
				ChatJoin.pwField2.setText("");
			}
			System.out.println();
		}

	}
}
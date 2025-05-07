package chat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Emoticon {
	PreparedStatement pstmt = null;
	Connection mysqlConn = null;
	
	public void main(String args[]) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			mysqlConn = DriverManager.getConnection("jdbc:mysql:://localhost:3306:database?serverTimezone=UTC", "eunae", "wpfflWpf2!");
		
			/* ���� �̸�Ƽ�� DB �Է�*/
			File file = new File("C:\\Users\\Eunae Cho\\eclipse-workspace\\ChattinProgram\\Emotion\\emotion1.png");
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

		while (true) {
			int x = fis.read();
			if (x == -1)
				break;
			bos.write(x);		//�⺻�̹��� �о���̸� bos�� ��½�Ʈ������ �ۼ�
		}
		fis.close();
		bos.close();
				
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

		/* gif �̸�Ƽ�� DB �Է�*/

		File file2 = new File("C:\\Users\\Eunae Cho\\eclipse-workspace\\ChattinProgram\\Emotion\\emotion_gif1.gif");
		FileInputStream fis2 = new FileInputStream(file2);
		ByteArrayOutputStream bos2 = new ByteArrayOutputStream();

		while (true) {
			int x = fis2.read();
			if (x == -1)
				break;
			bos2.write(x);		//�⺻�̹��� �о���̸� bos�� ��½�Ʈ������ �ۼ�
		}
		fis2.close();
		bos2.close();
				
		ByteArrayInputStream bis2 = new ByteArrayInputStream(bos2.toByteArray());

		String sql;
		sql = "INSERT INTO EMOTION(EMO1, EMO2) ";
		sql += "VALUES (?,?)";
		pstmt = mysqlConn.prepareStatement(sql);

		pstmt.setBinaryStream(1, bis, bos.size());
		pstmt.setBinaryStream(2, bis2, bos2.size());

		pstmt.executeUpdate();
		}
		catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
}

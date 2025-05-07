package chat;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.JPasswordField;
import java.awt.Color;

@SuppressWarnings("serial")
public class ChatAdminLogin extends JFrame {

	public JPanel contentPane;
	static JTextField txtAdminID;
	private JPasswordField AdminPwField;
	JFrame AdminLoginFrame;
	static ChatAdminLogin AdminLogin;

	public class AdminLoginActionListener implements ActionListener {
		Connection mysqlConn = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		
		String admin_ID;
		String admin_PW;
		String input_ID;
		String input_PW;
		String admin_NAME;
		String sql;
		char[] admin_pw;

		public void actionPerformed(ActionEvent a) {

			sql = "SELECT id,password FROM admin";
			input_ID = txtAdminID.getText();
			admin_pw = AdminPwField.getPassword();
			input_PW = String.valueOf(admin_pw);
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");
				
				pstmt = mysqlConn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					admin_ID = rs.getString(1);
					admin_PW = rs.getString(2);
				}
				
				if ((input_ID.equals(admin_ID))&(input_PW.equals(admin_PW))){
					ChatServer.main(null);
					AdminLogin.AdminLoginFrame.setVisible(false);
				}
				else {
					JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호가 틀렸습니다!");
					txtAdminID.setText(null);
					AdminPwField.setText(null);
				}
			
			}
			catch (Exception e) {
				System.out.println("ERROR : " + e.getMessage());
			}
			finally {
				try {
					rs.close();
					pstmt.close();
					mysqlConn.close();
				} catch (Exception ex) {}			
			}

		}

	}

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminLogin = new ChatAdminLogin();
					AdminLogin.AdminLoginFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*********			 Create the frame.	 		********/
	public ChatAdminLogin() {
		AdminLoginFrame = new JFrame();
		AdminLoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		AdminLoginFrame.setBounds(100, 100, 450, 200);
		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(3, 3, 3, 3));
		contentPane.setLayout(null);
		AdminLoginFrame.add(contentPane);

		JLabel lblAdminID = new JLabel("ID");
		lblAdminID.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdminID.setBounds(30, 41, 100, 30);
		contentPane.add(lblAdminID);

		JLabel lblAdminPW = new JLabel("PW");
		lblAdminPW.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdminPW.setBounds(30, 83, 100, 30);
		contentPane.add(lblAdminPW);

		txtAdminID = new JTextField();
		txtAdminID.setBounds(110, 38, 180, 36);
		contentPane.add(txtAdminID);
		txtAdminID.setColumns(10);

		JButton btnAdminLogin = new JButton("Login");
		btnAdminLogin.setForeground(Color.darkGray);
		btnAdminLogin.setBounds(310, 38, 93, 81);
		contentPane.add(btnAdminLogin);
		
		AdminPwField = new JPasswordField();
		AdminPwField.setBounds(110, 83, 180, 36);
		contentPane.add(AdminPwField);
		ActionListener listener_Ad = new AdminLoginActionListener();
		btnAdminLogin.addActionListener(listener_Ad);
	}
}


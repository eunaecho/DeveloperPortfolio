package chat;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPasswordField;

/*
+----------+--------------+------+-----+---------+-------+
| Field    | Type         | Null | Key | Default | Extra |
+----------+--------------+------+-----+---------+-------+
| name     | varchar(20)  | YES  |     | NULL    |       |
| birth    | date         | YES  |     | NULL    |       |
| email    | varchar(50)  | YES  |     | NULL    |       |
| addr     | varchar(150) | YES  |     | NULL    |       |
| phone    | varchar(15)  | YES  |     | NULL    |       |
| id       | varchar(20)  | NO   | PRI | NULL    |       |
| password | varchar(20)  | YES  |     | NULL    |       |
| image    | blob         | YES  |     | NULL    |       |
+----------+--------------+------+-----+---------+-------+
*/
public class ChatJoin extends JFrame {

	private static final long serialVersionUID = 1L;  // 왜 ?

	static JFrame JoinFrame;
	static JPanel contentPane;
	static JTextField txtName;
	static JTextField txtEmail;
	static JTextField txtBunji1;
	public static JTextField txtID;
	static JTextField txtP1;
	static JTextField txtP2;
	static JTextField txtP3;
	static JTextField txtT1;
	static JTextField txtT2;
	static JTextField txtT3;
	static JTextField txtAddress2;
	static JCheckBox chkT;
	static JCheckBox chkP;
	static JComboBox<String> cmbEMAIL;
	static JComboBox<String> cmbDAY;
	static JComboBox<String> cmbMONTH;
	static JComboBox<String> cmbYEAR;
	static JLabel FailPassword;
	static JButton btnJoin;
	static String resultChk;

	static String s_birth = null;
	static Date c_birth = null; 

	String[] str_Y;
	String[] str;
	String[] str_Year;
	String[] str_M;
	String[] str_D;
	String[] str_E;

	Calendar c = Calendar.getInstance();

	// static Statement stmt = null;
	static PreparedStatement pstmt = null;
	static Connection mysqlConn = null;

	static JPasswordField pwField1;
	static JPasswordField pwField2;

	static boolean IdCheck = false;
	static JTextField txtBunji2;

	/******** Launch the application. ********/

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatJoin frame = new ChatJoin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/************* �޺��ڽ� ��¥ �ֱ� *************/
	public String[] makeYear() {
		int thisYear = c.get(Calendar.YEAR);
		System.out.println("thisYear : " + thisYear);
		str_Y = new String[thisYear - 1900 + 1];
		String temp;

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				temp = String.valueOf(19) + String.valueOf(i) + String.valueOf(j);
				str_Y[i * 10 + j] = temp;
			}
		}

		for (int i = 100; i <= thisYear - 1900; i++) {
			str_Y[i] = String.valueOf(i + 1900);
		}
		return str_Y; // �迭 ��ȯ
	}

	public String[] makeYear2() {
		str = this.makeYear(); // 1900~ ���� �⵵���� �迭�����Ѱ� �޾ƿ�
		str_Year = new String[str.length]; // �迭 ������ŭ�� ���ο� �迭 ����

		for (int i = 0; i < str.length; i++) {
			str_Year[i] = str[str.length - 1 - i];
		}
		return str_Year;
	}

	public String[] makeMonth() {
		str_M = new String[12];
		for (int i = 0; i < 12; i++) {
			int date = i + 1;
			str_M[i] = addZero(date);
		}
		return str_M;
	}

	public String[] makeDay() {
		str_D = new String[31];
		for (int i = 0; i < 31; i++) {
			int date = i + 1;
			str_D[i] = addZero(date);
		}
		return str_D;
	}

	public String addZero(int date) {
		String Zerodate = String.format("%02d", date);
		return Zerodate;
	}

	/************* 이메일 ****************/
	public String[] makeEmail() {
		str_E = new String[4];
		str_E[0] = "naver.com";
		str_E[1] = "daum.net";
		str_E[2] = "google.com";
		str_E[3] = "nate.com";
		return str_E;
	}

	public class ChkBoxActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (chkP.isSelected()) {
				txtP1.setEnabled(true);
				txtP2.setEnabled(true);
				txtP3.setEnabled(true);
			} else if (!chkP.isSelected()) {
				txtP1.setText("");
				txtP2.setText("");
				txtP3.setText("");
				txtP1.setEnabled(false);
				txtP2.setEnabled(false);
				txtP3.setEnabled(false);
			}
		}
	}

	public class EmailListActionListener implements ActionListener {
		String E_myself;
		String Exp = "���� �Է�";

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			E_myself = cmbEMAIL.getSelectedItem().toString();
			if (E_myself.equals(Exp)) {
				cmbEMAIL.setEditable(true);
			}

		}

	}

	/*********** id 구성 체크 *************/
	public class IdCheckActionListener implements ActionListener {
		String sql;
		String inputId = null;
		String chkId;
		ResultSet rs;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			inputId = ChatJoin.txtID.getText();

			if ((inputId.length() < 5) || (inputId.length() > 10))
				JOptionPane.showMessageDialog(null, "아이디는 6-10자 이상으로 입력해주세요", "�˸�", JOptionPane.ERROR_MESSAGE);
			else if ((inputId.length() >= 6) && (inputId.length() <= 10)) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");

					sql = "SELECT * FROM CLIENT WHERE ID = ?";
					pstmt = mysqlConn.prepareStatement(sql);
					pstmt.setString(1, inputId);

					rs = pstmt.executeQuery();

					while (rs.next()) {
						chkId = rs.getString(7); // CLIENT ���̺��� 7��° Į���� ���� �ҷ�����
					}

					System.out.println("=======================================");
					System.out.println("입력된 아이디 : " + ChatJoin.txtID.getText());
					System.out.println("DB 아이 : " + chkId);
					System.out.println("=======================================");

					if (chkId != null) {
						JOptionPane.showMessageDialog(null, "\"" + inputId + "\" 사용불가능한 아이디입니다");
						ChatJoin.txtID.setText("");
						chkId = null;
					} else {
						JOptionPane.showMessageDialog(null, "\"" + inputId + "\" 사용 가능한 아이디입니다");
						chkId = null;
						IdCheck = true; // �ߺ�Ȯ�� �������
					}

				} catch (Exception e) {
					System.out.println("���� : " + e.getMessage());
				} finally {
					try {
						mysqlConn.close();
					} catch (SQLException sqle) {
						System.out.println("finally ���� : " + sqle.getMessage());

					}
				}

			} else if (inputId.length() == 0) {
				JOptionPane.showMessageDialog(null, "���̵� �Է����ּ���");
			}
		}

	}

	/************* 비밀번호 구성 체크  ****************/
	public class PasswordCheckKeyListener implements KeyListener {
		char[] chkpw1; // 비밀번호 
		char[] chkpw2; // 비밀번호 확인 
		String strPw1;
		String strPw2;

		@Override
		public void keyPressed(KeyEvent arg0) {
			strPw1 = String.valueOf(ChatJoin.pwField1.getPassword());
			strPw2 = String.valueOf(ChatJoin.pwField2.getPassword());

			if (strPw1.equals(strPw2)) {
				ChatJoin.FailPassword.setFont(new Font("���ʷ� ����", Font.PLAIN, 11));
				ChatJoin.FailPassword.setForeground(Color.BLUE);
				ChatJoin.FailPassword.setText("* ��й�ȣ�� ��ġ�մϴ� ");
				if ((strPw1.length() < 8) || (strPw1.length() > 15)) {
					pwField1.setText("");
					pwField2.setText("");
					JOptionPane.showMessageDialog(null, "��й�ȣ�� 8~15�ڷ� ���Ͽ� �ּ���");
				}
				else if ((strPw1.length() >= 8) && (strPw1.length() <= 15))
					System.out.println("��й�ȣ ���� : " + strPw1.length());
				else if ((strPw1.length() == 0)) {
					ChatJoin.FailPassword.setFont(new Font("���ʷ� ����", Font.PLAIN, 11));
					ChatJoin.FailPassword.setForeground(Color.DARK_GRAY);
					ChatJoin.FailPassword.setText("* 비밀번호 일치 ");
				}
			} else {
				ChatJoin.FailPassword.setFont(new Font("���ʷ� ����", Font.PLAIN, 11));
				ChatJoin.FailPassword.setForeground(Color.RED);
				ChatJoin.FailPassword.setText("* 비밀번호 불일치 ");
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			strPw1 = String.valueOf(ChatJoin.pwField1.getPassword());
			strPw2 = String.valueOf(ChatJoin.pwField2.getPassword());

			if (strPw1.equals(strPw2)) {
				ChatJoin.FailPassword.setFont(new Font("���ʷ� ����", Font.PLAIN, 12));
				ChatJoin.FailPassword.setForeground(Color.BLUE);
				ChatJoin.FailPassword.setText("* 입력한 비밀번호 동일  ");
				if ((strPw1.length() < 8) || (strPw1.length() > 15))
					JOptionPane.showMessageDialog(null, "��й�ȣ�� 8~15�ڷ� ���Ͽ� �ּ���");
				else if ((strPw1.length() >= 8) && (strPw1.length() <= 15))
					System.out.println("��й�ȣ ���� : " + strPw1.length());
				else if ((strPw1.length() == 0)) {
					ChatJoin.FailPassword.setFont(new Font("���ʷ� ����", Font.PLAIN, 12));
					ChatJoin.FailPassword.setForeground(Color.DARK_GRAY);
					ChatJoin.FailPassword.setText("* 비밀번호 길이를 8~15내로 입력해주세요. ");
				}
			} else {
				ChatJoin.FailPassword.setFont(new Font("���ʷ� ����", Font.PLAIN, 12));
				ChatJoin.FailPassword.setForeground(Color.RED);
				ChatJoin.FailPassword.setText("* 입력 비밀번호 불일치 ");
			}

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			strPw1 = String.valueOf(ChatJoin.pwField1.getPassword());
			strPw2 = String.valueOf(ChatJoin.pwField2.getPassword());

			if (strPw1.equals(strPw2)) {
				ChatJoin.FailPassword.setFont(new Font("���ʷ� ����", Font.PLAIN, 12));
				ChatJoin.FailPassword.setForeground(Color.BLUE);
				ChatJoin.FailPassword.setText("* 입력한 비밀번호 동일  ");
				if ((strPw1.length() < 8) || (strPw1.length() > 15))
					JOptionPane.showMessageDialog(null, "��й�ȣ�� 8~15�ڷ� ���Ͽ� �ּ���");
				else if ((strPw1.length() >= 8) && (strPw1.length() <= 15))
					System.out.println("��й�ȣ ���� : " + strPw1.length());
				else if ((strPw1.length() == 0)) {
					ChatJoin.FailPassword.setFont(new Font("���ʷ� ����", Font.PLAIN, 12));
					ChatJoin.FailPassword.setForeground(Color.DARK_GRAY);
					ChatJoin.FailPassword.setText("* 비밀번호 길이를 8~15내로 입력해주세요. ");
				}
			} else {
				ChatJoin.FailPassword.setFont(new Font("���ʷ� ����", Font.PLAIN, 12));
				ChatJoin.FailPassword.setForeground(Color.RED);
				ChatJoin.FailPassword.setText("* 입력 비밀번호 불일치 ");
			}
		}
	}

	/******** 주소 찾기  ******/
	public class SearchAddressActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			ZipSearch.main(null);
		}

	}

	/******** Create the frame. * @throws ParseException ***/
	public ChatJoin() {
		JoinFrame = new JFrame("회원가입");
//		JoinFrame.setBackground(new Color(0, 0, 0));
		JoinFrame.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		JoinFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JoinFrame.setBounds(100, 100, 400, 650);
		
		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		JoinFrame.getContentPane().add(contentPane);
		
		/****** 회원 정보 입력 ******/
		JPanel panelJoin = new JPanel();
		panelJoin.setBounds(0, 0, 400, 650);
//		panelJoin.setBackground(new Color(255, 255, 255));
		contentPane.add(panelJoin);
		panelJoin.setLayout(null);

		JLabel lblJoinId = new JLabel("아이디");
		lblJoinId.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblJoinId.setHorizontalAlignment(SwingConstants.LEFT);
		lblJoinId.setBounds(17, 30, 80, 35);
		panelJoin.add(lblJoinId);

		JLabel lblJoinPw = new JLabel("비밀번호");
		lblJoinPw.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblJoinPw.setHorizontalAlignment(SwingConstants.LEFT);
		lblJoinPw.setBounds(17, 75, 80, 35);
		panelJoin.add(lblJoinPw);

		JLabel lblJoinPwr = new JLabel("비밀번호 확인");
		lblJoinPwr.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblJoinPwr.setHorizontalAlignment(SwingConstants.LEFT);
		lblJoinPwr.setBounds(17, 120, 97, 35);
		panelJoin.add(lblJoinPwr);

		txtID = new JTextField();
		txtID.setColumns(10);
		txtID.setBounds(110, 30, 145, 35);
		panelJoin.add(txtID);

		pwField1 = new JPasswordField();
		pwField1.setBounds(110, 75, 145, 35);
		panelJoin.add(pwField1);

		pwField2 = new JPasswordField();
		pwField2.setBounds(110, 120, 145, 35);
		panelJoin.add(pwField2);
		KeyListener listener_pw = new PasswordCheckKeyListener();
		pwField2.addKeyListener(listener_pw);

		JButton btnIDchk = new JButton("중복확인");
		btnIDchk.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 12));
		btnIDchk.setBounds(275, 30, 91, 35);
		panelJoin.add(btnIDchk);
		btnIDchk.addActionListener(new IdCheckActionListener());

		FailPassword = new JLabel("");
		FailPassword.setBackground(new Color(255, 255, 255));
		FailPassword.setBounds(102, 150, 210, 35);
		panelJoin.add(FailPassword);

		/******* 			개인정보 입력 Panel 				******/

		JLabel lblName = new JLabel("이름");
		lblName.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		lblName.setBounds(17, 180, 80, 35);
		panelJoin.add(lblName);

		JLabel lblIdenti = new JLabel("생년월일");
		lblIdenti.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblIdenti.setHorizontalAlignment(SwingConstants.LEFT);
		lblIdenti.setBounds(17, 225, 80, 35);
		panelJoin.add(lblIdenti);

		JLabel lblEmail = new JLabel("이메일");
		lblEmail.setBackground(new Color(255, 255, 255));
		lblEmail.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
		lblEmail.setBounds(17, 270, 80, 35);
		panelJoin.add(lblEmail);

		JLabel lblNumber = new JLabel("핸드폰");
		lblNumber.setBackground(new Color(255, 255, 255));
		lblNumber.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblNumber.setHorizontalAlignment(SwingConstants.LEFT);
		lblNumber.setBounds(17, 315, 80, 35);
		panelJoin.add(lblNumber);

		JLabel lblAddr = new JLabel("주소");
		lblAddr.setBackground(new Color(255, 255, 255));
		lblAddr.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblAddr.setHorizontalAlignment(SwingConstants.LEFT);
		lblAddr.setBounds(17, 360, 80, 35);
		panelJoin.add(lblAddr);

		txtName = new JTextField();
		txtName.setBounds(95, 180, 120, 35);
		panelJoin.add(txtName);
		txtName.setColumns(10);

		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(95, 270, 120, 35);
		panelJoin.add(txtEmail);

		JLabel label = new JLabel("@");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(219, 270, 18, 18);
		panelJoin.add(label);

		makeEmail();
		cmbEMAIL = new JComboBox<String>();
		cmbEMAIL.setModel(new DefaultComboBoxModel<String>(str_E));
		cmbEMAIL.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		cmbEMAIL.setToolTipText("");
		cmbEMAIL.setBounds(245, 270, 149, 35);
		panelJoin.add(cmbEMAIL);
		ActionListener listener_Email = new EmailListActionListener();
		cmbEMAIL.addActionListener(listener_Email);

		txtBunji1 = new JTextField();
		txtBunji1.setColumns(10);
		txtBunji1.setBounds(95, 360, 80, 35);
		txtBunji1.setBackground(Color.lightGray);
		txtBunji1.setEditable(false);
		panelJoin.add(txtBunji1);

		txtBunji2 = new JTextField();
		txtBunji2.setColumns(10);
		txtBunji2.setBounds(210, 360, 70, 35);
		txtBunji2.setEditable(false);
		txtBunji2.setBackground(Color.lightGray);
		panelJoin.add(txtBunji2);

		txtAddress2 = new JTextField();
		txtAddress2.setColumns(10);
		txtAddress2.setEditable(false);
		txtAddress2.setBounds(95, 405, 280, 35);
		panelJoin.add(txtAddress2);

		makeYear2();
		cmbYEAR = new JComboBox<String>();
		cmbYEAR.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		cmbYEAR.setModel(new DefaultComboBoxModel<String>(str_Year));
		cmbYEAR.setToolTipText("");
		cmbYEAR.setBounds(95, 225, 100, 40);
		panelJoin.add(cmbYEAR);

		makeMonth();
		cmbMONTH = new JComboBox<String>();
		cmbMONTH.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		cmbMONTH.setModel(new DefaultComboBoxModel<String>(str_M));
		cmbMONTH.setToolTipText("");
		cmbMONTH.setBounds(220, 225, 70, 40);
		panelJoin.add(cmbMONTH);

		makeDay();
		cmbDAY = new JComboBox<String>();
		cmbDAY.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		cmbDAY.setModel(new DefaultComboBoxModel<String>(str_D));
		cmbDAY.setToolTipText("");
		cmbDAY.setBounds(300, 225, 70, 40);
		panelJoin.add(cmbDAY);

		JLabel lblJoinYear = new JLabel("년");
		lblJoinYear.setHorizontalAlignment(SwingConstants.CENTER);
		lblJoinYear.setBounds(180, 225, 30, 35);
		lblJoinYear.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		panelJoin.add(lblJoinYear);

		JLabel lblJoinMonth = new JLabel("월");
		lblJoinMonth.setHorizontalAlignment(SwingConstants.CENTER);
		lblJoinMonth.setBounds(280, 225, 30, 35);
		lblJoinMonth.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		panelJoin.add(lblJoinMonth);

		JLabel lblJoinDay = new JLabel("일");
		lblJoinDay.setHorizontalAlignment(SwingConstants.CENTER);
		lblJoinDay.setBounds(353, 225, 30, 35);
		lblJoinDay.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		panelJoin.add(lblJoinDay);

		txtP1 = new JTextField();
		txtP1.setHorizontalAlignment(SwingConstants.CENTER);
		txtP1.setColumns(10);
		txtP1.setBounds(95, 315, 61, 35);
		panelJoin.add(txtP1);

		txtP2 = new JTextField();
		txtP2.setHorizontalAlignment(SwingConstants.CENTER);
		txtP2.setColumns(10);
		txtP2.setBounds(182, 315, 61, 35);
		panelJoin.add(txtP2);

		txtP3 = new JTextField();
		txtP3.setHorizontalAlignment(SwingConstants.CENTER);
		txtP3.setColumns(10);
		txtP3.setBounds(272, 315, 61, 35);
		panelJoin.add(txtP3);

		JLabel label_7 = new JLabel("-");
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		label_7.setBounds(158, 323, 19, 18);
		panelJoin.add(label_7);

		JLabel label_9 = new JLabel("-");
		label_9.setHorizontalAlignment(SwingConstants.CENTER);
		label_9.setBounds(248, 323, 19, 18);
		panelJoin.add(label_9);

		JLabel label_1 = new JLabel("-");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(183, 368, 19, 18);
		panelJoin.add(label_1);

		JButton btnSearch = new JButton("검색");
		btnSearch.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnSearch.setBounds(302, 360, 92, 35);
		panelJoin.add(btnSearch);
		btnSearch.addActionListener(new SearchAddressActionListener());
		
		// 입력 
		String Rule[] = { "* 아이디 : 6-10글자, 영소문자와 숫자 혼합 ", "* 비밀번호  : 영대,소문자, 특수문자 포함 10자 이상 ",
				"(포함 특수문자 : !,@,#,$,^,&,*,',')" };
		String rule = "<html>" + Rule[0] + "<br>" + Rule[1] + "<br>" + Rule[2] + "</html>";
		JLabel lblRule = new JLabel();
		lblRule.setText(rule);
		lblRule.setForeground(new Color(0, 0, 139));
		lblRule.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 12));
		lblRule.setBounds(28, 450, 379, 95);
		panelJoin.add(lblRule);
		
		btnJoin = new JButton("회원가입");
		btnJoin.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 14));
		btnJoin.setBounds(15, 550, 370, 40);
		btnJoin.setEnabled(true);
		panelJoin.add(btnJoin);
		ActionListener listener = new JoinButtonActionListener();
		btnJoin.addActionListener(listener);
	}
}

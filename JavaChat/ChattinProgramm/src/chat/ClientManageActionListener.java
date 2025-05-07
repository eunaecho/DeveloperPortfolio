package chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import chat.ClientManageActionListener.FindActionListener.FindbuttonActionListener;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class ClientManageActionListener extends JFrame implements ActionListener {

	private static final String ClientRetrievalActionListener = null;
	public JPanel contentPane;
	static public JTable ClientTable;
	private Vector<Vector> data;
	public Vector<String> modify_in;
	public Vector<String> in;
	private Vector<String> title;
	public JButton btnModify;

	private Connection mysqlConn = null;
	private Statement stmt = null; // ���� �� ���� ����� ���ϴ� ��ü
	int selectedRow;
	String sql;
	ResultSet rs;

	/****** Launch the application. ******/
	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { ClientManageActionListener frame = new
	 * ClientManageActionListener(); frame.setVisible(true); } catch (Exception e) {
	 * e.printStackTrace(); } } }); }
	 */
	
	/********* Frame ����, ���̺� ����, model�� ����� ������(����) �����ϴ� ������ ********/
	public ClientManageActionListener() {
		title = new Vector<>();
		data = new Vector<>(); // ���̺��� ���� Ÿ��Ʋ�� �����͸� �����ϴ� ���� ��ü ����

		title.add("이름");
		title.add("생년월일");
		title.add("이메일");
		title.add("주소");
		title.add("전화번호");
		title.add("아이디");
		title.add("비밀번호");

		getData(); // �����͸� ���ϴ� �޼�Ʈ

		// Frame close �� �ڿ� �ݳ�
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent w) {
				try {
					stmt.close();
					mysqlConn.close();
					setVisible(true);
					//dispose();	
					//System.exit(0);
				} catch (Exception e) {
				}
			}
		});

		/*********** Frame ***********/
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 800, 482);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 754, 74);
		contentPane.add(panel);

		JScrollPane scrlTablePane = new JScrollPane();
		scrlTablePane.setBounds(14, 50, 754, 269);
		contentPane.add(scrlTablePane);

		ClientTable = new JTable(data, title) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		scrlTablePane.setViewportView(ClientTable);
		ClientTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ���� ��ũ��
		ClientTable.setRowHeight(25); // ���� ���ϱ�
		ClientTable.getTableHeader().setReorderingAllowed(false); // Į�� ��� �̵� �Ұ����ϰ� ����
		ClientTable.getColumn("이름").setPreferredWidth(80);
		ClientTable.getColumn("생년월일").setPreferredWidth(100);
		ClientTable.getColumn("이메일").setPreferredWidth(150);
		ClientTable.getColumn("주소").setPreferredWidth(200);
		ClientTable.getColumn("전화번호").setPreferredWidth(100);
		ClientTable.getColumn("아이디").setPreferredWidth(100);
		ClientTable.getColumn("비밀번호").setPreferredWidth(100);

		JButton btnAdd = new JButton("회원 등록");	// Register
		btnAdd.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnAdd.setForeground(Color.DARK_GRAY);
		btnAdd.setBounds(14, 371, 105, 44);
		contentPane.add(btnAdd);
		btnAdd.addActionListener(new NewClientActionListener());

		btnModify = new JButton("회원 수정");		// MODIFY
		btnModify.setForeground(Color.DARK_GRAY);
		btnModify.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnModify.setBounds(228, 371, 105, 44);
		contentPane.add(btnModify);
		btnModify.addActionListener(new ModifyActionListener());

		JButton btnRemove = new JButton("회원 제거"); // REMOVE
		btnRemove.setForeground(Color.DARK_GRAY);
		btnRemove.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnRemove.setBounds(440, 371, 105, 44);
		contentPane.add(btnRemove);
		btnRemove.addActionListener(new RemoveActionListener(ClientTable));

		JButton btnRetrieval = new JButton("회원 검색");	// SEARCH
		btnRetrieval.setForeground(Color.DARK_GRAY);
		btnRetrieval.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnRetrieval.setBounds(663, 371, 105, 44);
		contentPane.add(btnRetrieval);
		btnRetrieval.addActionListener(new FindActionListener());

	}

	private void getData() {
		System.out.println(" GETDATA() 진입 ");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");

			stmt = mysqlConn.createStatement();
			sql = "SELECT * FROM CLIENT";
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				in = new Vector<>();
				in.add(rs.getString(1));					//name
				in.add(rs.getString(2));					//birth
				in.add(rs.getString(3));					//email
				in.add(rs.getString(4));					//addr
				in.add(rs.getString(5));					//phone
				in.add(rs.getString(6));					//id
				in.add(rs.getString(7));					//pw
				in.add(rs.getString(8));					//image

				data.add(in);
			}

		} catch (ClassNotFoundException cnfe) {
			System.out.println("���� : " + cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println("���� : " + sqle.getStackTrace());
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			ClientManageActionListener frame = new ClientManageActionListener();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class RemoveActionListener implements ActionListener {
		JTable table;
		String sql;
		String rmID;
		PreparedStatement pstmt;
		int selectedRow_D;
		int IdIndex = 6;

		RemoveActionListener(JTable table) {
			this.table = table;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			selectedRow_D = table.getSelectedRow(); // ������ ��
			if (selectedRow_D == -1)
				return;
			DefaultTableModel model = (DefaultTableModel) table.getModel();

			rmID = (String) table.getValueAt(selectedRow_D, IdIndex);
			System.out.println(rmID);

			System.out.println(" ���̺� ���ܿ��� ���� �Ϸ�");
			try {
				sql = "DELETE FROM CLIENT ";
				sql += "WHERE ID=?";

				pstmt = mysqlConn.prepareStatement(sql);
				// ������ �غ�
				
				pstmt.setString(1, rmID);

				pstmt.executeUpdate();
				// DB�� ����
				
				sql = "DELETE FROM ROOMCHAT WHERE ID = ?";
				pstmt = mysqlConn.prepareStatement(sql);
				pstmt.setString(1, rmID);
				pstmt.executeUpdate();
				
			} catch (Exception e) {
				System.out.println(" ȸ�� ���� ���� �� ���� : " + e.getMessage());
			} finally {
				try {
					pstmt.close();
				} catch (Exception ee) {
				}
			}
			model.removeRow(selectedRow_D);
		}

	}

	public class NewClientActionListener extends JFrame implements ActionListener {

		private JPanel contentPane;
		public JFrame NewFrame;
		public JTable NewTable;
		Vector<Vector> data_v; // ���ڵ� ���ļ� ���ͷ� ����
		Vector<String> data_n; // 1���� ���ڵ� ����
		String[] title_n; // DB�÷��� �����
		Vector<String> column_n;

		public void actionPerformed(ActionEvent e) {

			/******** ���̺� ���� ********/
			data_v = new Vector<>();
			column_n = new Vector<>();
			column_n.add("ȸ�� ����");
			column_n.add("�Է� ����");

			title_n = new String[] { "이름", "생년월일", "이메일", "주소", "전화번호", "아이디", "비밀번호", "프로필사진" };

			for (int j = 0; j < 8; j++) {
				data_n = new Vector<>();
				data_n.add(title_n[j]);
				data_n.add(""); // ������ ������ ����
				data_v.add(data_n); // ���̺� ���� ����

			}
			/******* Frame ***********/
			NewFrame = new JFrame("회원 추가");
			NewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			NewFrame.setBounds(100, 100, 450, 415);

			contentPane = new JPanel();
			contentPane.setBackground(Color.WHITE);
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			NewFrame.getContentPane().add(contentPane);
			contentPane.setLayout(null);

			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(32, 28, 370, 250);
			contentPane.add(scrollPane);

			NewTable = new JTable(data_v, column_n);
			scrollPane.setViewportView(NewTable);
			NewTable.setVisible(true);
			NewTable.getTableHeader().setReorderingAllowed(false);
			NewTable.getTableHeader().setResizingAllowed(false);
			NewTable.setRowHeight(25);
			NewTable.getColumn("ȸ�� ����").setPreferredWidth(100);
			NewTable.getColumn("�Է� ����").setPreferredWidth(270);

			JButton btnnewset = new JButton("회원 등록 완료");
			btnnewset.setForeground(Color.DARK_GRAY);
			btnnewset.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 14));
			btnnewset.setBounds(32, 302, 370, 40);
			contentPane.add(btnnewset);
			btnnewset.addActionListener(new NewSetActionListener());
			NewFrame.setVisible(true);
		}

		public class NewSetActionListener implements ActionListener {
			String[] new_data = new String[9];
			String sql_insert;
			PreparedStatement pstmt_m;
			String n_birth;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("�߰� ��ư �׼� ������ ȣ��");
				for (int j = 0; j < 9; j++) {
					new_data[j] = ((String) NewTable.getValueAt(j, 1));
					System.out.println("---------------------------");
					System.out.print(NewTable.getValueAt(j, 0) + " : ");
					System.out.println(new_data[j]);
				}
				try {
					Date n_date = new SimpleDateFormat("yyyyMMdd").parse(new_data[1]);
					n_birth = new SimpleDateFormat("yyyyMMdd").format(n_date);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					// 프로필 사진 등록 
					File file = new File("C:\\Users\\Eunae Cho\\Pictures\\�⺻�̹���.png");
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
					sql_insert = "INSERT INTO CLIENT(NAME, BIRTH, EMAIL, ADDR, PHONE, ID, PASSWORD, IMAGE) ";
					sql_insert += "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
					pstmt_m = mysqlConn.prepareStatement(sql_insert);

					pstmt_m.setString(1, new_data[0]);
					pstmt_m.setString(2, new_data[1]);
					pstmt_m.setString(3, new_data[2]);
					pstmt_m.setString(4, new_data[3]);
					pstmt_m.setString(5, new_data[4]);
					pstmt_m.setString(6, new_data[5]);
					pstmt_m.setString(7, new_data[6]);
					pstmt_m.setBinaryStream(8, bis, bos.size());

					pstmt_m.executeUpdate();
					
					sql_insert = "INSERT INTO ROOMCHAT(ID, NAME1, ROOM1, NAME2, ROOM2, NAME3, ROOM3)";
					sql_insert += "VALUES (?,?,?,?,?,?,?)";
					
					pstmt_m = mysqlConn.prepareStatement(sql_insert);
					
					pstmt_m.setString(1, new_data[6]);
					pstmt_m.setString(2, "");
					pstmt_m.setBinaryStream(3, null);
					pstmt_m.setString(4, "");
					pstmt_m.setBinaryStream(5, null);
					pstmt_m.setString(6, "");
					pstmt_m.setBinaryStream(7, null);
					
					pstmt_m.executeUpdate();
					
				} catch (Exception e) {
					System.out.println("✖ ERROR ✖︎ : " + e.getMessage());
				} finally {
					try {
						pstmt_m.close();
					} catch (Exception ee) {
					}
				}
			}
		}

	}

	public class ModifyActionListener extends JFrame implements ActionListener {
		//Frame 변수
		public JFrame ModifyFrame;
		public JPanel contentPane;
		public JTable ModifyTable;

		public String click_name;
		public String click_birth;
		public String click_email;
		public String click_addr;
		public String click_phone;
		public String click_tel;
		public String click_id;
		public String click_pw;
		public String click_agree;

		public Vector<String> column_T;
		public Vector<Vector> data_m; // 1���� ���ڵ���� ���ļ� ���̺��� ��Ÿ�� ������
		public Vector<String> data_in; // 1���� ���ڵ� ����
		public String[] title_m;
		public String[] Click_data;
		ResultSet rs_m;

		@Override
		public void actionPerformed(ActionEvent e) {

			data_m = new Vector<>();
			Click_data = new String[8];
			column_T = new Vector<>();
			column_T.add("1?");
			column_T.add("2?");

			title_m = new String[]{ "이름", "생년월일", "이메일", "주소", "전화번호", "아이디", "비밀번호"};

			selectedRow = ClientTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(null, " ❗회원 선택을 해주세요 ❗️");
			} else {

				for (int j = 0; j < 7; j++) {
					data_in = new Vector<>();
					Click_data[j] = ((String) ClientTable.getValueAt(selectedRow, j));
					data_in.add(title_m[j]);
					data_in.add(Click_data[j]); // ������ ������ ����
					data_m.add(data_in); // ���̺� ���� ����
				}
				getFrame();
			}
		}

		/********** Modify Frame **********/
		public void getFrame() {

			ModifyFrame = new JFrame("회원 정보 수정");
			ModifyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			ModifyFrame.setBounds(100, 100, 450, 415);

			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			ModifyFrame.getContentPane().add(contentPane);
			contentPane.setLayout(null);

			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(32, 28, 370, 250);
			contentPane.add(scrollPane);

			ModifyTable = new JTable(data_m, column_T);
			scrollPane.setViewportView(ModifyTable);
			ModifyFrame.setVisible(true);
			ModifyTable.getTableHeader().setReorderingAllowed(false);
			ModifyTable.getTableHeader().setResizingAllowed(false);
			ModifyTable.setRowHeight(25);
			ModifyTable.getColumn("첫번째 컬럼").setPreferredWidth(100);
			ModifyTable.getColumn("두번째 컬럼").setPreferredWidth(270);

			JButton btnmodifyset = new JButton("수정 완료");
			btnmodifyset.setForeground(Color.DARK_GRAY);
			btnmodifyset.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 14));
			btnmodifyset.setBounds(32, 302, 370, 40);
			contentPane.add(btnmodifyset);
			btnmodifyset.addActionListener(new ModifyEndActionListener());
		}

		// 관리자(회원수정) 
		public class ModifyEndActionListener implements ActionListener {
			String[] modify_data = new String[8];
			String sql_update;
			PreparedStatement pstmt_m;
			String m_birth;

			public void actionPerformed(ActionEvent arg0) {
				System.out.println("[관리자] 회원 수정 Action");

				for (int j = 0; j < 8; j++) {
					modify_data[j] = ((String) ModifyTable.getValueAt(j, 1));
					System.out.println("---------------------------");
					System.out.print(ModifyTable.getValueAt(j, 0) + " : ");
					System.out.println(modify_data[j]);
				}

				try {
					Date m_date = new SimpleDateFormat("yyyyMMdd").parse(modify_data[1]);
					m_birth = new SimpleDateFormat("yyyyMMdd").format(m_date);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					sql_update = "UPDATE CLIENT SET NAME = ?, BIRTH = ?, EMAIL = ?, ADDR = ?, PHONE = ?, ID = ?,PASSWORD = ?";
					sql_update += "WHERE ID=?";

					pstmt_m = mysqlConn.prepareStatement(sql_update);

					pstmt_m.setString(1, modify_data[0]);
					pstmt_m.setString(2, m_birth);
					pstmt_m.setString(3, modify_data[2]);
					pstmt_m.setString(4, modify_data[3]);
					pstmt_m.setString(5, modify_data[4]);
					pstmt_m.setString(6, modify_data[5]);
					pstmt_m.setString(7, modify_data[6]);
					pstmt_m.setString(8, modify_data[7]);
					pstmt_m.setString(9, modify_data[6]);

					pstmt_m.executeUpdate();
				} catch (Exception e) {
					System.out.println("ȸ�� ���� ���� ���� : " + e.getMessage());
				} finally {
					try {
						pstmt_m.close();
					} catch (Exception ee) {
					}
				}
			}
		}
	}

	public class FindActionListener extends JFrame implements ActionListener {
		JFrame Find_C;
		JPanel contentPane;
		JComboBox<String> cmbKind;
		JTextField txtFindField;
		String[] Find_K = {"이름", "아이디" };
		FindActionListener FindClient;
		String Title;
		String Text;
		String sql_Find;
		PreparedStatement pstmt_F;
		ResultSet rs;
		int FindRow;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			FindClient = new FindActionListener();
			FindClient.Find_C.setVisible(true);
		}

		public FindActionListener() {
			Find_C = new JFrame("회원 찾기");
			Find_C.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			Find_C.setBounds(100, 100, 500, 165);
			contentPane = new JPanel();
			contentPane.setBackground(Color.WHITE);
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			Find_C.getContentPane().add(contentPane);
			contentPane.setLayout(null);

			cmbKind = new JComboBox<String>();
			cmbKind.setBounds(14, 43, 118, 41);
			cmbKind.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 14));
			cmbKind.setModel(new DefaultComboBoxModel<>(Find_K));
			contentPane.add(cmbKind);

			txtFindField = new JTextField();
			txtFindField.setBounds(145, 44, 243, 41);
			contentPane.add(txtFindField);
			txtFindField.setColumns(10);

			JButton btnFind = new JButton("찾기");
			btnFind.setBackground(new Color(51, 0, 102));
			btnFind.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 14));
			btnFind.setBounds(402, 43, 66, 41);
			contentPane.add(btnFind);
			btnFind.addActionListener(new FindbuttonActionListener());

		}

		/********** 버튼 액션리스너  ***************/
		public class FindbuttonActionListener implements ActionListener {

			public void actionPerformed(ActionEvent arg0) {
				Title = cmbKind.getSelectedItem().toString();
				Text = txtFindField.getText();

				String rsValue = null;

				if (Text.equals("")) {
					JOptionPane.showMessageDialog(null, "* 정보를 입력해주세요 ! *");
				} else {
					try {
						if (Title.equals(Find_K[0])) { // �̸����� �˻��Ѵٸ�
							sql_Find = "SELECT * FROM CLIENT WHERE NAME = ?";
							pstmt_F = mysqlConn.prepareStatement(sql_Find);
							pstmt_F.setString(1, Text);

							rs = pstmt_F.executeQuery();

							if (rs.next()) {
								rsValue = rs.getString("NAME");
								System.out.println(rsValue);
							}

							for (int j = 0; j < 8; j++) {
								String str = ClientTable.getValueAt(j, 0).toString();
								System.out.println(str);
								if (str.equals(rsValue)) {
										ClientTable.changeSelection(j, 1, false, false);
										break;
									}
								}
							
						} else if (Title.equals(Find_K[1])) {
							sql_Find = "SELECT * FROM CLIENT WHERE ID = ?";
							pstmt_F = mysqlConn.prepareStatement(sql_Find);
							pstmt_F.setString(1, Text);
							rs = pstmt_F.executeQuery();

							if (rs.next()) {
								rsValue = rs.getString(7);
							}

							for (int j = 0; j < 8; j++) {
								if (ClientTable.getValueAt(j, 6).equals(rsValue)) {
										ClientTable.changeSelection(j, 6, false, false);
										break;
								}
							}
						}
					} catch (Exception e) {
						System.out.println("에러 : " + e.getMessage());
					}
				}
			}
		}
	}
}

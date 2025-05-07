package chat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class Search extends JFrame implements ActionListener {

	public JFrame SearchFrame;
	private JPanel contentPane;
	private JTextField txtEmail1;
	private JTextField txtEmail2;
	private JTextField txtId;
	private JRadioButton radioID;
	private JRadioButton radioPW;
	private JPanel SearchID;
	private JPanel SearchPW;
	private JButton btnIdSearch;
	private JButton btnPwSearch;
	private JLabel resultId;
	private JLabel resultPw;
	
	PreparedStatement pstmt = null;
	Connection mysqlConn = null;

	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Search frame = new Search();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/********  Create the frame. ***********/
	
	public Search() {
		SearchFrame = new JFrame("아이디/비밀번호 찾기");
		SearchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SearchFrame.setBounds(100, 100, 500, 351);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		SearchFrame.getContentPane().add(contentPane);
		contentPane.setLayout(null);
		
		radioID = new JRadioButton(" \uC544\uC774\uB514 \uCC3E\uAE30");
		radioID.setBackground(Color.gray);
		radioID.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		radioID.setSelected(true);
		radioID.setHorizontalAlignment(SwingConstants.CENTER);
		radioID.setBounds(89, 31, 130, 35);
		contentPane.add(radioID);
		radioID.addActionListener(this);
		
		radioPW = new JRadioButton(" \uBE44\uBC00\uBC88\uD638 \uCC3E\uAE30");
		radioPW.setBackground(Color.gray);
		radioPW.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		radioPW.setSelected(false);
		radioPW.setHorizontalAlignment(SwingConstants.CENTER);
		radioPW.setBounds(238, 31, 140, 35);
		contentPane.add(radioPW);
		radioPW.addActionListener(this);
		
		SearchID = new JPanel();
		SearchID.setBorder(new LineBorder(Color.DARK_GRAY, 2));
		SearchID.setBackground(Color.WHITE);
		SearchID.setBounds(14, 84, 454, 208);
		contentPane.add(SearchID);
		SearchID.setLayout(null);
		SearchID.setVisible(true);
		
		JLabel lblem = new JLabel("\uC774\uBA54\uC77C");
		lblem.setHorizontalAlignment(SwingConstants.CENTER);
		lblem.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		lblem.setBounds(13, 70, 60, 39);
		SearchID.add(lblem);
		
		txtEmail1 = new JTextField();
		txtEmail1.setColumns(10);
		txtEmail1.setBounds(79, 70, 110, 39);
		SearchID.add(txtEmail1);
		
		JLabel lblNewLabel = new JLabel("@");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("���ʷҹ���", Font.PLAIN, 18));
		lblNewLabel.setBounds(195, 70, 38, 40);
		SearchID.add(lblNewLabel);
		
		txtEmail2 = new JTextField();
		txtEmail2.setColumns(10);
		txtEmail2.setBounds(239, 70, 124, 39);
		SearchID.add(txtEmail2);
		
		btnIdSearch = new JButton("\uCC3E\uAE30");
		btnIdSearch.setForeground(Color.DARK_GRAY);
		btnIdSearch.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		btnIdSearch.setBounds(377, 70, 65, 40);
		SearchID.add(btnIdSearch);
		btnIdSearch.addActionListener(this);
		
		resultId = new JLabel("");
		resultId.setBackground(Color.WHITE);
		resultId.setForeground(new Color(0, 0, 205));
		resultId.setHorizontalAlignment(SwingConstants.CENTER);
		resultId.setBounds(29, 126, 397, 52);
		resultId.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		SearchID.add(resultId);
		
		JLabel lblNewLabel_1 = new JLabel("\uD68C\uC6D0\uAC00\uC785\uC2DC \uC0AC\uC6A9\uD55C \uC774\uBA54\uC77C\uC744 \uC785\uB825\uD574\uC8FC\uC138\uC694");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("���ʷҹ���", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(13, 12, 427, 39);
		SearchID.add(lblNewLabel_1);
		
		SearchPW = new JPanel();
		SearchPW.setBorder(new LineBorder(Color.DARK_GRAY, 2));
		SearchPW.setBackground(Color.WHITE);
		SearchPW.setBounds(14, 84, 454, 208);
		contentPane.add(SearchPW);
		SearchPW.setLayout(null);
		SearchPW.setVisible(false);
		
		btnPwSearch = new JButton("\uCC3E\uAE30");
		btnPwSearch.setForeground(Color.WHITE);
		btnPwSearch.setBackground(Color.DARK_GRAY);
		btnPwSearch.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		btnPwSearch.setBounds(300, 70, 65, 40);
		SearchPW.add(btnPwSearch);
		btnPwSearch.addActionListener(this);
		
		resultPw = new JLabel("");
		resultPw.setHorizontalAlignment(SwingConstants.CENTER);
		resultPw.setForeground(new Color(0, 0, 205));
		resultPw.setBounds(29, 126, 397, 52);
		resultPw.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		SearchPW.add(resultPw);
		
		JLabel label_3 = new JLabel("\uD68C\uC6D0\uAC00\uC785\uC2DC \uC0AC\uC6A9\uD55C \uC544\uC774\uB514\uB97C \uC785\uB825\uD574\uC8FC\uC138\uC694");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setFont(new Font("���ʷҹ���", Font.PLAIN, 12));
		label_3.setBounds(13, 12, 427, 39);
		SearchPW.add(label_3);
		
		JLabel lblid = new JLabel("\uC544\uC774\uB514");
		lblid.setHorizontalAlignment(SwingConstants.CENTER);
		lblid.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		lblid.setBounds(90, 70, 60, 39);
		SearchPW.add(lblid);
		
		txtId = new JTextField();
		txtId.setColumns(10);
		txtId.setBounds(170, 70, 110, 39);
		SearchPW.add(txtId);
		
		SearchFrame.setVisible(true);
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()== radioID) {
			this.radioPW.setSelected(false);
			this.SearchID.setVisible(true);
			this.SearchPW.setVisible(false);
			txtId.setText("");
		}
		else if(e.getSource()==radioPW) {
			this.radioID.setSelected(false);
			this.SearchID.setVisible(false);
			this.SearchPW.setVisible(true);
			txtEmail1.setText("");
			txtEmail2.setText("");
		}
		else if(e.getSource()==btnIdSearch) {
			String rsId;
			System.out.println("1");
			if((!(txtEmail1.getText().equals("")))&&(!(txtEmail2.getText().equals("")))){
				String Email = txtEmail1.getText().trim() + "@" + txtEmail2.getText().trim();

				System.out.println("2");
				try {
				String sql = "SELECT * FROM CLIENT WHERE EMAIL = ?";
				pstmt = mysqlConn.prepareStatement(sql);
				pstmt.setString(1, Email);
				
				ResultSet rs = pstmt.executeQuery();

				System.out.println(String.valueOf(rs));
				if(rs.next()) {
					rsId = rs.getString("ID");
					resultId.setText(" ȸ���� ���̵�� " + "\"" + rsId + "\" �Դϴ�" );
				}
				else
					resultId.setText(" �ش� ȸ���� �������� �ʽ��ϴ� " );
				} catch (Exception ee) {
					System.out.println("ã�� ���� : " + ee.getMessage());
				}
			} else{
				JOptionPane.showMessageDialog(null, "* 입력 정보를 확인해주세요!! *");
			}
			
		}
		else if(e.getSource()==btnPwSearch) {
			String rsPw;
			String Id = txtId.getText().trim();

			if(!(Id.equals(""))){
				try {
					String sql = "SELECT * FROM CLIENT WHERE ID = ?";
					pstmt = mysqlConn.prepareStatement(sql);
					pstmt.setString(1, Id);
				
					ResultSet rs = pstmt.executeQuery();

					System.out.println(String.valueOf(rs));
					if(rs.next()) {
						rsPw = rs.getString("PASSWORD");
						System.out.println(rsPw);
						resultPw.setText(" ȸ���� ��й�ȣ�� " + "\"" + rsPw + "\" �Դϴ�" );
					} else
					resultPw.setText(" �ش� ȸ���� �������� �ʽ��ϴ� " );
				} catch (Exception ee) {
					System.out.println("ã�� ���� : " + ee.getMessage());
				}
			} else{
				JOptionPane.showMessageDialog(null, "* �̸����� �Է����ּ��� *");
			}
		}
	}
}

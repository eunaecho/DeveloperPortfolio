package chat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class AdminChat extends JFrame implements ItemListener, ActionListener {

	private JPanel contentPane;
	JFrame perChatFrame;
	JButton btnLook;
	String select;

	PreparedStatement pstmt = null;
	Connection mysqlConn = null;
	ResultSet rs = null;
	ResultSet rs1 = null;
	String sql = "";
	String sqllist = "";

	String[] ChatId;
	JComboBox cmbId;
	JList<String> listperChat;

	public AdminChat() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");
			
			sql = "SELECT * FROM CLIENT";

			pstmt = mysqlConn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			Vector<String> id = new Vector<String>();
			while (rs.next()) {
				id.add(rs.getString("ID"));
			}

			ChatId = new String[id.size()];
			for (int i = 0; i < id.size(); i++) {
				ChatId[i] = id.elementAt(i);
			}
		} catch (Exception e) {
			e.getMessage();
		}

		perChatFrame = new JFrame("대화 내용 리스트");
		perChatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		perChatFrame.setBounds(100, 100, 395, 342);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		perChatFrame.getContentPane().add(contentPane);

		listperChat = new JList();
		listperChat.setBounds(29, 113, 321, 113);
		listperChat.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		listperChat.setFixedCellHeight(25);
		contentPane.add(listperChat);

		btnLook = new JButton("here1");
		btnLook.setForeground(Color.darkGray);
		btnLook.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnLook.setBounds(29, 237, 321, 46);
		contentPane.add(btnLook);
		btnLook.addActionListener(this);

		cmbId = new JComboBox<String>();
		cmbId.setModel(new DefaultComboBoxModel<String>(ChatId));
		cmbId.setBounds(29, 55, 321, 46);
		cmbId.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		contentPane.add(cmbId);
		
		JLabel lblClientID = new JLabel("here2");
		lblClientID.setHorizontalAlignment(SwingConstants.CENTER);
		lblClientID.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblClientID.setBounds(146, 12, 104, 31);
		contentPane.add(lblClientID);
		cmbId.addItemListener(this);

		perChatFrame.setVisible(true);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == cmbId) {
			try {
				select = cmbId.getSelectedItem().toString();
				sqllist = "SELECT * FROM ROOMCHAT WHERE ID = ?";
				pstmt = mysqlConn.prepareStatement(sqllist);

				pstmt.setString(1, select);

				rs1 = pstmt.executeQuery();

				String list[] = new String[3];
				if (rs1.next()) {

					list[0] = rs1.getString("NAME1");
					list[1] = rs1.getString("NAME2");
					list[2] = rs1.getString("NAME3");
					listperChat.removeAll();
					listperChat.setListData(list);
				}
			} catch (Exception eee) {
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e1) {
		if (e1.getSource() == btnLook) {
			if (listperChat.getSelectedIndex() == -1)
				JOptionPane.showMessageDialog(null, "사용자를 선택해주세요.", "ERROR", JOptionPane.ERROR_MESSAGE);
			else {
				int a = listperChat.getSelectedIndex();
				Roomchat(a, select);
			}
		}
	}

	/***** ��ȭ�� ��ȭ ��� ******/
	private void Roomchat(int a, String str) {
		++a;
		Connection mysqlConn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		String col = "ROOM" + a;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");

			sql = "SELECT * FROM ROOMCHAT WHERE ID = ?";
			pstmt = mysqlConn.prepareStatement(sql);

			pstmt.setString(1, str);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				Blob mainChat = rs.getBlob(col);

				InputStream M_InStream = mainChat.getBinaryStream();
				String s = "";

				while (true) {
					int len = M_InStream.available();
					byte[] bytes = new byte[len];

					if (M_InStream.read() == -1)
						break;

					ByteArrayOutputStream BaOs_Chat = new ByteArrayOutputStream();
					M_InStream.read(bytes, 0, len);
					BaOs_Chat.write(bytes);

					s = new String(bytes, 0, len);

				}
				TextChat Chattinglog = new TextChat(select + "���� ��ȭ����");
				Chattinglog.ChatText.setText(s);
				Chattinglog.ChatLog.setVisible(true);
			}
		} catch (Exception e) {
			System.out.println("��ȭ�� ä�� ���� : " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
				mysqlConn.close();
			} catch (Exception e) {
			}
		}
	}
}

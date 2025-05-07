package chat;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import chat.Server.RoomInfo;
import chat.Server.UserInfo;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JList;

import java.awt.Font;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class ChatServer extends JFrame implements ActionListener, MouseListener {

	JFrame AdminFrame;
	private JPanel contentPane;
	private JButton btnSerStart;
	private JButton btnSerQuit;
	private JButton sbtnRemoveR;
	static ChatServer Admin;
	public static JList<String> SlistChatFriend;
	public static JList<String> SlistChatRoom;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	JLabel Chatlist;

	private String[] data;
//	private String[] title;
	private JPanel panel;

	/******** Chat Server Start() *********/
	public class ServerStartActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			Server.C_server.Server_Start();
			btnSerStart.setEnabled(false);
			btnSerQuit.setEnabled(true);
		}
	}

	/******* Chat Server Quit() ********/
	public class ServerQuitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			btnSerQuit.setEnabled(false);
			btnSerStart.setEnabled(true);

			String name = null;
			// ���̿��ڵ� MyRoom���� ����
			for (int i = 0; i < Server.room_V.size(); i++) {
				RoomInfo r = (RoomInfo) Server.room_V.elementAt(i);
				name = r.Room_name;
				// �����ڵ��� MyRoom���� ����
				r.BroadCast_Room("rmRoom_S/" + r.Room_name);

				// ������ ���Ϳ��� ��� ����� ����
				r.Room_user_V.removeAllElements();
				Server.room_V.removeElement(i);
			}
			// ��ü ����ڵ��� �� ��Ͽ��� ����
			for (int k = 0; k < Server.logOn_V.size(); k++) {
				UserInfo n = (UserInfo) Server.logOn_V.elementAt(k);
				n.send_Message("rmAllRoom_S/" + name);
			}

			Server.room_V.removeAllElements();
			Server.ChatRoom_V.removeAllElements();

			Server.room_V_list.removeAllElements();
			SlistChatRoom.setListData(Server.room_V_list);

			// 1. �� ȸ���� ���� ����, 2. logOn_V, logOn_S���� ���� 3. ������ ����Ʈ ����
			for (int i = 0; i < Server.logOn_V.size(); i++) {
				try {
					Server.UserInfo u = (UserInfo) Server.logOn_V.elementAt(i);
					u.send_Message("ServerQuit/" + u.ClientInfor);
					u.per_socket.close();
				} catch (Exception e) {
					System.out.println("�������� ȸ�� ���� ���� : " + e.getMessage());
				}
			}
			Server.logOn_S.removeAllElements();
			Server.logOn_V.removeAllElements();
			SlistChatFriend.setListData(Server.logOn_S);

			Server.C_server.QuitConnect();
		}

	}

	/*** Launch the application ****/
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Admin = new ChatServer();
					Admin.AdminFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/***** Frame ****/
	public ChatServer() {
		AdminFrame = new JFrame();
		AdminFrame.addWindowListener(new ServerWindowClosingEventHandler());
		AdminFrame.setBounds(100, 100, 498, 595);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		AdminFrame.getContentPane().add(contentPane);

		btnSerStart = new JButton("채팅 서버 시작");
		btnSerStart.setForeground(Color.DARK_GRAY);
		btnSerStart.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		btnSerStart.setBounds(25, 140, 137, 35);
		contentPane.add(btnSerStart);
		ActionListener listener_SerStart = new ServerStartActionListener();
		btnSerStart.addActionListener(listener_SerStart);

		btnSerQuit = new JButton("채팅 서버 종료");
		btnSerQuit.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		btnSerQuit.setBounds(188, 140, 137, 35);
		btnSerQuit.setForeground(Color.DARK_GRAY);
		btnSerQuit.setEnabled(false);
		contentPane.add(btnSerQuit);
		ActionListener listener_SerQuit = new ServerQuitActionListener();
		btnSerQuit.addActionListener(listener_SerQuit);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 245, 196, 212);
		contentPane.add(scrollPane);

		SlistChatFriend = new JList<String>();
		scrollPane.setViewportView(SlistChatFriend);
		SlistChatFriend.setBackground(Color.WHITE);
		SlistChatFriend.setBorder(new LineBorder(new Color(210, 180, 140), 2, true));
		SlistChatFriend.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));

		JLabel lblNewLabel = new JLabel("여기 모야?");
		lblNewLabel.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(28, 201, 193, 32);
		contentPane.add(lblNewLabel);

		JButton sbtnClientTable = new JButton("회원 DB 관리");
		sbtnClientTable.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		sbtnClientTable.setBounds(25, 480, 196, 35);
		sbtnClientTable.setForeground(Color.DARK_GRAY);
		contentPane.add(sbtnClientTable);
		ActionListener listener_c = new ClientManageActionListener();
		sbtnClientTable.addActionListener(listener_c);

		sbtnRemoveR = new JButton("대화방 삭제");
		sbtnRemoveR.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		sbtnRemoveR.setForeground(Color.DARK_GRAY);
		sbtnRemoveR.setBounds(260, 480, 196, 35);
		contentPane.add(sbtnRemoveR);
		sbtnRemoveR.addActionListener(this);

		JLabel label = new JLabel("\uB300\uD654\uBC29 \uBAA9\uB85D");
		label.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(260, 201, 193, 32);
		contentPane.add(label);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(260, 245, 196, 212);
		contentPane.add(scrollPane_1);

		SlistChatRoom = new JList<String>();
		scrollPane_1.setViewportView(SlistChatRoom);
		SlistChatRoom.setBorder(new LineBorder(new Color(210, 180, 140), 2, true));
		SlistChatRoom.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		
		/**** ���̺� ����  *******/
		getInfo();

		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new TitledBorder(new LineBorder(new Color(64, 64, 64), 2), "관리자 정", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font("���ʷҹ���", Font.PLAIN, 15), Color.BLACK));
		panel.setBounds(25, 24, 430, 100);
		contentPane.add(panel);
		panel.setLayout(null);
		
		// JLabel id = new JLabel(title[0] + " : " + data[0]);
		JLabel id = new JLabel(" 관리자 " + data[2]);
		id.setBackground(Color.WHITE);
		id.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		id.setBounds(14, 28, 402, 31);
		panel.add(id);
		
		// JLabel pass = new JLabel(title[1] + " : " + data[1]);
		JLabel pass = new JLabel(" 관리자 ID " + data[0]);
		pass.setBackground(Color.WHITE);
		pass.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		pass.setBounds(14, 60, 402, 31);
		panel.add(pass);
		
		Chatlist = new JLabel("��ȭ ���");
		Chatlist.setBackground(Color.WHITE);
		Chatlist.setHorizontalAlignment(SwingConstants.CENTER);
		Chatlist.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		Chatlist.setBounds(345, 141, 111, 35);
		contentPane.add(Chatlist);
		Chatlist.addMouseListener(this);
		

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sbtnRemoveR) {
			String rmRoom = SlistChatRoom.getSelectedValue();
			// ���̿��ڵ� MyRoom���� ����, ��ü ����ڵ��� �� ��Ͽ��� ����
			for (int i = 0; i < Server.room_V.size(); i++) {
				RoomInfo r = (RoomInfo) Server.room_V.elementAt(i);
				if (r.Room_name.equals(rmRoom)) {
					// �����ڵ��� MyRoom ���� ����
					r.BroadCast_Room("rmRoom_S/" + rmRoom);

					// ������ ���Ϳ��� ��� ����� ����
					r.Room_user_V.removeAllElements();
					Server.room_V.remove(i);
				}
			}
			for (int j = 0; j < Server.ChatRoom_V.size(); j++) {
				ChatRoom R = (ChatRoom) Server.ChatRoom_V.elementAt(j);
				if (R.Room_name.equals(rmRoom)) {
					Server.ChatRoom_V.remove(j);
				}
			}
			for (int k = 0; k < Server.logOn_V.size(); k++) {
				UserInfo n = (UserInfo) Server.logOn_V.elementAt(k);
				n.send_Message("rmAllRoom_S/" + rmRoom);

			}
			for (int c = 0; c < Server.room_V_list.size(); c++) {
				String S_rm = (String) Server.room_V_list.elementAt(c);
				if (S_rm.equals(rmRoom)) {
					Server.room_V_list.remove(c);
					SlistChatRoom.setListData(Server.room_V_list);
				}
			}

		}
	}

	class ServerWindowClosingEventHandler extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			btnSerQuit.setEnabled(false);
			btnSerStart.setEnabled(true);

			String name = null;
			// My ROOM
			for (int i = 0; i < Server.room_V.size(); i++) {
				RoomInfo r = (RoomInfo) Server.room_V.elementAt(i);
				name = r.Room_name;
				// My Room
				r.BroadCast_Room("rmRoom_S/" + r.Room_name);

				r.Room_user_V.removeAllElements();
				Server.room_V.removeElement(i);
			}
			
			for (int k = 0; k < Server.logOn_V.size(); k++) {
				UserInfo n = (UserInfo) Server.logOn_V.elementAt(k);
				n.send_Message("rmAllRoom_S/" + name);
			}

			Server.room_V.removeAllElements();
			Server.ChatRoom_V.removeAllElements();

			Server.room_V_list.removeAllElements();
			SlistChatRoom.setListData(Server.room_V_list);

			// 1. �� ȸ���� ���� ����, 2. logOn_V, logOn_S���� ���� 3. ������ ����Ʈ ����
			for (int i = 0; i < Server.logOn_V.size(); i++) {
				try {
					Server.UserInfo u = (UserInfo) Server.logOn_V.elementAt(i);
					u.send_Message("ServerQuit/" + u.ClientInfor);
					u.per_socket.close();
				} catch (Exception ee) {
					System.out.println("�������� ȸ�� ���� ���� : " + ee.getMessage());
				}
			}
			Server.logOn_S.removeAllElements();
			Server.logOn_V.removeAllElements();
			SlistChatFriend.setListData(Server.logOn_S);

			Server.C_server.QuitConnect();
		}
	}

	/***** 관리자 정보 불러오기 *******/
	private void getInfo() {
		Connection mysqlConn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");

			if (mysqlConn == null) {
				System.out.println("DB Connect ERROR");
			} else {
				sql = "SELECT id, password, name FROM admin";
				pstmt = mysqlConn.prepareStatement(sql);
				rs = pstmt.executeQuery();

//				title = new String[] {"admin info1", "admin info2", "admin info3"};
				
				data = new String[3];
				if (rs.next()) {
					data[0] = rs.getString("id");
					data[1] = rs.getString("password");
					data[2] = rs.getString("name");
				}

				rs.close();
				pstmt.close();
				mysqlConn.close();
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getSource()==Chatlist) {
			AdminChat ad = new AdminChat();
		}
	}
}

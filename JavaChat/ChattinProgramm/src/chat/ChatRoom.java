package chat;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


class ChatRoom extends JFrame implements ActionListener, KeyListener, MouseListener {

	JFrame ChatRoom;
	JPanel contentPane;
	JTextField txtInput;
	JTextPane RoomChat;
	JButton btnSendInRoom;
	JButton btnEmotion;
	JButton btnRoomOut;
	JButton btnSaveThis;
	JLabel lblColor;

	String UserName; 
	public String Room_name; // ���̸�
	// private Vector Room_user_V = new Vector(); // �� ���� ����
	static Vector<ChatClient> Client_V = new Vector<ChatClient>();

	Vector InRoomList = new Vector();
	JList<String> InRoomFriendlist;

	byte[] profile;

	// font color
	Color myColor = Color.BLACK;
	int R = Color.BLACK.getRed();
	int G = Color.BLACK.getGreen();
	int B = Color.BLACK.getBlue();

	/***** Dialog Record *****/
	FileWriter fw;
	String fileName;
	String filePath;
	int iYear = 0, iMonth = 0, iDate = 0, iHour = 0, iMinute = 0, iSecond = 0;
	String iGetDate = null;

	/******* Frame ************/
	public ChatRoom(String RoomName) {
		ChatRoom = new JFrame(RoomName);
		Room_name = RoomName;
		ChatRoom.setBounds(100, 100, 600, 598);
		ChatRoom.addWindowListener(new JFrameWindowClosingEventHandler(this));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		ChatRoom.getContentPane().add(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 408, 551);
		panel.setBackground(Color.WHITE);
		contentPane.add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 490, 305, 35);
		panel.add(scrollPane);

		txtInput = new JTextField();
		scrollPane.setViewportView(txtInput);
		txtInput.setColumns(10);
		txtInput.addKeyListener(this);

		btnSendInRoom = new JButton("\uC804\uC1A1");
		btnSendInRoom.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		btnSendInRoom.setBackground(new Color(51, 51, 51));
		btnSendInRoom.setForeground(new Color(255, 255, 255));
		btnSendInRoom.setBounds(331, 490, 63, 35);
		panel.add(btnSendInRoom);
		btnSendInRoom.addActionListener(this);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(14, 24, 380, 411);
		panel.add(scrollPane_1);

		RoomChat = new JTextPane();
		RoomChat.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		RoomChat.setBackground(new Color(255, 255, 255));
		scrollPane_1.setViewportView(RoomChat);
		RoomChat.setEditable(false);

		btnEmotion = new JButton("\u2661");
		btnEmotion.setBounds(14, 443, 58, 35);
		panel.add(btnEmotion);
		btnEmotion.setForeground(Color.RED);
		btnEmotion.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		btnEmotion.setBackground(Color.WHITE);
		btnEmotion.addActionListener(this);

		lblColor = new JLabel("Color");
		lblColor.setFont(new Font("���ʷҹ���", Font.PLAIN, 15));
		lblColor.setBackground(Color.WHITE);
		lblColor.setHorizontalAlignment(SwingConstants.CENTER);
		lblColor.setBounds(311, 447, 83, 35);
		panel.add(lblColor);
		lblColor.addMouseListener(this);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(408, 0, 175, 603);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel label = new JLabel("\uB300\uD654\uBC29 \uCE5C\uAD6C \uBAA9\uB85D");
		label.setBackground(new Color(250, 235, 215));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(14, 12, 145, 43);
		panel_1.add(label);

		JScrollPane scrlChatFriend = new JScrollPane();
		scrlChatFriend.setBounds(14, 67, 145, 176);
		panel_1.add(scrlChatFriend);

		InRoomFriendlist = new JList<String>();
		InRoomFriendlist.setForeground(new Color(0, 0, 0));
		InRoomFriendlist.setFont(new Font("���ʷҹ���", Font.PLAIN, 14));
		scrlChatFriend.setViewportView(InRoomFriendlist);

		btnRoomOut = new JButton("��ȭ�� ������");
		btnRoomOut.setFont(new Font("���ʷҹ���", Font.PLAIN, 14));
		btnRoomOut.setBackground(new Color(51, 51, 51));
		btnRoomOut.setBounds(14, 489, 145, 35);
		panel_1.add(btnRoomOut);
		btnRoomOut.addActionListener(this);

		btnSaveThis = new JButton("\uB300\uD654 \uB0B4\uC6A9 \uC800\uC7A5");
		btnSaveThis.setBackground(new Color(255, 255, 255));
		btnSaveThis.setForeground(new Color(0, 0, 0));
		btnSaveThis.setBounds(12, 255, 147, 27);
		panel_1.add(btnSaveThis);
		btnSaveThis.addActionListener(this);

		setDesign();
	}

	/******* ä�� ���� ���� **********/
	void Save() {
		String saveStr = " ��" + iGetDate + "��\r\n" + RoomChat.getText(); // ��ȭ ���� ����

		System.out.println("���� �ҷ���");
		try {
			fw = new FileWriter(filePath);
			fw.write(saveStr);
			fw.close();

		} catch (Exception e) {
			System.out.println("��ȭ���� ���� ���� : " + e.getMessage());
		}
	}

	public void setPath() {
		Calendar cal = Calendar.getInstance();

		iYear = cal.get(Calendar.YEAR);
		iMonth = cal.get(Calendar.MONTH) + 1;
		iDate = cal.get(Calendar.DATE);
		iHour = cal.get(Calendar.HOUR_OF_DAY);
		iMinute = cal.get(Calendar.MINUTE);
		iSecond = cal.get(Calendar.SECOND);

		iGetDate = String.format("%04d%02d%02d_%02d:%02d:%02d", iYear, iMonth, iDate, iHour, iMinute, iSecond);
		// ���ϸ�
		fileName = this.Room_name + "_" + String.format("%04d%02d%02d", iYear, iMonth, iDate);

		// ���� ���
		filePath = "C:\\Users\\EUNAE Cho\\SaveChat\\PerChat\\" + fileName + ".txt";
		System.out.println(fileName);
		System.out.println(filePath);
	}

	public void setDesign() {
		setPath(); // �޸��� ��� ����

		try {
			fw = new FileWriter(filePath); // �޸��� ����
		} catch (IOException ioe) {
			System.out.println("�޸��� ���� ���� : " + ioe.getMessage());
		}
	}

	public void actionPerformed(ActionEvent ae) {
		// �޽��� ����
		if (ae.getSource() == btnSendInRoom) {
			for (int i = 0; i < Client_V.size(); i++) {
				ChatClient c = (ChatClient) Client_V.elementAt(i);
				if (c.ClientInfor.equals(this.UserName)) {
					c.SendMessage("InRoomMainChat/" + this.Room_name + "/" + this.UserName + "/" + c.ClientId + "/"
							+ txtInput.getText().trim() + "/" + R + "/" + G + "/" + B);
					txtInput.setText("");
					txtInput.requestFocus();
				}
			}
		}
		// �̸�Ƽ�� ����
		else if (ae.getSource() == btnEmotion) {
			for (int i = 0; i < Client_V.size(); i++) {
				ChatClient c = (ChatClient) Client_V.elementAt(i);
				if (c.ClientInfor.equals(this.UserName)) {
					c.SendMessage("RoomEmotion/" +this.Room_name + "/" + this.UserName + "/" + c.ClientId + "/e1" + "/" + R + "/" + G + "/" + B);
					txtInput.setText("");
					txtInput.requestFocus();
				}

			}
		}
		// ������ ��ư Ŭ��
		if (ae.getSource() == btnRoomOut) {
			for (int i = 0; i < Client_V.size(); i++) {
				ChatClient c = (ChatClient) Client_V.elementAt(i);
				if (c.ClientInfor.equals(this.UserName)) {
					c.SendMessage("RemoveMyRoom/" + this.Room_name + "/" + this.UserName);
					this.ChatRoom.setVisible(false);
				}
			}
		}
		// ��ȭ ���� ����
		if (ae.getSource() == btnSaveThis) {
			for (int i = 0; i < Client_V.size(); i++) {
				ChatClient c = (ChatClient) Client_V.elementAt(i);
				if (c.ClientInfor.equals(this.UserName)) {
					if (c.saveOrder == 3) // ��ȭ���� DB�� ��á�ٸ�
						c.saveOrder = 1;
					else
						c.saveOrder++; // �ƴϸ�

					try {

						/**** ������ DB�� ���� *****/
						File saveFile = new File(filePath); // ���ϰ��
						FileInputStream fis = new FileInputStream(saveFile);
						
						//DB
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection mysqlConn = DriverManager.getConnection("jdbc:mysql:://localhost:3306/chat_db", "root", "wpfflWpf2!");
						String sql = null;

						if (mysqlConn == null)
							System.out.println("ä�� ���� ���� ���� ");
						else {
							System.out.println(c.saveOrder);

							// ������ ���� ������ �ٸ���
							if (c.saveOrder == 1) {
								sql = "UPDATE ROOMCHAT SET NAME1 = ?, ROOM1 = ? WHERE ID = ? ";
							} else if (c.saveOrder == 2) {
								sql = "UPDATE ROOMCHAT SET  NAME2 = ?, ROOM2 = ? WHERE ID = ? ";
							} else if (c.saveOrder == 3) {
								sql = "UPDATE ROOMCHAT SET NAME3 = ?, ROOM3 = ? WHERE ID = ? ";
							}
							System.out.println(sql);

							PreparedStatement pstmt = mysqlConn.prepareStatement(sql);

							ByteArrayOutputStream BaOs_Save = new ByteArrayOutputStream();

							while (true) {
								int x = fis.read();
								int len = fis.available();
								if (x == -1)
									break;
								BaOs_Save.write(x);

								byte[] byte_s = new byte[len];

								byte_s = BaOs_Save.toByteArray();
							}

							ByteArrayInputStream BaIs_Save = new ByteArrayInputStream(BaOs_Save.toByteArray());

							pstmt.setString(1, fileName);
							pstmt.setBinaryStream(2, BaIs_Save, BaOs_Save.size());
							pstmt.setString(3, c.ClientId);

							pstmt.executeUpdate();

							fis.close();
							BaOs_Save.close();
							pstmt.close();
							mysqlConn.close();
						}
					} catch (Exception e) {
						e.getStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 10) { // ����ĥ ���
			for (int i = 0; i < Client_V.size(); i++) {
				ChatClient c = (ChatClient) Client_V.elementAt(i);
				if (c.ClientInfor.equals(this.UserName)) {
					c.SendMessage("InRoomMainChat/" + this.Room_name + "/" + this.UserName + "/" + c.ClientId + "/"
							+ txtInput.getText().trim() + "/" + R + "/" + G + "/" + B);
					txtInput.setText("");
					txtInput.requestFocus();
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	/****** ������ ���� ä�� ȭ�� ********/

	public void Profile(String str) throws SQLException {
		Connection mysqlConn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			mysqlConn = DriverManager.getConnection("jdbc:mysql:://localhost:3306:database?serverTimezone=UTC", "eunae", "wpfflWpf2!");

			sql = "SELECT * FROM CLIENT WHERE ID = ?";
			pstmt = mysqlConn.prepareStatement(sql);

			pstmt.setString(1, str);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				Blob aBlob = rs.getBlob("IMAGE"); // �÷����� IMAGE�� ������ �̹����� �޾ƿ�

				InputStream InStream = aBlob.getBinaryStream();

				int readBytes;
				byte[] buffer = new byte[2048];
				ByteArrayOutputStream os = new ByteArrayOutputStream();

				while (true) {
					readBytes = InStream.read(buffer);
					if (readBytes == -1)
						break;
					os.write(buffer, 0, readBytes);
					profile = os.toByteArray();
				}
			}

		} catch (Exception e) {
			System.out.println("������ ��� ���� : " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
				mysqlConn.close();
			} catch (Exception e) {
			}
		}
	}

	/******** ä�ù� �ݱ� ***********/
	class JFrameWindowClosingEventHandler extends WindowAdapter {
		ChatRoom room;

		JFrameWindowClosingEventHandler(ChatRoom r) {
			room = r;
		}

		public void windowClosing(WindowEvent e) {
			room.ChatRoom.dispose();
			try {
				for (int i = 0; i < Client_V.size(); i++) {
					ChatClient c = (ChatClient) Client_V.elementAt(i);
					if (c.ClientInfor.equals(room.UserName)) {
						c.SendMessage("RemoveMyRoom/" + room.Room_name + "/" + room.UserName);
						room.ChatRoom.setVisible(false);
					}
				}
			} catch (Exception e1) {
				System.out.println("�ݱ� ���� : " + e1.getMessage());
			}
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
		if (e.getSource() == lblColor) {
			myColor = JColorChooser.showDialog(null, "내 폰트 색상", myColor);
			if (myColor != null) {
				R = myColor.getRed();
				G = myColor.getGreen();
				B = myColor.getBlue();
				System.out.println(myColor);
				System.out.println(R);
				System.out.println(G);
				System.out.println(B);
			}
		}
	}
}

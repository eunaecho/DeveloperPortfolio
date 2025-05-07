package chat;

import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

import java.net.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import java.io.*;

public class ChatClient extends JFrame implements ActionListener, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L; 

	Socket clientSocket = null;
	BufferedReader read; 
	InputStream InStream;
	OutputStream OutStream;
	DataOutputStream D_OutStream; 
	DataInputStream D_InStream; 
	ObjectInputStream O_InStream;
	ObjectOutputStream O_OutStream;
	ByteArrayOutputStream bos;
	byte[] emotion1;
	String sendData; 
	String receiveData;
	StringTokenizer st;
	byte[] profile;

	String ClientId; 
	String ClientInfor; 
	String ClientNAME;
	boolean endflag = false;

	Vector<String> user_list = new Vector<String>();
	static Vector<String> room_list = new Vector<String>(); 
	Vector<String> myRoom_list = new Vector<String>();

	JFrame ClientFrame;
	private JPanel contentPane;
	JTextPane txtBroadCast;
	JMenuItem Logout;
	JMenuItem Exit;
	JMenuItem FileSend;
	JLabel lblmainChat;
	JLabel lblroomChat;
	JButton btnFileSend;
	JButton btnEmotion1;
	JButton btnEmotion2;
	JButton btnEmotion3;
	JLabel lblColor;

	JTextField txtMainTalk;
	JButton btnMkRoom;
	JButton btnRmvRoom;
	JButton btnGetIn;
	JButton btnSend;
	JButton SetInfo;
	JLabel ClientName;
	ImageIcon icon2; // ư

	ImageIcon icon; // ʻ
	byte[] photo = null; // 
	JLabel ClientImage;
	Image img;
	byte[] emo_db;
	ImageIcon emoIcon;

	Color myColor = Color.BLACK;
	int R = Color.BLACK.getRed();
	int G = Color.BLACK.getGreen();
	int B = Color.BLACK.getBlue();

	SimpleAttributeSet styleSet;
	SimpleAttributeSet styleSet1;

	static JList<String> ClistChatFriend;
	static JList<String> ClistChatRoom;
	JList<String> ClistMyRoom;

	Vector<ChatRoom> CChatRoom_V = new Vector<ChatRoom>();

	/**** ä   ****/
	FileWriter fw;
	String fileName;
	String filePath;
	int iYear = 0, iMonth = 0, iDate = 0, iHour = 0, iMinute = 0, iSecond = 0;
	String iGetDate = null;

	Vector<String> SaveChatlist = new Vector<String>(3);

	int saveOrder = 0;

	/******** Ķ  *********/
	public ChatClient(String ID, String NAME) {
		ClientId = ID;
		ClientNAME = NAME;
	}

	/********** Frame ************/
	public void ClientFrame(String str1, String str2) { 

		ClientFrame = new JFrame(str2 + "(" + str1 + ")");
		ClientFrame.setBounds(100, 100, 500, 700);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		ClientFrame.getContentPane().add(contentPane);
		ClientFrame.addWindowListener(new JFrameWindowClosingEventHandler());

		JPanel pnlClient = new JPanel();
		pnlClient.setBounds(0, 0, 400, 600);
		contentPane.add(pnlClient);
		pnlClient.setLayout(null);

		try {
			ReadImage image = new ReadImage(str1);

			setDesign();

		} catch (SQLException e) {
			System.out.println("✖ ERROR ✖  : " + e.getMessage());
		}

		ClientImage = new JLabel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
			}
		};
		ClientImage.setIcon(icon);
		ClientImage.setHorizontalAlignment(SwingConstants.CENTER);
		ClientImage.setBounds(14, 12, 100, 100);
		pnlClient.add(ClientImage);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 150, 350, 300);
		pnlClient.add(scrollPane);
		
		txtBroadCast = new JTextPane();
		txtBroadCast.setEditable(false);
		txtBroadCast.setBounds(82, 49, 300, 320);
		txtBroadCast.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		scrollPane.setViewportView(txtBroadCast);

		txtMainTalk = new JTextField();
		txtMainTalk.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		txtMainTalk.setBounds(14, 530, 280, 35);
		pnlClient.add(txtMainTalk);
		txtMainTalk.setColumns(10);
		txtMainTalk.addKeyListener(this);

		btnSend = new JButton("전송");
		btnSend.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnSend.setBounds(320, 530, 70, 38);
		pnlClient.add(btnSend);
		btnSend.addActionListener(this);

		JLabel ClientName = new JLabel();
		ClientName.setFont(new Font("Copperplate Gothic Light", Font.BOLD, 15));
		ClientName.setBounds(182, 12, 200, 44);
		ClientName.setText(str2 + "(" + str1 + ")");
		pnlClient.add(ClientName);

		icon2 = new ImageIcon("C:\\Users\\Eunae Cho\\Pictures\\.png");
		SetInfo = new JButton() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(icon2.getImage(), 0, 0, d.width, d.height, null);
			}
		};
		SetInfo.setBounds(411, 14, 50, 50);
		pnlClient.add(SetInfo);
		ActionListener listener_I = new SetInfoActionListener(str1);
		SetInfo.addActionListener(listener_I);

		lblroomChat = new JLabel(" 내 대화 목록 ");
		lblroomChat.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblroomChat.setHorizontalAlignment(SwingConstants.CENTER);
		lblroomChat.setBounds(200, 90, 168, 33);
		pnlClient.add(lblroomChat);
		lblroomChat.addMouseListener(this);

		lblmainChat = new JLabel("");
		lblmainChat.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblmainChat.setHorizontalAlignment(SwingConstants.CENTER);
		lblmainChat.setBounds(286, 125, 168, 33);
		pnlClient.add(lblmainChat);
		lblmainChat.addMouseListener(this);

		btnEmotion1 = new JButton("이모티콘1");
		btnEmotion1.setBounds(14, 480, 80, 33);
		pnlClient.add(btnEmotion1);
		btnEmotion1.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnEmotion1.setForeground(Color.RED);
		btnEmotion1.addActionListener(this);

		btnEmotion2 = new JButton("이모티콘2");
		btnEmotion2.setBounds(105, 480, 80, 33);
		pnlClient.add(btnEmotion2);
		btnEmotion2.setForeground(Color.RED);
		btnEmotion2.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnEmotion2.addActionListener(this);

		btnEmotion3 = new JButton("이모티콘3");
		btnEmotion3.setBounds(205, 480, 80, 33);
		pnlClient.add(btnEmotion3);
		btnEmotion3.setForeground(Color.RED);
		btnEmotion3.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnEmotion3.addActionListener(this);

		lblColor = new JLabel("Color");
		lblColor.setBounds(300, 480, 100, 33);
		lblColor.setForeground(Color.BLACK);
		pnlClient.add(lblColor);
		lblColor.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblColor.addMouseListener(this);

		JPanel pnlFriend = new JPanel();
		pnlFriend.setBounds(400, 0, 210, 636);
		contentPane.add(pnlFriend);
		pnlFriend.setLayout(null);

		JLabel lblChatFriend = new JLabel("\uC811\uC18D\uD55C \uCE5C\uAD6C");
		lblChatFriend.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblChatFriend.setHorizontalAlignment(SwingConstants.CENTER);
		lblChatFriend.setBounds(10, 5, 182, 32);
		pnlFriend.add(lblChatFriend);

		JScrollPane PnlistChatFriend = new JScrollPane();
		PnlistChatFriend.setBounds(10, 40, 182, 113);
		pnlFriend.add(PnlistChatFriend);

		ClistChatFriend = new JList<String>();
		ClistChatFriend.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		PnlistChatFriend.setViewportView(ClistChatFriend);

		JScrollPane PnlistChatRoom = new JScrollPane();
		PnlistChatRoom.setBounds(10, 200, 182, 113);
		pnlFriend.add(PnlistChatRoom);

		ClistChatRoom = new JList<String>();
		PnlistChatRoom.setViewportView(ClistChatRoom);
		ClistChatRoom.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		ClistChatRoom.setListData(room_list);

		JLabel lblChatRoom = new JLabel("\uC804\uCCB4 \uB300\uD654\uBC29 \uBAA9\uB85D");
		lblChatRoom.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblChatRoom.setHorizontalAlignment(SwingConstants.CENTER);
		lblChatRoom.setBounds(10, 130, 182, 32);
		pnlFriend.add(lblChatRoom);

		btnMkRoom = new JButton("\uBC29\uCD94\uAC00");
		btnMkRoom.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnMkRoom.setBounds(10, 510, 86, 32);
		pnlFriend.add(btnMkRoom);
		btnMkRoom.addActionListener(this);

		btnRmvRoom = new JButton("\uBC29\uC0AD\uC81C");
		btnRmvRoom.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnRmvRoom.setBounds(100, 510, 86, 32);
		pnlFriend.add(btnRmvRoom);
		btnRmvRoom.addActionListener(this);

		btnGetIn = new JButton("\uB300\uD654\uBC29\r\n \uC785\uC7A5");
		btnGetIn.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnGetIn.setBounds(10, 540, 186, 32);
		pnlFriend.add(btnGetIn);
		btnGetIn.addActionListener(this);

		JLabel label = new JLabel("\uB0B4 \uB300\uD654\uBC29 \uBAA9\uB85D");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		label.setBounds(14, 200, 182, 32);
		pnlFriend.add(label);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(14, 350, 180, 128);
		pnlFriend.add(scrollPane_1);

		ClistMyRoom = new JList<String>();
		scrollPane_1.setViewportView(ClistMyRoom);
		ClistMyRoom.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));

		JMenuBar ClientMenuBar = new JMenuBar();
		ClientMenuBar.setBorderPainted(false);
		ClientMenuBar.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		ClientMenuBar.setBounds(0, 0, 676, 38);


		/******** ޴ ޴ ***********/
		JMenu file = new JMenu("");
		file.setMnemonic(KeyEvent.VK_ESCAPE);
		ClientMenuBar.add(file);
		contentPane.add(ClientMenuBar);

		Logout = new JMenuItem("메뉴");
		Logout.setAccelerator(KeyStroke.getKeyStroke('O'));
		file.add(Logout);
		Logout.addMouseListener(this);
		
		FileSend = new JMenuItem("파일전송(미완성)");
		FileSend.setAccelerator(KeyStroke.getKeyStroke('F'));
		file.add(FileSend);
		FileSend.addMouseListener(this);
		
		styleSet = new SimpleAttributeSet();
		StyleConstants.setForeground(styleSet, Color.BLACK); //  
		//StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_RIGHT);
		// StyleConstants.setBackground(styleSet, Color.PINK);  
		StyleConstants.setBold(styleSet, true); // Ʈ Ÿ
		//StyleConstants.setUnderline(styleSet, true); // ؽƮ 

		styleSet1 = new SimpleAttributeSet();
		StyleConstants.setForeground(styleSet1, Color.BLACK); //  
		//StyleConstants.setAlignment(styleSet1, StyleConstants.ALIGN_LEFT);
		// StyleConstants.setBackground(styleSet1, Color.blue);  
		StyleConstants.setBold(styleSet1, true); // Ʈ Ÿ
		//StyleConstants.setUnderline(styleSet1, true); // ؽƮ 

		ClientFrame.setVisible(true);
	}

	public void ClientServer_Start() {
		try {
			InetAddress client_ip = InetAddress.getLocalHost();
			String ip = client_ip.getHostAddress();
			
			clientSocket = new Socket(ip, 9000);
			
			System.out.println("=====	   Client Info	======");
			System.out.println(" ̵ : " + ClientId);
			System.out.println(" ̸ : " + ClientNAME);
			System.out.println(" IP : " + ip);
			System.out.println("==============================");
			System.out.println(ClientId + "");

			if (clientSocket != null) { //   Ǿٸ Connection Լ ȣ
				Connection();
			}

		} catch (UnknownHostException e) {
			System.out.println("ClientServer_Start()κ  : " + e.getMessage());
		} catch (IOException ioe) {
			System.out.println("ClientServer_Start()κ  : " + ioe.getMessage());
		}
	}

	public void Connection() {
		try {
			/********* ޽ , ۽ غ ***********/
			read = new BufferedReader(new InputStreamReader(System.in));
			// ۰ ̿ Է¹ޱ
			System.out.println("Űκ ޽ о Է Ʈ");
			// Űκ ޽ о Է Ʈ
			InStream = clientSocket.getInputStream();
			D_InStream = new DataInputStream(InStream);
			OutStream = clientSocket.getOutputStream();
			D_OutStream = new DataOutputStream(OutStream);
			
			/*********   ̵ ̸  ********/
			D_OutStream.writeUTF(ClientNAME);
			D_OutStream.flush();
			D_OutStream.writeUTF(ClientId);
			D_OutStream.flush();
			
			ClientInfor = ClientNAME + "(" + ClientId + ")";

			/****** ä  Ʈ  *******/
			user_list.add(ClientInfor);
			ClistChatFriend.setListData(user_list);

			/****   ͸ Źޱ   ****/
			ReceiveMessage();
			System.out.println("|->|     ");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Ŭ̾Ʈ  Connetion()  : " + e.getMessage());
		}

	}

	/*****  ->  ޽  κ ********/
	public void SendMessage(String str) {

		try {
			D_OutStream.writeUTF(str);
		} catch (IOException ioe) {
			System.out.println("| -> | ޽ ۽  : " + ioe.getMessage());
		}
	}

	/**** 받는 메시지 스레드  *****/
	public void ReceiveMessage() {
		Thread Receive = new Thread(new Runnable() {
			public void run() {
				try {
					while ((receiveData = D_InStream.readUTF()) != null) {
						System.out.println("  Receive : " + receiveData);
						inmessage(receiveData);
					}
				} catch (IOException ioe1) {
					try {
						OutStream.close();
						InStream.close();
						D_OutStream.close();
						D_InStream.close();
						clientSocket.close();
						System.out.println(" ->  UTF   : " + ioe1.getMessage());
					} catch (IOException ioe2) {
						System.out.println(" ->    + ݱ  : " + ioe2.getMessage());
					}

				}

			}
		});
		Receive.start();
	}

	/****  ޽ ̿  ϱ  *****/
	private void inmessage(String str) {
		st = new StringTokenizer(str, "/");
		
		String protocol = st.nextToken();
		String Message = st.nextToken();

		System.out.println(" : " + protocol);
		System.out.println(" : " + Message);

		if (protocol.equals("NewUser")) // ο
		{
			Update_Friend(Message); //  ڵ ο ڸ Ʈ ߰
		}
		//  ڵ Ʈ ߰
		else if (protocol.equals("OldUser")) {
			Update_Friend(Message);
		}
		//  äù ޾ƿ
		else if (protocol.equals("OldRoom")) {
			room_list.addElement(Message);
			ClistChatRoom.setListData(room_list);
		} else if (protocol.equals("User_out")) {
			user_list.remove(Message);
			ClistChatFriend.setListData(user_list);
		}
		//  
		else if (protocol.equals("CreateRoom")) {
			//  + ̸ +  -> Message : ̸ , msg : 
			String msg = st.nextToken();
			String s =  msg + "\n";

			ChatRoom N_room = new ChatRoom(Message); // äù 
			N_room.Room_name = Message; // äù ̸ ֱ
			N_room.UserName = this.ClientInfor; // äù ̸
			N_room.ChatRoom.setVisible(true); // äâ 

			Server.ChatRoom_V.add(N_room); //   Ϳ ο  ְ(ChatRoom)
			System.out.println(Server.ChatRoom_V.size());

			this.CChatRoom_V.add(N_room); //  Ϳ ο ְ(ChatRoom)

			myRoom_list.addElement(Message);
			ClistMyRoom.setListData(myRoom_list); // Ŭ̾Ʈ äù渮Ʈ 

			N_room.InRoomList.addElement(ClientInfor); // äù  Ʈ ̸ 
			N_room.InRoomFriendlist.setListData(N_room.InRoomList);

			StyledDocument doc = N_room.RoomChat.getStyledDocument();

			try {
				StyleConstants.setForeground(styleSet, Color.BLACK);
				StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_LEFT);
				doc.setParagraphAttributes( doc.getLength(), s.length(), styleSet, false);
				doc.insertString(doc.getLength(), msg + "\n", styleSet);
				N_room.Save();
			} catch (BadLocationException e1) {
				System.out.println("CreateRoom  : " + e1.getMessage());
			}
		}
		// 游⸦  
		else if (protocol.equals("CreateRoomFail")) {
			JOptionPane.showMessageDialog(null, "游  ", "˸", JOptionPane.ERROR_MESSAGE);
		}
		// ο  鶧
		else if (protocol.equals("New_Room")) {
			//  +  ̸ + ̸ -> Message : 游 str1 : ̸
			String str1 = st.nextToken();
			this.room_list.add(str1);
			this.ClistChatRoom.setListData(room_list); //   Ʈ ߰ϰ
		}

		else if (protocol.equals("NotifyJoin")) {
			//  + ̸ +   +  Message ̸,  
			String joinUser = st.nextToken(); //  
			String msg = st.nextToken(); // 
			System.out.println(msg);

			String s = joinUser + msg + "\n";
			
			for (int j = 0; j < CChatRoom_V.size(); j++) {
				ChatRoom r = (ChatRoom) CChatRoom_V.elementAt(j);

				if (r.Room_name.equals(Message)) { // ̸   
					try {
						StyledDocument doc = r.RoomChat.getStyledDocument();
						if (!joinUser.equals(ClientInfor)) { // ڰ ƴϸ

							r.ChatRoom.getFrames();

							// äù濡     Ʈ ī
							r.InRoomList.addElement(joinUser);
							r.InRoomFriendlist.setListData(r.InRoomList);

							StyleConstants.setForeground(styleSet1, Color.BLACK);
							StyleConstants.setAlignment(styleSet1, StyleConstants.ALIGN_LEFT);
							doc.setParagraphAttributes( doc.getLength(),s.length(), styleSet1, false);
							doc.insertString(doc.getLength(), joinUser + msg + "\n", styleSet1);
						}
						if (joinUser.equals(ClientInfor)) {
							StyleConstants.setForeground(styleSet, Color.BLACK);
							StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_RIGHT);
							doc.setParagraphAttributes( doc.getLength(),s.length(), styleSet, false);
							doc.insertString(doc.getLength(), joinUser + msg + "\n", styleSet);
						}
						r.Save();
					} catch (BadLocationException e1) {
						System.out.println("NotifyJoin : " + e1.getMessage());
					}

				} else {
					System.out.println("ϴ");
				}
			}
		}
		// 濡 ο  
		else if (protocol.equals("JoinRoom")) {
			//  + ̸
			ChatRoom JoinNewRoom = new ChatRoom(Message);
			JoinNewRoom.Room_name = Message;
			JoinNewRoom.UserName = this.ClientInfor;
			JoinNewRoom.ChatRoom.setVisible(true);

			this.CChatRoom_V.add(JoinNewRoom); // ش  âͿ   ߰

			this.myRoom_list.addElement(Message);
			this.ClistMyRoom.setListData(myRoom_list); // Ŭ̾Ʈ äù渮Ʈ 

			JoinNewRoom.InRoomList.addElement(ClientInfor); // äù  Ʈ ̸ 
			JoinNewRoom.InRoomFriendlist.setListData(JoinNewRoom.InRoomList);

			JOptionPane.showMessageDialog(null, " äù濡 Ͽϴ. ", "˸", JOptionPane.INFORMATION_MESSAGE);
		}
		//     ޾ƿ
		else if (protocol.equals("Preexistence")) {
			//  + ̸ +   ̸ ϳϳ -> Message : ̸ , pre_user :  ̸
			String pre_user = st.nextToken();

			System.out.println("CChatRoom_V size : " + CChatRoom_V.size());
			for (int c = 0; c < this.CChatRoom_V.size(); c++) {
				ChatRoom r = (ChatRoom) this.CChatRoom_V.elementAt(c);
				String room_name = r.Room_name;
				if (room_name.equals(Message))
					r.InRoomList.add(pre_user);
				r.InRoomFriendlist.setListData(r.InRoomList);
			}

		}
		//    ä (˸ ,,, )
		else if (protocol.equals("MainChatting")) {
			System.out.println("Message : " + Message);
			System.out.println("ClientInfor : " + this.ClientInfor);
			String msg = st.nextToken(); // Message   ̸ ״ ū ¥ 

			String s = Message + " : " + msg + "\n";
			StyledDocument doc = txtBroadCast.getStyledDocument();

			try {
				if (Message.equals(this.ClientInfor)) 
				{
					StyleConstants.setForeground(styleSet, Color.BLACK);
					StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_RIGHT);
					doc.setParagraphAttributes( doc.getLength(), s.length(),styleSet, false);
					doc.insertString(doc.getLength(), Message + " : " + msg + "\n", styleSet);
				} 
				else if (!(Message.equals(this.ClientInfor))) 
				{
					StyleConstants.setForeground(styleSet1, Color.BLACK);
					StyleConstants.setAlignment(styleSet1, StyleConstants.ALIGN_LEFT);
					doc.setParagraphAttributes(doc.getLength(), s.length(), styleSet1, false);
					doc.insertString(doc.getLength(), Message + " : " + msg + "\n", styleSet1);
				}
				Save(Message + " : " + msg);
			} catch (BadLocationException e1) {
				e1.getMessage();
			}
		}
		//   ִ ä
		else if (protocol.equals("MainChat")) {
			System.out.println("Message : " + Message);
			System.out.println("ClientInfor : " + this.ClientInfor);
			String id = st.nextToken(); // Message   ̸ ״ ̵ ״ ū ¥ 
			String msg = st.nextToken();
			String r_r = st.nextToken();
			String r_g = st.nextToken();
			String r_b = st.nextToken();
			int r = Integer.parseInt(r_r);
			int g = Integer.parseInt(r_g);
			int b = Integer.parseInt(r_b);
			
			String s = Message + "\n" + msg + "\n";

			StyledDocument doc = txtBroadCast.getStyledDocument();
			try {
				
				if (Message.equals(this.ClientInfor)) {
					StyleConstants.setForeground(styleSet, new Color(r,g,b));
					StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_RIGHT);
					doc.setParagraphAttributes(doc.getLength(), s.length(), styleSet, false);
					doc.insertString(doc.getLength(), Message + "\n" + msg + "\n", styleSet);

				} else if (!(Message.equals(this.ClientInfor))) {
					//   ֱ
					Profile(id);
					ImageIcon i = new ImageIcon(profile);
					Image origin = i.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
					i = null;
					i = new ImageIcon(origin);

					setEndLine();
					txtBroadCast.insertIcon(i);
					
					// 
					StyleConstants.setForeground(styleSet1, new Color(r,g,b));
					StyleConstants.setAlignment(styleSet1, StyleConstants.ALIGN_LEFT);
					doc.setParagraphAttributes(doc.getLength(), s.length(), styleSet1, false);
					doc.insertString(doc.getLength(), Message + "\n" + msg + "\n", styleSet1);
				}
				Save(Message + " : " + msg);
			} catch (Exception e1) {
				e1.getMessage();
			}
		} else if (protocol.equals("Emotion")) {
			//  +   + ̸Ƽ 
			String str1 = st.nextToken();
			//Ʈ 
			String r_r = st.nextToken();
			String r_g = st.nextToken();
			String r_b = st.nextToken();
			int r = Integer.parseInt(r_r);
			int g = Integer.parseInt(r_g);
			int b = Integer.parseInt(r_b);

			String s = Message + "\n";
			get_emo(str1);
			StyledDocument doc = txtBroadCast.getStyledDocument();

			try {
				ImageIcon emo = emoIcon;
				Image origin = emo.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
				emo = null;
				emo = new ImageIcon(origin);
				if (Message.equals(this.ClientInfor)) {
					setEndLine();
					StyleConstants.setForeground(styleSet, new Color(r,g,b));
					StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_RIGHT);
					doc.setParagraphAttributes(doc.getLength(), s.length(), styleSet, false);
					doc.insertString(doc.getLength(), Message + "\n", styleSet);
				} else if (!(Message.equals(this.ClientInfor))) {
					
					setEndLine();
					StyleConstants.setForeground(styleSet1, new Color(r,g,b));
					StyleConstants.setAlignment(styleSet1, StyleConstants.ALIGN_LEFT);
					doc.setParagraphAttributes(doc.getLength(), s.length(), styleSet1, false);
					doc.insertString(doc.getLength(), Message + "\n", styleSet1);
				}
				setEndLine();
				txtBroadCast.insertIcon(emo);
				doc.insertString(doc.getLength(), "\n", null);
				setEndIcon();

				emoIcon = null;
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		// ̸Ƽ ޱ
		else if (protocol.equals("EmotionSocket")) {
			//  +   + ̸
			try {
				InputStream in = clientSocket.getInputStream();
				
				if (O_InStream == null)
					O_InStream = new ObjectInputStream(clientSocket.getInputStream());
				
				int readBytes = in.available();
				
				byte[] buffer = new byte[readBytes];
				
				buffer = (byte[]) O_InStream.readObject();

				StyledDocument doc = txtBroadCast.getStyledDocument();
				
				String s = Message + " : " + "\n";
				
				ImageIcon emo = new ImageIcon(buffer);
				Image origin = emo.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);

				ImageIcon emo2 = new ImageIcon(origin);
				if (Message.equals(this.ClientInfor)) {
					setEndLine();
					StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_RIGHT);
					doc.setParagraphAttributes(doc.getLength(), s.length(), styleSet, false);
					doc.insertString(doc.getLength(), Message + " : " + "\n", styleSet);
				} else if (!(Message.equals(this.ClientInfor))) {
					setEndLine();
					StyleConstants.setAlignment(styleSet1, StyleConstants.ALIGN_LEFT);
					doc.setParagraphAttributes(doc.getLength(), s.length(), styleSet1, false);
					doc.insertString(doc.getLength(), Message + " : " + "\n", styleSet1);
				}
				setEndLine();
				txtBroadCast.insertIcon(emo2);
				doc.insertString(doc.getLength(), "\n", null);
				setEndIcon();

				//System.out.println("data size : " + emotion.length);

			} catch (Exception e) {
				System.out.println(" -> Ŭ̾Ʈ : ̸Ƽ   : " + e.getMessage());
			}

		}
		//   ư  
		else if (protocol.equals("RemoveMyRoom")) {
			//  + ̸
			for (int i = 0; i < CChatRoom_V.size(); i++) {
				ChatRoom r = (ChatRoom) CChatRoom_V.elementAt(i);
				if (r.Room_name.equals(Message)) {
					CChatRoom_V.remove(i); //  äù Ϳ 
					myRoom_list.remove(Message);//   Ͽ 
					ClistMyRoom.setListData(myRoom_list);
				}
			}
		}
		//    ä   ڽ 
		else if (protocol.equals("RemoveMe")) {
			//  + ̸ +  ̸ -> Message : ̸ , User : ̸
			String User = st.nextToken();

			for (int o = 0; o < CChatRoom_V.size(); o++) {
				ChatRoom RoomOut = (ChatRoom) CChatRoom_V.elementAt(o);
				if (RoomOut.Room_name.equals(Message)) {
					RoomOut.InRoomList.remove(User);
					RoomOut.InRoomFriendlist.setListData(RoomOut.InRoomList);

					StyledDocument doc = RoomOut.RoomChat.getStyledDocument();

					try {
						StyleConstants.setForeground(styleSet1, Color.BLACK);
						doc.insertString(doc.getLength(), User + "    ̽ϴ. " + "\n", styleSet1);
						RoomOut.Save();
					} catch (BadLocationException e1) {
						e1.getMessage();
					}

				}
			}
		} else if (protocol.equals("InRoomMainChat")) {
			//  + ̸ +  ̸ + ̵ +  -> Message : ̸
			String talking = st.nextToken();
			String id = st.nextToken();
			String msg = st.nextToken();
			String s = talking + "\n" + msg + "\n";
			
			String r_r = st.nextToken();
			String r_g = st.nextToken();
			String r_b = st.nextToken();
			int r = Integer.parseInt(r_r);
			int g = Integer.parseInt(r_g);
			int b = Integer.parseInt(r_b);

			for (int j = 0; j < this.CChatRoom_V.size(); j++) {
				ChatRoom room = (ChatRoom) this.CChatRoom_V.elementAt(j);

				if (room.Room_name.equals(Message)) { // ̸   
					StyledDocument doc = room.RoomChat.getStyledDocument();

					try {
						
						if (talking.equals(this.ClientInfor)) {
							StyleConstants.setForeground(styleSet, new Color(r,g,b));
							StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_RIGHT);
							doc.setParagraphAttributes(doc.getLength(), s.length(), styleSet, false);
							doc.insertString(doc.getLength(), talking + "\n" + msg + "\n", styleSet);
							
						} else if (!(talking.equals(this.ClientInfor))) {
							// 
							Profile(id);
							ImageIcon i = new ImageIcon(profile);
							Image origin = i.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
							i = null;
							i = new ImageIcon(origin);

							room.RoomChat.selectAll();
							room.RoomChat.setSelectionStart(room.RoomChat.getSelectionEnd());
							room.RoomChat.replaceSelection("\n");
							room.RoomChat.insertIcon(i);

							//۾ Ӽ
							StyleConstants.setForeground(styleSet1, new Color(r,g,b));
							StyleConstants.setAlignment(styleSet1, StyleConstants.ALIGN_LEFT);
							doc.setParagraphAttributes(doc.getLength(), s.length(), styleSet1, false);
							doc.insertString(doc.getLength(), talking + "\n" + msg + "\n", styleSet1);
						
						}

						room.RoomChat.selectAll();
						room.RoomChat.setSelectionStart(room.RoomChat.getSelectionEnd());
						room.RoomChat.replaceSelection("\n");
						
						room.Save();
					} catch (Exception e1) {
						e1.getMessage();
					}
				}
			}
		}
		//äù ̸Ƽ
		 else if (protocol.equals("InRoomMainChatEmo")) {
				//  + ̸ +  ̸ + ̵ + ̸Ƽ + RGB-> Message : ̸
				String talking = st.nextToken();
				String id = st.nextToken();
				String emot = st.nextToken();
				String s = talking + "\n" ;
				
				String r_r = st.nextToken();
				String r_g = st.nextToken();
				String r_b = st.nextToken();
				int r = Integer.parseInt(r_r);
				int g = Integer.parseInt(r_g);
				int b = Integer.parseInt(r_b);

				get_emo(emot);
				
				for (int j = 0; j < this.CChatRoom_V.size(); j++) {
					ChatRoom room = (ChatRoom) this.CChatRoom_V.elementAt(j);

					if (room.Room_name.equals(Message)) { // ̸   
						StyledDocument doc = room.RoomChat.getStyledDocument();

						try {
							
							if (talking.equals(this.ClientInfor)) {
								StyleConstants.setForeground(styleSet, new Color(r,g,b));
								StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_RIGHT);
								doc.setParagraphAttributes(doc.getLength(), s.length(), styleSet, false);
								doc.insertString(doc.getLength(), talking + "\n" , styleSet);
							} else if (!(talking.equals(this.ClientInfor))) {
								// 
								Profile(id);
								ImageIcon i = new ImageIcon(profile);
								Image origin = i.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
								i = null;
								i = new ImageIcon(origin);

								room.RoomChat.selectAll();
								room.RoomChat.setSelectionStart(room.RoomChat.getSelectionEnd());
								room.RoomChat.replaceSelection("\n");
								room.RoomChat.insertIcon(i);
								
								StyleConstants.setForeground(styleSet1, new Color(r,g,b));
								StyleConstants.setAlignment(styleSet1, StyleConstants.ALIGN_LEFT);
								doc.setParagraphAttributes(doc.getLength(), s.length(), styleSet1, false);
								doc.insertString(doc.getLength(), talking + "\n" , styleSet1);
							}
							//̸Ƽ 
							ImageIcon emo = emoIcon;
							Image origin2 = emo.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
							emo = null;
							emo = new ImageIcon(origin2);
							
							room.RoomChat.selectAll();
							room.RoomChat.setSelectionStart(room.RoomChat.getSelectionEnd());
							room.RoomChat.replaceSelection("\n");
							
							doc.insertString(doc.getLength(), "\n", null);
							room.RoomChat.insertIcon(emo);
							doc.insertString(doc.getLength(), "\n", null);
							setEndIcon();

							emoIcon = null;
							room.Save();
						} catch (Exception e1) {
							e1.getMessage();
						}
					}
				}
			}
		// ڰ   
		else if (protocol.equals("rmRoom_S")) {
			//  + ̸
			for (int i = 0; i < CChatRoom_V.size(); i++) {
				ChatRoom r = (ChatRoom) CChatRoom_V.elementAt(i);
				if (r.Room_name.equals(Message)) {
					r.ChatRoom.setVisible(false); // äâ ݰ
					CChatRoom_V.remove(i); //  äù Ϳ 
					myRoom_list.remove(Message); //   Ͽ 
					ClistMyRoom.setListData(myRoom_list);
				}
			}
		}
		//   Ͽ 
		else if (protocol.equals("rmAllRoom_S")) {
			//  + ̸
			for (int i = 0; i < room_list.size(); i++) {
				String r = (String) room_list.elementAt(i);
				if (r.equals(Message)) {
					room_list.remove(i); //  äù Ϳ 
					ClistChatRoom.setListData(room_list);
				}
			}
		} else if (protocol.equals("ServerQuit")) {
			//  + 
			System.out.println(Server.ChatClient_V.size());
			for (int i = 0; i < Server.ChatClient_V.size(); i++) {
				ChatClient c = (ChatClient) Server.ChatClient_V.elementAt(i);
				if (Message.equals(c.ClientInfor)) {
					c.ClientFrame.setVisible(false);
					Server.ChatClient_V.removeElementAt(i);
					System.out.println("");
				}
			}
		} else if (protocol.equals("LogOutthis")) {
			//  + 
			for (int i = 0; i < user_list.size(); i++) {
				String name = user_list.elementAt(i).toString();
				if (name.equals(Message)) {
					user_list.removeElement(i);
					ClistChatFriend.setListData(user_list);
				}
			}
		} 
	}

	public void Update_Friend(String str) {
		user_list.add(str);
		ClistChatFriend.setListData(user_list);
	}

	/********** äù ׼  **********/
	public void actionPerformed(ActionEvent e) {
		// 游 ư Ŭ ޽ 
		if (e.getSource() == btnMkRoom) {
			String roomname = JOptionPane.showInputDialog(" * äù ̸  * ");
			if (roomname != null) {
				if ((roomname.length() > 1) && (roomname.length() <= 10))
					SendMessage("CreateRoom/" + roomname + "/" + ClientInfor);
				else
					JOptionPane.showMessageDialog(null, " ̸ ڼ 1 ̻, 10 ", "", JOptionPane.ERROR_MESSAGE);
			}
		}
		//  ư Ŭ ޽ 
		else if (e.getSource() == btnRmvRoom) {
			String clickRoom = ClistMyRoom.getSelectedValue();
			if (clickRoom.equals("")) {
				JOptionPane.showMessageDialog(null, "  ּ", "˸", JOptionPane.ERROR_MESSAGE);
			} else {
				SendMessage("RemoveMyRoom/" + clickRoom + "/" + ClientInfor);
			}
		}
		//   ư Ŭ ޽ 
		else if (e.getSource() == btnGetIn) {
			int p = 0;
			String room = (String) ClistChatRoom.getSelectedValue();
			System.out.println(room);

			System.out.println("CChatRoom_V size() : " + CChatRoom_V.size());
			System.out.println("Server.ChatRoom_V size() : " + Server.ChatRoom_V.size());

			System.out.println(CChatRoom_V.size());
			// ̹    Ȯ -> ƴѰ Ȯϸ 

			for (int j = 0; j < CChatRoom_V.size(); j++) {
				ChatRoom R = (ChatRoom) CChatRoom_V.elementAt(j);
				if (room.equals(R.Room_name)) {
					p = 1;
					JOptionPane.showMessageDialog(null, "̹  äù Դϴ", "˸", JOptionPane.ERROR_MESSAGE);
					break;
				} else if (room.equals(""))
					p = 2;
				JOptionPane.showMessageDialog(null, "  ּ", "˸", JOptionPane.ERROR_MESSAGE);
			}
			// ش Ʈ  JoinRoom ޽ 
			if (p == 0)
				SendMessage("JoinRoom/" + ClientInfor + "/" + room);

		}
		// Ŭ̾Ʈ â ۹ư 
		else if (e.getSource() == btnSend) {
			SendMessage("MainChat/" + this.ClientInfor + "/" + this.ClientId + "/" + txtMainTalk.getText().trim() + "/" + R + "/" + G + "/" + B );
			txtMainTalk.setText("");
			txtMainTalk.requestFocus();
			// Save();
		} 
		
		/**** Send Emoticon *****/
		else if (e.getSource() == btnEmotion1) {
			SendMessage("Emotion/" + ClientInfor + "/e1" + "/" + R + "/" + G + "/" + B);
		}
		else if (e.getSource() == btnEmotion2) {
				try {
				Connection mysqlConn = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String sql = "";

				Class.forName("com.mysql.cj.jdbc.Driver");
				mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");
			
				sql = "SELECT * FROM EMOTION";

				pstmt = mysqlConn.prepareStatement(sql);

				rs = pstmt.executeQuery();

				if (rs.next()) {
					Blob aBlob = rs.getBlob("EMO1"); // ÷ IMAGE  ̹ ޾ƿ

					InputStream InStream = aBlob.getBinaryStream();

					int readBytes;
					byte[] buffer = new byte[2048];
					ByteArrayOutputStream os = new ByteArrayOutputStream();

					while (true) {
						readBytes = InStream.read(buffer);
						if (readBytes == -1)
							break;
						os.write(buffer, 0, readBytes);
						emo_db = os.toByteArray();
					}
				}

				SendMessage("EmotionSocket/" + ClientInfor);

				OutputStream os = clientSocket.getOutputStream();
				
				if(O_OutStream==null)
					O_OutStream = new ObjectOutputStream(clientSocket.getOutputStream());

				O_OutStream.writeObject(emo_db); 
				O_OutStream.flush();

			} catch (Exception ee) {
				System.out.println("nn  : " + ee.getMessage());
			} finally {
				try {
				} catch (Exception ee) {
				}
			}
				} 
		else if (e.getSource() == btnEmotion3) {
			SendMessage("Emotion/" + ClientInfor + "/e_gif2");
		}
	}

	/***** ä   *********/
	public void Save(String save_s) {
		String saveStr = "  " + iGetDate + " " + save_s + "\r\n"; //   
		if (save_s.equals("")) {
			saveStr += "\r\n";
		}
		System.out.println(" ҷ");
		try {
			/****   ********/
			fw = new FileWriter(filePath, true);
			fw.write(saveStr);
			fw.close();

			/****  DB  *****/
			File saveFile = new File(filePath); // ϰ
			FileInputStream fis = new FileInputStream(saveFile);

				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");
			

			if (mysqlConn == null)
				System.out.println("ä    ");
			else {
				String sql = "UPDATE CLIENT SET MAIN = ? WHERE ID = ? ";

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

				fis.close();
				BaOs_Save.close();

				ByteArrayInputStream BaIs_Save = new ByteArrayInputStream(BaOs_Save.toByteArray());

				pstmt.setBinaryStream(1, BaIs_Save, BaOs_Save.size());
				pstmt.setString(2, this.ClientId);

				pstmt.executeUpdate();

				pstmt.close();
				mysqlConn.close();
			}

		} catch (Exception e) {
			System.out.println("   : " + e.getMessage());
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
		// ϸ
		fileName = String.format("%04d%02d%02d_%s", iYear, iMonth, iDate, this.ClientId);

		//  
		filePath = "C:\\Users\\Eunae Cho\\SaveChat" + "\\" + fileName + ".txt";
		System.out.println(fileName);
		System.out.println(filePath);
	}

	public void setDesign() {
		setPath(); // ޸  

		try {
			fw = new FileWriter(filePath, true); // ޸ 
		} catch (IOException ioe) {
			System.out.println("޸   : " + ioe.getMessage());
		}
	}

	/****  äÿ  *****/
	private void MainChat() {
		String FilePath = "C:\\Users\\Eunae Cho\\UpChat" + "\\" + fileName + ".txt";
		System.out.println(FilePath);

		Connection mysqlConn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			// DB  ä  Ϸ ҷ
			Class.forName("com.mysql.cj.jdbc.Driver");
			mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");
			
			sql = "SELECT * FROM CLIENT WHERE ID = ?";
			pstmt = mysqlConn.prepareStatement(sql);

			pstmt.setString(1, ClientId);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				Blob mainChat = rs.getBlob("MAIN");

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
				TextChat Chattinglog = new TextChat(ClientInfor);
				Chattinglog.ChatText.setText(s);
				Chattinglog.ChatLog.setVisible(true);

			}

		} catch (Exception e) {
			System.out.println("   : " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
				mysqlConn.close();
			} catch (Exception e) {
			}
		}
	}

	public void Profile(String str) throws SQLException {
		Connection mysqlConn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");
			
			sql = "SELECT * FROM CLIENT WHERE ID = ?";
			pstmt = mysqlConn.prepareStatement(sql);

			pstmt.setString(1, str);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				Blob aBlob = rs.getBlob("IMAGE"); // ÷ IMAGE  ̹ ޾ƿ

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
			System.out.println("   : " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
				mysqlConn.close();
			} catch (Exception e) {
			}
		}
	}

	/****** KeyListener  ******/

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 10) { // ĥ 
			SendMessage("MainChat/" + this.ClientInfor + "/" + this.ClientId + "/" + txtMainTalk.getText().trim() + "/" + R + "/" + G + "/" + B );
			txtMainTalk.setText("");
			txtMainTalk.requestFocus();

		}

	}

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	/******   ҷ ********/

	public class ReadImage {
		Connection mysqlConn;
		PreparedStatement pstmt;
		ResultSet rs;
		String sql = "";

		public ReadImage(String str) throws SQLException {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");
			
				sql = "SELECT * FROM CLIENT WHERE ID = ?";
				pstmt = mysqlConn.prepareStatement(sql);

				pstmt.setString(1, str);

				rs = pstmt.executeQuery();

				if (rs.next()) {
					Blob aBlob = rs.getBlob("IMAGE"); // ÷ IMAGE  ̹ ޾ƿ

					InputStream InStream = aBlob.getBinaryStream();

					int readBytes;
					byte[] buffer = new byte[2048];
					ByteArrayOutputStream os = new ByteArrayOutputStream();

					while (true) {
						readBytes = InStream.read(buffer);
						if (readBytes == -1)
							break;
						os.write(buffer, 0, readBytes);
						photo = os.toByteArray();
					}

					//  Ʈ о photo ϰ icon photo 
					icon = new ImageIcon(photo);
				}

			} catch (Exception e) {
				System.out.println("   : " + e.getMessage());
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

	/******   ̺Ʈ  ************/

	public class SetInfoActionListener implements ActionListener {

		public JFrame InfoFrame;
		public JPanel contentPane;
		public JTable InfoTable;
		JButton ChangeImage;
		JButton btnmodifyset;
		JLabel lblClientImage;

		File profile;
		FileInputStream FIs;
		ByteArrayOutputStream BaOs;
		ByteArrayInputStream BaIs;

		public Vector<String> column_T;
		public Vector<Vector> data_m; // 1 ڵ ļ ̺ Ÿ 
		public Vector data_in; // 1 ڵ 
		public String[] title_m;

		// ̺  
		public String[] Info_data = new String[9];
		public Blob b_data;
		byte[] picture;

		// DB
		PreparedStatement pstmt;
		ResultSet rs;
		Connection mysqlConn;
		String ClientId;

		public SetInfoActionListener(String id) {
			ClientId = id;
		}

		public void actionPerformed(ActionEvent e) {

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");
			

				String sql = "SELECT * FROM CLIENT WHERE ID = ?";

				pstmt = mysqlConn.prepareStatement(sql);

				pstmt.setString(1, ClientId);

				rs = pstmt.executeQuery();
				//     rs setting

				if (rs.next()) { // DB 1  ϴ     Է
					Info_data[0] = rs.getString("NAME");
					Info_data[1] = rs.getString("BIRTH");
					Info_data[2] = rs.getString("EMAIL");
					Info_data[3] = rs.getString("ADDR");
					Info_data[4] = rs.getString("PHONE");
					Info_data[6] = rs.getString("ID");
					Info_data[7] = rs.getString("PASSWORD");
					b_data = rs.getBlob("IMAGE");

					ReadImage r = new ReadImage(Info_data[6]);

				}

			} catch (Exception cnfe) {
				System.out.println(" : " + cnfe.getMessage());
			}

			/***** ̺  ********/
			data_m = new Vector<>();

			column_T = new Vector<>();
			column_T.add("ȸ ");
			column_T.add("Է ");

			title_m = new String[] { "̸", "", "̸", "ּ", "ڵ", "", "̵", "йȣ", "", };

			for (int j = 0; j < 9; j++) {
				data_in = new Vector<>();
				data_in.add(title_m[j]);
				data_in.add(Info_data[j]); //   

				data_m.add(data_in); // ̺  
			}

			getFrame();
		}

		/********** Frame **********/
		public void getFrame() {
			InfoFrame = new JFrame("  ");
			InfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			InfoFrame.setBounds(100, 100, 450, 630);

			contentPane = new JPanel();
			contentPane.setBackground(Color.WHITE);
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			InfoFrame.getContentPane().add(contentPane);
			contentPane.setLayout(null);

			lblClientImage = new JLabel() {
				public void paintComponent(Graphics g) {
					Dimension d = getSize();
					g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null); // Ʈ  °
				}
			};
			lblClientImage.setIcon(icon);
			lblClientImage.setHorizontalAlignment(SwingConstants.CENTER);
			lblClientImage.setBounds(120, 23, 185, 185);
			contentPane.add(lblClientImage);

			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(32, 247, 370, 250);
			contentPane.add(scrollPane);

			InfoTable = new JTable(data_m, column_T);
			scrollPane.setViewportView(InfoTable);
			InfoTable.setVisible(true);
			InfoTable.getTableHeader().setReorderingAllowed(false);
			InfoTable.getTableHeader().setResizingAllowed(false);
			InfoTable.setRowHeight(25);
			InfoTable.getColumn("ȸ ").setPreferredWidth(100);
			InfoTable.getColumn("Է ").setPreferredWidth(270);

			btnmodifyset = new JButton("   Ϸ");
			btnmodifyset.setBackground(Color.DARK_GRAY);
			btnmodifyset.setForeground(Color.WHITE);
			btnmodifyset.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
			btnmodifyset.setBounds(32, 520, 370, 40);
			contentPane.add(btnmodifyset);

			icon2 = new ImageIcon("C:\\Users\\Eunae Cho\\Pictures\\  ư.png");
			ChangeImage = new JButton("") {
				public void paintComponent(Graphics g) {
					Dimension d = getSize();
					g.drawImage(icon2.getImage(), 0, 0, d.width, d.height, null); // Ʈ  °
				}
			};
			ChangeImage.setBounds(320, 169, 76, 66);
			ChangeImage.setBackground(Color.WHITE);
			contentPane.add(ChangeImage);

			ChangeImage.addActionListener(new ChangeImageAcionListener());
			btnmodifyset.addActionListener(new InfoEndActionListener());

			InfoFrame.setVisible(true);

		}

		/******    *******/
		public class ChangeImageAcionListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					JFileChooser file = new JFileChooser(); //  / 丮 
					file.setCurrentDirectory(new File(System.getProperty("user.home")));
					FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg", "gif", "png"); // 
																													// Ȯ
					file.addChoosableFileFilter(filter);
					file.setMultiSelectionEnabled(false); // ϳ  

					int result = file.showOpenDialog(null); // â 

					if (result == JFileChooser.APPROVE_OPTION) { // ϻ
						String path = file.getSelectedFile().getAbsolutePath(); //   

						profile = new File(path);
						FIs = new FileInputStream(profile);
						BaOs = new ByteArrayOutputStream();

						while (true) {
							int x = FIs.read();
							if (x == -1)
								break;
							BaOs.write(x); // ⺻̹ о̸ bos ½Ʈ ۼ

							photo = null;
							photo = BaOs.toByteArray();
						}
						FIs.close();
						BaOs.close();

						BaIs = new ByteArrayInputStream(BaOs.toByteArray());
						icon = null;
						icon = new ImageIcon(photo);

						lblClientImage.setIcon(icon);

					}
				} catch (Exception e) {
					System.out.println(" ٲٱ  : " + e.getMessage());
				}

			}
		}

		/********** ̺  ϰ ư  Ϸ (̺  ) ************/
		public class InfoEndActionListener implements ActionListener {
			String[] modify_data = new String[9];
			String sql_update;
			PreparedStatement pstmt_m;
			String m_birth;

			public void actionPerformed(ActionEvent arg0) {
				System.out.println("ư ׼  ȣ");

				for (int j = 0; j < 8; j++) {
					modify_data[j] = ((String) InfoTable.getValueAt(j, 1));
					System.out.println("---------------------------");
					System.out.print(InfoTable.getValueAt(j, 0) + " : ");
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
					sql_update = "UPDATE CLIENT SET NAME = ?, BIRTH = ?, EMAIL = ?, ADDR = ?, PHONE = ?, ID = ?,PASSWORD = ?, IMAGE=?";
					sql_update += "WHERE ID=?";

					pstmt_m = mysqlConn.prepareStatement(sql_update);

					pstmt_m.setString(1, modify_data[0]);
					pstmt_m.setString(2, m_birth);
					pstmt_m.setString(3, modify_data[2]);
					pstmt_m.setString(4, modify_data[3]);
					pstmt_m.setString(5, modify_data[4]);
					pstmt_m.setString(6, modify_data[5]);
					pstmt_m.setString(7, modify_data[6]);
					pstmt_m.setBinaryStream(8, BaIs, BaOs.size());
					pstmt_m.setString(9, modify_data[5]);

					pstmt_m.executeUpdate();
					// DB 
					InfoFrame.setVisible(false);
					ClientImage.setIcon(icon);

				} catch (Exception e) {
					System.out.println("ȸ    : " + e.getMessage());
				} finally {
					try {
						pstmt_m.close();
					} catch (Exception ee) {
					}
				}
			}
		}
	}

	/***** äù  *****/
	private void setEndIcon() {
		//    Ŀ ̵ϰ 
		setEndLine();
		txtBroadCast.replaceSelection("\n");
		setEndLine();
	}

	private void setEndLine() {
		txtBroadCast.selectAll();
		txtBroadCast.setSelectionStart(txtBroadCast.getSelectionEnd());
		txtBroadCast.replaceSelection("\n");
		txtBroadCast.replaceSelection("\n");
	}

	/******** äù ݱ ***********/
	class JFrameWindowClosingEventHandler extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			JFrame frame = (JFrame) e.getWindow();
			frame.dispose();
			try {
				for (int s = 0; s < Server.ChatClient_V.size(); s++) {
					ChatClient c = (ChatClient) Server.ChatClient_V.elementAt(s);
					if (c.ClientInfor.equals(frame.getName())) {
						Server.ChatClient_V.remove(s);
					}
				}
				System.out.println(ClientInfor + " α׾ƿ ϼ̽ϴ");
				clientSocket.close();
			} catch (IOException e1) {
				System.out.println("ݱ ư     : " + e1.getMessage());
			}
		}
	}

	/***** ̸Ƽ DB ҷ *****/
	public void get_emo(String str) {
		Connection mysqlConn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");
			
			sql = "SELECT * FROM EMOTION ";

			pstmt = mysqlConn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				Blob aBlob = rs.getBlob("EMO1"); // ÷ IMAGE  ̹ ޾ƿ

				InputStream InStream = aBlob.getBinaryStream();

				int readBytes;
				byte[] buffer = new byte[2048];
				ByteArrayOutputStream os = new ByteArrayOutputStream();

				while (true) {
					readBytes = InStream.read(buffer);
					if (readBytes == -1)
						break;
					os.write(buffer, 0, readBytes);
					emo_db = os.toByteArray();
				}

				//  Ʈ о photo ϰ icon photo 
				emoIcon = new ImageIcon(emo_db);
			}

		} catch (Exception e) {
			System.out.println("nn  : " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
				mysqlConn.close();
			} catch (Exception e) {
			}
		}
	}

	/**** 콺  *****/
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
		// ޴  
		if (e.getSource() == Logout) {
			// logOn_V,_S     â ׷̵, ڵ鿡 ˸

			SendMessage("Logout/" + this.ClientInfor);

			this.ClientFrame.setVisible(false);

		}
		else if(e.getSource() == FileSend) {
		        FileDialog file_D = new FileDialog(this); //this <  ;    
		        file_D.setDirectory(".."); //.. θ͸
		        file_D.setVisible(true);
		        
		        // ̸ 
		        String FileName = file_D.getFile();
		        		    
		}
		//   ϱ
		else if (e.getSource() == lblColor) {
			myColor = JColorChooser.showDialog(null, "  ", myColor);
			if (myColor != null) {
				R = myColor.getRed();
				G = myColor.getGreen();
				B = myColor.getBlue();
				System.out.println(myColor);
			}
		} else if (e.getSource() == lblmainChat) {
			MainChat();
		} else if (e.getSource() == lblroomChat) {

			Connection mysqlConn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = "";

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");
			
				sql = "SELECT * FROM ROOMCHAT WHERE ID = ?";

				pstmt = mysqlConn.prepareStatement(sql);
				pstmt.setString(1, this.ClientId);

				rs = pstmt.executeQuery();

				if (rs.next()) {
					this.SaveChatlist.removeAllElements();
					this.SaveChatlist.add(0, rs.getString("NAME1"));
					this.SaveChatlist.add(1, rs.getString("NAME2"));
					this.SaveChatlist.add(2, rs.getString("NAME3"));
				}

				listFrame lf = new listFrame(" ");
				lf.Chatlist.setListData(this.SaveChatlist);
				lf.listframe.setVisible(true);

			} catch (Exception i) {
				i.getStackTrace();
			}
		}

	}

	/*****    ******/
	private void Roomchat(int a) {
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

			pstmt.setString(1, ClientId);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				Blob mainChat = rs.getBlob(col);

				InputStream M_InStream = mainChat.getBinaryStream();
				String s = "";

				while (true) {
					int len = M_InStream.available();
					byte[] bytes = new byte[len];

					// о ڰ ٸ
					if (M_InStream.read() == -1)
						break;

					ByteArrayOutputStream BaOs_Chat = new ByteArrayOutputStream();
					M_InStream.read(bytes, 0, len);
					BaOs_Chat.write(bytes);
					// д Ʈ  Ʈ ۼ

					s = new String(bytes, 0, len);
					// System.out.println(s);

				}
				TextChat Chattinglog = new TextChat(ClientInfor);
				Chattinglog.ChatText.setText(s);
				Chattinglog.ChatLog.setVisible(true);

			}

			//

		} catch (Exception e) {
			System.out.println(" ä  : " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
				mysqlConn.close();
			} catch (Exception e) {
			}
		}
	}
	
	/***** Ʈ  ********/
	private class listFrame extends JFrame implements ActionListener {

		private JFrame listframe;
		private JPanel contentPane;
		JList Chatlist;
		JButton btnLook;

		/**
		 * Create the frame.
		 */
		public listFrame(String str) {
			listframe = new JFrame(str);
			listframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			listframe.setBounds(100, 100, 350, 288);
			contentPane = new JPanel();
			contentPane.setBackground(Color.WHITE);
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(null);
			listframe.add(contentPane);

			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(31, 56, 264, 104);
			contentPane.add(scrollPane);

			Chatlist = new JList();
			Chatlist.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
			Chatlist.setFixedCellHeight(20);
			scrollPane.setViewportView(Chatlist);

			btnLook = new JButton("\uB300\uD654 \uB0B4\uC6A9 \uBCF4\uAE30");
			btnLook.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
			btnLook.setBounds(31, 176, 262, 36);
			contentPane.add(btnLook);
			btnLook.addActionListener(this);

			JLabel lblNewLabel = new JLabel("\uC800\uC7A5\uB41C \uB0B4 \uB300\uD654 \uBAA9\uB85D");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
			lblNewLabel.setBounds(31, 12, 262, 36);
			contentPane.add(lblNewLabel);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnLook) {
				int index = Chatlist.getSelectedIndex();
				if (index == -1)
					JOptionPane.showMessageDialog(null, "   ּ", "", JOptionPane.ERROR_MESSAGE);
				Roomchat(index);

			}
		}
	}
}

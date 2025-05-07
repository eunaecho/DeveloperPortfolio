package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.StringTokenizer;
import javax.swing.JList;

/****** 챗 서버 ***********/
public class Server {

	static Vector<ChatRoom> ChatRoom_V = new Vector<ChatRoom>(); 
	static Vector<ChatClient> ChatClient_V = new Vector<ChatClient>();

	static Server C_server = new Server();
	static int Client_N = 0;

	private ServerSocket serverSocket = null; // 서버 소켓
	private Socket socket; // 클라이언트 소켓

	static Vector<String> room_V_list = new Vector<String>(); // ä�ù� �̸�����
	static Vector<RoomInfo> room_V = new Vector<RoomInfo>(); // ä�ù� ����(ä�ù� ��ü ����)

	static Vector<UserInfo> logOn_V = new Vector<UserInfo>(); // UserInfo 
	static Vector<String> logOn_S = new Vector<String>(); // String

	//토큰으로 분리
	StringTokenizer st;

	/******** (1)서버, Connection() **********/
	public void Server_Start() {
		try {
			serverSocket = new ServerSocket(9000); 
		} catch (IOException e) { 
			System.out.println("!! ServerSocket ERROR !! " + '\n' + e.getMessage());
			System.exit(0);
		}
		if (serverSocket != null)
		{
			Connection();
			System.out.println("******** 	  채팅서버 오픈	     *********");		}
	}

	/******** 스레드로 계속 서버 돌리기 ************/
	public void Connection() {
		Thread serverThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) { 	
					try {
						socket = serverSocket.accept();

						/**** 개인쓰레드 만들 *******/
						UserInfo PerThread = new UserInfo(socket);
						PerThread.start();

					} catch (Exception e) {
						break;
					}
				}

			}
		});
		serverThread.start();
	}

	/********* 연결 종료  ***********/
	public void QuitConnect() {
		try {

			serverSocket.close();
			logOn_V.removeAllElements();

			logOn_S.removeAllElements();
			ChatServer.SlistChatFriend.setListData(logOn_S);

			room_V.removeAllElements();
			room_V_list.removeAllElements();
			Update_Room_S(room_V_list);

			System.out.println("서버소켓종료 : " + serverSocket.isClosed());
		} catch (Exception e) {
			System.out.println("연결종료 에러 : " + e.getMessage());
		}
	}

	/********** 로그인 객체 (UserInfo) **********/
	public class UserInfo extends Thread {

		Socket per_socket; 

		InputStream is;
		OutputStream os;
		ObjectInputStream O_InStream;
		ObjectOutputStream O_OutStream;
		Object obj;

		InputStream InStream;
		OutputStream OutStream;
		DataInputStream D_InStream; // Ŭ���̾�Ʈ�κ��� �����͸� ���Źޱ� ���� ��Ʈ��
		DataOutputStream D_OutStream; // Ŭ���̾�Ʈ���� �����͸� �۽��ϱ� ���� ��Ʈ��

		String getName; // �����ڰ� ������ �ڽ��� �̸� ����
		String getId; // �������� ���̵� ������ ����
		String ClientInfor; // ������ ������ ����

		boolean RoomChk = true;

		// ChatServerThread
		public UserInfo(Socket s) {
			this.per_socket = s;
			UserNetwork();
		}

		/****** ��Ʈ��ũ �ڿ� ���� *********/
		private void UserNetwork() {
			try {
				/****** 데이터 주고받기  ******/
				InStream = per_socket.getInputStream();
				OutStream = per_socket.getOutputStream();
				D_InStream = new DataInputStream(InStream);
				D_OutStream = new DataOutputStream(OutStream);

				// ����ڰ� �����ϰ� ���� ���̵� ����
				getName = D_InStream.readUTF();
				getId = D_InStream.readUTF();

				ClientInfor = getName + "(" + getId + ")";
				System.out.println("==== ���ο�  ����� ����  : " + ClientInfor + " ====");

				System.out.println(" ���� ���ӵ� ����� �� : " + logOn_V.size());

				// ���� â�� �����ڿ� �ڽ��� �̸��� ���ϰ� ����Ʈ ������Ʈ
				logOn_S.add(ClientInfor);
				ChatServer.SlistChatFriend.setListData(logOn_S);

				// ���� ���ӵ� ����ڿ��� ���ο� ����� �˸� �� �ڽ��� ���Ϳ� ����
				BroadCast("NewUser/" + ClientInfor);

				// �ڽſ��� ���� ����ڸ� �޾ƿ��� �κ�
				update_preexist();

				// �ڽ��� ���Ϳ� ����
				logOn_V.add(this);

				BroadCastMain(ClientInfor, " �����Ͽ����ϴ�.");

				for (int i = 0; i < room_V.size(); i++) {
					RoomInfo r = (RoomInfo) room_V.elementAt(i);
					send_Message("OldRoom/" + r.Room_name);
					// CChatRoom_V.addElement(room_V.elementAt(i));
				}

			} catch (IOException ioe) {
				ioe.getMessage();
			}

		}

		/*** 기존 로그인 사용자 ****/
		public void update_preexist() {
			for (int i = 0; i < logOn_V.size(); i++) {
				UserInfo u = (UserInfo) logOn_V.elementAt(i);
				send_Message("OldUser/" + u.ClientInfor);
			}
		}

		/****** ������.start() �ϸ� �����ϴ� �κ� **********/
		public void run() {
			while (true) {
				try {
					System.out.println("|����� -> ����| ������ �޽��� ���� ������ ����");
					String msg = D_InStream.readUTF();
					System.out.println(" ========== ������ ���Ź��� �޽��� ==========");
					System.out.println(ClientInfor + " : " + msg + "\n");
					InMessage(msg);
				} catch (IOException e) {
					try {
						D_OutStream.close();
						D_InStream.close();
						per_socket.close();

						Update_User_S(ClientInfor);

						logOn_V.remove(this);
						BroadCast("User_out/" + ClientInfor);
						break;
					} catch (IOException ioe) {
					}

				}
			}
		}

		private void InMessage(String str) // Ŭ���̾�Ʈ�κ��� ������ �޽��� ó��
		{
			st = new StringTokenizer(str, "/");

			String protocol = st.nextToken();
			String message = st.nextToken();

			System.out.println("protocol : " + protocol);
			
			System.out.println("text : " + message);

			if (protocol.equals("CreateRoom")) {
				// 1.���� ���� ���� �����ϴ��� Ȯ��
				for (int i = 0; i < room_V.size(); i++) {
					RoomInfo r = (RoomInfo) room_V.elementAt(i);
					// ���� �� �̸��� �����ϸ� ����
					if (r.Room_name.equals(message)) {
						send_Message("CreateRoomFail/ok");
						RoomChk = false;
						break;
					}
				}
				// ���� �� �̸��� �������� ������
				if (RoomChk) { // message : ���̸� �״��� ��ū : ������ �̸�
					String msg = st.nextToken(); // ������ �̸�

					RoomInfo new_room = new RoomInfo(message, this); // ���� �����

					room_V_list.add(message); // �� ���Ϳ� �־���
					Update_Room_S(room_V_list); // ����â�� �� ����Ʈ ������Ʈ

					room_V.add(new_room); // �ش� ���� ������ room_v�� ����
					System.out.println("room_V size : " + room_V.size());

					send_Message("CreateRoom/" + message + "/*********" + msg + "���� ���� �����Ͽ����ϴ�\n");

					BroadCast("New_Room/" + msg + "/" + message);
					BroadCastMain(msg, message + " ���� ��������ϴ�."); // message : ���̸�
				}
				RoomChk = true;
			} else if (protocol.equals("JoinRoom")) {
				// ���� + ������ ��� + ���̸� -> message : ������ ��� JroomName : ���̸�
				String JroomName = st.nextToken();

				send_Message("JoinRoom/" + JroomName); // ����ڰ� �� �� �̸� �����ֱ�
				// �游�鶧 room_V�� �� �־���
				for (int i = 0; i < room_V.size(); i++) { // �ش� �濡 ����鿡�� JoinRoom�޽��� ����
					RoomInfo r = (RoomInfo) room_V.elementAt(i); // message�� ���̸�
					if (r.Room_name.equals(JroomName)) {
						// ���� �� �̿��ڵ� Join�̿��ڿ��� �˷��ֱ�
						System.out.println("r.Room_user_V.size : " + r.Room_user_V.size());
						for (int k = 0; k < r.Room_user_V.size(); k++) {
							UserInfo u = (UserInfo) r.Room_user_V.elementAt(k);
							send_Message("Preexistence/" + r.Room_name + "/" + u.ClientInfor + "/");
						}

						// ����� �߰�
						r.Add_User(this);

						// ���ο� ����� ������ ���� ���� ����ڵ鿡�� �˸�
						r.BroadCast_Room("NotifyJoin/" + JroomName + "/" + message + "/" + "���� �����ϼ̽��ϴ� *****\n");

						break;
					}
				}

			} else if (protocol.equals("RemoveMyRoom")) {
				// �������� + ���̸� + �泪���� ��� �̸� -> message : ���̸� , User : ������ ���
				String User = st.nextToken();

				for (int i = 0; i < room_V.size(); i++) {
					RoomInfo r = (RoomInfo) room_V.elementAt(i);
					if (r.Room_name.equals(message)) { // ������ �� �̸��� ��ü �濡�� �����Ѵٸ�
						r.Room_user_V.remove(this);

						// �����϶�� �޽��� ������
						send_Message("RemoveMyRoom/" + message);

						// �� �����ڵ鿡�� ���� �����϶�� �޽��� ������
						r.BroadCast_Room("RemoveMe/" + message + "/" + User);
					}
				}
			} 
			//������ ���� ��  ����ȭ�� ä��
			else if (protocol.equals("MainChat")) { // message�� ������ ����
				String id = st.nextToken(); // ���̵�
				String msg = st.nextToken(); //���� ����
				String r = st.nextToken();//R, G , B
				String g = st.nextToken();
				String b = st.nextToken();

				BroadCastMain(message, id, msg , r, g, b);
			}
			//���� ȭ�� ä��
			else if (protocol.equals("MainChatting")) { // message�� ������ ����
				String msg = st.nextToken(); // ���� ����
				
				BroadCastMain(message, msg);
			}
			// ��ȭ�� �� ä��
			else if (protocol.equals("InRoomMainChat")) {
				// �������� + ���̸� + ������ �̸� + ������ ���̵� +  ���� -> ���̸� : message
				String fromTalk = st.nextToken();
				String id = st.nextToken();
				String msg = st.nextToken();
				String r = st.nextToken();//R, G , B
				String g = st.nextToken();
				String b = st.nextToken();

				for (int i = 0; i < room_V.size(); i++) {
					RoomInfo room = (RoomInfo) room_V.elementAt(i);
					if (room.Room_name.equals(message)) {
						room.BroadCast_Room("InRoomMainChat/" + message + "/" + fromTalk + "/" + id + "/" + msg + "/" + r + "/" + g + "/" + b);
					}
				}
			} 
			//���� ä�� ���� �̸�Ƽ��
			else if (protocol.equals("Emotion")) {
				// ���� + ���� + �̸�Ƽ�� ����
				String str1 = st.nextToken(); //�̸�Ƽ��
				
				String r = st.nextToken();//R, G , B
				String g = st.nextToken();
				String b = st.nextToken();

				BroadCastEmotion(message, str1, r, g, b);
			}
			//ä�ù� ���� �̸�Ƽ��
			else if (protocol.equals("RoomEmotion")) {
				
				//���� + ���̸� + ������ + ���̵� + �̸�Ƽ�� + RGB
				String str1 = st.nextToken(); //������
				String id = st.nextToken(); //id
				String str2 = st.nextToken();//�̸�
				
				String r = st.nextToken();//R, G , B
				String g = st.nextToken();
				String b = st.nextToken();
				
				for (int i = 0; i < room_V.size(); i++) {
					RoomInfo room = (RoomInfo) room_V.elementAt(i);
					if (room.Room_name.equals(message)) {
						room.BroadCast_RoomEmo("InRoomMainChatEmo/" + message + "/" + str1 + "/" + id + "/" + str2 + "/" + r + "/" + g + "/" + b);
					}
				}
				
			}
			// �̸�Ƽ��
			else if (protocol.equals("EmotionSocket")) {
				// ���� + ������ ���				
				try {
					System.out.println("��");
					
					is = per_socket.getInputStream();
					int len = is.available();

					
					if (O_InStream == null) {
						O_InStream = new ObjectInputStream(per_socket.getInputStream());
					}
					byte[] emotion = new byte[len];

					emotion = (byte[]) O_InStream.readObject();

					//message = ClientInfor
					BroadCastEmotion(message, emotion);

					
				} catch (Exception e) {
					System.out.println("Ŭ���̾�Ʈ -> ���� : �̸�Ƽ�� ���� ���� : " + e.getMessage());
				}

			}
			// �α׾ƿ�
			else if (protocol.equals("Logout")) {
				// ���� + ����
				try {
					System.out.println("�α׾ƿ� ���� : " + logOn_V.size());
					for (int c = 0; c < logOn_S.size(); c++) {
						String S_name = logOn_S.elementAt(c).toString();
						if (message.equals(S_name)) {
							logOn_S.remove(c);
							ChatServer.SlistChatFriend.setListData(logOn_S);
						}
					}
					for (int s = 0; s < logOn_V.size(); s++) {
						UserInfo u = (UserInfo) logOn_V.elementAt(s);
						if (u.ClientInfor.equals(message)) {
							logOn_V.removeElementAt(s);

							// �����ڵ鿡�� �˸���
							BroadCast("LogOutthis/" + u.ClientInfor);

							// ���� �ݱ�
							u.per_socket.close();

							break;
						}
					}
				} catch (Exception e) {
					System.out.println("�α׾ƿ� ����(������) : " + e.getMessage());
				}
			}
		}

		/********* Ŭ���̾�Ʈ â�� Mainä��â **********/
		private void BroadCastMain(String str, String str1) {
			for (int i = 0; i < logOn_V.size(); i++) {
				UserInfo u = (UserInfo) logOn_V.elementAt(i);
				u.send_Message("MainChatting/" + str + "/" + str1);
			}
		}
		
		private void BroadCastMain(String str1, String str2, String str3, String r, String g, String b) {
			for (int i = 0; i < logOn_V.size(); i++) {
				UserInfo u = (UserInfo) logOn_V.elementAt(i);
				u.send_Message("MainChat/" + str1 + "/" + str2 + "/" + str3 + "/" + r + "/" + g + "/" + b);
			}
		}

		private void BroadCastEmotion(String str1, String str2, String r, String g, String b) {
			// str1 = ����, str2 = �̸�Ƽ�� ����
			for (int i = 0; i < logOn_V.size(); i++) {
				UserInfo u = (UserInfo) logOn_V.elementAt(i);
				u.send_Message("Emotion/" + str1 + "/" + str2  + "/" + r + "/" + g + "/" + b);
			}
		}

		/********* Ŭ���̾�Ʈ â�� �̸�Ƽ�� *********/
		private void BroadCastEmotion(String str, byte[] buffer) {
			try {
				
				for (int i = 0; i < logOn_V.size(); i++) {
					UserInfo u = (UserInfo) logOn_V.elementAt(i);
					u.send_Message("EmotionSocket/" + str);
					
					if (O_OutStream == null) {
						O_OutStream = new ObjectOutputStream(per_socket.getOutputStream());
					}

					O_OutStream.writeObject(buffer); // byte[] ������ object ������� 
				}
			} catch (Exception ee) {
				System.out.println("����->Ŭ���̾�Ʈ �̸�Ƽ�� ���� ���� : " + ee.getMessage());
			}
			
			
		}

		public void BroadCast(String str) { // ��ü ����ڿ��� �޽����� ����
			for (int i = 0; i < logOn_V.size(); i++) {
				UserInfo u = (UserInfo) logOn_V.elementAt(i);
				u.send_Message(str);
			}
		}

		public void send_Message(String str) { // ���ڿ��� �޾Ƽ� ����
			try {
				D_OutStream.writeUTF(str);
			} catch (IOException ioe) {
				System.out.println("������ send_Message() ���� : " + ioe.getMessage());
			}
		}

	}

	/******** RoomInfo ************/
	public class RoomInfo {

		String Room_name; // ���̸�
		Vector<UserInfo> Room_user_V = new Vector<UserInfo>(); // �� ���� ����
		Vector<String> Room_user_name = new Vector<String>();

		// Vector InRoomList = new Vector();
		JList<String> InRoomFriendlist;

		public RoomInfo(String RoomName, UserInfo user) {
			this.Room_name = RoomName;
			this.Room_user_V.add(user);
			this.Room_user_name.addElement(user.ClientInfor);
		}

		public void BroadCast_Room(String str) { // ���� ���� ��� ����鿡�� �˸�
			for (int i = 0; i < Room_user_V.size(); i++) {
				UserInfo u = (UserInfo) Room_user_V.elementAt(i);
				u.send_Message(str);
			}
		}
		public void BroadCast_RoomEmo(String str) { // ���� ���� ��� ����鿡�� �˸�
			for (int i = 0; i < Room_user_V.size(); i++) {
				UserInfo u = (UserInfo) Room_user_V.elementAt(i);
				u.send_Message(str);
			}
		}

		public void Add_User(UserInfo u) {
			this.Room_user_V.add(u);
			this.Room_user_name.add(u.ClientInfor);
		}

	}

	public void Update_User_S(String str) {
		logOn_S.remove(str);
		ChatServer.SlistChatFriend.setListData(logOn_S);
	}

	public void Update_Room_S(Vector vector) {
		ChatServer.SlistChatRoom.setListData(vector); // ������ â�� �� ����Ʈ ������Ʈ
	}
}
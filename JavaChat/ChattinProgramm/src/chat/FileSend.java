package chat;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class FileSend extends JFrame implements ActionListener {
	
	//���ϴ��̾�α�
	FileDialog fd;
	JButton btnPick, btnSend;
	JTextField txt;
	String directory = "", file= "";
	
	//�⺻ ������ſ� �ʿ��� ��
	ServerSocket sSocket;
	Socket socket;
	String ip;
	String id;
	
	//��ü ��Ʈ��
	InputStream is;
	OutputStream os;
	BufferedReader br;
	
	//���� ��
	public FileSend(ServerSocket sSocket, Socket socket, String ip, String id) {
		this.sSocket = sSocket;
		this.socket = socket;
		this.ip = ip;
		this.id = id;
	}
	
	//Ŭ���̾�Ʈ ��
	public FileSend(Socket socket, String ip, String id) {
		this.socket = socket;
		this.ip = ip;
		this.id = id;
		
		btnPick = new JButton("���� ����");
		btnPick.addActionListener(this);
		btnSend = new JButton("���� ����");
		btnSend.addActionListener(this);
		add(btnPick,"North");
		add(txt,"Center");
		add(btnSend,"South");
		setBounds(200,200,200,200);
		setVisible(true);
	}
	
	public void main(String args[]) {
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}

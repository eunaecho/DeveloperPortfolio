package chat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.JScrollPane;

public class TextChat extends JFrame {

	JFrame ChatLog;
	JTextPane ChatText;
	private JPanel contentPane;

	/*** LAUNCH
	Launch the application.

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TextChat frame = new TextChat();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	 */

	/****** Create the frame.	*******/
	
	public TextChat(String str) {
		ChatLog = new JFrame(str + "대화 내용");
		ChatLog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ChatLog.setBounds(100, 100, 544, 543);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setForeground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		ChatLog.getContentPane().add(contentPane);
		contentPane.setLayout(null);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 12, 498, 472);
		contentPane.add(scrollPane);
		
		ChatText = new JTextPane();
		scrollPane.setViewportView(ChatText);
		ChatText.setEditable(false);
		ChatText.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		
	}
}

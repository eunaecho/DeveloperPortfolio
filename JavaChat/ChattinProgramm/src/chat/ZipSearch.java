package chat;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JButton;

/******		 ZipDao 	********/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;
import javax.swing.JTextArea;
import java.awt.Font;

public class ZipSearch extends JFrame implements ActionListener {

	public JFrame SearchAddr;
	private JPanel contentPane;
	private JTable table;
	private JComboBox<String> comboBox;
	private JComboBox<String> comboBox_1;
	private JComboBox<String> comboBox_2;

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private JScrollPane scrollPane;
	private JPanel panel;
	private JTextField tfDong;
	static public JTextField txtPost1;
	static public JTextField txtPost2;
	static public JTextField txtAddr1;
	static public JTextField txtAddr2;
	private JButton btnSelect;
	private JButton btnInputAddr;
	
	public static int selectRow;
	public StringTokenizer st;
	public String post;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ZipSearch frame = new ZipSearch();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ZipSearch() {
		SearchAddr = new JFrame("주소 검색");
		SearchAddr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SearchAddr.setBounds(100, 100, 746, 642);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		SearchAddr.add(contentPane);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "주소 선택", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 10, 708, 83);
		panel.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		contentPane.add(panel);
		panel.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 185, 702, 200);
		contentPane.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		table.setModel(new DefaultTableModel(new Object[][] { { " ", " ", " ", " ", " ", " ", " ", " " }, },
				new String[] { "우편번호", "번지", "시", "구", "동", "리", "건물명", "호" }) {
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.setRowHeight(25);

		scrollPane.setViewportView(table);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(99, 40, 147, 31);
		comboBox.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		panel.add(comboBox);
		comboBox.addItem("시/도");

		displaySido();
		// ��.�� �޺��ڽ�=============================================
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					selectSido(comboBox.getSelectedItem().toString());

			}
		});
		comboBox.setToolTipText("");

		JLabel label = new JLabel("주소 선택");
		label.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		label.setBounds(118, 14, 100, 20);
		panel.add(label);
		label.setHorizontalAlignment(SwingConstants.CENTER);

		// ��.�� ComboBox=============================================
		comboBox_1 = new JComboBox();
		comboBox_1.setBounds(268, 40, 115, 31);
		comboBox_1.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		panel.add(comboBox_1);

		JLabel label_1 = new JLabel("구.군");
		label_1.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		label_1.setBounds(272, 14, 100, 20);
		panel.add(label_1);
		label_1.setHorizontalAlignment(SwingConstants.CENTER);

		comboBox_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					selectGugun(comboBox.getSelectedItem().toString(), comboBox_1.getSelectedItem().toString());
			}
		});

		comboBox_2 = new JComboBox();
		comboBox_2.setBounds(397, 40, 126, 31);
		comboBox_2.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		panel.add(comboBox_2);

		JLabel label_2 = new JLabel("동");
		label_2.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		label_2.setBounds(410, 14, 100, 20);
		panel.add(label_2);
		label_2.setHorizontalAlignment(SwingConstants.CENTER);

		comboBox_2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)

					selectDong(comboBox.getSelectedItem().toString(), comboBox_1.getSelectedItem().toString(),
							comboBox_2.getSelectedItem().toString());
			}
		});

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "���̸� ������ȣ �˻�", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(6, 105, 708, 68);
		panel_1.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel = new JLabel("주소 검색");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(175, 19, 84, 36);
		lblNewLabel.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		panel_1.add(lblNewLabel);

		tfDong = new JTextField(20);
		tfDong.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		tfDong.setBounds(273, 26, 181, 29);
		panel_1.add(tfDong);
		tfDong.setColumns(10);

		JButton btnDongSearce = new JButton("검색");
		btnDongSearce.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnDongSearce.setBounds(468, 25, 68, 30);
		panel_1.add(btnDongSearce);

		txtPost1 = new JTextField();
		txtPost1.setBounds(94, 440, 82, 37);
		contentPane.add(txtPost1);
		txtPost1.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		txtPost1.setColumns(10);

		txtPost2 = new JTextField();
		txtPost2.setColumns(10);
		txtPost2.setBounds(210, 440, 82, 37);
		txtPost2.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		contentPane.add(txtPost2);

		txtAddr1 = new JTextField();
		txtAddr1.setBounds(94, 490, 415, 37);
		contentPane.add(txtAddr1);
		txtAddr1.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		txtAddr1.setColumns(10);

		txtAddr2 = new JTextField();
		txtAddr2.setColumns(10);
		txtAddr2.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		txtAddr2.setBounds(94, 539, 415, 37);
		contentPane.add(txtAddr2);

		btnInputAddr = new JButton("\uC8FC\uC18C \uC785\uB825 \uC644\uB8CC\r\n");
		btnInputAddr.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnInputAddr.setBounds(572, 533, 142, 37);
		contentPane.add(btnInputAddr);
		btnInputAddr.addActionListener(this);

		JLabel lblNewLabel_1 = new JLabel("-");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(178, 445, 28, 28);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("\uC8FC\uC18C");
		lblNewLabel_2.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(14, 490, 66, 37);
		contentPane.add(lblNewLabel_2);

		JLabel label_3 = new JLabel("\uC0C1\uC138\uC8FC\uC18C");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		label_3.setBounds(6, 539, 89, 37);
		contentPane.add(label_3);

		JLabel label_4 = new JLabel("\uC6B0\uD3B8\uBC88\uD638");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		label_4.setBounds(6, 440, 89, 37);
		contentPane.add(label_4);

		btnSelect = new JButton("주소 선택");
		btnSelect.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		btnSelect.setBounds(629, 440, 85, 37);
		contentPane.add(btnSelect);
		btnSelect.addActionListener(this);
		
		SearchAddr.setVisible(true);
		btnDongSearce.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				searchDong(tfDong.getText());
			}
		});

	}

	/******* �� �޾ƿ��� **********/
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSelect) {
			selectRow = table.getSelectedRow(); // ������ ���̺� ���� �ް�

			if (selectRow == -1)
				JOptionPane.showMessageDialog(null, "�ּҸ� �������ּ���", "�˸�", JOptionPane.ERROR_MESSAGE);
			else {
				post = (String) table.getValueAt(selectRow, 1);
				st = new StringTokenizer(post, "-");

				txtPost1.setText(st.nextToken());
				txtPost2.setText(st.nextToken());

				String addr="";
				String j;
				for (int i = 2; i < 8; i++) {
					j = (String) table.getValueAt(selectRow, i);
						addr += (j) + " ";
				}
				addr = String.valueOf(addr).replace("null", " ");
				txtAddr1.setText(addr);
			}
		}
		else if(e.getSource() == btnInputAddr) {
			ChatJoin.txtBunji1.setText(txtPost1.getText());
			ChatJoin.txtBunji2.setText(txtPost2.getText());
			ChatJoin.txtAddress2.setText(txtAddr1.getText() + " " + txtAddr2.getText());
			SearchAddr.setVisible(false);
		}
	}

	public void searchDong(String dongName) {
		ZipDao controller = new ZipDao();
		controller.connection();

		ArrayList<ZipDto> addressList = controller.searchKeyDong(dongName);
		Object[][] arrAdd = new Object[addressList.size()][8];
		for (int i = 0; i < addressList.size(); i++) {
			ZipDto zipcode = addressList.get(i);

			arrAdd[i][0] = zipcode.getidx();
			arrAdd[i][1] = zipcode.getZipcode();
			arrAdd[i][2] = zipcode.getSido();
			arrAdd[i][3] = zipcode.getGugun();
			arrAdd[i][4] = zipcode.getDong();
			arrAdd[i][5] = zipcode.getRi();
			arrAdd[i][6] = zipcode.getBldg();
			arrAdd[i][7] = zipcode.getBunji();

			table.setModel(new ZipTableModel(arrAdd));

		}
		controller.disconnection();
	}

	public void displaySido() {
		ZipDao controller = new ZipDao();
		controller.connection();
		//
		ArrayList<ZipDto> sidoList = controller.searchSido();
		for (int i = 0; i < sidoList.size(); i++) {
			ZipDto zipcode = sidoList.get(i);
			comboBox.addItem(zipcode.getSido());
		}
		controller.disconnection();
	}

	public void selectSido(String sido) {
		System.out.println(sido);
		ZipDao controller = new ZipDao();
		// DB����
		controller.connection();
		//
		ArrayList<ZipDto> gugunList = controller.searchGugun(sido);
		comboBox_1.removeAllItems();
		comboBox_2.removeAllItems();
		comboBox_1.addItem("시/도");
		for (int i = 0; i < gugunList.size(); i++) {
			ZipDto zipcode = gugunList.get(i);
			comboBox_1.insertItemAt(zipcode.getGugun(), i);
		}
		table.setModel(new ZipTableModel());
		// DB���� ����
		controller.disconnection();
	}

	// gugun ����(dong
	// ���)====================================================================
	public void selectGugun(String sido, String gugun) {
		System.out.println(gugun);
		ZipDao controller = new ZipDao();
		// DB����
		controller.connection();
		//
		ArrayList<ZipDto> dongList = controller.searchDong(sido, gugun);
		comboBox_2.removeAllItems();
		comboBox_2.addItem("구/군");
		for (int i = 0; i < dongList.size(); i++) {
			ZipDto zipcode = dongList.get(i);
			comboBox_2.insertItemAt(zipcode.getDong(), i);
		}
		table.setModel(new ZipTableModel());
		// DB���� ����
		controller.disconnection();
	}

	// ������ Dong ����(���̺���
	// ���)====================================================================
	public void selectDong(String sido, String gugun, String dong) {
		System.out.println("Selected Dong : " + dong);
		ZipDao controller = new ZipDao();
		// DB����
		controller.connection();
		//
		ArrayList<ZipDto> addressList = controller.searchAddress(sido, gugun, dong);

		Object[][] arrAdd = new Object[addressList.size()][8];

		for (int i = 0; i < addressList.size(); i++) {
			ZipDto zipcode = addressList.get(i);
			// ���!
			System.out.println(zipcode.getidx() + " " + zipcode.getZipcode() + " " + zipcode.getSido() + " "
					+ zipcode.getGugun() + " " + zipcode.getDong() + " " + zipcode.getRi() + " " + zipcode.getBldg()
					+ " " + zipcode.getBunji());
			// ���̺��� �ֱ�!
			arrAdd[i][0] = zipcode.getidx();
			arrAdd[i][1] = zipcode.getZipcode();
			arrAdd[i][2] = zipcode.getSido();
			arrAdd[i][3] = zipcode.getGugun();
			arrAdd[i][4] = zipcode.getDong();
			arrAdd[i][5] = zipcode.getRi();
			arrAdd[i][6] = zipcode.getBldg();
			arrAdd[i][7] = zipcode.getBunji();

			table.setModel(new ZipTableModel(arrAdd));
			System.out.println("table Setting ");
		}
		// DB���� ����
		controller.disconnection();

	}

	/***** ZipDao **********/

	public class ZipDao {
		Connection conn;
		PreparedStatement pstmt;
		ResultSet rs;

		public void connection() {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "wpfflWpf2!");

			} catch (ClassNotFoundException e) {

			} catch (SQLException e) {

			}
		}

		public void disconnection() {
			try {
				if (pstmt != null)
					pstmt.close();

				if (rs != null)
					rs.close();

				if (conn != null)
					conn.close();

			} catch (SQLException e) {

			}
		}

		public ArrayList<ZipDto> searchSido() {
			ArrayList<ZipDto> sidoList = new ArrayList<ZipDto>();
			try {
				String query = "select distinct(sido) from SEARCH_POST order by sido";
				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ZipDto zipcode = new ZipDto();
					zipcode.setSido(rs.getString("SIDO"));
					sidoList.add(zipcode);
				}
			} catch (SQLException e) {
			}

			return sidoList;
		}

		public ArrayList<ZipDto> searchGugun(String sido) {
			ArrayList<ZipDto> gugunList = new ArrayList<>();

			try {
				String query = "select distinct(gugun) from SEARCH_POST where sido = \'" + sido + "\' order by gugun";
				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ZipDto zipcode = new ZipDto();
					zipcode.setGugun(rs.getString("GUGUN"));
					gugunList.add(zipcode);
				}
			} catch (SQLException e) {
			}

			return gugunList;
		}

		public ArrayList<ZipDto> searchDong(String sido, String gugun) {
			ArrayList<ZipDto> dongList = new ArrayList<>();

			try {
				String query = "select distinct(dong) from SEARCH_POST where sido = \'" + sido + "\'  and gugun = \'"
						+ gugun + "\' order by dong";
				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ZipDto zipcode = new ZipDto();
					zipcode.setDong(rs.getString("DONG"));
					dongList.add(zipcode);
				}
			} catch (SQLException e) {
			}

			return dongList;
		}

		public ArrayList<ZipDto> searchAddress(String sido, String gugun, String dong) {

			ArrayList<ZipDto> addressList = new ArrayList<>();

			try {
				String query = "select * from SEARCH_POST where sido = \'" + sido + "\'  and gugun = \'" + gugun
						+ "\' and dong = \'" + dong + "\'";

				pstmt = conn.prepareStatement(query);

				rs = pstmt.executeQuery();

				while (rs.next()) {

					ZipDto zipcode = new ZipDto();

					zipcode.setidx(rs.getString("idx"));
					zipcode.setZipcode(rs.getString("zipcode"));
					zipcode.setSido(rs.getString("sido"));
					zipcode.setGugun(rs.getString("gugun"));
					zipcode.setDong(rs.getString("dong"));
					zipcode.setRi(rs.getString("ri"));
					zipcode.setBldg(rs.getString("bldg"));
					zipcode.setBunji(rs.getString("bungi"));

					addressList.add(zipcode);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return addressList;
		}

		public ArrayList<ZipDto> searchKeyDong(String dongName) {

			ArrayList<ZipDto> addressList = new ArrayList<>();

			try {
				String query = "select * from SEARCH_POST where dong like \'%" + dongName + "%\'";

				pstmt = conn.prepareStatement(query);

				rs = pstmt.executeQuery();

				while (rs.next()) {

					ZipDto zipcode = new ZipDto();

					zipcode.setidx(rs.getString("idx"));
					zipcode.setZipcode(rs.getString("zipcode"));
					zipcode.setSido(rs.getString("sido"));
					zipcode.setGugun(rs.getString("gugun"));
					zipcode.setDong(rs.getString("dong"));
					zipcode.setRi(rs.getString("ri"));
					zipcode.setBldg(rs.getString("bldg"));
					zipcode.setBunji(rs.getString("bungi"));

					addressList.add(zipcode);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return addressList;
		}
	}

	/********* ZipDto *************/

	public class ZipDto {
		private String idx;
		private String zipcode;
		private String sido;
		private String gugun;
		private String dong;
		private String ri;
		private String bldg;
		private String bunji;

		public String getidx() {
			return idx;
		}

		public void setidx(String idx) {
			this.idx = idx;
		}

		public String getZipcode() {
			return zipcode;
		}

		public void setZipcode(String zipcode) {
			this.zipcode = zipcode;
		}

		public String getSido() {
			return sido;
		}

		public void setSido(String sido) {
			this.sido = sido;
		}

		public String getGugun() {
			return gugun;
		}

		public void setGugun(String gugun) {
			this.gugun = gugun;
		}

		public String getDong() {
			return dong;
		}

		public void setDong(String dong) {
			this.dong = dong;
		}

		public String getRi() {
			return ri;
		}

		public void setRi(String ri) {
			this.ri = ri;
		}

		public String getBldg() {
			return bldg;
		}

		public void setBldg(String bldg) {
			this.bldg = bldg;
		}

		public String getBunji() {
			return bunji;
		}

		public void setBunji(String bunji) {
			this.bunji = bunji;
		}

	}

	/******* ZipTableModel *******/
	public class ZipTableModel extends AbstractTableModel {

		String[] columNames = { "우편번호", "번지", "시", "구", "동", "리", "건물명", "호" };
		// ������
		Object[][] data = { { " ", " ", " ", " ", " ", " ", " ", " " } };

		public ZipTableModel() {

		}

		public ZipTableModel(Object[][] data) {
			this.data = data;
		}

		@Override
		public int getColumnCount() {
			return columNames.length;
		}

		@Override
		public int getRowCount() {
			return data.length;
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			return data[arg0][arg1];
		}

		@Override
		public String getColumnName(int arg0) {
			return columNames[arg0];
		}

	}

}

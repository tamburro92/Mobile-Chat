package client.UI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import org.json.JSONException;

import client.Client;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.List;
import javax.swing.JList;
import javax.swing.JRadioButton;
import java.awt.Color;

public class ClientUI {

	private JFrame frame;
	private JTextField userField;
	private JTextField ipField;
	private JTextField portField;
	
	private Client client;
	private JTabbedPane tabbedPane;
	private JPanel panel;
	private JPanel panel_1;
	private JTextField warningField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientUI window = new ClientUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 11, 434, 225);
		frame.getContentPane().add(tabbedPane);
		
		panel = new JPanel();
		tabbedPane.addTab("Login", null, panel, null);
		panel.setLayout(null);
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setBounds(124, 128, 114, 50);
		panel.add(btnLogin);
		
		userField = new JTextField();
		userField.setBounds(250, 50, 122, 20);
		panel.add(userField);
		userField.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(265, 28, 81, 14);
		panel.add(lblUsername);
		
		ipField = new JTextField();
		ipField.setBounds(22, 56, 86, 20);
		panel.add(ipField);
		ipField.setText("localhost");
		ipField.setColumns(10);
		
		JLabel lblServerIp = new JLabel("Server IP:");
		lblServerIp.setBounds(30, 34, 65, 14);
		panel.add(lblServerIp);
		
		portField = new JTextField();
		portField.setBounds(136, 54, 46, 20);
		panel.add(portField);
		portField.setText("1676");
		portField.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(136, 32, 46, 14);
		panel.add(lblPort);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String ip=ipField.getText();
				int port=Integer.parseInt(portField.getText());
				String userName=userField.getText();
				try {
					client=new Client("localhost",port);
					client.login(userName);
					
				} catch (UnknownHostException e) {
					System.out.println(ip+" "+port);
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch(JSONException e){
					
				}
				if(client.isLogged()){
					panel_1.setEnabled(true);
					warningField.setText("LOGIN SUCCESS, Username: "+userName);
				}else{
					warningField.setText("LOGIN FAILED, Username: "+userName);

				}
				
				
				
			}
		});
		
		panel_1 = new JPanel();
		tabbedPane.addTab("Utenti", null, panel_1, null);
		panel_1.setLayout(null);
		
		JList usersOnline = new JList();
		usersOnline.setBounds(239, 139, 83, -116);
		panel_1.add(usersOnline);
		
		JButton btnRefresh = new JButton("refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					java.util.List<String> userOnlineList = client.getUserList();
					DefaultListModel listModel = new DefaultListModel();
					for(String user:userOnlineList){
						listModel.addElement(user);						
					}
					usersOnline.setModel(listModel);
				} catch (JSONException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnRefresh.setBounds(239, 163, 89, 23);
		panel_1.add(btnRefresh);
		
		warningField = new JTextField();
		warningField.setBounds(0, 230, 434, 31);
		frame.getContentPane().add(warningField);
		warningField.setColumns(10);
	}
}

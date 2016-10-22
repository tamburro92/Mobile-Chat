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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONException;

import client.Client;
import protocol.Code;

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
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class ClientUI {

	private JFrame frame;
	private JTextField userField;
	private JTextField ipField;
	private JTextField portField;
	
	private Client client;
	private JTabbedPane tabbedPane;
	private JPanel loginPanel;
	private JPanel usersPanel;
	private JTextField warningField;
	private JList usersOnlinelist;
	private JButton btnSendMessage;
	private JPanel messagesPanel;
	private JTextPane messagesPane;
	private JTextField messageField;
	private JButton sendMessageBtn;
	
	private Set<String> destinationUsers;
	private JLabel lblChatWith;
	private JButton btnRefreshMessage;

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
		
		loginPanel = new JPanel();
		tabbedPane.addTab("Login", null, loginPanel, null);
		loginPanel.setLayout(null);
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setBounds(124, 128, 114, 50);
		loginPanel.add(btnLogin);
		
		userField = new JTextField();
		userField.setBounds(250, 50, 122, 20);
		loginPanel.add(userField);
		userField.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(265, 28, 81, 14);
		loginPanel.add(lblUsername);
		
		ipField = new JTextField();
		ipField.setBounds(22, 56, 86, 20);
		loginPanel.add(ipField);
		ipField.setText("localhost");
		ipField.setColumns(10);
		
		JLabel lblServerIp = new JLabel("Server IP:");
		lblServerIp.setBounds(30, 34, 65, 14);
		loginPanel.add(lblServerIp);
		
		portField = new JTextField();
		portField.setBounds(136, 54, 46, 20);
		loginPanel.add(portField);
		portField.setText("1676");
		portField.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(136, 32, 46, 14);
		loginPanel.add(lblPort);
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
					usersPanel.setEnabled(true);
					warningField.setText("LOGIN SUCCESS, Username: "+userName);
				}else{
					warningField.setText("LOGIN FAILED, Username: "+userName);

				}
				
				
				
			}
		});
		
		usersPanel = new JPanel();
		tabbedPane.addTab("Utenti", null, usersPanel, null);
		usersPanel.setLayout(null);
		
		JButton btnRefreshUsersList = new JButton("Refresh Users");
		btnRefreshUsersList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					java.util.List<String> userOnlineList = client.getUserList();
					DefaultListModel listModel = new DefaultListModel();
					for(String user:userOnlineList){
						listModel.addElement(user);						
					}
					usersOnlinelist.setModel(listModel);
				} catch (JSONException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnRefreshUsersList.setBounds(261, 151, 108, 23);
		usersPanel.add(btnRefreshUsersList);
		
		usersOnlinelist = new JList();
		usersOnlinelist.setBounds(224, 11, 169, 129);
		usersPanel.add(usersOnlinelist);
		
		btnSendMessage = new JButton("Send Message");
		btnSendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				destinationUsers=new HashSet();
				int[] index = usersOnlinelist.getSelectedIndices();
				if(index.length==0){
					warningField.setText("NESSUN UTENTE SELEZIONATO");
				}else if(index.length==1){
					warningField.setText("CHAT SINGOLA: "+usersOnlinelist.getModel().getElementAt(index[0]));
					destinationUsers.add((String) usersOnlinelist.getModel().getElementAt(index[0]));
					lblChatWith.setText("Chath with "+(String) usersOnlinelist.getModel().getElementAt(index[0]));
					createNewTab();
				}else if(index.length>1){
					String users="";
					for(int i=0;i<index.length;i++){
						users+=" "+usersOnlinelist.getModel().getElementAt(index[i]);
						destinationUsers.add((String) usersOnlinelist.getModel().getElementAt(index[i]));
					}
					lblChatWith.setText("Chath with "+users);
					warningField.setText("CHAT MULTIPLA: "+users);

				}
			}
		});
		btnSendMessage.setBounds(49, 151, 101, 23);
		usersPanel.add(btnSendMessage);
		
		messagesPanel = new JPanel();
		tabbedPane.addTab("New tab", null, messagesPanel, null);
		messagesPanel.setLayout(null);
		
		messagesPane = new JTextPane();
		messagesPane.setEditable(false);
		messagesPane.setBounds(27, 21, 207, 121);
		messagesPanel.add(messagesPane);
		
		messageField = new JTextField();
		messageField.setBounds(27, 153, 207, 20);
		messagesPanel.add(messageField);
		messageField.setColumns(10);
		
		sendMessageBtn = new JButton("Send Message");
		sendMessageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.sendsMessage(destinationUsers, messageField.getText());
				String txt=messagesPane.getText();
				messagesPane.setText(txt+client.getUserName()+": "+messageField.getText()+"\n");
			}
		});
		sendMessageBtn.setBounds(279, 152, 101, 23);
		messagesPanel.add(sendMessageBtn);
		
		lblChatWith = new JLabel("Chat with: ");
		lblChatWith.setBounds(27, -4, 392, 28);
		messagesPanel.add(lblChatWith);
		
		btnRefreshMessage = new JButton("Refresh Message");
		btnRefreshMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JSONArray arrayMSG = client.updateMessage();
					for (int i = 0; i < arrayMSG.length(); i++) {
						String sender=arrayMSG.getJSONObject(i).getString(Code.SENDER);
						arrayMSG.getJSONObject(i).get(Code.RECEIVERS);
						String txt=messagesPane.getText();
						messagesPane.setText(txt+sender+": "+arrayMSG.getJSONObject(i).getString(Code.MESSAGE)+"\n");
					}
					
				} catch (IOException | JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnRefreshMessage.setBounds(279, 70, 120, 23);
		messagesPanel.add(btnRefreshMessage);
		
		warningField = new JTextField();
		warningField.setEditable(false);
		warningField.setBounds(0, 230, 434, 31);
		frame.getContentPane().add(warningField);
		warningField.setColumns(10);
	}
	private void createNewTab(){
		JPanel messagePanel = new JPanel();
		tabbedPane.addTab("MessagePanel", null, messagePanel, null);
		messagePanel.setLayout(null);
	}
}

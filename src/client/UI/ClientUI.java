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
import java.net.ConnectException;
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
import javax.swing.Timer;
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
import javax.swing.JScrollBar;
import javax.swing.ScrollPaneConstants;

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
	private JList<String> usersOnlinelist;
	private JButton btnSendMessage;
	private JPanel messagesPanel;
	private JTextPane messagesPane;
	private JTextField messageField;
	private JButton sendMessageBtn;

	private Set<String> destinationUsers;
	private JLabel lblChatWith;
	private JButton btnRefreshMessage;
	private JButton btnLogout;
	private JLabel lblSelectOneOr;

	Timer timerUpdateListUser;
	Timer timerUpdateMessage;
	private JScrollPane scrollPane;

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
		initTimer();
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
		btnLogin.setBounds(65, 115, 114, 50);
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

		btnLogout = new JButton("LOGOUT");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(2, false);
				timerUpdateMessage.stop();
				timerUpdateListUser.stop();
				try {
					client.logout();
					warningField.setText("LOGOUT: SUCCESS");
				} catch (JSONException e1) {

				} catch (IOException e1) {
					warningField.setText("ERRORE I/0");
				} catch (NullPointerException e1) { // può essere sollevata
													// quando il client non è
													// stato creato
					warningField.setText("ESEGUI IL LOGIN!!!");
				}
			}
		});
		btnLogout.setBounds(243, 115, 114, 50);
		loginPanel.add(btnLogout);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					String ip = ipField.getText();
					int port = Integer.parseInt(portField.getText());
					String userName = userField.getText();
					client = new Client(ip, port);
					client.login(userName);
					if (client.isLogged()) {
						usersPanel.setEnabled(true);
						tabbedPane.setEnabledAt(1, true);
						timerUpdateListUser.start();// start timer to update
													// list user automatically
						warningField.setText("LOGIN SUCCESS, Username: " + userName);
					} else {
						warningField.setText("LOGIN FAILED, Username: " + userName);

					}

				} catch (UnknownHostException | ConnectException e) {
					warningField.setText("SERVER OFFLINE " + ipField.getText() + ":" + portField.getText());

				} catch (IOException e) {
					warningField.setText("ERRORE I/0");

				} catch (JSONException e) {

				} catch (NumberFormatException e) {
					warningField.setText("FORMATO PORTA ERRATO " + portField.getText());

				}

			}
		});

		usersPanel = new JPanel();
		tabbedPane.addTab("Users", null, usersPanel, null);
		tabbedPane.setEnabledAt(1, false);
		usersPanel.setLayout(null);

		JButton btnRefreshUsersList = new JButton("Refresh Users");
		btnRefreshUsersList.setEnabled(false);
		btnRefreshUsersList.setVisible(false); //BOTTONO CHE DOVRA ESSERE CANCELLATO, DA MANTENRE PER DEBUG
		btnRefreshUsersList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					java.util.List<String> userOnlineList = client.getUserList();
					DefaultListModel<String> listModel = new DefaultListModel<String>();
					for (String user : userOnlineList) {
						listModel.addElement(user);
					}
					usersOnlinelist.setModel(listModel);
				} catch (JSONException e1) {

				} catch (IOException e1) {
					warningField.setText("ERRORE I/0");
				}
			}
		});
		btnRefreshUsersList.setBounds(261, 151, 108, 23);
		usersPanel.add(btnRefreshUsersList);

		usersOnlinelist = new JList<String>();
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		usersOnlinelist.setModel(listModel);
		usersOnlinelist.setBounds(224, 11, 169, 129);
		usersPanel.add(usersOnlinelist);

		btnSendMessage = new JButton("Send Message");
		btnSendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				destinationUsers = new HashSet<String>();
				int[] index = usersOnlinelist.getSelectedIndices();
				if (index.length == 0) {
					warningField.setText("NESSUN UTENTE SELEZIONATO");
				} else {
					tabbedPane.setEnabledAt(2, true);
					timerUpdateMessage.start();
					if (index.length == 1) {
						warningField.setText("CHAT SINGOLA: " + usersOnlinelist.getModel().getElementAt(index[0]));
						destinationUsers.add((String) usersOnlinelist.getModel().getElementAt(index[0]));
						lblChatWith.setText("Chath with " + (String) usersOnlinelist.getModel().getElementAt(index[0]));
					} else {
						String users = "";
						for (int i = 0; i < index.length; i++) {
							users += " " + usersOnlinelist.getModel().getElementAt(index[i]);
							destinationUsers.add((String) usersOnlinelist.getModel().getElementAt(index[i]));
						}
						lblChatWith.setText("Chath with " + users);
						warningField.setText("CHAT MULTIPLA: " + users);

					}
				}
			}
		});
		btnSendMessage.setBounds(49, 151, 101, 23);
		usersPanel.add(btnSendMessage);

		lblSelectOneOr = new JLabel("Select one or more users:");
		lblSelectOneOr.setBounds(70, 61, 130, 23);
		usersPanel.add(lblSelectOneOr);

		messagesPanel = new JPanel();
		tabbedPane.addTab("Chat", null, messagesPanel, null);
		tabbedPane.setEnabledAt(2, false);
		messagesPanel.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(27, 21, 207, 121);
		messagesPanel.add(scrollPane);

		messagesPane = new JTextPane();
		scrollPane.setViewportView(messagesPane);
		messagesPane.setEditable(false);

		messageField = new JTextField();
		messageField.setBounds(27, 153, 207, 20);
		messagesPanel.add(messageField);
		messageField.setColumns(10);

		sendMessageBtn = new JButton("Send Message");
		sendMessageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.sendsMessage(destinationUsers, messageField.getText());
				String txt = messagesPane.getText();
				messagesPane.setText(txt + client.getUserName() + ": " + messageField.getText() + "\n");
			}
		});
		sendMessageBtn.setBounds(279, 152, 101, 23);
		messagesPanel.add(sendMessageBtn);

		lblChatWith = new JLabel("Chat with: ");
		lblChatWith.setBounds(27, -4, 392, 28);
		messagesPanel.add(lblChatWith);

		btnRefreshMessage = new JButton("Refresh Message");
		btnRefreshMessage.setVisible(false); //NASCONDIAMO ALL'UTENTE //DA CANCELLARE DOPO 
		btnRefreshMessage.setEnabled(false);
		btnRefreshMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JSONArray arrayMSG = client.updateMessage();
					for (int i = 0; i < arrayMSG.length(); i++) {
						String sender = arrayMSG.getJSONObject(i).getString(Code.SENDER);
						arrayMSG.getJSONObject(i).get(Code.RECEIVERS);
						String txt = messagesPane.getText();
						messagesPane.setText(
								txt + sender + ": " + arrayMSG.getJSONObject(i).getString(Code.MESSAGE) + "\n");
					}
				} catch (IOException e1) {
					warningField.setText("ERRORE I/0");

				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					/// e1.printStackTrace();
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

	private void initTimer() {
		timerUpdateListUser = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					java.util.List<String> userOnlineList = client.getUserList();
					
					DefaultListModel<String> listModel = (DefaultListModel<String>) usersOnlinelist.getModel();
					//AGGIORNA LA LISTA DI UTENTI: (tutto questo per non ricreare la lista daccapo e quindi perdere il riferimento
					//vedi gli elementi da rimuovere 
					java.util.List<String> elemToRemove=new LinkedList();
					for(int i=0;i<listModel.size();i++){
						if(!userOnlineList.contains(listModel.getElementAt(i))){
							elemToRemove.add(listModel.getElementAt(i));
						}

					}//elimina gli elementi non online
					for(String iesim: elemToRemove){
						listModel.removeElement(iesim);
					}
					//aggiungi i nuovi elementi
					for(String iesim: userOnlineList){
						if(!listModel.contains(iesim))
							listModel.addElement(iesim);
					}
				} catch (JSONException e1) {

				} catch (IOException e1) {
					warningField.setText("ERRORE I/0");
				}
			}
		});
		timerUpdateListUser.setRepeats(true); // Only execute once
		timerUpdateMessage = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					JSONArray arrayMSG = client.updateMessage();
					for (int i = 0; i < arrayMSG.length(); i++) {
						String sender = arrayMSG.getJSONObject(i).getString(Code.SENDER);
						arrayMSG.getJSONObject(i).get(Code.RECEIVERS);
						String txt = messagesPane.getText();
						messagesPane.setText(
								txt + sender + ": " + arrayMSG.getJSONObject(i).getString(Code.MESSAGE) + "\n");
					}
				} catch (IOException e1) {
					warningField.setText("ERRORE I/0");
				} catch (JSONException e1) {

				}
			}
		});
		timerUpdateMessage.setRepeats(true);

	}
}

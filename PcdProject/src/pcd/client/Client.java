package pcd.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import pcd.shared.Message;

public class Client {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	private Socket notifySocket;
	private ClientGUI window;
	private RemoteDirectory directory;
	
	public Client(InetAddress a, int port, int notifyPort) {
		connectToServer(a,port,notifyPort);
		try {
			directory = (RemoteDirectory) in.readObject();
		} catch (ClassNotFoundException | IOException e) {}
		openGUI();
		NotificationHandler handler = new NotificationHandler(notifySocket, this);
		handler.start();
	}

	private void connectToServer(InetAddress a, int p, int notifyPort) {
		try {
			socket = new Socket(a,p);
			notifySocket = new Socket(a, notifyPort);
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Couldn't connect to socket");
		} 

	}

	public void openGUI() {
		window = new ClientGUI(this);
		window.open();
	}
	
	public void sendMessage(Object o) {
		try {
			out.writeObject(o);
		} catch (IOException e) {}
	}
	
	public void sendMessageAndWaitToReceive(Object o) {
		try {
			out.writeObject(o);
			receiveObject();
		} catch (IOException e) {}
	}
	
	private void receiveObject() {
		try {
			Object obj = in.readObject();
			if(obj instanceof Message) {
				if(((Message) obj).getType().name().equals("READ"))
					window.exhibitWindow(((Message) obj).getMessage(), ((Message) obj).getFileName());
				else if(((Message) obj).getType().name().equals("WRITE"))
					window.editWindow(((Message) obj).getMessage(), ((Message) obj).getFileName());
				else if(((Message) obj).getType().name().equals("SIZE")) {
					JOptionPane.showMessageDialog(null, ((Message) obj).getMessage());
				}
			}
			else if(obj instanceof RemoteDirectory) {
				directory = ((RemoteDirectory) obj); 
				window.refreshFileList();
			}
		} catch (IOException e) {} catch (ClassNotFoundException e) {} 
	}

	public RemoteDirectory getDirectory() {
		return directory;
	}
	
	public ClientGUI getWindow() {
		return window;
	}
	
	public void add(RemoteFile file) {
		directory.add(file);
		window.refreshFileList();
	}

	public static void main(String[] args) {
		try {
			InetAddress address = InetAddress.getByName(args[0]);
			int port, notifyPort;
			try {
				port = Integer.parseInt(args[1]);
				notifyPort = Integer.parseInt(args[2]);
			}catch(NumberFormatException e) {
				System.out.println("one of the ports couldn't be parsed to integer");
				System.out.println("Enter the InetAdress first then the port number and only after that the port number for the notifications");
				return;
			}
			
			@SuppressWarnings("unused")
			Client c  = new Client(address,port,notifyPort);
		} catch (UnknownHostException e) {
			System.out.println("Unkown host, couldn't connect");
		} 
	}

	public void remove(String fileName) {
		directory.remove(fileName);
		window.refreshFileList();
	}

}

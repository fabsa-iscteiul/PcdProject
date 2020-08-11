package pcd.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import pcd.shared.Notification;

public class NotificationHandler extends Thread{

	private Client client;
	private Socket socket;
	private ObjectInputStream in;
	public NotificationHandler(Socket s, Client client) {
		socket = s;
		try {
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {}
		this.client = client;
	}

	@Override
	public void run() {
		while(true) {
			try {
				Notification notification = (Notification) in.readObject();
				if(notification.getType().name().equals("CREATE")) {
					RemoteFile file= new RemoteFile(notification.getFileName());
					client.add(file);
				}
				else if(notification.getType().name().equals("DELETE")) {
					client.remove(notification.getFileName());
				}
				JOptionPane.showMessageDialog(null, notification.getNotificationContext());
			} 
			catch (IOException e) {} 
			catch (ClassNotFoundException e) {}
		}
	}
}

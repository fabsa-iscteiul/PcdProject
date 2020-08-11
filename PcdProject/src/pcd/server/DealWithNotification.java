package pcd.server;

import java.io.IOException;

import pcd.shared.Notification;

public class DealWithNotification extends Thread{

	private Server server;
	public DealWithNotification(Server server) {
		this.server = server;
	}

	@Override
	public void run() {
		while(true) {
			try {
				Notification notification = server.removeNotification();
				server.notifyAllClients(notification);
			} 
			catch (IOException e) {} 
			catch (InterruptedException e) {}
		}
	}
}

package pcd.server;

import java.io.IOException;
import pcd.shared.Message;
import pcd.shared.Notification;
import pcd.shared.Message.MsgType;
import pcd.shared.Notification.NotificationType;

public class Request implements Runnable {

	private Message msg;
	private Server server;
	private DealWithClient deal;

	public Request(DealWithClient deal, Message message, Server sv) {
		msg = message;
		server = sv;
		this.deal=deal;
	}

	@Override
	public void run() {
		try {
			Message msgToSend = null;
			if(msg.getType().name().equals("CREATE")) {
				LocalFile file = (LocalFile) server.getLocalDirectory().newFile(msg.getFileName());
				String fileText = file.read();
				file.lockWrite();
				deal.setFileWriteSent(file);
				msgToSend = new Message(MsgType.WRITE, file.getName());
				msgToSend.setMessage(fileText);
				Notification notification = new Notification(NotificationType.CREATE, file.getName());
				notification.setNotificationContext("The file " + file.getName() + " was created");
				server.addNotification(notification);
				deal.sendObject(msgToSend);
			}
			else {
				LocalFile file = server.getFile(msg.getFileName());
				if(msg.getType().name().equals("SAVE")) {
					file.write(msg.getMessage());
					file.writeUnlock();
					deal.setFileWriteSent(null);
					Notification notification = new Notification(NotificationType.EDIT, file.getName());
					notification.setNotificationContext("The file " + file.getName() + " was edited");
					server.addNotification(notification);
				}
				else if(msg.getType().name().equals("READ") || msg.getType().name().equals("WRITE")) {
					String fileText = file.read();
					if(msg.getType().name().equals("READ")) {
						file.lockRead();
						deal.setFileReadSent(file);
						msgToSend = new Message(MsgType.READ,file.getName());
						msgToSend.setMessage(fileText);
					}
					else {
						file.lockWrite();
						deal.setFileWriteSent(file);
						msgToSend = new Message(MsgType.WRITE,file.getName());
						msgToSend.setMessage(fileText);
					}
					deal.sendObject(msgToSend);
				}
				else if(msg.getType().name().equals("SIZE")) {
					int len = file.length();
					msgToSend = new Message(MsgType.SIZE,file.getName());
					msgToSend.setMessage("The size of the file "+ file.getName() + " is "+len);
					deal.sendObject(msgToSend);
				}
				else if(msg.getType().name().equals("CLOSE")) {
					file.readUnlock();
					deal.setFileReadSent(null);
				}
				else if(msg.getType().name().equals("DELETE")) {
					file.lockWrite();
					Notification notification = new Notification(NotificationType.DELETE, file.getName());
					notification.setNotificationContext("The file " + file.getName() + " was deleted");
					server.getLocalDirectory().delete(msg.getFileName());
					server.addNotification(notification);
				}
			}
		}	catch(IOException e) {} catch (InterruptedException e) {}
	}
}

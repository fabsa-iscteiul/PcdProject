package pcd.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Notification implements Serializable {
	public enum NotificationType{
		DELETE, CREATE, EDIT;
	}
	
	private String notificationContext="", fileName;
	private NotificationType type;
	
	public Notification(NotificationType ntype, String fileName) {
		type = ntype;
		this.fileName = fileName;
	}
	
	public NotificationType getType() {
		return type;
	}
	
	public void setNotificationContext(String notificationContext) {
		this.notificationContext = notificationContext;
	}
	
	public String getNotificationContext() {
		return notificationContext;
	}

	public String getFileName() {
		// TODO Auto-generated method stub
		return fileName;
	}
}

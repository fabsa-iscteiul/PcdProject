package pcd.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable {
	public enum MsgType{
		READ,WRITE,SIZE,CREATE,DELETE,CLOSE, SAVE;
	}
	
	private String fileName;
	private String message = "";
	private MsgType type;
	
	
	public Message(MsgType t, String name) {
		type=t;
		fileName=name;
	}
	
	public MsgType getType() {
		return type;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setType(MsgType type) {
		this.type = type;
	}
	
	public void setMessage(String fileContent) {
		this.message = fileContent;
	}
	
	public String getMessage() {
		return message;
	}
}

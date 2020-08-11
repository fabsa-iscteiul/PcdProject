package pcd.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RemoteFile implements Serializable{
	
	private String fileName;
	
	public RemoteFile(String name) {
		fileName = name;
	}
	
	public String getName() {
		return fileName;
	}

}

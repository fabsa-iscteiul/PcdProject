package pcd.client;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class RemoteDirectory implements Serializable{
	private List<RemoteFile> fileList = new LinkedList<>();
	private String localDirectoryName;

	public RemoteDirectory(String directoryName, List<RemoteFile> file) {
		localDirectoryName = directoryName;
		fileList = file;
	}

	public String getLocalDirectoryName() {
		return localDirectoryName;
	}

	public String[] getDirectoryListing() {
		String[] fileNames = new String[fileList.size()];
		int i=0;
		for (RemoteFile remoteFile : fileList) {
			fileNames[i]= remoteFile.getName();
			i++;
		}
		return fileNames;
	}

	public List<RemoteFile> getFileList() {
		return fileList;
	}

	public void add(RemoteFile file) {
		fileList.add(file);
	}

	public boolean exists(String fileName) {
		for (RemoteFile remoteFile : fileList) {
			if(fileName.equals(remoteFile.getName()))
				return true;
		}
		return false;
	}
	
	public RemoteFile getFile(String fileName){
		for (RemoteFile remoteFile : fileList) {
			if(remoteFile.getName().equals(fileName))
				return remoteFile;
		}
		return null;
	}
	
	public void remove(String fileName) {
		if(exists(fileName)) {
			RemoteFile fileToDelete =getFile(fileName);
			fileList.remove(fileToDelete);
		}
	}
}

package pcd.server;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileSystemException;
import java.util.LinkedList;
import java.util.List;

import pcd.shared.PCDDirectory;
import pcd.shared.PCDFile;

@SuppressWarnings("serial")
public class LocalDirectory implements PCDDirectory, Serializable{

	private String directoryPath;
	private String folderName;
	private List<LocalFile> fileList = new LinkedList<>();

	public LocalDirectory(String path, String name) {
		directoryPath = path;
		folderName = name;
		initFileList();
	}

	public String getDirectoryPath() {
		return directoryPath;
	}

	private void initFileList() { 
		File[] files = new File(directoryPath).listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(pathname.getName().endsWith(".txt"))
					return true;
				return false;
			}
		});
		for (File file : files) {
			LocalFile f = new LocalFile(file);
			fileList.add(f);
		}
	}

	@Override
	public boolean fileExists(String name) throws IOException {
		// TODO Auto-generated method stub
		for (LocalFile localFile : fileList) {
			if(localFile.getName().equals(name))
				return true;
		}
		return false;
	}

	@Override
	public PCDFile newFile(String name) throws FileSystemException, IOException {
		File file = new File(directoryPath, name);
		file.createNewFile();
		LocalFile newFile = new LocalFile(file);
		fileList.add(newFile);
		return newFile;
	}

	@Override
	public void delete(String name) throws FileSystemException, IOException {
		LocalFile fileToDelete = null;
		for (LocalFile localFile : fileList) {
			if(localFile.getName().equals(name)) {
				fileToDelete = localFile;
				break;
			}
		}
		fileList.remove(fileToDelete);
		fileToDelete.delete();
	}

	@Override
	public String[] getDirectoryListing() throws FileSystemException, IOException {
		String[] arrayToReturn = new String[fileList.size()];
		int i = 0;
		for (LocalFile localFile : fileList) {
			arrayToReturn[i] = localFile.getName();
			i++;
		}
		return arrayToReturn;
	}

	@Override
	public PCDFile getFile(String name) throws FileSystemException, IOException {
		for (LocalFile localFile : fileList) {
			if(localFile.getName().equals(name))
				return localFile;
		}
		return null;
	}

	public List<LocalFile> getFileList(){
		return fileList;
	}

	public String getFolderName() {
		// TODO Auto-generated method stub
		return folderName;
	}
}

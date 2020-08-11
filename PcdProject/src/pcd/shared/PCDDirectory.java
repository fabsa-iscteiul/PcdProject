package pcd.shared;

import java.io.IOException;
import java.nio.file.FileSystemException;

public interface PCDDirectory {
	public abstract boolean fileExists(String name) throws IOException;
	public abstract PCDFile newFile(String name) throws FileSystemException, IOException;
	public abstract void delete(String name) throws FileSystemException, IOException;
	public abstract String[] getDirectoryListing() throws FileSystemException, IOException;
	public abstract PCDFile getFile(String name) throws FileSystemException, IOException;
}

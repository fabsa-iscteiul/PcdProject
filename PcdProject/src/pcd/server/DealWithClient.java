package pcd.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import pcd.client.RemoteDirectory;
import pcd.client.RemoteFile;
import pcd.shared.Message;

public class DealWithClient extends Thread {

	private Socket socket;
	private Server server;
	private ObjectOutputStream out,notifyOut;
	private ObjectInputStream in;
	private LocalFile fileReadSent = null;
	private LocalFile fileWriteSent = null;

	public DealWithClient(Socket s, Server sv, ObjectOutputStream notifyOut) {
		socket = s;
		server = sv;
		this.notifyOut = notifyOut;
		doConnections();
	}

	private void doConnections() {
		try {
			out= new ObjectOutputStream((socket.getOutputStream()));
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("Error connecting");
		}
	}

	private void dealWithClient() throws ClassNotFoundException, IOException, InterruptedException {
		Message msg = (Message)in.readObject();
		Request requestToAdd = new Request(this,msg,server);
		server.getPool().submit(requestToAdd);

	}

	@Override
	public void run() {
		try {
			refreshFileList();
		} catch (IOException e) {}
		try {
			while(true)
				dealWithClient();
		}
		catch (ClassNotFoundException e) {} 
		catch (IOException e) { 
			try {
				if(!(fileReadSent == null))
					fileReadSent.readUnlock();
				if(!(fileWriteSent == null))
					fileWriteSent.writeUnlock();
				server.removeNotifyClient(notifyOut);
			} catch (IOException e1) {}
		} catch (InterruptedException e) {}
	}
	
	public void setFileReadSent(LocalFile fileReadSent) {
		this.fileReadSent = fileReadSent;
	}
	
	public void setFileWriteSent(LocalFile fileWriteSent) {
		this.fileWriteSent = fileWriteSent;
	}
	
	public void sendObject(Object obj) {
		try {
			out.writeObject(obj);
		} catch (IOException e) {}
	}
	
	private void refreshFileList() throws IOException {
		List<RemoteFile> fileList = new LinkedList<>();
		for (LocalFile localFile : server.getLocalDirectory().getFileList()) {
			RemoteFile file = new RemoteFile(localFile.getName());
			fileList.add(file);
		}
		RemoteDirectory directoryToSend = new RemoteDirectory(server.getLocalDirectory().getFolderName(),fileList);
		out.writeObject(directoryToSend);
	}
}

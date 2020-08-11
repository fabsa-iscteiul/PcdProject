package pcd.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystemException;
import java.util.LinkedList;
import java.util.List;

import pcd.shared.BlockingQueue;
import pcd.shared.Notification;

public class Server {
	private ServerSocket serverSocket; 
	private ServerSocket notificationSocket;
	private BlockingQueue<Notification> notifications = new BlockingQueue<Notification>();
	private List<ObjectOutputStream> allOuts = new LinkedList<>();
	private LocalDirectory localDirectory;
	private ThreadPool pool;
	private final int MAX_WORKERS = 10;

	public Server(int port, int notifyPort,LocalDirectory directory) {
		try{
			pool = new ThreadPool(MAX_WORKERS);
			localDirectory= directory;
			serverSocket = new ServerSocket(port);
			notificationSocket = new ServerSocket(notifyPort);
		}catch (IOException e) {
			System.out.println("Error criating server socket");
			try {
				serverSocket.close();
				notificationSocket.close();
			}catch(IOException e1) {
				System.out.println("Error closing server socket");
			}
		}
	}
	private void startServer() {
		DealWithNotification notificationHandler = new DealWithNotification(this);
		notificationHandler.start();
		while(true) {
			try {
				Socket socket= serverSocket.accept();
				Socket notifySocket = notificationSocket.accept();
				ObjectOutputStream out = new ObjectOutputStream(notifySocket.getOutputStream());
				allOuts.add(out);
				DealWithClient deal = new DealWithClient(socket,this,out);
				deal.start();
			} 
			catch (IOException e) {} 					
		}
	}
	
	public ThreadPool getPool() {
		return pool;
	}
	
	public synchronized LocalFile getFile(String fileName) {
		LocalFile fileToReturn = null;
		try {
			fileToReturn = (LocalFile) localDirectory.getFile(fileName);
		} 
		catch (FileSystemException e) {} 
		catch (IOException e) {}
		return fileToReturn;

	}
	
	public void removeNotifyClient(ObjectOutputStream out) {
		allOuts.remove(out);
	}
	
	public void notifyAllClients(Notification notification) throws IOException {
		for (ObjectOutputStream objectOutputStream : allOuts) {
			objectOutputStream.writeObject(notification);
		}
	}

	public LocalDirectory getLocalDirectory() {
		return localDirectory;
	}

	public Notification removeNotification() throws InterruptedException {
		return notifications.take();
	}
	
	public void addNotification(Notification notification) throws InterruptedException {
		notifications.add(notification);
	}

	public static void main(String[] args) throws FileSystemException, IOException {
		int port, notifyPort;
		try{
			port = Integer.parseInt(args[0]);
			notifyPort = Integer.parseInt(args[1]);
		}catch (NumberFormatException e){
			System.out.println("One of the indicated ports couldn´t be parsed to integer");
			System.out.println("Enter the port number where the server will wait for clients then the port number for the notifications and finally then the name of the folder(PCDFiles)");
			return ;
		}
		String path = System.getProperty("user.dir")+"\\"+args[2];
		LocalDirectory localDirectory = new LocalDirectory(path,args[2]);
		Server s = new Server(port,notifyPort,localDirectory);
		s.startServer();
	}
}

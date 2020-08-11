package pcd.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystemException;
import java.util.Scanner;

import pcd.shared.PCDFile;
import pcd.shared.Semaphore;

@SuppressWarnings("serial")
public class LocalFile implements PCDFile {
	
	public static final int NUM_READERS=3;
	public static final int NUM_WRITERS=1;
	private File file;
	private Semaphore semRead = new Semaphore(NUM_READERS);
	private Semaphore semWrite = new Semaphore(NUM_WRITERS);
	
	public LocalFile(File f) {
		file = f;
	}
	
	@Override
	public String read() throws FileSystemException, IOException {
		Scanner sc = new Scanner(file);
		String dataToReturn = "";
		while(sc.hasNextLine()) 
			dataToReturn+=sc.nextLine()+"\n";
		sc.close();
		if (dataToReturn.endsWith("\n"))
			dataToReturn = dataToReturn.substring(0,dataToReturn.length()-1);
		return dataToReturn;
	}
	
	@Override
	public void write(String dataToWrite) throws FileSystemException, FileNotFoundException {
		PrintWriter writer = new PrintWriter(file);
		writer.write(dataToWrite);
		writer.close();
	}


	public Semaphore getSemRead() {
		return semRead;
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return file.getName();
	}

	@Override
	public boolean readLock() throws IOException {
		try {
			semRead.down();
		} catch (InterruptedException e) {System.out.println("Interrupted while trying to down sem read");}
		return false;
	}

	@Override
	public boolean writeLock() throws IOException { 
		try {
			semWrite.down();
		} catch (InterruptedException e) {}
		return false;
	}

	@Override
	public void readUnlock() throws IOException {
		// TODO Auto-generated method stub
		try {
			semRead.up();
		} catch (InterruptedException e) {
			System.out.println("Interrupted while trying to up sem read");
		}
	}

	@Override
	public void writeUnlock() throws IOException {
		// TODO Auto-generated method stub
		try {
			semWrite.up();
		} catch (InterruptedException e) {}
	}

	@Override
	public boolean exists() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int length() throws FileSystemException, IOException {
		// TODO Auto-generated method stub
		return (int) file.length();
	}


	public Semaphore getSemWrite() {
		// TODO Auto-generated method stub
		return semWrite;
	}


	public void delete() {
		// TODO Auto-generated method stub
		file.delete();
	}
	
	public void lockRead() {
		try {
			semRead.down();
		} catch (InterruptedException e) {}
	}


	public void lockWrite() {
		try {
			semWrite.down();
		} catch (InterruptedException e) {}
	}

}

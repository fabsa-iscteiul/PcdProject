package pcd.shared;

import java.util.LinkedList;

public class BlockingQueue <T>{
	private LinkedList<T> list= new LinkedList<>();
	private final int max ;
	
	public BlockingQueue() {
		max=0;
	}
	
	public BlockingQueue(int max) {
		this.max = max;
	}
	 public LinkedList<T> getList() {
		return list;
	}
	 
	 public int getMax() {
		return max;
	}
	 
	 public synchronized void add(T objectToAdd) throws InterruptedException {
		 if(max == 0) {
			list.add(objectToAdd);
			notify();
		 }
		 else {
			 while(list.size() == max)
				 wait();
			 list.add(objectToAdd);
			 notifyAll();
		 }
	 }
	 
	 public synchronized T take() throws InterruptedException{
		 while(list.isEmpty())
			 wait();
		 T objectToReturn = list.getFirst();
		 list.removeFirst();
		 if(max == 0)
			 return objectToReturn;
		 else {
			 notifyAll();
			 return objectToReturn;
		 }
	 }
}

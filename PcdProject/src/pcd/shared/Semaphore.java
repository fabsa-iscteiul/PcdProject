package pcd.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Semaphore implements Serializable {

	private final int max;
	private int current;

	public Semaphore() {
		max=-1;
		current = 1;
	}

	public Semaphore(int m) {
		max=m;
		current = max;
	}

	public synchronized void up() throws InterruptedException {
		if(max != -1) {
			while((current+1) > max) 
				wait();
			current++;
			notifyAll();
		}
		else {
			current++;
			notify();
		}
	}

	public synchronized void down() throws InterruptedException {
		while((current - 1 ) < 0)
			wait();
		current--;
		if(max != -1)
			notifyAll();
	}

	public int getCurrent() {
		return current;
	}
}

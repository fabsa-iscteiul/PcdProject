package pcd.server;

import pcd.shared.BlockingQueue;

public class ThreadPool {
	private int maxWorkers;
	private BlockingQueue<Runnable> requests = new BlockingQueue<>();
	
	public ThreadPool(int max) {
		maxWorkers = max;
		for (int i = 0; i < maxWorkers; i++) {
			WorkerThread worker = new WorkerThread(this);
			worker.start();
		}
	}
	
	public void submit(Runnable request) throws InterruptedException {
		requests.add(request);
	}
	
	public Runnable takeTask() throws InterruptedException{
		Runnable task = requests.take();
		return task;
	}

}

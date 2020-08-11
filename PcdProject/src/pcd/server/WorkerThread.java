package pcd.server;

public class WorkerThread extends Thread{
	private ThreadPool pool;

	public WorkerThread(ThreadPool threadPool) {
		pool = threadPool;
	}

	@Override
	public void run() {
		while(true) {
			try {
				pool.takeTask().run();
			} catch (InterruptedException e) {}
		}
	}
}

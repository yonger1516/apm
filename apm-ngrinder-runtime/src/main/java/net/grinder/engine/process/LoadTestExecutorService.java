package net.grinder.engine.process;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by fuyong on 12/7/15.
 */
public class LoadTestExecutorService implements ExecutorService{

	ThreadPoolExecutor executor;

	ScheduledExecutorService ses;

	public LoadTestExecutorService() {
		this(0, Integer.MAX_VALUE, 60L, TimeUnit.MILLISECONDS);
	}

	public LoadTestExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
		executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
				keepAliveTime, unit,
				new SynchronousQueue<Runnable>());

		ses = Executors.newScheduledThreadPool(3);
	}

	public void scheduleAtFixedRate(final Runnable task, int delay, int period, TimeUnit unit) {
		ses.scheduleAtFixedRate(new Runnable() {
			public void run() {
				executor.submit(task);
			}
		}, delay, period, unit);
	}


	public <T> Future<T> submit(Callable<T> task) {
		return executor.submit(task);
	}

	public <T> Future<T> submit(Runnable task, T result) {
		//return executor.submit(task);
		return null;
	}

	public Future<?> submit(Runnable task) {
		return executor.submit(task);
	}

	public int getActiveThreads() {
		return executor.getActiveCount();
	}

	public int getQueueSize() {
		return executor.getQueue().size();
	}

	public long getTaskCount() {
		return executor.getTaskCount();
	}


	public void shutdown() {

	}

	public List<Runnable> shutdownNow() {
		return null;
	}

	public boolean isShutdown() {
		return false;
	}

	public boolean isTerminated() {
		return false;
	}

	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return false;
	}


	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return null;
	}

	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
		return null;
	}

	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return null;
	}

	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return null;
	}

	public void execute(Runnable command) {

	}
}

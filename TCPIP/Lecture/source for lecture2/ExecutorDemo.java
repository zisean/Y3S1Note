import java.util.concurrent.*;

public class ExecutorDemo {
	public static void main(String[] args) {
		// Create a thread pool
		ExecutorService executor = Executors.newFixedThreadPool(3);
		
		// Submit Runnable taks to executor
		executor.execute(new PrintChar('X',100));
		executor.execute(new PrintChar('Y',100));
		executor.execute(new PrintInt(100));

		// Shutdown the executor
		// IMPORTANT! Otherwise program will not terminate
		executor.shutdown();
	}
}


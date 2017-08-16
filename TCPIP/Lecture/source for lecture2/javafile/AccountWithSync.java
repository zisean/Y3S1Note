import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class AccountWithSync {
	private static Account account = new Account();

	public static void main (String... arg) {
		ExecutorService executor = Executors.newCachedThreadPool();

		//create and launch 100 threads
		for(int i = 0; i < 100; ++i){
			executor.execute(new AddAPennyTask());
		}//end for

		executor.shutdown();

		//wait for all tasks to terminate
		while(!executor.isTerminated()) {}

		System.out.println("Account Balance: " + account.getBalance());
	}

	//a thread for adding a penny to the account
	private static class AddAPennyTask implements Runnable {
		public void run() {
			account.deposit(1);
		}
	}


	//inner class
	private static class Account {
		//create a lock
		private static Lock lock = new ReentrantLock();

		private int balance = 0;

		public int getBalance() {
			return balance;
		}

		public void deposit(int amount) {
			//acquire a lock
			lock.lock();

			try{
				int newBalance = balance + amount;
				Thread.sleep(10);
				balance = newBalance;
			}
			catch(InterruptedException ie) {}
			finally {
				//release the lock
				lock.unlock();
			}
		}
	}//end Account
}
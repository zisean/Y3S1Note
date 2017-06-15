import java.util.concurrent.*;

public class AccountWithoutSync {
	private static Account account = new Account();

	public static void main(String args[]) {
		ExecutorService executor = Executors.newCachedThreadPool();
	  
		//create and launch 100 threads
		for(int i = 0; i < 100; ++i) {
			executor.execute(new AddAPennyTask());
		}//end for

		executor.shutdown();

		//wait for all tasks to terminate
		while(!executor.isTerminated()) {}

		System.out.println("Account Balance: " + account.getBalance());
	}
 
	//a thread for adding a penny to the account
	private static class AddAPennyTask implements Runnable {
		public void run(){
			account.deposit(1);
		}
	}

	//inner class
	private static class Account {
		private int balance = 0;

		public int getBalance() {
			return balance;
		}

		public void deposit(int amount) {
			int newBalance = balance + amount;
			try{
				Thread.sleep(10);
			}
			catch(InterruptedException ie) {}

			balance = newBalance;
		}
	}//end Account
}

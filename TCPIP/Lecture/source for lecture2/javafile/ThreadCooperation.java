import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ThreadCooperation {
	private static Account account = new Account();

	public static void main(String... arg) {
		ExecutorService executor = Executors.newFixedThreadPool(2);

		executor.execute(new DepositTask());
		executor.execute(new WithdrawTask());
		executor.shutdown();

		System.out.println("Thread 1\t\tThread 2\t\tBalance");
	} //main 

	public static class DepositTask implements Runnable {
		public void run() {
			try {
				while(true){
					account.deposit((int)(Math.random() * 10) + 1);
					Thread.sleep(10);
				} //while
			}
			catch(InterruptedException ie) {}
		} //run
	} //end DepositTask

	public static class WithdrawTask implements Runnable {
		public void run(){
			while(true){
				account.withdraw((int)(Math.random() * 10) + 1);
			}
		} //run
	} //end WithdrawTask

	//an inner class for account
	private static class Account {
		//create new lock
		private static Lock lock = new ReentrantLock();

		//create new condition
		private static Condition newDeposit = lock.newCondition();

		private int balance = 0;

		public int getBalance() {
			return balance;
		}//end getBalance()  

		public void withdraw(int amount){
			lock.lock();
			try{
				while(balance < amount){
					newDeposit.await();
				}//while
				balance -= amount;
				System.out.println("\t\t\tWithdraw " + amount + "\t\t" + getBalance());
			}
			catch(InterruptedException ie) {}
			finally {
				lock.unlock();
			}
		}//end withdraw()

		public void deposit(int amount) {
			lock.lock();
			try{
				balance += amount;
				System.out.println("Deposit " + amount + "\t\t\t\t\t" + getBalance());
				newDeposit.signalAll();
			}
			catch(Exception ie) {}
			finally {
				lock.unlock();
			}
		}//deposit
	}//end Account class
}//end main
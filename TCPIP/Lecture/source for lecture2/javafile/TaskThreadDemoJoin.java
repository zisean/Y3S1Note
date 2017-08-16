public class TaskThreadDemoJoin {
	public static void main(String[] args) {
		// Task objects
		
PrintChar printX = new PrintChar('X',100);
		PrintInt print100 = new PrintInt(100);

		// Thread objects
		Thread printXThread = new Thread(printX);
		Thread print100Thread = new Thread(print100);

		// Start threads
		printXThread.start();
		print100Thread.start();
	}
}

class PrintChar implements Runnable {
	char ch;
	int n;

	public PrintChar(char ch, int n) {
		this.ch = ch;
		this.n = n;
	}

	public void run() {
		for(int i = 1; i <= n; i++) {
			System.out.print(ch + " ");
		}
	}
}

class PrintInt implements Runnable {
	int n;

	public PrintInt(int n) {
		this.n = n;
	}

	public void run() {
		try {
			PrintChar printY = new PrintChar('Y', 100);
			Thread printYThread = new Thread(printY);
			printYThread.start();
			for(int i = 1; i <= n; i++) {
				System.out.print(i + " ");
				if(i == 25) printYThread.join();
			}
		}
		catch(InterruptedException ex) {
		}
	}
}

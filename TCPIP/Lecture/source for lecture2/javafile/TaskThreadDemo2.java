public class TaskThreadDemo2 {
	public static void main(String[] args) {
		// Thread objects
		PrintChar printX = new PrintChar('X',100);
		PrintChar printY = new PrintChar('Y',100);
		PrintInt print100 = new PrintInt(100);

		// Start threads
		printX.start();
		printY.start();
		print100.start();
	}
}

class PrintChar extends Thread {
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

class PrintInt extends Thread {
	int n;

	public PrintInt(int n) {
		this.n = n;
	}

	public void run() {
		for(int i = 1; i <= n; i++) {
			System.out.print(i + " ");
		}
	}
}
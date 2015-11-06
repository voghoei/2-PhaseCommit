import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcertOperation extends Thread {
	static String transactionId;
	static int[] ticketAvailable;
	ConcurrentLinkedQueue<String> qinlocal;
	ConcurrentLinkedQueue<String> qoutlocal;
	
	
	
	int statusLocal;

	ConcertOperation(ConcurrentLinkedQueue<String> qin, ConcurrentLinkedQueue<String> qout, int status) {
		this.statusLocal = status;
		this.qinlocal = qin;
		this.qoutlocal = qout;
		concertConfig();
		start();
	}

	public void run() {
		while (true) {
			try {
				if (statusLocal != 1) {
					while (true) {
						Thread.sleep(100);
					}
				}
				if (qinlocal.size() > 0) {
					System.out.println("Concert Opration qin while " + qinlocal.toString());
					String msg = qinlocal.poll();
					switch (msg.split(":")[0]) {
					case "VOTE-REQUEST":
						if (checkavailabality(msg.split(":")[1])) {
							System.out.println("Commit");
							qoutlocal.add("VOTE-COMMIT:" + msg.split(":")[1]);
						} else {
							System.out.println("Abort");
							qoutlocal.add("VOTE-ABORT:" + msg.split(":")[1]);
						}
						break;
					case "GLOBAL-COMMIT":
						System.out.println("GLOBAL-COMMIT");
						deductTicket(msg);
						break;

					case "GLOBAL-ABORT":
						System.out.println("GLOBAL-ABORT");
						break;

					}
				}
			} catch (InterruptedException ex) {
				System.out.println("exp : Callee  ");
				try {
					while (true) {
						System.out.println("sleep ");
						Thread.sleep(100);
					}
				} catch (InterruptedException ex1) {
					System.out.println("awaik ");
					statusLocal = 1;
				}

			}
		}

	}
	
	public static void logHandeler(){
		
	}

	public static boolean checkavailabality(String Command) {
		boolean flag = true;
		if (Command.contains("[")) {
			transactionId = Command.split(" ")[0];
			String days = Command.split("\\[")[1].split("\\]")[0];
			int numTicket = Integer.parseInt(Command.split(" ")[1]);
			for (int i = 0; i < days.split(" ").length; i++) {
				if (numTicket > ticketAvailable[Integer.parseInt(days.split(" ")[i])]) {
					flag = false;
				}
			}
		}
		return flag;
	}

	public static void deductTicket(String Command) {

		if (Command.contains("[")) {
			transactionId = Command.split(" ")[0];
			String days = Command.split("\\[")[1].split("\\]")[0];
			int numTicket = Integer.parseInt(Command.split(" ")[1]);
			for (int i = 0; i < days.split(" ").length; i++) {
				ticketAvailable[Integer.parseInt(days.split(" ")[i])] -= numTicket;

			}
		}
	}

	public static void concertConfig() {
		try {
			BufferedReader brConcert = new BufferedReader(new InputStreamReader(new FileInputStream("concert-configuration-file.txt")));
			ticketAvailable = new int[10];
			brConcert.readLine();
			for (int i = 0; i < 10; i++) {
				ticketAvailable[i] = Integer.parseInt(brConcert.readLine().split(" ")[1]);
			}

		} catch (IOException e) {
			System.out.println("exp: Concert :concertConfig ");
			e.printStackTrace();
		}

	}
}

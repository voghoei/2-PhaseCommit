import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HotelOperation extends Thread {
	static String transactionId;
	static int[] roomAvailable;
	ConcurrentLinkedQueue<String> qinlocal;
	ConcurrentLinkedQueue<String> qoutlocal;

	HotelOperation(ConcurrentLinkedQueue<String> qin, ConcurrentLinkedQueue<String> qout) {
		this.qinlocal = qin;
		this.qoutlocal = qout;
		hotelConfig();
		start();
	}

	public void run() {
		while (true) {
			if (qinlocal.size() > 0) {
				System.out.println("*****  qin value " + qinlocal.toString()
						+ " qin size = " + qinlocal.size());
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
		}
	}

	public static boolean checkavailabality(String Command) {
		boolean flag = true;
		System.out.println("Hotel Buffer In. Run : Command = " + Command);
		if (Command.contains("[")) {
			transactionId = Command.split(" ")[0];
//			System.out.println("transactionId = " + transactionId);

			String days = Command.split("\\[")[1].split("\\]")[0];
//			System.out.println("days = " + days);

			int numRoom = Integer.parseInt(Command.split(" ")[1]);
//			System.out.println("numRoom = " + numRoom);

			for (int i = 0; i < days.split(" ").length; i++) {
//				System.out.println("Each day  = "
//						+ Integer.parseInt(days.split(" ")[i]));
//
//				System.out
//				.println("roomAvailable = "
//						+ roomAvailable[Integer.parseInt(days
//								.split(" ")[i])]);

				if (numRoom > roomAvailable[Integer.parseInt(days
						.split(" ")[i])]) {
					flag = false;
				}
			}
		}
		return flag;
	}

	public static void deductTicket(String Command) {

		if (Command.contains("[")) {
			transactionId = Command.split(" ")[0];
//			System.out.println("transactionId = " + transactionId);

			String days = Command.split("\\[")[1].split("\\]")[0];
//			System.out.println("days = " + days);

			int numTicket = Integer.parseInt(Command.split(" ")[1]);
//			System.out.println("numTicket = " + numTicket);

			for (int i = 0; i < days.split(" ").length; i++) {
				roomAvailable[Integer.parseInt(days.split(" ")[i])] -= numTicket;
//				System.out.println("Each day  = "
//						+ Integer.parseInt(days.split(" ")[i]));
//
//				System.out
//						.println("numavailable = "
//								+ roomAvailable[Integer.parseInt(days
//										.split(" ")[i])]);
//				
			}
		}
	}
	
	public static void hotelConfig() {
		try {
			BufferedReader brHotel = new BufferedReader(
					new InputStreamReader(new FileInputStream(
							"hotel-configuration-file.txt")));
			roomAvailable = new int[10];
			brHotel.readLine();
			for (int i = 0; i < 10; i++) {
				roomAvailable[i] = Integer.parseInt(brHotel.readLine()
						.split(" ")[1]);
			}

		} catch (IOException e) {
			System.out.println("exp: Hotel :hotelConfig ");
			e.printStackTrace();
		}

	}
}

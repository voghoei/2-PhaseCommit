import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Queue;

public class HotelOperation {
	static String transactionId;
	static int[] roomAvailable;
	Queue<String> qinlocal;
	Queue<String> qoutlocal;

	HotelOperation(Queue<String> qin, Queue<String> qout) {
		qinlocal = qin;
		qoutlocal = qout;
		hotelConfig();
		decisionMaker();
	}

	public void decisionMaker() {
		while (true) {
			if (qinlocal.size() > 0) {
				System.out.println("*****  qin value " + qinlocal.toString()
						+ " qin size = " + qinlocal.size());
				String msg = qinlocal.poll();
				switch (msg.split(": ")[0]) {
				case "VOTE-REQUEST": {
					if (checkavailabality(msg.split(": ")[1])) {
						System.out.println("Commit");
						qoutlocal.add("VOTE-COMMIT:HOTEL:"+transactionId);
					} else {
						System.out.println("Abort");
						qoutlocal.add("VOTE-ABORT:HOTEL:"+transactionId);
					}
					break;
				}
				}
			}
		}
	}

	public static boolean checkavailabality(String Command) {
		boolean flag = true;
		System.out.println("Hotel Buffer In. Run : Command = " + Command);
		if (Command.contains("[")) {
			transactionId = Command.split(" ")[0];
			System.out.println("transactionId = " + transactionId);

			String days = Command.split("\\[")[1].split("\\]")[0];
			System.out.println("days = " + days);

			int numRoom = Integer.parseInt(Command.split(" ")[1]);
			System.out.println("numRoom = " + numRoom);

			for (int i = 0; i < days.split(" ").length; i++) {
				System.out.println("Each day  = "
						+ Integer.parseInt(days.split(" ")[i]));

				System.out
						.println("roomAvailable = "
								+ roomAvailable[Integer.parseInt(days
										.split(" ")[i])]);

				if (numRoom > roomAvailable[Integer.parseInt(days
						.split(" ")[i])]) {
					flag = false;
				}
			}
		}
		return flag;
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

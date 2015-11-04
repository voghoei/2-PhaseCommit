import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Queue;

public class ConcertOperation {
	static String transactionId;
	static int[] ticketAvailable;
	Queue<String> qinlocal;
	Queue<String> qoutlocal;

	ConcertOperation(Queue<String> qin, Queue<String> qout) {
		qinlocal = qin;
		qoutlocal = qout;
		concertConfig();
		decisionMaker();
	}

	public void decisionMaker() {
		while (true) {
			if (qinlocal.size() > 0) {
				System.out.println("*****  qin value " + qinlocal.toString()
						+ " qin size = " + qinlocal.size());
				String msg = qinlocal.poll();
				switch (msg.split(":")[0]) {
				case "VOTE-REQUEST": {
					if (checkavailabality(msg.split(":")[1])) {
						System.out.println("Commit");
						qoutlocal.add("VOTE-COMMIT:CONCERT:"+transactionId);
					} else {
						System.out.println("Abort");
						qoutlocal.add("VOTE-ABORT:CONCERT:"+transactionId);
					}
					break;
				}
				}
			}
		}
	}

	public static boolean checkavailabality(String Command) {
		boolean flag = true;
		System.out.println("Concert Buffer In. Run : Command = " + Command);
		if (Command.contains("[")) {
			transactionId = Command.split(" ")[0];
			System.out.println("transactionId = " + transactionId);

			String days = Command.split("\\[")[1].split("\\]")[0];
			System.out.println("days = " + days);

			int numTicket = Integer.parseInt(Command.split(" ")[1]);
			System.out.println("numTicket = " + numTicket);

			for (int i = 0; i < days.split(" ").length; i++) {
				System.out.println("Each day  = "
						+ Integer.parseInt(days.split(" ")[i]));

				System.out
						.println("numavailable = "
								+ ticketAvailable[Integer.parseInt(days
										.split(" ")[i])]);

				if (numTicket > ticketAvailable[Integer.parseInt(days
						.split(" ")[i])]) {
					flag = false;
				}
			}
		}
		return flag;
	}

	public static void concertConfig() {
		try {
			BufferedReader brConcert = new BufferedReader(
					new InputStreamReader(new FileInputStream(
							"concert-configuration-file.txt")));
			ticketAvailable = new int[10];
			brConcert.readLine();
			for (int i = 0; i < 10; i++) {
				ticketAvailable[i] = Integer.parseInt(brConcert.readLine()
						.split(" ")[1]);
			}

		} catch (IOException e) {
			System.out.println("exp: Concert :concertConfig ");
			e.printStackTrace();
		}

	}
}

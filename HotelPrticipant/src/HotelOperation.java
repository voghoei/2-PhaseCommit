import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class HotelOperation extends Thread {
	static String transactionId;
	static int[] roomAvailable;
	static ConcurrentLinkedQueue<String> qinlocal;
	static ConcurrentLinkedQueue<String> qoutlocal;

	AtomicInteger statusLocal;
	static String LastLog;

	HotelOperation(ConcurrentLinkedQueue<String> qin, ConcurrentLinkedQueue<String> qout, AtomicInteger status) throws IOException {

		this.statusLocal = status;
		this.qinlocal = qin;
		this.qoutlocal = qout;

		hotelConfig();
		LastLog = toString(roomAvailable);
		logDelete();
		start();
	}

	public void run() {
		while (true) {
			try {
				if (!(statusLocal.get() == 1)) {
					while (true) {
						Thread.sleep(100);
					}
				}
				if (qinlocal.size() > 0) {
					System.out.println("Hotel Opration qin while " + qinlocal.toString());
					String msg = qinlocal.poll();
					switch (msg.split(":")[0]) {
					case "VOTE-REQUEST":
						logHandeler("VOTE-REQUEST:" + msg.split(":")[1]);

						if (checkavailabality(msg.split(":")[1])) {
							System.out.println("Commit");
							qoutlocal.add("VOTE-COMMIT:" + msg.split(":")[1]);
							logHandeler("VOTE-COMMIT:" + msg.split(":")[1]);
						} else {
							System.out.println("Abort");
							qoutlocal.add("VOTE-ABORT:" + msg.split(":")[1]);
							logHandeler("VOTE-ABORT:" + msg.split(":")[1]);
						}
						break;
					case "GLOBAL-COMMIT":
						System.out.println("GLOBAL-COMMIT");
						logHandeler("GLOBAL-COMMI:" + msg.split(":")[1]);
						deductRoom(msg);
						LastLog = toString(roomAvailable);
						break;

					case "GLOBAL-ABORT":
						System.out.println("GLOBAL-ABORT");
						logHandeler("GLOBAL-ABORT:" + msg.split(":")[1]);
						break;

					}
				}
			} catch (InterruptedException ex) {
				System.out.println("exp : intrupt hotel operation catch  ");
				failOpration();
				try {
					while (true) {
						System.out.println("sleep ");
						Thread.sleep(100);
					}
				} catch (InterruptedException ex1) {
					System.out.println("awake ");
					recoveryOpration();
					statusLocal.set(1);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void failOpration() {

		System.out.println("Cleaning all buffers and memories");

		qinlocal.clear();

		// Clean ticketAvailable
		for (int i = 0; i < 10; i++) {
			roomAvailable[i] = 0;
		}
	}

	public static void recoveryOpration() {
		System.out.println("recover the roomAvailable and restart buffer in");

		String[] days = LastLog.split(",");
		for (int i = 0; i < 10; i++) {
			roomAvailable[i] = Integer.parseInt(days[i]);
		}
	}

	public static void logHandeler(String msg) throws IOException {

		File file = new File("HotelLog.txt");
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file, true);
		BufferedWriter bw = new BufferedWriter(fw);

		bw.write(msg);
		bw.newLine();
		bw.close();
		fw.close();

	}

	public static void logDelete() throws IOException {
		File file = new File("HotelLog.txt");
		if (file.exists()) {
			file.delete();
		}
	}

	public static boolean checkavailabality(String Command) {
		boolean flag = true;
		if (Command.contains("[")) {
			transactionId = Command.split(" ")[0];
			String days = Command.split("\\[")[1].split("\\]")[0];
			int numRoom = Integer.parseInt(Command.split(" ")[1]);
			for (int i = 0; i < days.split(" ").length; i++) {
				if (numRoom > roomAvailable[Integer.parseInt(days.split(" ")[i])]) {
					flag = false;
				}
			}
		}
		return flag;
	}

	public static void deductRoom(String Command) {

		if (Command.contains("[")) {
			transactionId = Command.split(" ")[0];
			String days = Command.split("\\[")[1].split("\\]")[0];
			int numRoom = Integer.parseInt(Command.split(" ")[1]);
			for (int i = 0; i < days.split(" ").length; i++) {
				roomAvailable[Integer.parseInt(days.split(" ")[i])] -= numRoom;
			}
		}
	}

	public static void hotelConfig() {
		try {
			BufferedReader brHotel = new BufferedReader(new InputStreamReader(new FileInputStream("hotel-configuration-file.txt")));
			roomAvailable = new int[10];
			brHotel.readLine();
			for (int i = 0; i < 10; i++) {
				roomAvailable[i] = Integer.parseInt(brHotel.readLine().split(" ")[1]);				
			}

		} catch (IOException e) {
			System.out.println("exp: Hotel :hotelConfig ");
			e.printStackTrace();
		}

	}

	public static String toString(int[] a) {
		String stringArray = "";
		for (int i = 0; i < 10; i++) {
			stringArray += a[i] + ",";			
		}
		return stringArray.substring(0,stringArray.length()-1);
	}
}

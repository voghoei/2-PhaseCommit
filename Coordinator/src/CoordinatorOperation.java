import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class CoordinatorOperation extends Thread {
	// timeout
	static long startTime;

	static // last Taransaction
	String lastTransaction;

	// status
	AtomicInteger statusLocal;

	static String transactionId;
	static int[] ticketAvailable;

	// buffers
	ConcurrentLinkedQueue<String> cqinlocal;
	ConcurrentLinkedQueue<String> hqinlocal;
	static Queue<String> qoutlocal;

	// input request file
	static ArrayList<String> reservations;

	// Input coordinate file
	static FileInputStream fstreamCoordinateFile = null;
	static BufferedReader brCoordinator = null;
	static BufferedReader brCoordinatorConfigLocal;
	Hashtable<String, Boolean> commitHashtableConcert;
	Hashtable<String, Boolean> commitHashtableHotel;

	CoordinatorOperation(ConcurrentLinkedQueue<String> cqin, ConcurrentLinkedQueue<String> hqin, ConcurrentLinkedQueue<String> qout,
			BufferedReader brCoordinatorConfig, AtomicInteger status) throws IOException {
		this.statusLocal = status;
		reservations = new ArrayList<String>();
		this.brCoordinatorConfigLocal = brCoordinatorConfig;
		loadRequest();
		commitHashtableConcert = new Hashtable<String, Boolean>();
		commitHashtableHotel = new Hashtable<String, Boolean>();
		this.cqinlocal = cqin;
		this.hqinlocal = hqin;
		this.qoutlocal = qout;
		logDelete();
		start();
	}

	public void run() {

		boolean transactionFlag;
		try {

			for (String reservation : reservations) {
				if (!(statusLocal.get() == 1)) {
					while (true) {
						Thread.sleep(100);
					}
				}
				qoutlocal.add("VOTE-REQUEST:" + reservation);
				logHandeler("VOTE-REQUEST:" + reservation);
				System.out.println("VOTE-REQUEST added to local cout");

				transactionFlag = true;
				startTime = System.currentTimeMillis();

				while (transactionFlag) {
					transactionId = "";
					if (!(statusLocal.get() == 1)) {
						while (true) {
							Thread.sleep(100);
						}
					}
					if (cqinlocal.size() > 0) {
						String msg = cqinlocal.poll();
						transactionId = msg.split(":")[1].split(" ")[0];
						System.out.println("Message " + msg);
						logHandeler("Concert:" + msg);
						switch (msg.split(":")[0]) {
						case "VOTE-COMMIT":
							commitHashtableConcert.put(transactionId, true);
							break;

						case "VOTE-ABORT":
							commitHashtableConcert.put(transactionId, false);
							break;
						}
					}
					if (hqinlocal.size() > 0) {
						System.out.println("Coordinator Opration hqin commit " + hqinlocal.toString());
						String msg = hqinlocal.poll();
						transactionId = msg.split(":")[1].split(" ")[0];
						logHandeler("Hotel:" + msg);
						switch (msg.split(":")[0]) {
						case "VOTE-COMMIT":
							commitHashtableHotel.put(transactionId, true);
							break;
						case "VOTE-ABORT":
							commitHashtableHotel.put(transactionId, false);
							break;
						}
					}

					if (commitHashtableConcert.get(reservation.split(" ")[0]) != null
							&& commitHashtableHotel.get(reservation.split(" ")[0]) != null) {
						if (commitHashtableConcert.get(reservation.split(" ")[0]) && commitHashtableHotel.get(reservation.split(" ")[0])) {
							qoutlocal.add("GLOBAL-COMMIT:" + reservation);
							logHandeler("GLOBAL-COMMIT:" + reservation);
							lastTransaction = reservation.split(" ")[0];
							System.out.println("lastTransaction =" + lastTransaction);

						} else {
							qoutlocal.add("GLOBAL-ABORT:" + reservation);
							logHandeler("GLOBAL-ABORT:" + reservation);
							lastTransaction = reservation.split(" ")[0];
						}
						commitHashtableConcert.remove(reservation.split(" ")[0]);
						commitHashtableHotel.remove(reservation.split(" ")[0]);
						transactionFlag = false;
					}
					if (System.currentTimeMillis() - startTime > 5000) {
						qoutlocal.add("GLOBAL-ABORT:" + reservation);
						logHandeler("GLOBAL-ABORT:" + reservation);
						lastTransaction = reservation.split(" ")[0];
						transactionFlag = false;
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ex) {
			System.out.println("exp : Callee  ");
			reservations.clear();
			try {
				while (true) {
					System.out.println("sleep ");
					Thread.sleep(100);
				}
			} catch (InterruptedException ex1) {
				System.out.println("awaik ");
				statusLocal.set(1);
				Recovery(lastTransaction);
			}
		}
	}

	public static void loadRequest() throws FileNotFoundException, IOException {
		fstreamCoordinateFile = new FileInputStream(brCoordinatorConfigLocal.readLine());
		brCoordinator = new BufferedReader(new InputStreamReader(fstreamCoordinateFile));
		String request;

		while ((request = brCoordinator.readLine()) != null) {
			reservations.add(request);
		}

	}

	public static void Recovery(String currentTransaction) {
		try {
			fstreamCoordinateFile = new FileInputStream(brCoordinatorConfigLocal.readLine());

			brCoordinator = new BufferedReader(new InputStreamReader(fstreamCoordinateFile));
			String request;
			
			while ((request = brCoordinator.readLine()) != null && !request.split(" ")[0].equals(lastTransaction ) ) {
				break;
			}
			while ((request = brCoordinator.readLine()) != null){
				reservations.add(request);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void logHandeler(String msg) throws IOException {

		File file = new File("CoordinatorLog.txt");
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

		File file = new File("CoordinatorLog.txt");
		if (file.exists()) {
			file.delete();
		}
	}
}

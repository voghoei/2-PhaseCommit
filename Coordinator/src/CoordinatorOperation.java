import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class CoordinatorOperation extends Thread {
	static String transactionId;
	static int[] ticketAvailable;
	ConcurrentLinkedQueue<String> cqinlocal;
	ConcurrentLinkedQueue<String> hqinlocal;
	static Queue<String> qoutlocal;

	// Input coordinate file
	static FileInputStream fstreamCoordinateFile = null;
	static BufferedReader brCoordinator = null;
	static BufferedReader brCoordinatorConfigLocal;
	Hashtable<String, Boolean> commitHashtableConcert;
	Hashtable<String, Boolean> commitHashtableHotel;
	int statusLocal;

	CoordinatorOperation(ConcurrentLinkedQueue<String> cqin,
			ConcurrentLinkedQueue<String> hqin,
			ConcurrentLinkedQueue<String> qout,
			BufferedReader brCoordinatorConfig, int status ) {
		this.statusLocal=status;
		brCoordinatorConfigLocal = brCoordinatorConfig;
		commitHashtableConcert = new Hashtable<String, Boolean>();
		commitHashtableHotel = new Hashtable<String, Boolean>();
		this.cqinlocal = cqin;
		this.hqinlocal = hqin;
		this.qoutlocal = qout;
		start();
	}

	public void run() {
		String reservation;
		boolean transactionFlag;
		try {
			fstreamCoordinateFile = new FileInputStream(
					brCoordinatorConfigLocal.readLine());
			brCoordinator = new BufferedReader(new InputStreamReader(
					fstreamCoordinateFile));
			
			while ((reservation = brCoordinator.readLine()) != null) {
				if (statusLocal != 1) {
					while (true) {
						Thread.sleep(100);
					}
				}
				qoutlocal.add("VOTE-REQUEST:" + reservation);
				System.out.println("VOTE-REQUEST added to local cout");
				
				transactionFlag = true;
				
				while (transactionFlag) {
					if (statusLocal != 1) {
						while (true) {
							Thread.sleep(100);
						}
					}
					if (cqinlocal.size() > 0) {
						String msg = cqinlocal.poll();
						System.out.println("Message " + msg);

						switch (msg.split(":")[0]) {
						case "VOTE-COMMIT":
							commitHashtableConcert.put(
									msg.split(":")[1].split(" ")[0], true);
							break;

						case "VOTE-ABORT":
							commitHashtableConcert.put(
									msg.split(":")[1].split(" ")[0], false);
							break;
						}
					}
					if (hqinlocal.size() > 0) {
						System.out.println("Coordinator Opration hqin commit "
								+ hqinlocal.toString());
						String msg = hqinlocal.poll();

						switch (msg.split(":")[0]) {
						case "VOTE-COMMIT":
							commitHashtableHotel.put(
									msg.split(":")[1].split(" ")[0], true);
							break;
						case "VOTE-ABORT":
							commitHashtableHotel.put(
									msg.split(":")[1].split(" ")[0], false);
							break;
						}
					}

					if (commitHashtableConcert.get(reservation.split(" ")[0]) != null
							&& commitHashtableHotel
									.get(reservation.split(" ")[0]) != null) {
						if (commitHashtableConcert
								.get(reservation.split(" ")[0])
								&& commitHashtableHotel.get(reservation
										.split(" ")[0])) {
							qoutlocal.add("GLOBAL-COMMIT:" + reservation);
						} else {
							qoutlocal.add("GLOBAL-ABORT:" + reservation);
						}
						commitHashtableConcert
								.remove(reservation.split(" ")[0]);
						commitHashtableHotel.remove(reservation.split(" ")[0]);
						transactionFlag = false;
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
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

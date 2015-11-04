import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Queue;


public class CoordinatorOperation {
static String transactionId;
static int[] ticketAvailable;
Queue<String> cqinlocal;
Queue<String> hqinlocal;
static Queue<String> qoutlocal;

// Input coordinate file
static FileInputStream fstreamCoordinateFile = null;
static BufferedReader brCoordinator = null;
static BufferedReader brCoordinatorConfigLocal;


CoordinatorOperation(Queue<String> cqin,Queue<String> hqin, Queue<String> qout,BufferedReader brCoordinatorConfig) {
	brCoordinatorConfigLocal = brCoordinatorConfig;
	cqinlocal = cqin;
	hqinlocal = hqin;
	qoutlocal = qout;	
	booking();
}


public static void booking() {
	String reservation;
	try {
		fstreamCoordinateFile = new FileInputStream(
				brCoordinatorConfigLocal.readLine());
		brCoordinator = new BufferedReader(new InputStreamReader(
				fstreamCoordinateFile));
		
		//while ((reservation = brCoordinator.readLine()) != null) {
		reservation = brCoordinator.readLine();
			qoutlocal.add("VOTE-REQUEST:"+reservation);
			System.out.println("VOTE-REQUEST added to local cout");
		
		//}

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
}

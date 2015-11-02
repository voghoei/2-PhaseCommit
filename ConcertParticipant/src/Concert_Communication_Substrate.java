import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Concert_Communication_Substrate {
	static Socket cSoc;
	static ServerSocket concertSoc;
	static int[] ticketAvailable;
	static Queue<String> qin = new LinkedList<String>();
	static Queue<String> qout = new LinkedList<String>();
	static String transactionId;

	public static void main(String[] args) {
		try {

			concertConfig();
			concertSoc = new ServerSocket(5217);
			System.out.println("Concert Socket on Port Number 5217");
			System.out.println("Waiting for Connection ...");
			cSoc = concertSoc.accept();
			Concert_Buffer_In in = new Concert_Buffer_In(cSoc, qin);
			Concert_Buffer_Out out = new Concert_Buffer_Out(cSoc, qout);
			
			while (true) {
				if (qin.size() > 0) {
					System.out.println("*****  qin value " + qin.toString()
							+ " qin size = " + qin.size());
					if (checkavailabality(qin.poll())) {
						System.out.println("Commit");
						qout.add(transactionId + " Commit");
						//out.qoutlocal.add(transactionId + " Commit");
						//out.dout.writeUTF(transactionId + " Commit");
					} else {
						out.dout.writeUTF(transactionId + " Abort");
						System.out.println("Abort");
						qout.add(transactionId + " Abort");
					}
				}

			}

		} catch (IOException e) {
			System.out.println("exp: Concert Communication_Substrate ");
			e.printStackTrace();
		} finally {
			try {
				if (concertSoc != null) {
					concertSoc.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean checkavailabality(String Command) {
		boolean flag = true;
		System.out.println("Concert Buffer In. Run : Command = " + Command);
		if (Command.contains("[")){
			transactionId = Command.split(" ")[0];
			System.out.println("transactionId = " + transactionId);

			String days = Command.split("\\[")[1].split("\\]")[0];
			System.out.println("days = " + days);

			int numTicket = Integer.parseInt(Command.split(" ")[1]);
			System.out.println("numTicket = " + numTicket);
			
			for (int i = 0; i < days.split(" ").length; i++) {
				System.out.println("Each day  = "+ Integer.parseInt(days.split(" ")[i]));
				
				System.out.println("numavailable = "+ ticketAvailable[Integer.parseInt(days.split(" ")[i])]);
				
				if (numTicket > ticketAvailable[Integer.parseInt(days.split(" ")[i])]) {
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class Hotel_Communication_Substrate extends Thread {
	static Socket hSoc;
	static ServerSocket hotelSoc;
	static int[] roomAvailable;
	static Queue<String> qin = new LinkedList<String>();
	static Queue<String> qout = new LinkedList<String>();
	static String transactionId;

	
	public static void main(String[] args) {

		try {
			hotelConfig();
			hotelSoc = new ServerSocket(5218);
			System.out.println("Hotel Socket on Port Number 5218");
			System.out.println("Waiting for Connection ...");
			hSoc = hotelSoc.accept();
			Hotel_Buffer_In in = new Hotel_Buffer_In(hSoc, qin);
			Hotel_Buffer_Out out = new Hotel_Buffer_Out(hSoc, qout);


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
			System.out.println("exp: Hotel Communication_Substrate ");
			e.printStackTrace();
		} finally {
			try {
				if (hotelSoc != null) {
					hotelSoc.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
				
	
	public static boolean checkavailabality(String Command) {
		boolean flag = true;
		System.out.println("Hotel Buffer In. Run : Command = " + Command);
		if (Command.contains("[")){
			transactionId = Command.split(" ")[0];
			System.out.println("transactionId = " + transactionId);

			String days = Command.split("\\[")[1].split("\\]")[0];
			System.out.println("days = " + days);

			int numRoom = Integer.parseInt(Command.split(" ")[1]);
			System.out.println("numRoom = " + numRoom);
			
			for (int i = 0; i < days.split(" ").length; i++) {
				System.out.println("Each day  = "+ Integer.parseInt(days.split(" ")[i]));
				
				System.out.println("numavailable = "+ roomAvailable[Integer.parseInt(days.split(" ")[i])]);
				
				if (numRoom > roomAvailable[Integer.parseInt(days.split(" ")[i])]) {
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




	
	

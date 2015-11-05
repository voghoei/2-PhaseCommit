import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Hotel_Buffer_In extends Thread {
	Socket hsoc;
	DataInputStream din;
	String inputCommand;
	ConcurrentLinkedQueue<String> qinlocal;

	Hotel_Buffer_In(Socket hsoc, ConcurrentLinkedQueue<String> qin) {
		try {
			this.hsoc = hsoc;
			this.qinlocal = qin;
			din = new DataInputStream(hsoc.getInputStream());
			System.out.println("Hotel Buffer In Connected ...");
			start();

		} catch (IOException e) {
			System.out.println("Hotel Buffer In Connection faild ...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {

			System.out.println("Hotel Buffer In thread:  "
					+ Thread.currentThread().getId());
			while (true) {
				inputCommand = din.readUTF();
//				System.out.println("&&&&&&&&  read: " + inputCommand);
				System.out.println("Hotel Buffer In while loop : " + inputCommand);
				qinlocal.add(inputCommand);
				//Thread.sleep(1000);
			}

		} catch (Exception ex) {
			System.out.println("\tConnection Reset ");
			System.out.println("Waiting for Connection ...");
		}
	}

	protected void finalize() throws IOException {
		din.close();
		hsoc.close();
	}

}

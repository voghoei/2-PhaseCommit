import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;

public class Concert_Buffer_In extends Thread {
	Socket csoc;
	DataInputStream din;
	String inputCommand;
	Queue<String> qinlocal;

	Concert_Buffer_In(Socket csoc, Queue<String> qin) {
		try {
			this.csoc = csoc;
			this.qinlocal = qin;
			din = new DataInputStream(csoc.getInputStream());
			System.out.println("Concert Buffer In Connected ...");
			start();

		} catch (IOException e) {
			System.out.println("Concert Buffer In Connection faild ...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {

			System.out.println("Concert Buffer In thread:  "
					+ Thread.currentThread().getId());
			while (true) {
				inputCommand = din.readUTF();
				System.out.println("Concert Buffer In while loop : " + inputCommand);
				qinlocal.add(inputCommand);
				Thread.sleep(1000);
			}

		} catch (Exception ex) {
			System.out.println("\tConnection Reset ");
			System.out.println("Waiting for Connection ...");
		}
	}

	protected void finalize() throws IOException {
		din.close();
		csoc.close();
	}

}

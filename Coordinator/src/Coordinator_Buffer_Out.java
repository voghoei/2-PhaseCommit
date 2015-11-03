import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.logging.Level;

public class Coordinator_Buffer_Out extends Thread {
	Socket hsoc;
	Socket csoc;
	DataOutputStream hdout;
	DataOutputStream cdout;
	Queue<String> qoutlocal;

	public Coordinator_Buffer_Out(Socket hsoc, Socket csoc,Queue<String> qout) {
		try {
			qoutlocal= qout;
			hsoc = hsoc;
			csoc = csoc;
			hdout = new DataOutputStream(hsoc.getOutputStream());
			cdout = new DataOutputStream(csoc.getOutputStream());
			System.out.println("Coordinator Buffer Out Connected ...");
			start();
		} catch (Exception ex) {
			System.out.println("exp: Buffer out Constructor  ...");
		}
	}

	public void run() {
		try {
			System.out.println("Coordinator Buffer Out thread:  "
					+ Thread.currentThread().getId());
			while(true){
				if (qoutlocal.size()>0){
					String Command = qoutlocal.poll();
					cdout.writeUTF(Command);
					hdout.writeUTF(Command);
					System.out.println("is not empty .....");
					Thread.sleep(1000);
				}
//				hdout.writeUTF("Hi Hotel");
//				cdout.writeUTF("Hi Concert");
			}			

		} catch (Exception ex) {
			System.out.println("exp: Coordinator Buffer out run  ...");
		}

	}

	protected void finalize() throws IOException {
		hdout.close();
		cdout.close();
		hsoc.close();
		csoc.close();

	}

}

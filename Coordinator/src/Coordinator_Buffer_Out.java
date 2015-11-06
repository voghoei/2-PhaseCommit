import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Coordinator_Buffer_Out extends Thread {
	Socket hsoc;
	Socket csoc;
	DataOutputStream hdout;
	DataOutputStream cdout;
	ConcurrentLinkedQueue<String> qoutlocal;
	int statusLocal;

	public Coordinator_Buffer_Out(Socket hsoc, Socket csoc, ConcurrentLinkedQueue<String> qout, int status) {
		try {
			this.statusLocal = status;
			this.qoutlocal = qout;
			this.hsoc = hsoc;
			this.csoc = csoc;
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
			String msg;
			System.out.println("Coordinator Buffer Out thread:  " + Thread.currentThread().getId());
			while (true) {
				if (qoutlocal.size() > 0) {
					if (statusLocal == 1) {
						msg = qoutlocal.poll();
						cdout.writeUTF(msg);
						hdout.writeUTF(msg);
						System.out.println("Coordinator Buffer Out, message: " + msg);
					} else {
						qoutlocal.clear();
						System.out.println("Coordinator Buffer Out, qout clean ");
					}
				}
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

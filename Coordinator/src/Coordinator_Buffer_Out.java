import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Coordinator_Buffer_Out extends Thread {
	Socket hsoc;
	Socket csoc;
	DataOutputStream hdout;
	DataOutputStream cdout;
	ConcurrentLinkedQueue<String> qoutlocal;
	AtomicInteger statusLocal;

	public Coordinator_Buffer_Out(Socket hsoc, Socket csoc, ConcurrentLinkedQueue<String> qout, AtomicInteger status) {
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
					if (statusLocal.get() == 1) {
						msg = qoutlocal.poll();
						cdout.writeUTF(msg);
						hdout.writeUTF(msg);
						System.out.println("Buffer Out, message: " + msg);
					} else {
						qoutlocal.clear();
						System.out.println("Buffer Out, qout clean ");
					}
				}
				sleep(1000);
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

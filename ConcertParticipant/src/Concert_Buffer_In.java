import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;


public class Concert_Buffer_In extends Thread {
	Socket csoc;
	DataInputStream din;

	Concert_Buffer_In(Socket csoc) {
		try {
			csoc = csoc;
			din = new DataInputStream(csoc.getInputStream());
			System.out.println("Concert Buffer In Connected ...");
			start();

		} catch (IOException e) {
			System.out.println("Concert Buffer In Connection faild ...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void finalize() throws IOException {
		din.close();
		csoc.close();

	}

	public void run() {
		try {

			System.out.println("Concert Buffer In thread:  "+ Thread.currentThread().getId());
			String Command = din.readUTF();
            System.out.println(Command);

		} catch (Exception ex) {
			// Logger.getLogger(Transferfile.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
	}

}

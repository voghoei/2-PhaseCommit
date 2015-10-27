import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;


public class Hotel_Buffer_In extends Thread {
	Socket hsoc;
	DataInputStream din;

	Hotel_Buffer_In(Socket hsoc) {
		try {
			hsoc = hsoc;
			din = new DataInputStream(hsoc.getInputStream());
			System.out.println("Hotel Buffer In Connected ...");
			start();

		} catch (IOException e) {
			System.out.println("Hotel Buffer In Connection faild ...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void finalize() throws IOException {
		din.close();
		hsoc.close();

	}

	public void run() {
		try {

			System.out.println("Hotel Buffer In thread:  "+ Thread.currentThread().getId());
			String Command = din.readUTF();
            System.out.println(Command);

		} catch (Exception ex) {
			// Logger.getLogger(Transferfile.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
	}

}

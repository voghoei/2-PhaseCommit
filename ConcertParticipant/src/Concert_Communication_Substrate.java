import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Concert_Communication_Substrate {
	static Socket cSoc;
	static ServerSocket concertSoc;

	public static void main(String[] args) {
		try {
			concertSoc = new ServerSocket(5217);
			System.out.println("Concert Socket on Port Number 5217");
			System.out.println("Waiting for Connection ...");
			while (true) {
				cSoc = concertSoc.accept();
				Concert_Buffer_In in = new Concert_Buffer_In(cSoc);
				Concert_Buffer_Out out = new Concert_Buffer_Out(cSoc);
				
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
}

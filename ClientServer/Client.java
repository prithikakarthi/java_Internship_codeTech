import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
        ) {
            System.out.println(in.readLine());  // Welcome message from server

            // Thread to read messages from server
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    System.out.println("🔌 Disconnected from server");
                }
            }).start();

            // Main thread to send messages
            while (true) {
                String message = scanner.nextLine();
                out.println(message);
            }

        } catch (IOException e) {
            System.out.println("❌ Could not connect to server: " + e.getMessage());
        }
    }
}
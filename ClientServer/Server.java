import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 1234;
    private static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("🔌 Server started... Waiting for clients...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("✅ Client connected: " + clientSocket);

                ClientHandler handler = new ClientHandler(clientSocket);
                clientHandlers.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.out.println("❌ Server Error: " + e.getMessage());
        }
    }

    static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clientHandlers) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    static void removeClient(ClientHandler handler) {
        clientHandlers.remove(handler);
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("🟢 You are connected to the server!");
            String message;

            while ((message = in.readLine()) != null) {
                System.out.println("📩 " + message);
                Server.broadcast(message, this);
            }
        } catch (IOException e) {
            System.out.println("❗ Connection closed");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {}
            Server.removeClient(this);
        }
    }

    void sendMessage(String message) {
        out.println(message);
    }
}

package Module4.Part3HW;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Server {
    int port = 3001;
    // connected clients
    private List<ServerThread> clients = new ArrayList<ServerThread>();
    private Random random = new Random();

    private void start(int port) {
        this.port = port;
        // server listening
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            Socket incoming_client = null;
            System.out.println("Server is listening on port " + port);
            do {
                System.out.println("waiting for next client");
                if (incoming_client != null) {
                    System.out.println("Client connected");
                    ServerThread sClient = new ServerThread(incoming_client, this);
                    
                    clients.add(sClient);
                    sClient.start();
                    incoming_client = null;
                    
                }
            } while ((incoming_client = serverSocket.accept()) != null);
        } catch (IOException e) {
            System.err.println("Error accepting connection");
            e.printStackTrace();
        } finally {
            System.out.println("closing server socket");
        }
    }
    protected synchronized void disconnect(ServerThread client) {
		long id = client.getId();
        client.disconnect();
		broadcast("Disconnected", id);
	}
    
    protected synchronized void broadcast(String message, long id) {
        if(processCommand(message, id)){

            return;
        }
        // let's temporarily use the thread id as the client identifier to
        // show in all client's chat. This isn't good practice since it's subject to
        // change as clients connect/disconnect
        message = String.format("User[%d]: %s", id, message);
        // end temp identifier
        
        // loop over clients and send out the message
        Iterator<ServerThread> it = clients.iterator();
        while (it.hasNext()) {
            ServerThread client = it.next();
            boolean wasSuccessful = client.send(message);
            if (!wasSuccessful) {
                System.out.println(String.format("Removing disconnected client[%s] from list", client.getId()));
                it.remove();
                broadcast("Disconnected", id);
            }
        }
    }

    private boolean processCommand(String message, long clientId) {
        System.out.println("Checking command: " + message);
        if (message.toLowerCase().startsWith("/shufflewords ")) {
            String[] parts = message.split(" ", 2);
            if (parts.length == 2) {
                String originalText = parts[1];
                String shuffledNewMessage = shuffleMessage(originalText);
                broadcast(String.format("User[%d]: %s", clientId, shuffledNewMessage), clientId);
            }
            return true;
        } else if (message.equalsIgnoreCase("/cointoss")) {  //msa224 2/22/24
            String result = executeCoinToss();
            broadcast(String.format("User[%d] has asked for a coin toss and got %s", clientId, result), clientId);
            return true;
        } else if (message.equalsIgnoreCase("disconnect")) {
            Iterator<ServerThread> it = clients.iterator();
            while (it.hasNext()) {
                ServerThread client = it.next();
                if (client.getId() == clientId) {
                    it.remove();
                    disconnect(client);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    private String shuffleMessage(String message) {
        char[] characters = message.toCharArray();
        for (int x = 0; x < characters.length; x++) {
            int y = random.nextInt(characters.length);
            char temp = characters[x];
            characters[x] = characters[y];
            characters[y] = temp;
        }
        return new String(characters);
    }

    private String executeCoinToss() { //msa224 2/22/24
        int randomNum = (int) (Math.random() * 2);
        return (randomNum == 0) ? "It's Heads!" : "It's Tails!";
    }
    
   
    public static void main(String[] args) {
        System.out.println("Starting Server");
        Server server = new Server();
        int port = 3000;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            // can ignore, will either be index out of bounds or type mismatch
            // will default to the defined value prior to the try/catch
        }
        server.start(port);
        System.out.println("Server Stopped");
    }
}
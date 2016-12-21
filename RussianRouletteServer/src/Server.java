import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Admin on 20.12.2016.
 */

/**
 * To begin the game you should wait 6 players. After that the first connected player to server starts the game.
 * Everybody in the room should decide for theirselves: to shot or to leave. There are 5 bullets in pistol.
 * The winner is player who will be alive last.
 */
public class Server {
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static final int PORT = 8081;
    private static BufferedReader in;


    public static void main(String[] args) throws IOException {

        int countPlayers = 0;
        try {
            serverSocket = new ServerSocket(Server.PORT, 0, InetAddress.getByName("localhost"));
        } catch (IOException e) {
            System.out.println("Couldn't listen to port 8081");
        }
        try {
            while (true) {
                ArrayList<ClientThread> players = new ArrayList<ClientThread>(); // list of connected players
                Room room = new Room();

                while (countPlayers != 6) {
                    socket = serverSocket.accept();
                    System.out.println("Connected");

                    ClientThread client = new ClientThread(socket, room);
                    client.setName(String.valueOf(countPlayers));
                    countPlayers++;
                    players.add(client);
                }
                room.setPlayers(players);
                for (int i = 0; i <countPlayers ; i++) {
                    players.get(i).setPlayers(players);
                    players.get(i).start();
                }
                countPlayers = 0;
            }

        } catch (IOException e) {
            System.out.println("Can't accept");
        }

    }

    public static void closeServer() throws IOException {
        in.close();
        socket.close();
        serverSocket.close();
    }
}

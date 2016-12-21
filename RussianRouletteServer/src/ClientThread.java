import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Created by Admin on 20.12.2016.
 */
public class ClientThread extends Thread {
    private BufferedReader in;
    private char[][] area = new char[10][10];

    private PrintWriter out;
    private Socket socket;
    private String name;
    private Room room;
    private ArrayList<ClientThread> players;

    public ClientThread(Socket socket, Room room) {
        this.socket = socket;
        this.room = room;
    }

    @Override
    public synchronized void run() {
        while (true) {
            name = getName();
            System.out.println(name);
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("##start##");
                out.println(getName());
                while (room.getCountPlayer() > 1) {
                    synchronized (room) {
                        while (!room.checkCourse(name)) {
                            room.wait(1000);
                        }
                        System.out.println("##shot##");
//                        try {
                        out.println("##shot##");
                        try {
                            String answer = in.readLine();

                            System.out.println(answer);
                            if (answer.equals("##shot##")) {
                                if (room.shot(ClientThread.this)) {
                                    out.println("##died##");
                                    ArrayList<ClientThread> players = room.getPlayers();
                                    for (int i = 0; i < players.size(); i++) {
                                        if (!players.get(i).equals(ClientThread.this)) {
                                            players.get(i).out.println("##opponent##");
                                            System.out.println(getName());
                                            players.get(i).out.println(getName());
                                            players.get(i).out.println("##died##");
                                        }
                                    }
                                    closeThread();
                                } else {
                                    out.println("##miss##");
                                    ArrayList<ClientThread> players = room.getPlayers();
                                    for (int i = 0; i < players.size(); i++) {
                                        if (!players.get(i).equals(ClientThread.this)) {
                                            players.get(i).out.println("##opponent##");
                                            System.out.println(getName());
                                            players.get(i).out.println(getName());
                                            players.get(i).out.println("##miss##");
                                        }
                                    }
                                }
                            } else if (answer.equals("##exit##")) {
                                room.removePlayer(ClientThread.this);
                                ArrayList<ClientThread> players = room.getPlayers();
                                for (int i = 0; i < players.size(); i++) {
                                    if (!players.get(i).equals(ClientThread.this)) {
                                        players.get(i).out.println("##opponent##");
                                        players.get(i).out.println(getName());
                                        players.get(i).out.println("##exit##");
                                    }
                                }
                                closeThread();
                            }

//                            closeThread();
                        }
                        catch (SocketException e) {
                            closeThread();
                            System.out.println("Клиент отключен3");
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
                ArrayList<ClientThread> players = room.getPlayers();
                players.get(0).out.println("##end##");
                players.get(0).out.println("##win##");


                closeThread();
//                Server.closeServer();

            } catch (SocketException e) {
                e.printStackTrace();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeThread() throws IOException {
        System.out.println("Противник отключился, вы победили!");
        in.close();
        out.close();
        socket.close();
    }

    public void setPlayers(ArrayList<ClientThread> players) {
        this.players = players;
    }
}

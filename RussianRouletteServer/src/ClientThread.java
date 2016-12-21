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
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                /*
                Notify about starting the game to client
                */
                out.println("##start##");
                //Giving name to client
                out.println(getName());
                while (room.getCountPlayer() > 1) {
                    synchronized (room) {
                        while (!room.checkCourse(name)) {
                            room.wait(1000);
                        }
                        //Notify player about making choice
                        out.println("##shot##");
                        try {
                            String answer = in.readLine();
                            //Player pulled trigger
                            if (answer.equals("##shot##")) {
                                if (room.shot(ClientThread.this)) {
                                    out.println("##died##");//Player, we are sorry, you died:(
                                    ArrayList<ClientThread> players = room.getPlayers();
                                    //Notify others about bad new happened with player higher
                                    for (int i = 0; i < players.size(); i++) {
                                        if (!players.get(i).equals(ClientThread.this)) {
                                            players.get(i).out.println("##opponent##");
                                            players.get(i).out.println(getName());
                                            players.get(i).out.println("##died##");
                                        }
                                    }
                                    closeThread();
                                } else {
                                    out.println("##miss##");//Man, you are alive! Our congratulations:)
                                    ArrayList<ClientThread> players = room.getPlayers();
                                    //Notify others that they still have the opponent
                                    for (int i = 0; i < players.size(); i++) {
                                        if (!players.get(i).equals(ClientThread.this)) {
                                            players.get(i).out.println("##opponent##");
                                            players.get(i).out.println(getName());
                                            players.get(i).out.println("##miss##");
                                        }
                                    }
                                }
                                //Player decided to exit this game
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
                        }
                        catch (SocketException e) {
                            closeThread();
                            System.out.println("Клиент отключен");
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
                //You are winner, guy!
                ArrayList<ClientThread> players = room.getPlayers();
                players.get(0).out.println("##end##");
                players.get(0).out.println("##win##");


                closeThread();

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
        in.close();
        out.close();
        socket.close();
    }

    public void setPlayers(ArrayList<ClientThread> players) {
        this.players = players;
    }
}

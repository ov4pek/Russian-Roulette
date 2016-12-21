import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Admin on 20.12.2016.
 */
public class Room {
    private ArrayList<ClientThread> players;
    private final int capacityDrum = 6;
    private int countBullet = 5;
    private Random random = new Random();
//    private int countPlayer;
    private int course = 0;

    public boolean shot(ClientThread player) {
        for (int i = 0; i < countBullet; i++) {
            if (twistDrum() == i) {
//                minusPlayer();
                setCountBullet();
                players.remove(player);
                setCourse();
                return true;
            }
        }
        setCourse();
        return false;
    }

    private int twistDrum() {
        return random.nextInt(capacityDrum);
    }

//    public void setCountPlayer(int countPlayer) {
//        this.countPlayer = countPlayer;
//    }

//    public void minusPlayer() {
//        countPlayer--;
//    }

    public int getCountPlayer() {
//        return countPlayer;
        return players.size();
    }

    public int getCourse() {
        return course;
    }

    public synchronized void setCourse() {
        this.course += 1;
//        if (course == countPlayer) course = 0;
        if (course == players.size()) course = 0;
    }

    public void setCountBullet() {
        this.countBullet -= 1;
    }

    public boolean checkCourse(String name) {
        if (name.equals(players.get(course).getName())) return true;
        else return false;
    }

    public ArrayList<ClientThread> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<ClientThread> players) {
        this.players = players;
    }
    public void removePlayer(ClientThread player){
        players.remove(player);
    }

}

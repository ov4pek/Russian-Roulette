package Threads;

import controllers.ControllerGame;
import javafx.application.Platform;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by Admin on 21.12.2016.
 */

/**
 * Thread for game running on client
 */
public class SendThread extends Thread {
    private ControllerGame game;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String name;


    public SendThread(ControllerGame game) {
        this.game = game;
        try {
            socket = new Socket("localhost", 8081);
        } catch (IOException e) {
            System.out.println("Не удалось подключиться к серверу");
        }
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("Windows-1251")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message = "";
        sendMessageToLabel("Ожидание игроков");

        try {
            while (!(message = in.readLine()).equals("##start##")) {
                sleep(1000);
            }
            String name = in.readLine();
            sendMessageToNamePlayer(Integer.valueOf(name), "Ты");
            sendMessageToLabel("Игра начата!");
            while (!(message = in.readLine()).equals("##end##")) {
                //Let`s decide what do you want to do
                if (message.equals("##shot##")) {
                    sendMessageToLabel("Твой ход");
                    while (game.getClick() == 0) {
                        sleep(1000);
                    }
                    if (game.getClick() == 1) {
                        out.println("##shot##");

                        String status = in.readLine();
                        System.out.println(status);

                        //Die or live?
                        if (status.equals("##died##")) {
                            sendMessageToLabel("Тебе не повезло, ты умер!!");
                        } else if (status.equals("##miss##")) {
                            sendMessageToLabel("А ты везунчик!");
                        }

                        game.setClick();
                    } else if (game.getClick() == -1) {
                        out.println("##exit##");
                    }
                    //Messages about lives of opponents
                } else if (message.equals("##opponent##")) {
                    String number = in.readLine();
                    String status = in.readLine();
                    if (status.equals("##died##")) {
                        sendMessageToNamePlayer(Integer.valueOf(number), "Умер");
                        sendMessageToLabel("Игрок " + (Integer.valueOf(number) + 1) + " погиб");
                    } else if (status.equals("##miss##")) {
                        sendMessageToLabel("Игрок " + (Integer.valueOf(number) + 1) + " остался жив");
                    } else if (status.equals("##exit##")) {
                        sendMessageToLabel("Игрок " + (Integer.valueOf(number) + 1) + " испугался и вышел");
                    }
                    game.setClick();

                }
            }
            message = in.readLine();
            if (message.equals("##win##")) ;
            sendMessageToLabel("Ты победил!!!");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(message);


        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Change messages in javaFX
    public void sendMessageToLabel(final String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                game.setMessageLabel(message);
            }
        });
    }

    //Change names of the players
    public void sendMessageToNamePlayer(final int number, final String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                game.setLabelPlayer(Integer.valueOf(number), message);
            }
        });
    }
}

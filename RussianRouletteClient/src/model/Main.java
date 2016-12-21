package model;

import Threads.SendThread;
import controllers.ControllerGame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
     ControllerGame children;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GameView.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Russian Roulete");
        primaryStage.setScene(new Scene(root, 424, 451));
        primaryStage.show();
        //Getting access to the ControllerGame
        children =loader.getController();
        SendThread sendThread = new SendThread(children);
        sendThread.start();
    }


    public static void main(String[] args) {
        launch(args);

    }
}

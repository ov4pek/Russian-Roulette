package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * Created by Admin on 21.12.2016.
 */
public class ControllerGame {
    @FXML
    private Label messageLabel;
    @FXML
    private Label labelPlayer1;
    @FXML
    private Label labelPlayer2;
    @FXML
    private Label labelPlayer3;
    @FXML
    private Label labelPlayer4;
    @FXML
    private Label labelPlayer5;
    @FXML
    private Label labelPlayer6;
    @FXML
    private Button btnShot;
    @FXML
    private Button btnExit;
    private Label [] players ={labelPlayer1,labelPlayer2,labelPlayer3,labelPlayer4,labelPlayer5,labelPlayer6};
    private int click =0;

    @FXML
    public void initialize() {

        btnShot.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                click=1;
            }
        });
        btnExit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                click=-1;
            }
        });

    }


    public void setMessageLabel(String message) {
        this.messageLabel.setText(message);
    }

    public void setLabelPlayer(int i, String namePlayer) {
        switch (i){
            case 0: this.labelPlayer1.setText(namePlayer);
            break;
            case 1: this.labelPlayer2.setText(namePlayer);
                break;
            case 2: this.labelPlayer3.setText(namePlayer);
                break;
            case 3: this.labelPlayer4.setText(namePlayer);
                break;
            case 4: this.labelPlayer5.setText(namePlayer);
                break;
            case 5: this.labelPlayer6.setText(namePlayer);
                break;
        }

    }

    public int getClick() {
        return click;
    }

    public void setClick() {
        this.click = 0;
    }
}

package bd.edu.seu.chat.seuquest;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    public void OnClickLoginBtn(javafx.event.ActionEvent actionEvent) throws IOException {
        HelloApplication.changeScene("login-view","Login To SeuQuest", 800, 744);
    }

    @FXML
    public void OnClickChatBtn(javafx.event.ActionEvent actionEvent) throws IOException {
        HelloApplication.changeScene("chat-view","SeuQuest- Training Dashboard", 1300, 744);
    }

    @FXML
    public void onClickGithubBtn(javafx.event.ActionEvent actionEvent) throws IOException, URISyntaxException {
        HelloApplication.visitUrl("https://github.com/tanjim750");
    }

    @FXML
    public void onClickWebsiteBtn(javafx.event.ActionEvent actionEvent) throws IOException, URISyntaxException {
        HelloApplication.visitUrl("https://tanjim-abubokor.github.io/");
    }

    @FXML
    public void onClickLinkedinBtn(javafx.event.ActionEvent actionEvent) throws IOException, URISyntaxException {
        HelloApplication.visitUrl("https://linkedin.com/in/tanjim-abubokor/");
    }
}
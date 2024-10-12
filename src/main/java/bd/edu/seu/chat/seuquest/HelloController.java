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

    public void visitUrl(String url) throws IOException, URISyntaxException {
        URI targetUrl = new URI(url);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(targetUrl);
        }else {
            Runtime.getRuntime().exec(new String[]{"xdg-open", url});
        }
    }

    @FXML
    public void onClickGithubBtn(javafx.event.ActionEvent actionEvent) throws IOException, URISyntaxException {
        visitUrl("https://github.com/tanjim750");
    }

    @FXML
    public void onClickWebsiteBtn(javafx.event.ActionEvent actionEvent) throws IOException, URISyntaxException {
        visitUrl("https://tanjim-abubokor.github.io/");
    }

    @FXML
    public void onClickLinkedinBtn(javafx.event.ActionEvent actionEvent) throws IOException, URISyntaxException {
        visitUrl("https://linkedin.com/in/tanjim-abubokor/");
    }
}
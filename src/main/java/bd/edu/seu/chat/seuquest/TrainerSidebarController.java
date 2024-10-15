package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class TrainerSidebarController implements Initializable {

    @FXML
    private Button chatBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label name;

    @FXML
    private ImageView profle;

    @FXML
    private Button trainingBtn;

    @FXML
    private Text userRole;

    private UserDetails userDetails = HelloApplication.getDetails();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profle.setImage(userDetails.getProfileImage());
        name.setText(userDetails.getFullName());
        userRole.setText(userDetails.getRole().name());
    }


    @FXML
    void onClickChatBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("chat-view","SeuQuest- welcome", 1300, 744);
    }

    @FXML
    void onClickLogoutBtn(ActionEvent event) throws IOException {
        HelloApplication.logout();
        HelloApplication.changeScene("hello-view","SeuQuest- welcome", 640, 744);
    }

    @FXML
    void onClickTrainBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("trainer-view","SeuQuest- welcome", 1300, 744);
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

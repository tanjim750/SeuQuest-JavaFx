package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StudentSidebarController implements Initializable {
    @FXML
    private HBox buttons;

    @FXML
    private Button chatBtn;

    @FXML
    private Button helloBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label name;

    @FXML
    private ImageView profle;

    @FXML
    private Text userRole;

    private UserDetails userDetails = HelloApplication.getDetails();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setText(userDetails.getFullName());
        String studentId = userDetails.getUsername().split("@")[0];
        userRole.setText(studentId);
        Image img = new Image(userDetails.getProfilePath());
        profle.setImage(img);
    }

    @FXML
    void onClickChatBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("chat-view","SeuQuest - Welcome", 1300, 744);
    }

    @FXML
    void onClickHomeBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("student-view","SeuQuest - Welcome", 1300, 744);
    }


    @FXML
    void onClickLogoutBtn(ActionEvent event) throws IOException, SQLException {
        HelloApplication.logout();
        HelloApplication.changeScene("hello-view","SeuQuest - Welcome", 640, 744);
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

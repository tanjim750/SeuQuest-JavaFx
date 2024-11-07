package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.user.Role;
import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GeneralSidebarController implements Initializable {
    @FXML
    private HBox buttons;

    @FXML
    private Button helloBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button changePassBtn;

    @FXML
    private Label name;

    @FXML
    private ImageView profle;

    @FXML
    private Button registrationBtn;

    @FXML
    private Text userRole;

    @FXML
    void onClickHomeBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("hello-view","SeuQuest - Welcome", 640, 744);
    }

    @FXML
    void onClickLoginBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("login-view","Login To SeuQuest", 800, 744);
    }

    @FXML
    void onClickLogoutBtn(ActionEvent event) throws IOException, SQLException {
        HelloApplication.logout();
        HelloApplication.changeScene("hello-view","SeuQuest - Welcome", 640, 744);
    }

    @FXML
    void onClickRegistrationBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("registration-view","SeuQuest- Registration page", 800, 744);
    }

    private UserDetails userDetails = HelloApplication.getDetails();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(userDetails.getRole() == Role.GENERAL){
            buttons.getChildren().remove(loginBtn);
            buttons.getChildren().remove(helloBtn);
            buttons.getChildren().remove(registrationBtn);

            profle.setImage(userDetails.getProfileImage());
            name.setText(userDetails.getFullName());
            userRole.setText(userDetails.getRole().name());
        }else {
            buttons.getChildren().remove(logoutBtn);
            buttons.getChildren().remove(changePassBtn);
        }
    }

    @FXML
    public void onClickGithubBtn(javafx.event.ActionEvent actionEvent) throws IOException, URISyntaxException {
        HelloApplication.visitUrl("https://github.com/tanjim750");
    }

    @FXML
    private void onClickChangePassBtn(javafx.event.ActionEvent event) throws IOException {
        HelloApplication.changeScene("changePass-view","Change Password", 1170, 744);
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

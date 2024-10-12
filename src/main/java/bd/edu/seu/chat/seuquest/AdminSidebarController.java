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
import java.net.URL;
import java.util.ResourceBundle;

public class AdminSidebarController implements Initializable {
    @FXML
    private Button addKeyBtn;

    @FXML
    private Button addUserBtn;

    @FXML
    private Button chatBtn;

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
        profle.setImage(userDetails.getProfileImage());
        name.setText(userDetails.getFullName());
        userRole.setText(userDetails.getRole().name());
    }

    @FXML
    void onClickGithubBtn(ActionEvent event) {

    }

    @FXML
    void onClickLinkedinBtn(ActionEvent event) {

    }

    @FXML
    void onClickWebsiteBtn(ActionEvent event) {

    }

    @FXML
    public void onClickLogoutBtn(ActionEvent event) throws IOException {
        HelloApplication.logout();
        HelloApplication.changeScene("hello-view","SeuQuest- welcome", 640, 744);
    }

    @FXML
    public void onClickCreateUserBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("adduser-view","SeuQuest- Create user", 1170, 744);
    }

    @FXML
    public void onClickAddKeyBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("addkey-view","SeuQuest- Add secret key", 1170, 744);
    }

    @FXML
    public void onClickHomeBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("admin-view","SeuQuest- Add secret key", 1300, 744);
    }

}

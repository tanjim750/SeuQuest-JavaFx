package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TrainerController implements Initializable {
    @FXML
    private ChoiceBox<?> deptChoice;

    @FXML
    private VBox inbox;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label name;

    @FXML
    private ImageView profle;

    @FXML
    private TextField queryField;

    @FXML
    private HBox sidebarHbox;

    @FXML
    private ScrollPane tableScroll;

    @FXML
    private Button trainingBtn;

    @FXML
    private Text userRole;

    private UserDetails userDetails = HelloApplication.getDetails();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        profle.setImage(userDetails.getProfileImage());
//        name.setText(userDetails.getFullName());
//        userRole.setText(userDetails.getRole().name());

        HelloApplication.loadNewLayout(sidebarHbox,"trainerSidebar-view.fxml");

    }

    @FXML
    void onClickChatBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("chat-view","SeuQuest- welcome", 1300, 744);
    }

    @FXML
    void onClickGithubBtn(ActionEvent event) {

    }

    @FXML
    void onClickLinkedinBtn(ActionEvent event) {

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
    void onClickWebsiteBtn(ActionEvent event) {

    }
}

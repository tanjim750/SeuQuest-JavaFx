package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.user.Role;
import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button githubBtn;

    @FXML
    private Hyperlink homeBtn;

    @FXML
    private Button linkedinBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private TextField passwordField;

    @FXML
    private Hyperlink registrationBtn;

    @FXML
    private TextField usernameField;

    @FXML
    private Button websiteBtn;

    @FXML
    private Label warningLable;

    private UserDetails userDetails;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void onClickLoginBtn(javafx.event.ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            warningLable.setText("Username and password are required");
        }else {
            try{
                boolean is_loggedIn = HelloApplication.login(username,password);
                userDetails = HelloApplication.getDetails();

                if(is_loggedIn){


                    if(userDetails.isAuthenticated() && userDetails.hasRole()){
                        if(userDetails.getRole() == Role.ADMIN){
                            HelloApplication.changeScene("admin-view","SeuQuest- Admin Dashboard", 1300, 744);
                        }else if(userDetails.getRole() == Role.TRAINER){
                            HelloApplication.changeScene("trainer-view","SeuQuest- Training Dashboard", 1300, 744);
                        }else{
                            HelloApplication.changeScene("chat-view","SeuQuest- Training Dashboard", 1300, 744);
                        }
                    }
                }else{
                    warningLable.setText("Wrong username or password");
                }
            }catch (Exception e){
                e.printStackTrace();
                warningLable.setText(e.getMessage());
            }
        }
    }

    @FXML
    public void OnClickRegistrationBtn(javafx.event.ActionEvent actionEvent) throws IOException {
        HelloApplication.changeScene("registration-view","SeuQuest- Registration page", 800, 744);
    }

    @FXML
    public void OnClickHomeBtn(javafx.event.ActionEvent actionEvent) throws IOException {
        HelloApplication.changeScene("hello-view","SeuQuest - welcome", 640, 744);
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

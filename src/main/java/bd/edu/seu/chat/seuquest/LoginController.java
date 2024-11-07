package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.modules.GoogleLogin;
import bd.edu.seu.chat.seuquest.user.DatabaseManager;
import bd.edu.seu.chat.seuquest.user.Role;
import bd.edu.seu.chat.seuquest.user.UserDetails;
import com.google.gson.JsonObject;
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
import java.sql.ResultSet;
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

    private GoogleLogin googleLogin = new GoogleLogin();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDetails = HelloApplication.getDetails();
    }

    private void manageGoogleLogin(JsonObject credentials) throws SQLException, IOException {
        String status = credentials.get("status").getAsString();

        if(!status.equals("200")){
            HelloApplication.infoAlart("Unauthenticated","User not found","Make sure you are successfully logged in");
            return;
        }

        String email = credentials.get("email").getAsString();
        String name = credentials.get("name").getAsString();
        String hd = credentials.get("hd").getAsString();
        String picture = credentials.get("picture").getAsString();

        if(hd.equals("seu.edu.bd")){
            HelloApplication.loginStudent(email,name,picture);
            HelloApplication.changeScene("student-view","SeuQuest- Training Dashboard", 1300, 744);
        }else {
            HelloApplication.infoAlart("Unauthenticated","Not allow","Make sure your using the mail that is given by Southeast.");
        }
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
                        if(userDetails.getRole() == Role.ADMIN || userDetails.getRole() == Role.SUPERUSER){
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
    public void OnClickForgotPassBtn(javafx.event.ActionEvent actionEvent) throws IOException {
        HelloApplication.changeScene("forgotPass-view","Request - Forget Password", 640, 744);
    }

    @FXML
    public void OnClickHomeBtn(javafx.event.ActionEvent actionEvent) throws IOException {
        HelloApplication.changeScene("hello-view","SeuQuest - welcome", 640, 744);
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

    @FXML
    public void onClickLoginWithGoogleBtn(javafx.event.ActionEvent actionEvent) throws IOException, URISyntaxException, SQLException {
        HelloApplication.visitUrl("http://127.0.0.1:5000/login");
        boolean confirm = HelloApplication.confirmationAlart("Login","Login Confirmation","Did you logged in with google?");
        if(confirm){
            JsonObject credentials = googleLogin.login();
            System.out.println(credentials);
            manageGoogleLogin(credentials);
        }
    }

}

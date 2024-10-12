package bd.edu.seu.chat.seuquest;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {
    @FXML
    private Button githubBtn;

    @FXML
    private Hyperlink homeBtn;

    @FXML
    private Button linkedinBtn;

    @FXML
    private Hyperlink loginBtn;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField secretKey;

    @FXML
    private Button signUpBtn;

    @FXML
    private TextField usernameField;

    @FXML
    private Label warnigLable;

    @FXML
    private Button websiteBtn;

    @FXML
    private RadioButton userProfile1,userProfile2,userProfile3,userProfile4,userProfile5,userProfile6;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warnigLable.setVisible(false);
    }

    @FXML
    public void onClickRegistrationBtn(javafx.event.ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String key = secretKey.getText();
        String name = nameField.getText();

        warnigLable.setVisible(true);

        if(username.isEmpty() || password.isEmpty() || key.isEmpty() || name.isEmpty()){
            warnigLable.setStyle("-fx-text-fill: red;");
            warnigLable.setText("All fields are required");
        }else {
            try{
                boolean isRegisterd = HelloApplication.register(username,password,key,name,getProfilePath());

                if(isRegisterd){
                    warnigLable.setText("Successfully registered. please login ");
                    warnigLable.setStyle("-fx-text-fill: green;");
                    usernameField.clear();
                    passwordField.clear();
                    secretKey.clear();
                    nameField.clear();
                }else {
                    warnigLable.setText("Invalid secret key");
                    warnigLable.setStyle("-fx-text-fill: red;");
                }


            }catch (SQLException e){
                System.out.println(e.getMessage());
                warnigLable.setText(e.getMessage());
            }

        }
    }

    @FXML
    public void OnClickLoginBtn(javafx.event.ActionEvent actionEvent) throws IOException {
        HelloApplication.changeScene("login-view","Login To SeuQuest", 800, 744);
    }

    @FXML
    public void OnClickHomeBtn(javafx.event.ActionEvent actionEvent) throws IOException {
        HelloApplication.changeScene("hello-view","SeuQuest", 640, 744);
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

    private String getProfilePath(){
        if(userProfile1.isSelected()){
            return "boy.png";
        } else if (userProfile2.isSelected()) {
            return "girl.png";
        } else if (userProfile3.isSelected()) {
            return "boy2.png";
        }else if (userProfile4.isSelected()) {
            return "girl3.png";
        }else if (userProfile5.isSelected()) {
            return "guest-user.png";
        } else if (userProfile6.isSelected()) {
            return "tanjim.png";
        }else {
            return "guest-user.png";
        }
    }
}

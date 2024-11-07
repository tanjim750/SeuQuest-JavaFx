package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.modules.DeviceInfo;
import bd.edu.seu.chat.seuquest.user.DatabaseManager;
import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ForgotPassController implements Initializable {

    @FXML
    private Button forgotPassBtn;

    @FXML
    private Button githubBtn;

    @FXML
    private Button linkedinBtn;

    @FXML
    private TextField usernameField;

    @FXML
    private Button websiteBtn;

    private String deviceMac = DeviceInfo.getMacAddress();
    private DatabaseManager db = HelloApplication.dbManager;
    private UserDetails userDetails;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDetails = HelloApplication.getDetails();
    }

    @FXML
    void OnClickForgotPassBtn(ActionEvent event) throws SQLException, IOException {

        String username = usernameField.getText();
        StringBuilder query = new StringBuilder();
        db.customCommand("UPDATE users set forgot_pass= true where username='" + username + "'");

        boolean confirm = HelloApplication.confirmationAlart(
                "Confirmation",
                "Confirm to continue",
                "Trying recognize your device. If your device is bounded with the account a screen will be appear to set new password"
        );

        if (deviceMac == null) {
            HelloApplication.infoAlart("Alert","Finding Device","Your device is not trusted.");
            return;
        }

        query.append(
                "SELECT * FROM users WHERE username = '"
                +username + "' && mac="
                + deviceMac
        );

        ResultSet result = db.customQuery(query.toString());
        if (result.next() && !result.getString("username").endsWith("seu.edu.bd")) {
            userDetails.forgotPassword(result.getString("username"), result.getInt("id"));
            if (confirm) {
                HelloApplication.changeScene("changePass-view","Forgot password",1300,744);
            }
            db.customCommand("UPDATE users set forgot_pass= false where username='" + username + "'");
            return;
        }else {
            HelloApplication.infoAlart("Alert","Not found","Can't recognize your device Or invalid username");
        }

    }

    @FXML
    public void OnClickHomeBtn(javafx.event.ActionEvent actionEvent) throws IOException {
        HelloApplication.changeScene("hello-view","SeuQuest - welcome", 640, 744);
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


}

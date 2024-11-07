package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.user.DatabaseManager;
import bd.edu.seu.chat.seuquest.user.Role;
import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePassController implements Initializable {

    @FXML
    private Button changePassBtn;

    @FXML
    private PasswordField confirmPass;

    @FXML
    private PasswordField newPass;

    @FXML
    private HBox sidebarHbox;

    @FXML
    private Label warningLable;

    private UserDetails userDetails;
    private DatabaseManager db = HelloApplication.dbManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDetails = HelloApplication.getDetails();

        if (userDetails.getRole() == Role.ADMIN || userDetails.getRole() == Role.SUPERUSER) {
            HelloApplication.loadNewLayout(sidebarHbox,"adminSidebar-view.fxml");
        }else if(userDetails.getRole() == Role.TRAINER){
            HelloApplication.loadNewLayout(sidebarHbox,"trainerSidebar-view.fxml");
        } else if (userDetails.getRole() == Role.STUDENT) {
            HelloApplication.loadNewLayout(sidebarHbox,"studentSidebar-view.fxml");
        } else {
            HelloApplication.loadNewLayout(sidebarHbox,"generalSidebar-view.fxml");
        }
    }
    @FXML
    void onClickChangePass(ActionEvent event) {
        String newPassText = newPass.getText();
        String confirmPassText = confirmPass.getText();
        String response = changePassword(newPassText, confirmPassText);
        newPass.clear();
        confirmPass.clear();
        warningLable.setText(response);
    }

    private String changePassword(String newPass, String confirmPass) {
        StringBuilder command = new StringBuilder();
        String encodePass = userDetails.encyptPassword(newPass);
        command.append(
                "UPDATE users set password='"
                + encodePass + "'"
                +"where id="
                + userDetails.getId()
        );

        if(newPass.isEmpty() || confirmPass.isEmpty()){
            return "All fields are required";
        }

        if(newPass.equals(confirmPass)){
            try{
                db.customCommand(command.toString());
                return "Password successfully changed";

            } catch (Exception e){
                e.printStackTrace();
                return "Something went wrong";
            }
        }else {
            return "Password didn't match";
        }
    }

}

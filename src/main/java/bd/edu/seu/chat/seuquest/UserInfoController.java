package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.user.DatabaseManager;
import bd.edu.seu.chat.seuquest.user.Role;
import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import javafx.event.ActionEvent;

import java.net.URL;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserInfoController implements Initializable {
    @FXML
    private Label hasRole;

    @FXML
    private Button generatePassBtn;

    @FXML
    private HBox action;

    @FXML
    private HBox tableRow;

    @FXML
    private Label userFullName;

    @FXML
    private Label userId;

    @FXML
    private ChoiceBox<String> userRole;

    @FXML
    private Label username;

    private ChangeListener<String> listener;

    private DatabaseManager db = HelloApplication.dbManager;
    private UserDetails userDetails;

    private ObservableList<String> roles = FXCollections.observableArrayList(
            Role.ADMIN.name(),
            Role.TRAINER.name(),
            Role.GENERAL.name()
    );


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userRole.setItems(roles);
        generatePassBtn.setDisable(true);

    }

    public void setData(UserDetails user) {
        userDetails = user;

        if(user.hasRole()) this.hasRole.setText("Yes");
        else this.hasRole.setText("No");

        this.userFullName.setText(user.getFullName());
        this.userId.setText(String.valueOf(user.getId()));
        this.userRole.setValue(user.getRole().name());
        this.username.setText(user.getUsername().replace("@seu.edu.bd",""));

        if(user.isUserForgotPass()){
            generatePassBtn.setDisable(false);
        }

        // Create the listener
        listener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    onChangeUserRole(newValue,oldValue);
                } catch (SQLException e) {
                    HelloApplication.infoAlart("Error",
                            "An error occured",e.getMessage());
                }
            }
        };

        this.userRole.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    @FXML
    void clickOnDeleteBtn(ActionEvent event) {
        boolean isAdmin = HelloApplication.userDetails.getRole() == Role.ADMIN;
        boolean isSuperuser = HelloApplication.userDetails.getRole() == Role.SUPERUSER;
        boolean isDeleteUserSuperuser = userDetails.getRole() == Role.SUPERUSER;
        System.out.println("has permission "+(isDeleteUserSuperuser || !(isSuperuser || isAdmin)));

        try{
            if(isDeleteUserSuperuser || !(isSuperuser || isAdmin)){
                HelloApplication.infoAlart("No permission",
                        "Not allowed to delete superuser","You do not have permission to delete superuser");

                return;
            }

            boolean is_confirmed = HelloApplication.confirmationAlart("Confirmation Dialog",
                    "User Delete Confirmation","Are You sure you want to delete this user?");
            if(is_confirmed) {
                db.deleteById(String.valueOf(this.userDetails.getId()),"users");
                tableRow.setVisible(false);
                tableRow.setMaxHeight(0);
                this.userFullName.setMaxHeight(0);
                this.userId.setMaxHeight(0);
                this.userRole.setMaxHeight(0);
                this.username.setMaxHeight(0);
            }

            // delete user from allUser list
            for(int i = 0; i < AdminController.allUsers.size(); i++){
                UserDetails user = AdminController.allUsers.get(i);
                if(Objects.equals(userDetails.getId(), user.getId())){
                    AdminController.allUsers.remove(i);
                    break;
                }
            }
        }catch(SQLException e){
            HelloApplication.infoAlart("Error","Error On execution",e.getMessage());
        }
    }

    @FXML
    private void onChangeUserRole(String role, String prevRole) throws SQLException {
        boolean isAdmin = HelloApplication.userDetails.getRole() == Role.ADMIN;
        boolean isSuperuser = HelloApplication.userDetails.getRole() == Role.SUPERUSER;
        boolean isDeleteUserSuperuser = userDetails.getRole() == Role.SUPERUSER;
        boolean isStudent = userDetails.getRole() == Role.STUDENT;

//        System.out.println("has permission "+(isDeleteUserSuperuser || !(isSuperuser || isAdmin)));
        userRole.getSelectionModel().selectedItemProperty().removeListener(listener); // removing the listener

        if(isDeleteUserSuperuser || isStudent || !(isSuperuser || isAdmin)){
            HelloApplication.infoAlart("No permission",
                    "Not allowed to change role","You do not have permission to change role");
            userRole.setValue(prevRole);

            userRole.getSelectionModel().selectedItemProperty().addListener(listener); // adding the listener
            return;
        }

        boolean is_confirmed = HelloApplication.confirmationAlart("Confirmation",
                "User role change Confirmation","Are You sure you want to change user role?");

        if(is_confirmed) {
            StringBuilder command = new StringBuilder();
            command.append(
                    "UPDATE users SET role = '"
                    + role + "' WHERE ID = '"
                    + this.userDetails.getId() + "'"
            );
            db.customCommand(command.toString());
            // delete user from allUser list
            for(int i = 0; i < AdminController.allUsers.size(); i++){
                UserDetails user = AdminController.allUsers.get(i);
                if(Objects.equals(userDetails.getId(), user.getId())){
                    AdminController.allUsers.get(i).setRole(user.getRoleFromString(role));
                    break;
                }
            }
        }else {
            userRole.setValue(prevRole);
        }

        userRole.getSelectionModel().selectedItemProperty().addListener(listener); // adding the listener
    }

    @FXML
    private void onClickGeneratePassBtn() throws SQLException {
        String password = generatePassword(8);
        String encodePass = userDetails.encyptPassword(password);
        StringBuilder command = new StringBuilder();
        command.append(
                "UPDATE users SET forgot_pass = false, password = '"
                + encodePass
                + "' WHERE ID = "
                + this.userDetails.getId()
        );

        boolean confirm = HelloApplication.confirmationAlart("Confirmation","Set new password","Are you sure you want to set new password?");
        if(confirm) {
            db.customCommand(command.toString());
            StringBuilder info = new StringBuilder();
            info.append(
                    "Password has been changed for the user '"
                    +this.userDetails.getUsername() + "'"
                    +"\nNew password is: '"
                    + password +"'"
            );
            HelloApplication.infoAlart("Important","Password was set",info.toString());
        }
    }

    private String generatePassword(int length){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?";
        String password = "";
        SecureRandom random = new SecureRandom();

        for(int i = 0; i < length; i++){
            int index = random.nextInt(characters.length());
            password += characters.charAt(index);
        }


        return  password;
    }
}

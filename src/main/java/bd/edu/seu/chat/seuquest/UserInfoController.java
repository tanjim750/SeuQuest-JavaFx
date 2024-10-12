package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.user.Role;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import javafx.event.ActionEvent;

import java.sql.SQLException;

public class UserInfoController {
    @FXML
    private Label hasRole;

    @FXML
    private HBox tableRow;

    @FXML
    private Label userFullName;

    @FXML
    private Label userId;

    @FXML
    private Label userRole;

    @FXML
    private Label username;

    private Integer id;

    public void setData(boolean hasRole, String userFullName, int userId, Role userRole, String username) {
        this.id = userId;

        if(hasRole) this.hasRole.setText("Yes");
        else this.hasRole.setText("No");

        this.userFullName.setText(userFullName);
        this.userId.setText(String.valueOf(userId));
        this.userRole.setText(userRole.name());
        this.username.setText(username);
    }

    @FXML
    void clickOnDeleteBtn(ActionEvent event) {
        try{
            boolean is_confirmed = HelloApplication.confirmationAlart("Confirmation Dialog",
                    "User Delete Confirmation","Are You sure you want to delete this user?");
            if(is_confirmed) {
                HelloApplication.dbManager.deleteById(String.valueOf(this.id),"users");
                tableRow.setVisible(false);
                tableRow.setMaxHeight(0);
                this.userFullName.setMaxHeight(0);
                this.userId.setMaxHeight(0);
                this.userRole.setMaxHeight(0);
                this.username.setMaxHeight(0);
            }
        }catch(SQLException e){
            HelloApplication.infoAlart("Error","Error On execution",e.getMessage());
        }
    }
}

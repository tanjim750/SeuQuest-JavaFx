package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.user.Role;
import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddKeyController implements Initializable {


    @FXML
    private Button addUserBtn;

    @FXML
    private RadioButton adminRole;

    @FXML
    private Label anonymousQueries;

    @FXML
    private Button chatBtn;

    @FXML
    private Button addKeyBtn;

    @FXML
    private Button dashboardBtn;

    @FXML
    private RadioButton generalRole;

    @FXML
    private TextField keyField;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label name;

    @FXML
    private ImageView profile2;

    @FXML
    private ImageView profle;

    @FXML
    private Label totalQuerey;

    @FXML
    private Label totalUsers;

    @FXML
    private RadioButton trainerRole;

    @FXML
    private Text userRole;

    @FXML
    private ToggleGroup user_role;

    @FXML
    private Label username;

    @FXML
    private Label warningLable;
    private UserDetails userDetails = HelloApplication.getDetails();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profle.setImage(userDetails.getProfileImage());
        profile2.setImage(userDetails.getProfileImage());
        name.setText(userDetails.getFullName());
        userRole.setText(userDetails.getRole().name());
        username.setText("@".concat(userDetails.getUsername()));


        setTotalUsers();
        setTotalQueries();
        setAnonymousQueries();

        addKeyBtn.setOnAction(event -> {
            createNewKey();
        });
    }

    private void setTotalUsers() {
        try{
            String query = "SELECT COUNT(*) AS user_count FROM users";
            ResultSet userCount = HelloApplication.dbManager.customQuery(query);
            if (userCount.next()) {
                totalUsers.setText(String.valueOf(userCount.getInt("user_count")-1));
            }else {
                totalUsers.setText("0");
            }
        }catch (SQLException e){
            totalUsers.setText("0");
        }

    }

    private void setTotalQueries() {
        try{
            String query = "SELECT COUNT(*) AS count FROM messages;";
            ResultSet count = HelloApplication.dbManager.customQuery(query);
            if (count.next()) {
                totalQuerey.setText(String.valueOf(count.getInt("count")));
            }else {
                totalQuerey.setText("0");
            }
        }catch (SQLException e){
            totalQuerey.setText("0");
        }

    }

    private void setAnonymousQueries() {
        try{
            String query = "SELECT COUNT(*) AS count FROM messages where user is null;";

            ResultSet count = HelloApplication.dbManager.customQuery(query);
            if (count.next()) {
                anonymousQueries.setText(String.valueOf(count.getInt("count")));
            }else {
                anonymousQueries.setText("0");
            }
        }catch (SQLException e){
            anonymousQueries.setText("0");
        }

    }

    private void createNewKey(){
        String key = userDetails.encyptPassword(keyField.getText());
        Role role = getRole();
        if(keyField.getText().isEmpty() || role == null){
            HelloApplication.infoAlart("Warning","Invalid request","All fields are required");
            return;
        }

        try{
            String command = "INSERT INTO secret_key(role,skey) VALUES('"+role.name()+"','"+key+"')";
            HelloApplication.dbManager.customCommand(command);
            HelloApplication.infoAlart("Information","Success","Successfully created new key");
            keyField.clear();
            generalRole.setSelected(true);

        }catch (SQLException e){
            HelloApplication.infoAlart("Error","Error on execution",e.getMessage());
        }
    }

    private Role getRole(){
        if(adminRole.isSelected()){
            return Role.ADMIN;
        }if(trainerRole.isSelected()){
            return Role.TRAINER;
        } else if (generalRole.isSelected()) {
            return Role.GENERAL;
        }else {
            return null;
        }
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
    public void onClickDashboardBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("admin-view","SeuQuest- Admin dashboard", 1170, 744);
    }

    @FXML
    public void onClickChatBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("chat-view","SeuQuest- Add secret key", 1300, 744);
    }
}

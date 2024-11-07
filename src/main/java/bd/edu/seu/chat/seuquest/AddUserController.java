package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.user.Role;
import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {
    @FXML
    private Button addKeyBtn;

    @FXML
    private RadioButton adminRole;

    @FXML
    private Label anonymousQueries;

    @FXML
    private Button chatBtn;

    @FXML
    private Button createUserBtn;

    @FXML
    private Button dashboardBtn;

    @FXML
    private RadioButton generalRole;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label name;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ImageView profile2;

    @FXML
    private ToggleGroup profiles;

    @FXML
    private ImageView profle;

    @FXML
    private Label totalQuerey;

    @FXML
    private Label totalUsers;

    @FXML
    private RadioButton trainerRole;

    @FXML
    private RadioButton userProfile1, userProfile2, userProfile3, userProfile4, userProfile5, userProfile6;

    @FXML
    private Text userRole;

    @FXML
    private ToggleGroup user_role;

    @FXML
    private Label username;

    @FXML
    private TextField usernameField;

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
    public void onClickLogoutBtn(ActionEvent event) throws IOException, SQLException {
        HelloApplication.logout();
        HelloApplication.changeScene("hello-view","SeuQuest- welcome", 640, 744);
    }

    @FXML
    public void onClickAddKeyBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("addkey-view","SeuQuest- Add secret key", 1170, 744);
    }

    @FXML
    public void onClickDashboardBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("admin-view","SeuQuest- Admin dashboard", 1170, 744);
    }

    @FXML
    public void onClickChatBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("chat-view","SeuQuest- Add secret key", 1300, 744);
    }

    @FXML
    public void onClickCreateUserBtn(ActionEvent event){
        String name = nameField.getText();
        String password = passwordField.getText();
        String username = usernameField.getText();
        Role role = getRole();
        String profilePath = getProfilePath();

        if(name.isEmpty() || password.isEmpty() || username.isEmpty() || role == null){
            HelloApplication.infoAlart("Warning","Invalid request","All fields are required");
            return;
        }

        try{
            HelloApplication.register(username,password,role,name,profilePath);
            HelloApplication.infoAlart("Information","Success","Successfully created new user");
            nameField.clear();
            passwordField.clear();
            usernameField.clear();
            generalRole.setSelected(true);
            userProfile1.setSelected(true);
        }catch (Exception e){
            HelloApplication.infoAlart("Error","Error While creating user",e.getMessage());
        }

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

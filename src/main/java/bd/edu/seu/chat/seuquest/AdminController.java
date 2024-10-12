package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.user.DatabaseManager;
import bd.edu.seu.chat.seuquest.user.Role;
import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML
    private Button addKeyBtn;

    @FXML
    private Button addUserBtn;

    @FXML
    private Label anonymousQueries;

    @FXML
    private Button chatBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label name;

    @FXML
    private ImageView profile2;

    @FXML
    private ImageView profle;

    @FXML
    private VBox tableDataVbox;

    @FXML
    private Label tableHasRole;

    @FXML
    private HBox tableHbox;

    @FXML
    private HBox tableHeading;

    @FXML
    private HBox tableHeading2;

    @FXML
    private Label tableId;

    @FXML
    private Label tableName;

    @FXML
    private Label tableRole;

    @FXML
    private ScrollPane tableScroll;

    @FXML
    private Label tableUsername;

    @FXML
    private Label totalQuerey;

    @FXML
    private Label totalUsers;

    @FXML
    private Text userRole;

    @FXML
    private Label username;

    private UserDetails userDetails = HelloApplication.getDetails();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        profle.setImage(userDetails.getProfileImage());
        profile2.setImage(userDetails.getProfileImage());
        name.setText(userDetails.getFullName());
        userRole.setText(userDetails.getRole().name());
        username.setText("@".concat(userDetails.getUsername()));


        setTotalUsers();
        displayAllUsers();
        setTotalQueries();
        setAnonymousQueries();
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

    private void displayAllUsers() {
        try{
            ResultSet users = HelloApplication.dbManager.getAll("users");
            while (users.next()) {
                boolean hasRole = users.getBoolean("has_role");
                String userFullName = users.getString("full_name");
                int userId = users.getInt("id");
                Role userRole = userDetails.getRoleFromString(users.getString("role"));
                String username = users.getString("username");
                if(!username.equals(userDetails.getUsername())) {
                    loadUserLayout(hasRole,userFullName,userId,userRole,username);
                }

            }
        }catch (SQLException e){
            HelloApplication.infoAlart("Error","Error On execution",e.getMessage());
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
    public void onClickAddKeyBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("addkey-view","SeuQuest- Add secret key", 1170, 744);
    }

    @FXML
    public void onClickChatBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("chat-view","SeuQuest- Add secret key", 1300, 744);
    }

    public void loadUserLayout(boolean hasRole, String userFullName,
                               int userId, Role userRole, String username) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("userInfo.fxml"));
            Parent newContent = loader.load();

            UserInfoController controller = loader.getController();
            controller.setData(hasRole,userFullName,userId,userRole,username);

            tableDataVbox.getChildren().add(newContent);

        } catch (IOException e) {
            HelloApplication.infoAlart("Error","Error On Runtime",e.getMessage());
        }
    }

}

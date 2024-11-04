package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.user.Role;
import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

    @FXML
    private TextField searchField;

    private UserDetails userDetails = HelloApplication.getDetails();
    public static List<UserDetails> allUsers = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allUsers.clear(); // remove all users

        profle.setImage(userDetails.getProfileImage());
        profile2.setImage(userDetails.getProfileImage());
        name.setText(userDetails.getFullName());
        userRole.setText(userDetails.getRole().name());
        username.setText("@".concat(userDetails.getUsername()));


        setTotalUsers();
        getAllUsers();
        setTotalQueries();
        setAnonymousQueries();

        searchField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                String searchText = searchField.getText().trim();
                if(!searchText.isEmpty()){
                    searchUser(searchText);
                }
            }else {
                String searchText = searchField.getText().trim();
                if(searchText.isEmpty()){
                    displayUsersList(allUsers);
                }
            }
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

    private void getAllUsers() {
        try{
            ResultSet users = HelloApplication.dbManager.getAll("users");
            while (users.next()){
                boolean hasRole = users.getBoolean("has_role");
                String userFullName = users.getString("full_name");
                int userId = users.getInt("id");
                Role userRole = userDetails.getRoleFromString(users.getString("role"));
                String username = users.getString("username");

                UserDetails user = new UserDetails(false,userId,username,userFullName,hasRole,userRole);
                allUsers.add(user);
            }
            displayUsersList(allUsers);
        }catch (SQLException e){
            HelloApplication.infoAlart("Error","Error On execution",e.getMessage());
        }
    }

    private void displayUsersList(List<UserDetails> users) {
        tableDataVbox.getChildren().clear();
        for (UserDetails user : users) {
            if(!username.equals(userDetails.getUsername())) {
                if(!user.getUsername().equals(userDetails.getUsername())) {
                    loadUserLayout(user.hasRole(),user.getFullName(), user.getId(), user.getRole(),user.getUsername());
                }
            }
        }
    }

    private void searchUser(String text){
        List<UserDetails> users = allUsers.stream().filter( user ->
                user.getFullName().toLowerCase().contains(text) ||
                user.getUsername().toLowerCase().contains(text)

        ).collect(Collectors.toList());

        displayUsersList(users);
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

    @FXML
    private void onClickTrainingBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("trainer-view","SeuQuest- Training dashboard", 1300, 744);
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

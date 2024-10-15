package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.user.DatabaseManager;
import bd.edu.seu.chat.seuquest.user.Role;
import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Optional;

public class HelloApplication extends Application {
    private static Stage stage;
    public static UserDetails userDetails;
    public static DatabaseManager dbManager;

    static {
        try {
            dbManager = new DatabaseManager("jdbc:mysql://localhost:3306/seuquestdb",
                    "seuquest", "seuquest");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean login(String username, String password) throws SQLException {
        userDetails = new UserDetails(username,password);
        boolean is_loggedIn = userDetails.login();
//        System.out.println(userDetails.getDetails());
        return is_loggedIn;
    }

    public static boolean register(String username, String password,String secretKey,
                                   String fullName,String profile) throws SQLException {
        UserDetails newUserDetails = new UserDetails(username,password,secretKey,fullName,profile);
        return newUserDetails.register();
    }

    public static boolean register(String username, String password,Role role,
                                   String fullName,String profile) throws SQLException {
        UserDetails newUserDetails = new UserDetails(username,password,role,fullName,profile);
        return newUserDetails.register();
    }

    public static boolean logout(){
        return userDetails.logout();
    }

    public static UserDetails getDetails(){
        if(userDetails==null){
            return new UserDetails();
        }
        return userDetails;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 744);
        stage.setTitle("SeuQuest");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch();
    }

    public static void changeScene(String fxml, String title, int width, int height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml.concat(".fxml")));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public static void changeScene(String fxml, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml.concat(".fxml")));
        Scene scene = new Scene(fxmlLoader.load(), 640, 744);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public static void loadNewLayout(HBox targetHbox, String fxml) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxml));
            Parent newContent = loader.load();

            // Clear the VBox and add the new FXML content
            targetHbox.getChildren().clear();
            targetHbox.getChildren().add(newContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadNewLayout(VBox targetVbox, String fxml) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxml));
            Parent newContent = loader.load();

            // Clear the VBox and add the new FXML content
            targetVbox.getChildren().clear();
            targetVbox.getChildren().add(newContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String encyptPassword(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Unable to apply SHA-256 hashing algorithm.", e);
        }
    }

    public static void infoAlart(String title, String header, String info){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Set alert properties
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(info);

        // Show the alert and wait for user response
        alert.showAndWait();
    }

    public static boolean confirmationAlart(String title, String header, String info){
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(header);
        confirmationAlert.setContentText(info);

        // Show the alert and capture the response
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        // Check if the user clicked OK or Cancel
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    public static void visitUrl(String url) throws IOException, URISyntaxException {
        URI targetUrl = new URI(url);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(targetUrl);
        }else {
            Runtime.getRuntime().exec(new String[]{"xdg-open", url});
        }
    }
}


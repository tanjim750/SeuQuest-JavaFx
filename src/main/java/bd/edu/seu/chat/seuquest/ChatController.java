package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.gemini.Gemini;
import bd.edu.seu.chat.seuquest.user.DatabaseManager;
import bd.edu.seu.chat.seuquest.user.Role;
import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    private Button askBtn;

    @FXML
    private ChoiceBox<String> deptChoice;

    @FXML
    private VBox inbox;

    @FXML
    private TextField queryField;

    @FXML
    private HBox sidebarHbox;

    @FXML
    private HBox tableHbox;

    @FXML
    private ScrollPane tableScroll;

    private Gemini gemini;
    private DatabaseManager db = HelloApplication.dbManager;
    private UserDetails userDetails = HelloApplication.getDetails();

    private ObservableList<String> choices = FXCollections.observableArrayList(
        "CSE","BBA","PHARMACY","TEXTILE"
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gemini = new Gemini("AIzaSyDBPSCKl709XE1mGjxySjgxEgX2MJHtRZY");
        gemini.setSystemInstruction("You are SeuQuest, an AI chatbot developed by Tanjim Abubokor. You are here to provide information about Southeast University.");

        deptChoice.setItems(choices);
        deptChoice.setValue("CSE");

        if (userDetails.getRole() == Role.ADMIN) {
            HelloApplication.loadNewLayout(sidebarHbox,"adminSidebar-view.fxml");
        }else if(userDetails.getRole() == Role.TRAINER){
            HelloApplication.loadNewLayout(sidebarHbox,"trainerSidebar-view.fxml");
        }else {
            HelloApplication.loadNewLayout(sidebarHbox,"generalSidebar-view.fxml");
        }

        // load previous messages
        if(userDetails.isAuthenticated()){
            try {
                loadAllMessages();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void onClickAskBtn(ActionEvent event) throws IOException {
        String query = queryField.getText().trim();
        if(!query.isEmpty()){
            askBtn.setDisable(true);
            MessageController controller = addMessage(query,"Texting....");
            queryField.clear();
            tableScroll.setVvalue(1.0);
//            String seuQuestMsg = gemini.query(query);
//            controller.setSeuquestMsg(seuQuestMsg);

            // Create a Task to run the query asynchronously
            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    return gemini.query(query);  // Perform the long-running operation
                }

                @Override
                protected void succeeded() {
                    // This method runs on the JavaFX Application Thread after the task completes
                    String seuQuestMsg = getValue();  // get the result of gemini.query(query)
                    controller.setSeuquestMsg(seuQuestMsg);
                    try {
                        storeMessageToDB(query,seuQuestMsg); // store message into database
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    askBtn.setDisable(false);  // Enable the button
                    tableScroll.setVvalue(1.0);  // Scroll to the bottom
                }

                @Override
                protected void failed() {
                    // Handle error if the task fails
                    controller.setSeuquestMsg("Error occurred while querying.");
                    askBtn.setDisable(false);  // Enable the button
                }
            };

            // Run the task in a separate thread
            new Thread(task).start();

        }
        tableScroll.setVvalue(1.0);
        askBtn.setDisable(false);
    }

    private MessageController addMessage(String query, String seuQuestMsg) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("message-view.fxml"));
        Parent newContent = loader.load();

        MessageController controller = loader.getController();
        controller.setData(query, seuQuestMsg);

        inbox.getChildren().add(newContent);
        return controller;
    }

    private void storeMessageToDB(String query, String seuQuestMsg) throws SQLException {
        StringBuilder command = new StringBuilder();
        String userId = (userDetails.getId() != null)? String.valueOf(userDetails.getId()):"NULL";

        command.append(
                "INSERT INTO messages(message,reply,user) "
                + "VALUES("
                + "\"" + query + "\", "
                + "\"" + seuQuestMsg + "\", "
                + userId
                + ")"
        );

        db.customCommand(command.toString());
    }

    private void loadAllMessages() throws SQLException, IOException {
        StringBuilder command = new StringBuilder();
        String userId = String.valueOf(userDetails.getId());

        command.append(
                "SELECT * FROM messages"
                + " WHERE user = "
                +userId
        );

        ResultSet messages = db.customQuery(command.toString());

        while (messages.next()) {
            String message = messages.getString("message");
            String reply = messages.getString("reply");

            addMessage(message,reply);
        }

        tableScroll.setVvalue(1.0);
    }


    @FXML
    void onClickAddKeyBtn(ActionEvent event) {

    }

    @FXML
    void onClickCreateUserBtn(ActionEvent event) {

    }

    @FXML
    void onClickGithubBtn(ActionEvent event) {

    }

    @FXML
    void onClickLinkedinBtn(ActionEvent event) {

    }

    @FXML
    void onClickLogoutBtn(ActionEvent event) {

    }

    @FXML
    void onClickWebsiteBtn(ActionEvent event) {

    }



}

package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.modules.qdrant.QDrant;
import bd.edu.seu.chat.seuquest.user.DatabaseManager;
import bd.edu.seu.chat.seuquest.user.Role;
import bd.edu.seu.chat.seuquest.user.UserDetails;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TrainerController implements Initializable {
    @FXML
    private ChoiceBox<String> deptChoice;

    @FXML
    private VBox inbox;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label name;

    @FXML
    private ImageView profle;

    @FXML
    private TextField dataField;

    @FXML
    private HBox sidebarHbox;

    @FXML
    private ScrollPane tableScroll;

    @FXML
    private Button trainingBtn;

    @FXML
    private Text userRole;

    private UserDetails userDetails = HelloApplication.getDetails();
    private DatabaseManager db = HelloApplication.dbManager;
    private ObservableList<String> choices = FXCollections.observableArrayList(
            "CSE","EEE","BBA","PHARMACY","TEXTILE","ARCHITECTURE",
            "ENGLISH","LAW","BANGLA","ECONOMICS","MDS","OTHER"
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(userDetails.getRole() == Role.ADMIN || userDetails.getRole() == Role.SUPERUSER){
            HelloApplication.loadNewLayout(sidebarHbox,"adminSidebar-view.fxml");
        }else {
            HelloApplication.loadNewLayout(sidebarHbox,"trainerSidebar-view.fxml");
        }


        // set chocies
        deptChoice.setItems(choices);
        deptChoice.setValue("CSE");

        // load all previous training
        try {
            loadAllMessages();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void training(ActionEvent event) throws IOException {
        String data = dataField.getText().trim();
        String dept = deptChoice.getValue();

        JsonObject metadata = new JsonObject();
        if(dept.equals("OTHER")){
            JsonArray array = new JsonArray();
            choices.forEach(choice -> array.add(choice));
            metadata.add("data", array);
        }else {
            metadata.addProperty("data", dept);
        }

        if (!data.isEmpty()) {
            trainingBtn.setDisable(true);
            dataField.clear();

            MessageController controller = addMessage(data,"Please wait");

            // Create a Task to run the query asynchronously
            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    JsonObject response = QDrant.train(data,metadata);
                    boolean ok = response.get("Ok").getAsBoolean();
                    StringBuilder msg = new StringBuilder();
                    if(ok){
                        msg.append(
                                "Thank You "
                                + userDetails.getFullName()
                                + "\nI am always happy to learn about Southeast University"
                        );
                        return msg.toString();
                    }else {
                        msg.append(
                                "Sorry "
                                + userDetails.getFullName()
                                + "\nSome this went wrong. Try again"
                        );
                        return msg.toString();
                    } // Perform the long-running operation
                }

                @Override
                protected void succeeded() {
                    // This method runs on the JavaFX Application Thread after the task completes
                    String seuQuestMsg = getValue();  // get the result of gemini.query(query)
                    controller.setSeuquestMsg(seuQuestMsg);
                    try {
                        storeMessageToDB(data,dept,seuQuestMsg);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    trainingBtn.setDisable(false);  // Enable the button
                    tableScroll.setVvalue(1.0);  // Scroll to the bottom
                }

                @Override
                protected void failed() {
                    // Handle error if the task fails
                    controller.setSeuquestMsg("Error occurred while texting.");
                    trainingBtn.setDisable(false);  // Enable the button
                }
            };

            // Run the task in a separate thread
            new Thread(task).start();
            trainingBtn.setDisable(false);  // Enable the button
            tableScroll.setVvalue(1.0);
        }
    }

    private void loadAllMessages() throws SQLException, IOException {
        StringBuilder command = new StringBuilder();
        String userId = String.valueOf(userDetails.getId());

        command.append(
                "SELECT * FROM training"
                        + " WHERE user = "
                        +userId
        );

        ResultSet messages = db.customQuery(command.toString());

        while (messages.next()) {
            String data = messages.getString("data");
            String reply = messages.getString("reply");

            addMessage(data,reply);
        }

        tableScroll.setVvalue(1.0);
    }


    private MessageController addMessage(String query, String seuQuestMsg) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("message-view.fxml"));
        Parent newContent = loader.load();

        MessageController controller = loader.getController();
        controller.setData(query, seuQuestMsg);

        inbox.getChildren().add(newContent);
        return controller;
    }

    private void storeMessageToDB(String data, String dept, String reply) throws SQLException {
        StringBuilder command = new StringBuilder();
        String userId = (userDetails.getId() != null)? String.valueOf(userDetails.getId()):"NULL";

        command.append(
                "INSERT INTO training(data,department,reply,user) "
                        + "VALUES("
                        + "\"" + data + "\", "
                        + "\"" + dept + "\", "
                        + "\"" + reply + "\","
                        + userId
                        + ")"
        );

        db.customCommand(command.toString());
    }

    @FXML
    void onClickChatBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("chat-view","SeuQuest- welcome", 1300, 744);
    }

    @FXML
    void onClickGithubBtn(ActionEvent event) {

    }

    @FXML
    void onClickLinkedinBtn(ActionEvent event) {

    }

    @FXML
    void onClickLogoutBtn(ActionEvent event) throws IOException, SQLException {
        HelloApplication.logout();
        HelloApplication.changeScene("hello-view","SeuQuest- welcome", 640, 744);
    }

    @FXML
    void onClickTrainBtn(ActionEvent event) throws IOException {
        HelloApplication.changeScene("trainer-view","SeuQuest- welcome", 1300, 744);
    }

    @FXML
    void onClickWebsiteBtn(ActionEvent event) {

    }
}

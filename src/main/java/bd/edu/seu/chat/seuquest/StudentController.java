package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.modules.gemini.Gemini;
import bd.edu.seu.chat.seuquest.modules.qdrant.QDrant;
import bd.edu.seu.chat.seuquest.user.DatabaseManager;
import bd.edu.seu.chat.seuquest.user.UserDetails;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class StudentController implements Initializable {
    @FXML
    private TextField queryField;

    @FXML
    private VBox inbox;

    @FXML
    private HBox sidebarHbox;

    @FXML
    private ScrollPane tableScroll;

    @FXML
    private Button askBtn;

    @FXML
    private Text chooseFileText;

    private static String geminiSystemMsg = """
            You are SeuQuest developed by Tanjim Abubokor for help students by answering question from given data. Use the following pieces of context to answer the question.
            --------------------------------------------------------------------------------------
            context:
            """;

    private Gemini gemini;
    private DatabaseManager db = HelloApplication.dbManager;
    private UserDetails userDetails;

    private String chosenFileType;
    private String chosenFilePath = "";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDetails = HelloApplication.getDetails();
        System.out.println(userDetails.getRole());
        gemini = new Gemini("AIzaSyDBPSCKl709XE1mGjxySjgxEgX2MJHtRZY");
        HelloApplication.loadNewLayout(sidebarHbox,"studentSidebar-view.fxml");
    }

    @FXML
    void onClickAskBtn(ActionEvent event) throws IOException {
        String query = queryField.getText();
        if (query.isEmpty() || chosenFilePath.isEmpty()) {
            return;
        }

        askBtn.setDisable(true);
        queryField.clear();
        MessageController messageController = addMessage(query,"Writing...");

        Thread thread = new Thread(() -> {
            try {
                JsonObject response = QDrant.textSimilarity(query,chosenFilePath,chosenFileType);
                System.out.println(response);
                boolean ok = response.get("OK").getAsBoolean();

                if(ok){
                    String context = response.get("context").getAsString();
                    gemini.setSystemInstruction(geminiSystemMsg.concat(context));
                }else {
                    gemini.setSystemInstruction(geminiSystemMsg.concat("\n No information found"));
                }

                String answer = gemini.query(query);
                messageController.setSeuquestMsg(answer);

            } catch (Exception e) {
                e.printStackTrace();
                messageController.setSeuquestMsg(e.getMessage());
            }

            askBtn.setDisable(false);
        });

        thread.start();

    }

    @FXML
    private void onClickChooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file for Q&A");
        FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter("All supported Files (*.pdf, *.txt)", "*.pdf","*.txt");

        fileChooser.getExtensionFilters().addAll(allFilter);
        File file = fileChooser.showOpenDialog(inbox.getScene().getWindow());
        chooseFileText.setText(file.getAbsolutePath());

        String fileExtension = "";
        int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            fileExtension = file.getName().substring(i + 1); // Get the extension without the dot
        }
        System.out.println(fileExtension);
        chosenFileType = fileExtension;
        chosenFilePath = file.getAbsolutePath();
    }

    private MessageController addMessage(String query, String seuQuestMsg) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("message-view.fxml"));
        Parent newContent = loader.load();

        MessageController controller = loader.getController();
        controller.setData(query, seuQuestMsg);

        inbox.getChildren().add(newContent);
        return controller;
    }

}

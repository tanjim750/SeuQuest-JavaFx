package bd.edu.seu.chat.seuquest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentController implements Initializable {
    @FXML
    private TextField dataField;

    @FXML
    private VBox inbox;

    @FXML
    private HBox sidebarHbox;

    @FXML
    private ScrollPane tableScroll;

    @FXML
    private Button trainingBtn;

    @FXML
    private Text chooseFileText;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HelloApplication.loadNewLayout(sidebarHbox,"studentSidebar-view.fxml");
    }

    @FXML
    void training(ActionEvent event) {

    }

    @FXML
    private void onClickChooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file for Q&A");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("Text files", "*.pdf");
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text files", "*.txt");

        fileChooser.getExtensionFilters().addAll(pdfFilter, txtFilter);
        File file = fileChooser.showOpenDialog(inbox.getScene().getWindow());
        chooseFileText.setText(file.getAbsolutePath());
    }

}

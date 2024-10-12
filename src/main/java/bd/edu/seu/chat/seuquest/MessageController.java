package bd.edu.seu.chat.seuquest;

import bd.edu.seu.chat.seuquest.user.UserDetails;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javax.swing.text.html.HTML;
import java.net.URL;
import java.util.ResourceBundle;

public class MessageController implements Initializable {
    @FXML
    private Text seuquestMsg;

    @FXML
    private Text userMsg;

    @FXML
    private ImageView userProfile;

    private UserDetails userDetails = HelloApplication.getDetails();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userProfile.setImage(userDetails.getProfileImage());
    }

    public void setData(String msg, String reply){
        userMsg.setText(msg);
        seuquestMsg.setText(reply);
    }

    public void setUserMsg(String msg){
        userMsg.setText(msg);
    }

    public void setSeuquestMsg(String msg){
        seuquestMsg.setText(msg);
    }

}

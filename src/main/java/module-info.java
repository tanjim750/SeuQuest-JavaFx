module bd.edu.seu.chat.seuquest {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.json;
    requires org.apache.httpcomponents.httpcore;
    requires com.google.gson;
    requires org.apache.httpcomponents.httpclient;


    opens bd.edu.seu.chat.seuquest to javafx.fxml;
    exports bd.edu.seu.chat.seuquest;
}
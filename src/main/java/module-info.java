module bd.edu.seu.chat.seuquest {
    requires javafx.fxml;
    requires org.json;
    requires org.apache.httpcomponents.httpcore;
    requires com.google.gson;
    requires org.apache.httpcomponents.httpclient;
    requires javafx.web;
    requires java.desktop;


    opens bd.edu.seu.chat.seuquest to javafx.fxml;
    exports bd.edu.seu.chat.seuquest;
}
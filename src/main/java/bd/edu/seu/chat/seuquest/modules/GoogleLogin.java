package bd.edu.seu.chat.seuquest.modules;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class GoogleLogin {
    private static final String LOGIN_URL = "http://127.0.0.1:5000/login/credentials";

    public GoogleLogin() {}

    public static String request(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        get.setHeader("Content-Type", "application/json");

        CloseableHttpResponse response = httpClient.execute(get);
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        httpClient.close();

        return responseString;
    }

    public JsonObject login() throws IOException {
        String responseString = request(LOGIN_URL);
        return JsonParser.parseString(responseString).getAsJsonObject();
    }
}

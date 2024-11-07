package bd.edu.seu.chat.seuquest.modules.qdrant;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class QDrant {
    private static final String API_URL = "http://127.0.0.1:5000/";
    private static final String search_URL = API_URL.concat("search");
    private static final String train_URL = API_URL.concat("train");
    private static final String TEXT_SIMILARITY = API_URL.concat("text-similarity");

    public static String request(String url, JsonObject payload) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json");

        StringEntity entity = new StringEntity(payload.toString());
        post.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(post);
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        httpClient.close();

        return responseString;
    }

    public static JsonObject search(String query, JsonObject metadata) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("query", query);
        payload.add("metadata", metadata);

        String responseString =  request(search_URL, payload);
        System.out.println(responseString);
        return JsonParser.parseString(responseString).getAsJsonObject();
    }

    public static JsonObject train(String data, JsonObject metadata) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("data", data);
        payload.add("metadata", metadata);

        String responseString = request(train_URL, payload);
        return JsonParser.parseString(responseString).getAsJsonObject();
    }

    public static JsonObject textSimilarity(String question, String filePath, String fileType) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("question", question);
        payload.addProperty("file_path", filePath);
        payload.addProperty("file_type", fileType);

        String responseString = request(TEXT_SIMILARITY, payload);
        return JsonParser.parseString(responseString).getAsJsonObject();
    }

}

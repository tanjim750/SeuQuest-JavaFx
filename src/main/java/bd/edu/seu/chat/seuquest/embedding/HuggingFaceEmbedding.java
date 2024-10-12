package bd.edu.seu.chat.seuquest.embedding;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HuggingFaceEmbedding {
    private static final String API_URL = "https://api-inference.huggingface.co/models/bert-base-uncased"; // Change to BERT model
    private static final String API_KEY = "Bearer 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";


    public static String getEmbedding(String text) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(API_URL);

        post.setHeader("Authorization", API_KEY);
        post.setHeader("Content-Type", "application/json");

        JsonObject payload = new JsonObject();
        payload.addProperty("inputs", text);

        StringEntity entity = new StringEntity(payload.toString());
        post.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(post);
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        httpClient.close();

        return responseString;
    }
}



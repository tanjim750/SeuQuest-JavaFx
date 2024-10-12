package bd.edu.seu.chat.seuquest.gemini;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Gemini {

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";
    private String apiKey;
    private String systemInstructionText;
    private JsonArray previousMessages;

    public Gemini(String apiKey) {
        this.apiKey = apiKey;
        this.previousMessages = new JsonArray();
    }

    public JsonArray getContents(){
        return previousMessages;
    }
    /**
     * Set the system instruction that guides the AI model.
     * @param instruction the instruction for the system, similar to setting up a persona for the model.
     */
    public void setSystemInstruction(String instruction) {
        this.systemInstructionText = instruction;
    }

    /**
     * Add a message to the conversation history (up to 5 messages max).
     * @param role either "user" or "model" for the message sender.
     * @param message the message text to add to the history.
     */
    public void setPreviousMessage(String role, String message) {
        JsonObject messageObject = new JsonObject();
        messageObject.addProperty("role", role);

        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", message);
        parts.add(part);

        messageObject.add("parts", parts);

        if (previousMessages.size() >= 5) {
            previousMessages.remove(0); // Limit to the last 5 messages
        }
        previousMessages.add(messageObject);
    }

    /**
     * Send a query to the Gemini API using the conversation history and system instruction.
     * @param userInput the current user input.
     * @return the response from the Gemini API.
     * @throws IOException if there's an error in making the HTTP request.
     */
    public String query(String userInput) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(API_URL + apiKey);

        post.setHeader("Content-Type", "application/json");

        JsonObject payload = buildPayload(userInput);
        StringEntity entity = new StringEntity(payload.toString());
        post.setEntity(entity);

        HttpResponse response = httpClient.execute(post);
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        httpClient.close();
        
        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();

        JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
        JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
        JsonObject content = firstCandidate.getAsJsonObject("content");
        JsonArray parts = content.getAsJsonArray("parts");
        JsonObject firstPart = parts.get(0).getAsJsonObject();
        String text = firstPart.get("text").getAsString();

        setPreviousMessage("model", text);
        
        return text;
    }

    private JsonObject buildPayload(String userInput) {
        JsonObject payload = new JsonObject();

        // Add system instruction
        JsonObject systemInstruction = new JsonObject();
        systemInstruction.addProperty("role", "user");

        JsonArray systemParts = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", this.systemInstructionText);
        systemParts.add(part);

        systemInstruction.add("parts", systemParts);
        payload.add("systemInstruction", systemInstruction);

        // Add previous messages
        JsonArray contents = new JsonArray();
        setPreviousMessage("user", userInput);
        contents.addAll(previousMessages);

        // Attach the conversation to the payload
        payload.add("contents", contents);

        // Generation config
        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", 1);
        generationConfig.addProperty("topK", 64);
        generationConfig.addProperty("topP", 0.95);
        generationConfig.addProperty("maxOutputTokens", 8192);
        generationConfig.addProperty("responseMimeType", "text/plain");

        payload.add("generationConfig", generationConfig);

        return payload;
    }
}

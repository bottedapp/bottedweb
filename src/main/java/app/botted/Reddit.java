package app.botted;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import javax.security.sasl.AuthenticationException;
import java.time.Instant;
import java.util.Base64;
import java.io.IOException;
import com.google.gson.*;

public class Reddit {

    private final String BASE_URL = "https://www.reddit.com";
    private final String OAUTH_URL = "https://oauth.reddit.com";
    private final String clientId = "GgPNctP2KQdth-iX6aMGUQ";
    private final String clientSecret = "6zov1gDWJ8Ij60yH3L7q6N_LnPUZHA";
    private final String userAgent = "botted 0.0.1";
    private String token;
    private long expirationDate;
    protected String subreddit;

    public Reddit() throws IOException, InterruptedException {
        ensureConnection();
    }

    public Reddit(String subreddit) throws IOException, InterruptedException {
        this.subreddit = subreddit;
        ensureConnection();
    }

    //getters

    public String getSubreddit() {
        return subreddit;
    }

    //setters

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public void connect() throws IOException {
        // Get access token
        Connection conn = Jsoup.connect(BASE_URL + "/api/v1/access_token").ignoreContentType(true).ignoreHttpErrors(true).method(Method.POST).userAgent(userAgent);
        conn.data("grant_type", "client_credentials");

        // Generate the Authorization header
        String combination = clientId + ":" + clientSecret;
        combination = Base64.getEncoder().encodeToString(combination.getBytes());
        conn.header("Authorization", "Basic " + combination);

        // Open the connection and get response from server
        Response res = conn.execute();
        JsonObject object = JsonParser.parseString(res.body()).getAsJsonObject();

        // Set access token and expiration time
        this.token = object.get("access_token").getAsString();
        this.expirationDate = object.get("expires_in").getAsInt() + Instant.now().getEpochSecond();
    }

    public JsonObject useEndpoint(String endpointPath) throws IOException, InterruptedException {
        ensureConnection();
        Connection connection = Jsoup.connect(OAUTH_URL + endpointPath);
        connection.header("Authorization", "bearer " + token).ignoreContentType(true).userAgent(userAgent);
        return JsonParser.parseString(connection.execute().body()).getAsJsonObject();
    }

    public JsonArray useEndpointSubmission(String endpointPath) throws IOException, InterruptedException {
        ensureConnection();
        Connection connection = Jsoup.connect(OAUTH_URL + endpointPath);
        connection.header("Authorization", "bearer " + token).ignoreContentType(true).userAgent(userAgent);
        return JsonParser.parseString(connection.execute().body()).getAsJsonArray();
    }

    /**
     * Ensure the connection is Authenticated
     * @throws IOException
     * @throws InterruptedException
     * @throws AuthenticationException
     */
    public void ensureConnection() throws IOException, InterruptedException, AuthenticationException {
        // There is no token
        if (token == null) {
            connect();
        }
        // The token is expired
        if (Instant.now().getEpochSecond() > expirationDate) {
            connect();
        }
    }

    /**
     * Get username from input valie
     * @returns username
     */
    public String readInput(String input) throws IOException, InterruptedException {
        String user = "";
        String endpoint = "";
        if (!input.contains("/")) {
            user = input;
        }
        if (input.startsWith("t2_")) {
            //fullname
        }
        if (input.startsWith("u/") || input.contains("/u/") || input.contains("/user/")) {
            String[] e = input.split("u/");
            String[] f = e[1].split("/");
            user = f[0];
        }

        if (input.contains("/comments/") && !input.contains("/comment/")) {
            String[] c = input.split(".com/");
            String[] d = c[1].split("/");
            if (d.length <= 5) // submission
                endpoint = "/r/" + d[1] + "/comments/" + d[3];
            JsonArray submissionInfo = useEndpointSubmission(endpoint);
            JsonObject array0 = submissionInfo.get(0).getAsJsonObject();
            JsonObject submissionData = (JsonObject) array0.getAsJsonObject().get("data");
            JsonArray submissionChildren = submissionData.getAsJsonArray("children");
            for (JsonElement items : submissionChildren) {
                JsonObject data3 = (JsonObject) items.getAsJsonObject().get("data");
                String author = String.valueOf(data3.getAsJsonObject().get("author"));
                user = author.replace("\"", "");
            }

            if (d.length >= 6) { // comment
                endpoint = "/r/" + d[1] + "/api/info?id=t1_" + d[5];
                JsonObject info = useEndpoint(endpoint);
                JsonObject data = info.getAsJsonObject("data");
                JsonArray children = data.getAsJsonArray("children");
                for (JsonElement item : children) {
                    JsonObject data11 = (JsonObject) item.getAsJsonObject().get("data");
                    String author = String.valueOf(data11.getAsJsonObject().get("author"));
                    user = author.replace("\"", "");
                }
            }
        }

        if (input.contains("/comments/") && input.contains("/comment/")) { // comment
            String[] a = input.split("/r/");
            String[] subreddit = a[1].split("/");
            String[] b = input.split("/comment/");
            String[] commentId = b[1].split("/");
            endpoint = "/r/" + subreddit[0] + "/api/info?id=t1_" + commentId[0];
            JsonObject info = useEndpoint(endpoint);
            JsonObject data = info.getAsJsonObject("data");
            JsonArray children = data.getAsJsonArray("children");
            for (JsonElement item : children) {
                JsonObject data1 = (JsonObject) item.getAsJsonObject().get("data");
                String author = String.valueOf(data1.getAsJsonObject().get("author"));
                user = author.replace("\"", "");
            }
        }
        return user;
    }

    public static double findSimilarity(String x, String y) {
        double maxLength = Double.max(x.length(), y.length());
        if (maxLength > 0)
            return (maxLength - StringUtils.getLevenshteinDistance(x, y)) / maxLength;
        return 0.0;
    }

    @Override
    public String toString() {
        return "Reddit{" +
                "BASE_URL='" + BASE_URL + '\'' +
                ", OAUTH_URL='" + OAUTH_URL + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", token='" + token + '\'' +
                ", expirationDate=" + expirationDate +
                ", subreddit='" + subreddit + '\'' +
                '}';
    }

}
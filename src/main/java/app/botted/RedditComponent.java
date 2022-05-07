package app.botted;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import javax.security.sasl.AuthenticationException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.Base64;
import java.io.IOException;
import com.google.gson.*;

/**
 * Reddit API Component class
 */
public class RedditComponent {

    /**
     * Private variables
     */

    /**
     * Reddit Base Url
     */
    private final String BASE_URL = "https://www.reddit.com";
    /**
     * Reddit OAUTH Url
     */
    private final String OAUTH_URL = "https://oauth.reddit.com";
    /**
     * Client ID
     */
    private final String clientId = "GgPNctP2KQdth-iX6aMGUQ";
    /**
     * Client Secret
     */
    private final String clientSecret = "6zov1gDWJ8Ij60yH3L7q6N_LnPUZHA";
    /**
     * User Agent
     */
    private final String userAgent = "botted 0.0.1";
    /**
     * Reddit API Token
     */
    private String token, tokenDb;
    /**
     * Reddit API Token Expiration Date
     */
    private long expirationDate, expirationDateDb;
    /**
     * Username
     */
    private String user;

    /**
     * Default constructor
     */
    public RedditComponent() {
    }

    /**
     * Constructor with user parameter
     * @param user The reddit user
     */
    public RedditComponent(String user) {
        this.user = user;
    }

    /**
     * Getters
     */

    /**
     * Get username
     * @return user
     */
    public String getUser() {
        return user;
    }
    /**
     * Get auth token
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * Setters
     */

    /**
     * Set username
     * @param user the username
     */
    public void setUser(String user) {
        this.user = user;
    }
    /**
     * Set auth token
     * @param token the auth token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets access token
     * Generates authorization header
     * Opens the connection and gets response from the server
     * Sets access token and expiration time
     * @throws IOException I/O exception
     * @throws SQLException SQL Exception
     */
    public void connect() throws IOException, SQLException {
        //String dbUrl = System.getenv("JDBC_DATABASE_URL");
        String dbUrl = "jdbc:postgresql://ec2-34-194-158-176.compute-1.amazonaws.com:5432/da2g0o7m136sp5?password=7b04e1735374fcb6ba8f984fdcbcaaf5bada71f4d85df12c0e62cab2ca2b4022&sslmode=require&user=fzbeyehwmqhuxn";
        java.sql.Connection sql = DriverManager.getConnection(dbUrl);
        Statement stmt = sql.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM api");
        while (rs.next()) {
            this.tokenDb = rs.getString(1);
            this.expirationDateDb = Long.parseLong(rs.getString(2));
        }
        if (Instant.now().getEpochSecond() < expirationDateDb) {
            this.token = tokenDb;
            this.expirationDate = expirationDateDb;
        } else {
            Connection conn = Jsoup.connect(BASE_URL + "/api/v1/access_token").ignoreContentType(true).ignoreHttpErrors(true).method(Method.POST).userAgent(userAgent);
            conn.data("grant_type", "client_credentials");
            String combination = clientId + ":" + clientSecret;
            combination = Base64.getEncoder().encodeToString(combination.getBytes());
            conn.header("Authorization", "Basic " + combination);
            Response res = conn.execute();
            JsonObject object = JsonParser.parseString(res.body()).getAsJsonObject();
            this.token = object.get("access_token").getAsString();
            this.expirationDate = object.get("expires_in").getAsInt() + Instant.now().getEpochSecond();
            System.out.println(conn.response().body());
            String delToken = "DELETE FROM api WHERE token='" + this.tokenDb + "'";
            stmt.executeUpdate(delToken);
            String stm = "INSERT INTO api (token, expiration) VALUES ('" + this.token + "', '" + this.expirationDate + "')";
            stmt.executeUpdate(stm);
        }
        sql.close();
    }

    /**
     * Ensure the connection is authenticated
     * @throws IOException I/O exception
     * @throws SQLException SQL exception
     */
    public void ensureConnection() throws IOException, SQLException {
        // There is no token
        if (token == null || Instant.now().getEpochSecond() > expirationDate) {
            connect();
        }
    }

    /**
     * Send reddit endpoint request to reddit api and get Json Object result
     * @param endpointPath Path for end point
     * @return JsonObject with reddit api data
     */
    public JsonObject useEndpoint(String endpointPath) {
        try {
            ensureConnection();
            Connection connection = Jsoup.connect(OAUTH_URL + endpointPath);
            connection.header("Authorization", "bearer " + token).ignoreContentType(true).userAgent(userAgent);
            return JsonParser.parseString(connection.execute().body()).getAsJsonObject();
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Use end point for connection with JSON array return value
     * @param endpointPath Path for end point
     * @return The end point response array
     */
    public JsonArray useEndpointArray(String endpointPath) {
       try {
           ensureConnection();
           Connection connection = Jsoup.connect(OAUTH_URL + endpointPath);
           connection.header("Authorization", "bearer " + token).ignoreContentType(true).userAgent(userAgent);
           return JsonParser.parseString(connection.execute().body()).getAsJsonArray();
       } catch (Exception e) {

       }
       return null;
    }

    /**
     * Get input in form of username/comment/submission/url
     * @param input input entered
     * @return the username
     */
    public String readInput(String input) {
        String user = "";
        String endpoint = "";
        if (!input.contains("/")) {
            JsonObject about = useEndpoint("/user/" + input +"/about");
            JsonObject data = (JsonObject) about.get("data");
            user = String.valueOf(data.get("name")).replace("\"","");
        }
        if (input.startsWith("u/") || input.contains("/u/") || input.contains("/user/")) {
            String[] e = input.split("u/");
            String[] f = e[1].split("/");
            JsonObject about = useEndpoint("/user/" + f[0] +"/about");
            JsonObject data = (JsonObject) about.get("data");
            user = String.valueOf(data.get("name")).replace("\"","");
        }

        if (input.contains("/comments/") && !input.contains("/comment/")) {
            String[] c = input.split(".com/");
            String[] d = c[1].split("/");
            if (d.length <= 5) // submission
                endpoint = "/r/" + d[1] + "/comments/" + d[3];
            JsonArray submissionInfo = useEndpointArray(endpoint);
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
        this.user = user;
        return user;
    }
    /**
     * Send results to string
     * @return BASE_URL, OAUTH_URL, clientId, clientSecret, userAgent, token, expirationDate, and subreddit
     */
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
                '}';
    }
}
package app.botted;

import com.google.gson.*;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.Base64;
import java.io.IOException;

/**
 * Botted Request class
 */
public class BottedRequest {

    /**
     * Private and protected variables
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
    private final String clientId = "_JW4OUQt7_krXZd420ycuw";
    /**
     * Client Secret
     */
    private final String clientSecret = "BJFz2IB-EMvu_ye3EZ66oOcoDzWgwg";
    /**
     * User Agent
     */
    private final String userAgent = "botted 1.0";
    /**
     * Username
     */
    private final String username = "bottedapp";
    /**
     * Password
     */
    private final String password = "mc3.edu!";
    /**
     * Reddit API Token
     */
    private String token, tokenDb;
    /**
     * Reddit API Token Expiration Date
     */
    private long expirationDate, expirationDateDb;
    /**
     * Reddit Subreddit
     */
    private String subreddit;
    /**
     * RedditComponent
     */
    private RedditComponent reddit;

    /**
     * Default Constructor
     * @throws IOException I/O Exception
     * @throws InterruptedException Interrupted Exception
     * @throws SQLException SQL Exception
     */
    public BottedRequest() throws IOException, InterruptedException, SQLException {
        reddit = new RedditComponent();
        userConnect();
        getComments();
        getSubmissions();
    }

    /**
     * Getters
     */

    /**
     * Get Reddit API Token
     * @return auth token
     */
    public String getToken() {
        return token;
    }
    /**
     * Get Reddit API Token Expiration Date
     * @return auth token expiration date
     */

    public long getExpirationDate() {
        return expirationDate;
    }
    /**
     * Get Reddit Subreddit
     * @return Reddit subreddit
     */
    public String getSubreddit() {
        return subreddit;
    }

    /**
     * Setters
     */

    /**
     * Set Reddit API Token
     * @param token Auth token
     */
    public void setToken(String token) {
        this.token = token;
    }
    /**
     * Set Reddit API Token Expiration
     * @param expirationDate Auth token expiration date
     */
    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }
    /**
     * Set Reddit Subreddit
     * @param subreddit Reddit subreddit
     */
    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }


    /**
     * Connect to Reddit API using bottedapp user
     * @throws IOException I/O Exception
     * @throws SQLException SQL Exception
     */
    public void userConnect() throws IOException, SQLException {
        //String dbUrl = System.getenv("JDBC_DATABASE_URL");
        String dbUrl = "jdbc:postgresql://ec2-34-194-158-176.compute-1.amazonaws.com:5432/da2g0o7m136sp5?password=7b04e1735374fcb6ba8f984fdcbcaaf5bada71f4d85df12c0e62cab2ca2b4022&sslmode=require&user=fzbeyehwmqhuxn";
        java.sql.Connection sql = DriverManager.getConnection(dbUrl);
        Statement stmt = sql.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM bot");
        while (rs.next()) {
            this.tokenDb = rs.getString(1);
            this.expirationDateDb = Long.parseLong(rs.getString(2));
        }
        if (Instant.now().getEpochSecond() < expirationDateDb) {
            this.token = tokenDb;
            this.expirationDate = expirationDateDb;
        } else {
            Connection conn = Jsoup.connect(BASE_URL + "/api/v1/access_token").ignoreContentType(true).ignoreHttpErrors(true).method(Connection.Method.POST).userAgent(userAgent);
            conn.data("grant_type", "password");
            conn.data("username", username).data("password", password);
            String combination = clientId + ":" + clientSecret;
            combination = Base64.getEncoder().encodeToString(combination.getBytes());
            conn.header("Authorization", "Basic " + combination);
            Connection.Response res = conn.execute();
            JsonObject object = JsonParser.parseString(res.body()).getAsJsonObject();
            String delToken = "DELETE FROM bot WHERE token='" + this.tokenDb + "'";
            stmt.executeUpdate(delToken);
            this.token = object.get("access_token").getAsString();
            this.expirationDate = object.get("expires_in").getAsInt() + Instant.now().getEpochSecond();
            String stm = "INSERT INTO bot (token, expiration) VALUES ('" + this.token + "', '" + this.expirationDate + "')";
            stmt.executeUpdate(stm);
        }
    }

    /**
     * Get submissions summoning bottedapp
     * @throws IOException I/O Exception
     * @throws InterruptedException Interrupted Exception
     */
    public void getSubmissions() throws IOException, InterruptedException {
        JsonObject posts = (JsonObject) useEndpoint("/r/all/search?q=bottedapp");
        JsonObject data = posts.getAsJsonObject("data");
        JsonArray children = (JsonArray) data.get("children");

        for (JsonElement item : children) {
            JsonObject dat = (JsonObject) item.getAsJsonObject().get("data");
            String author = String.valueOf(dat.getAsJsonObject().get("author")).replace("\"", "");
            String id = String.valueOf(dat.getAsJsonObject().get("id")).replace("\"", "");
            String body = StringEscapeUtils.unescapeJava(String.valueOf(dat.getAsJsonObject().get("selftext")));

            if (!(author == "[deleted]") && body.startsWith("\"!bottedapp ") || body.startsWith("\"u/bottedapp ")) {
                if (scanReplies("/comments/" + id)) {
                    //replied
                } else {
                    getResult(body, id);
                }
            }
        }
    }

    /**
     * Get comments summoning bottedapp
     * @throws IOException I/O Exception
     * @throws InterruptedException Interrupted Exception
     */
    public void getComments() throws IOException, InterruptedException{
        Connection conn = Jsoup.connect("https://api.pushshift.io/reddit/search/comment/?q=bottedapp&after=2h").ignoreContentType(true).ignoreHttpErrors(true);
        Connection.Response res = conn.execute();
        JsonArray object = JsonParser.parseString(res.body()).getAsJsonObject().getAsJsonArray("data");

        for (JsonElement item : object) {
            String parent = String.valueOf(item.getAsJsonObject().get("link_id")).replace("\"","").substring(3);
            String id = String.valueOf(item.getAsJsonObject().get("id")).replace("\"","");
            String body = String.valueOf(item.getAsJsonObject().get("body"));
            String author = String.valueOf(item.getAsJsonObject().get("author")).replace("\"", "");
            if (!(author == "[deleted]") && body.startsWith("\"!bottedapp ") || body.startsWith("\"u/bottedapp ") ) {
                if (scanReplies("/comments/" + parent + ".json?comment=" + id)) {
                    //replied
                } else {
                    getResult(body, id);
                }
            }
        }
    }

    /**
     * get username/comment/submission from reddit comment/post
     * @param body text of post
     * @param id id of post
     * @throws IOException I/O Exception
     * @throws InterruptedException Interrupted Exception
     */
    public void getResult(String body,String id) throws IOException, InterruptedException {
        try {
            String[] split = body.split("bottedapp ");
            String[] input = split[1].replace("\"","").replace("\\","").split(" ");
            String username = new RedditComponent().readInput(input[0]);
            System.out.println(username);
            replyComment(id, new BotAccount(username).BotOrNot((new UserComments(username).getScore()), (new UserSubmissions(username).getScore())));
            System.out.println(new BotAccount(username).BotOrNot((new UserComments(username).getScore()), (new UserSubmissions(username).getScore())));
        } catch (Exception e){
            String result = "Hi! Thank you for summoning me! Hm... my apologies, I am unable to locate the account." +
                    "\nHere is a link to my webpage if you would like a more detailed analysis!" +
                    "\nhttps://botted.app";
            replyComment(id, result);
        }
    }

    /**
     * Scan comment/submission to check if bottedapp has already replied
     * @param endpoint endpoint of Reddit api comment/submission json
     * @return if comment has already been replied to
     */
    public boolean scanReplies(String endpoint) {
        JsonArray comments = (JsonArray) useEndpointArray(endpoint);
        for (JsonElement item : comments) {
            JsonArray data = item.getAsJsonObject().getAsJsonObject("data").getAsJsonArray("children");
            for (JsonElement ite : data) {
                JsonObject dat = ite.getAsJsonObject().getAsJsonObject("data");
                if (dat.has("replies")) {
                    if (dat.get("replies").isJsonPrimitive()) {
                    } else {
                        JsonObject da = dat.getAsJsonObject("replies");
                        JsonArray d = da.getAsJsonObject().getAsJsonObject("data").getAsJsonArray("children");
                        for (JsonElement it : d) {
                            JsonObject p = it.getAsJsonObject().getAsJsonObject("data");
                            String author = String.valueOf(p.getAsJsonObject().get("author"));
                            if (author.equals("\"bottedapp\"") || author.equals("\"[deleted]\"")) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Send reddit endpoint request to reddit api and get Json result
     * @param endpointPath endpoint of comment/post
     * @return JsonObject with reddit api data
     */
    public JsonElement useEndpoint (String endpointPath) {
        try {
            Connection connection = Jsoup.connect(OAUTH_URL + endpointPath);
            connection.header("Authorization", "bearer " + token).ignoreContentType(true).userAgent(userAgent);
            return JsonParser.parseString(connection.execute().body()).getAsJsonObject();
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Send reddit endpoint request to reddit api and get Json result
     * @param endpointPath endpoint of comment/post
     * @return JsonArray with reddit api data
     */
    public JsonElement useEndpointArray(String endpointPath) {
        try {
            Connection connection = Jsoup.connect(OAUTH_URL + endpointPath);
            connection.header("Authorization", "bearer " + token).ignoreContentType(true).userAgent(userAgent);
            return JsonParser.parseString(connection.execute().body()).getAsJsonArray();
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Reply to comment/post with result of whether user is a bot or human
     * @param id is of comment/post
     * @param response response of whether user is bot or human
     * @throws IOException I/O Exception
     */
    public void replyComment(String id, String response) throws IOException {
        Connection connect = Jsoup.connect(OAUTH_URL + "/api/comment").ignoreContentType(true).ignoreHttpErrors(true).postDataCharset("UTF-8")
                .data("api_type", "json")
                .data("text", response)
                .data("thing_id", "t1_" + id);
        connect.header("Authorization", "bearer " + token).userAgent(userAgent).post();
    }
}
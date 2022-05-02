package app.botted;

import com.google.gson.*;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Base64;
import java.io.IOException;

public class BottedRequest {

    private final String BASE_URL = "https://www.reddit.com";
    private final String OAUTH_URL = "https://oauth.reddit.com";
    private final String clientId = "_JW4OUQt7_krXZd420ycuw";
    private final String clientSecret = "BJFz2IB-EMvu_ye3EZ66oOcoDzWgwg";
    private final String userAgent = "botted 0.0.1";
    private final String username = "bottedapp";
    private final String password = "mc3.edu!";
    private String token;
    private long expirationDate;
    protected String subreddit;
    RedditComponent reddit;

    public BottedRequest() throws IOException, InterruptedException, SQLException {
        reddit = new RedditComponent();
        userConnect();
        getComments();
        getSubmissions();
    }

    //getters

    public String getToken() {
        return token;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public String getSubreddit() {
        return subreddit;
    }

    //setters

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    /**
     *
     * @throws IOException
     */
    public void userConnect() throws IOException, InterruptedException {
        Connection conn = Jsoup.connect(BASE_URL + "/api/v1/access_token").ignoreContentType(true).ignoreHttpErrors(true).method(Connection.Method.POST).userAgent(userAgent);
        conn.data("grant_type", "password");
        conn.data("username", username).data("password", password);
        String combination = clientId + ":" + clientSecret;
        combination = Base64.getEncoder().encodeToString(combination.getBytes());
        conn.header("Authorization", "Basic " + combination);

        Connection.Response res = conn.execute();
        JsonObject object = JsonParser.parseString(res.body()).getAsJsonObject();
        this.token = object.get("access_token").getAsString();
        this.expirationDate = object.get("expires_in").getAsInt() + Instant.now().getEpochSecond();
    }

    /**
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void getSubmissions() throws IOException, InterruptedException, SQLException {
        JsonObject posts = (JsonObject) useEndpoint("/r/all/search?q=bottedapp");
        JsonObject data = posts.getAsJsonObject("data");
        JsonArray children = (JsonArray) data.get("children");

        for (JsonElement item : children) {
            JsonObject dat = (JsonObject) item.getAsJsonObject().get("data");
            String author = String.valueOf(dat.getAsJsonObject().get("author")).replace("\"","");
            String id = String.valueOf(dat.getAsJsonObject().get("id")).replace("\"","");
            String body = StringEscapeUtils.unescapeJava(String.valueOf(dat.getAsJsonObject().get("selftext")));

            if (body.startsWith("\"!bottedapp ") || body.startsWith("\"u/bottedapp ") ) {
                if (scanReplies("/comments/" + id)) {
                    //replied
                } else {
                    getResult(body, id);
                }
            }
        }
    }
    public void getComments() throws IOException, InterruptedException, SQLException {
        Connection conn = Jsoup.connect("https://api.pushshift.io/reddit/search/comment/?q=bottedapp").ignoreContentType(true).ignoreHttpErrors(true);
        Connection.Response res = conn.execute();
        JsonArray object = JsonParser.parseString(res.body()).getAsJsonObject().getAsJsonArray("data");

        for (JsonElement item : object) {
            String parent = String.valueOf(item.getAsJsonObject().get("parent_id")).replace("\"","").substring(3);
            String id = String.valueOf(item.getAsJsonObject().get("id")).replace("\"","");
            String body = String.valueOf(item.getAsJsonObject().get("body"));
            if (body.startsWith("\"!bottedapp ") || body.startsWith("\"u/bottedapp ") ) {
                if (scanReplies("/comments/" + parent + ".json?comment=" + id)) {
                    //replied
                } else {
                    getResult(body, id);
                }
            }
        }
    }
    public void getResult(String body,String id) throws IOException, InterruptedException, SQLException {
        try {
            String[] split = {"",""};
            split = body.split("bottedapp ");
            String[] input = {"",""};
            input = split[1].replace("\"","").replace("\\","").split(" ");
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

    public boolean scanReplies(String endpoint) throws IOException, InterruptedException {
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
                            if (author.equals("\"bottedapp\"")) {
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
         *
         * @param endpointPath
         * @return
         * @throws IOException
         * @throws InterruptedException
         */
        public JsonElement useEndpoint (String endpointPath) throws IOException, InterruptedException {
            Connection connection = Jsoup.connect(OAUTH_URL + endpointPath);
            connection.header("Authorization", "bearer " + token).ignoreContentType(true).userAgent(userAgent);
            return JsonParser.parseString(connection.execute().body()).getAsJsonObject();
        }

        public JsonElement useEndpointArray(String endpointPath) throws IOException, InterruptedException {
            Connection connection = Jsoup.connect(OAUTH_URL + endpointPath);
            connection.header("Authorization", "bearer " + token).ignoreContentType(true).userAgent(userAgent);
            return JsonParser.parseString(connection.execute().body()).getAsJsonArray();
        }

        public void replyComment(String id, String respon) throws IOException, InterruptedException {
            Connection connect = Jsoup.connect(OAUTH_URL + "/api/comment").ignoreContentType(true).ignoreHttpErrors(true).postDataCharset("UTF-8")
                    .data("api_type", "json")
                    .data("text", respon)
                    .data("thing_id", "t1_" + id);
            connect.header("Authorization", "bearer " + token).userAgent(userAgent).post();
        }

        public String userComments (String comment){
            //send request to reddit backend for comments
            return comment;
        }

        public String getBody (String comment){
            //send request to reddit backend for comment contents
            return userComments(comment);
        }

        public static void reply (Object responses){
            //send reply to reddit backend
        }
    }
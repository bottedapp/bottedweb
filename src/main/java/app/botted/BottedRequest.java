package app.botted;

import com.google.gson.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
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

    public static void main(String[] args) throws IOException, InterruptedException {
        BottedRequest r = new BottedRequest();
        r.userConnect();
        r.getComments();
        r.getSubmissions();
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
        System.out.println(res.body());
    }

    /**
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void getSubmissions() throws IOException, InterruptedException {
        JsonObject posts = (JsonObject) useEndpoint("/r/all/search?q=bottedapp");
        JsonObject data = posts.getAsJsonObject("data");
        JsonArray children = (JsonArray) data.get("children");

        for (JsonElement item : children) {
            JsonObject dat = (JsonObject) item.getAsJsonObject().get("data");
            String id = String.valueOf(dat.getAsJsonObject().get("id")).replace("\"","");
            System.out.println(id);
            if (scanReplies("/comments/" + id + ".json")) {
                System.out.println("replied");
            } else {
                System.out.println("not replied");
                //get username ex: bottedapp <username>
                //see if is a bot
                // reply to comment with result
            }
        }
    }
    public void getComments() throws IOException, InterruptedException {
        Connection conn = Jsoup.connect("https://api.pushshift.io/reddit/search/comment/?q=bottedapp").ignoreContentType(true).ignoreHttpErrors(true);
        Connection.Response res = conn.execute();
        JsonArray object = JsonParser.parseString(res.body()).getAsJsonObject().getAsJsonArray("data");

        for (JsonElement item : object) {
            String parent = String.valueOf(item.getAsJsonObject().get("parent_id")).replace("\"","").substring(3);
            String id = String.valueOf(item.getAsJsonObject().get("id")).replace("\"","");
            String body = String.valueOf(item.getAsJsonObject().get("body"));
            System.out.println(parent + " " + id + " " + body);
            if (scanReplies("/comments/" + parent + ".json?comment=" + id)) {
                System.out.println("replied");
            } else {
                System.out.println("not replied");
                //get username ex: bottedapp <username>
                //see if is a bot
                // reply to comment with result
            }
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

        public void replyComment() throws IOException, InterruptedException {
            System.out.println("a");
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
        public void commentConnect () throws IOException {
            Connection connect = Jsoup.connect(OAUTH_URL + "/api/comment").ignoreContentType(true).ignoreHttpErrors(true).postDataCharset("UTF-8")
                    .data("api_type", "json")
                    .data("text", "test")
                    .data("thing_id", "t1_i5kycps");
            connect.header("Authorization", "bearer " + token).userAgent(userAgent).post();
        }
    }
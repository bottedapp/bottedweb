package app.botted;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.text.StringEscapeUtils;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.sql.SQLException;

public class UserComments extends UserAccount {

    /**
     * Private variables
     */
    private List commentSubreddits;
    private Map<String, String> commentMap;
    private Map<String, List<Object>> userComments;

    /**
     * Default constructor
     * @throws IOException
     * @throws InterruptedException
     */
    public UserComments() throws IOException, InterruptedException {
        super();
    }

    /**
     * Constructor with parameters
     * @param user The username
     * @throws IOException
     * @throws InterruptedException
     */
    public UserComments(String user) throws IOException, InterruptedException, SQLException {
        this.user = user;
        analyze();
    }

    /**
     * Constructor
     * @param user The username
     * @param commentSubreddits Subreddits commented in
     * @param commentSubreddits 25 latest comments by user
     * @param userComments Map of all user comment data
     * @throws IOException
     * @throws InterruptedException
     */
    public UserComments(String user, List commentSubreddits, Map<String, String> commentMap, Map<String, List<Object>> userComments) throws IOException, InterruptedException, SQLException {
        super(user);
        this.commentSubreddits = commentSubreddits;
        this.commentMap = commentMap;
        this.userComments = userComments;
    }

    /**
     * Getters
     */

    public List getCommentSubreddits() {
        return commentSubreddits;
    }

    public Map<String, String> getCommentMap() { return commentMap; }

    public Map<String, List<Object>> getUserComments() { return userComments; }

    /**
     * Setters
     */

    public void setCommentSubreddits(List commentSubreddits) {
        this.commentSubreddits = commentSubreddits;
    }

    public void setCommentMap(Map<String, String> commentMap) { this.commentMap = commentMap; }

    public void setUserComments(Map<String, List<Object>> userComments) { this.userComments = userComments; }


    /**
     * Retrieves and assigns all comments data from Reddit API
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void analyze() throws IOException, InterruptedException, SQLException {
        commentSubreddits = new ArrayList<>();
        commentMap = new LinkedHashMap<>();
        userComments = new LinkedHashMap<>();

        JsonArray comments = useEndpoint("/user/" + user + "/comments").getAsJsonObject("data").getAsJsonArray("children");

        for (JsonElement item : comments) {
            JsonObject dat = (JsonObject) item.getAsJsonObject().get("data");
            String id = String.valueOf(dat.getAsJsonObject().get("id"));
            String subreddit = String.valueOf(dat.getAsJsonObject().get("subreddit_name_prefixed"));
            String body = String.valueOf(dat.getAsJsonObject().get("body"));
            String author = String.valueOf(dat.getAsJsonObject().get("link_author"));
            String permalink = String.valueOf(dat.getAsJsonObject().get("permalink"));
            String title = StringEscapeUtils.unescapeJava(String.valueOf(dat.getAsJsonObject().get("link_title")));
            int ups = Integer.valueOf(String.valueOf(dat.getAsJsonObject().get("ups")));
            int downs = Integer.valueOf(String.valueOf(dat.getAsJsonObject().get("downs")));
            boolean nsfw = Boolean.valueOf(String.valueOf(dat.getAsJsonObject().get("over_18")));
            long utc = Long.parseLong(String.valueOf(dat.get("created").getAsInt()));
            Date date = new Date(utc * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("M/dd/Y h:mm:ss a");
            String created = sdf.format(date);

            commentSubreddits.add(subreddit.substring(1,subreddit.length()-1));
            commentMap.put(id, body.substring(1,body.length()-1));

            List commentsArray = new ArrayList<>();
            commentsArray.add(body.substring(1,body.length()-1)); // [0]
            commentsArray.add(title.substring(1,title.length()-1)); // [1]
            commentsArray.add(subreddit.substring(1,subreddit.length()-1)); // [2]
            commentsArray.add(ups); // [3]
            commentsArray.add(downs); // [4]
            commentsArray.add(nsfw); // [5]
            commentsArray.add(created); // [6]
            commentsArray.add(permalink.substring(1, permalink.length()-1)); // [7]
            commentsArray.add(author.substring(1,author.length()-1)); // [8]
            userComments.put(id, commentsArray);
        }
    }

    /**
     * Get list of user comments
     * @param userComments Map of all user comments data
     * @return List of all user comments
     */
    public String commentsList(Map<String, List<Object>> userComments) {
        String commentList = "<table style=\"width:100%;max-width:100%;display:block;word-wrap:break-word;\"><tbody style=\"width: 100%;max-width: 100%;display: block;word-wrap: break-word;\">";

        for (Map.Entry<String, List<Object>> comment : userComments.entrySet()) {
            commentList += "<tr style=\"display:block; border-bottom: #363636 solid 15px;\"\">" +
                    "<td style=\"background:#1A1A1B;width: 100%;max-width: 100%;display:block;word-wrap: break-word;color:#d7dadc;border: #d7dadc solid 1px;\">" +
                    "<strong>" + userComments.get(comment.getKey()).get(1) + "</strong> by <a href=\"http://reddit.com/user/" + userComments.get(comment.getKey()).get(8) + "\" target=\"_blank\">u/" + userComments.get(comment.getKey()).get(8) + "</a><br><br>" +
                    StringEscapeUtils.unescapeJava((String) userComments.get(comment.getKey()).get(0)).replace("\n", "<br>").replace("\\", "") + "<br><br>" +
                    "upvotes: " + userComments.get(comment.getKey()).get(3) + " | downvotes: " + userComments.get(comment.getKey()).get(4) + " | nsfw: " + userComments.get(comment.getKey()).get(5) + "<br>" +
                    "<a href=\"https://reddit.com" + userComments.get(comment.getKey()).get(2) + "\" target=\"_blank\">" + userComments.get(comment.getKey()).get(2) + "</a> | " + userComments.get(comment.getKey()).get(6) + " | <a href=\"https://reddit.com" + userComments.get(comment.getKey()).get(7) + "\" target=\"_blank\">permalink</a></td></tr>";
        }
        commentList += "</tbody></table>";
        return commentList;
    }

    /**
     * Get comments similarity score from compareScore method
     * @return Comment Score
     */
    public double getScore() {
        return compareScore(commentMap);
    }

    /**
     * Send results to string
     * @return All data of analyzed comments
     */
    @Override
    public String toString() {
        return "<h4 style=\"font-family:system-ui;color:#d7dadc;\">Comments</h4><span style=\"font-family:system-ui;color:#eb5528;\">" +
                "<span style=\"color:#d7dadc;\">comment score: </span>" + compareScore(commentMap) + "<br>" +
                "<span style=\"color:#d7dadc;\">comments compared: </span>" + commentMap.size() + "<br>" +
                "<span style=\"color:#d7dadc;\">popular subreddit: </span><a href=\"https://reddit.com/" + popularSubreddit(commentSubreddits) + "\" target=\"_blank\">" + popularSubreddit(commentSubreddits) + "</a><br>" +
                "<span style=\"color:#d7dadc;\">popular subreddit count: </span>" + popularSubredditCount(commentSubreddits) + "<br>" +
                "<span style=\"color:#d7dadc;\">comment upvotes: </span>" + upvotes(userComments) + "<br>" +
                "<span style=\"color:#d7dadc;\">comment downvotes: </span>" + downvotes(userComments) + "<br>" +
                "<span style=\"color:#d7dadc;\">comment subreddits: </span>" + subredditsList(userComments) + "<br>" +
                "<span style=\"color:#d7dadc;\">comments: </span>" + commentsList(userComments) + "</span>";
    }
}
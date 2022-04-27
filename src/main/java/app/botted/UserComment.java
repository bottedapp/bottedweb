package app.botted;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserComment extends UserAccount {

    /**
     * Private variables
     */
    private ArrayList commentSubreddits = new ArrayList<>();
    Map<String, String> commentMap = new LinkedHashMap<>();
    Map<String, List<Object>> userComments = new HashMap<>();

    /**
     * Default constructor
     * @throws IOException
     * @throws InterruptedException
     */
    public UserComment() throws IOException, InterruptedException {
        super();
    }

    /**
     * Constructor with parameters
     * @param user The username
     * @throws IOException
     * @throws InterruptedException
     */
    public UserComment(String user) throws IOException, InterruptedException {
        super(user);
        analyzeComments();
    }

    /**
     * Constructor
     * @param subreddit The specific subreddit
     * @param name The name associated with the account
     * @param id The account ID
     * @param user The username
     * @param verified If account is verified
     * @param has_verified_email If E-mail has been verified on account
     * @param is_gold If account has gold status
     * @param is_mod Is a moderator
     * @param is_employee Is an employee
     * @param awardee_karma Karma gained from awardee
     * @param awarder_karma Karma gained from awarder
     * @param link_karma Karma gained from link
     * @param comment_karma Karma gained from comment
     * @param total_karma Total karma on account
     * @param created When the account was created
     * @param comment A comment on a post
     * @param upvote An upvote on a post or comment
     * @param downvote A downvote on a post or comment
     * @param commentTotalScore Total karma from comments
     * @param popularCommentSubreddit Most popular comment in subreddit
     * @param commentSubredditCount How many comments in particular subreddit
     * @param commentSubreddits Subreddits commented in
     * @throws IOException
     * @throws InterruptedException
     */
    public UserComment(String subreddit, String name, String id, String user, Boolean verified, Boolean has_verified_email, Boolean is_gold, Boolean is_mod, Boolean is_employee, int awardee_karma, int awarder_karma, int link_karma, int comment_karma, int total_karma, Date created, String comment, boolean upvote, boolean downvote, double commentTotalScore, String popularCommentSubreddit, int commentSubredditCount, ArrayList commentSubreddits) throws IOException, InterruptedException {
        super(subreddit, name, id, user, verified, has_verified_email, is_gold, is_mod, is_employee, awardee_karma, awarder_karma, link_karma, comment_karma, total_karma, created, comment, upvote, downvote);
        this.commentSubreddits = commentSubreddits;
    }

    //getters

    public ArrayList getCommentSubreddits() {
        return commentSubreddits;
    }

    //setters

    public void setCommentSubreddits(ArrayList commentSubreddits) {
        this.commentSubreddits = commentSubreddits;
    }

    /**
     * Finds most active subreddit user comments in
     * Calculates similarities between comments
     * @throws IOException
     * @throws InterruptedException
     */
    public void analyzeComments() throws IOException, InterruptedException {
        JsonObject comments = useEndpoint("/user/" + user + "/comments");
        JsonObject data = comments.getAsJsonObject("data");
        JsonArray children = data.getAsJsonArray("children");

        for (JsonElement item : children) {
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
            SimpleDateFormat sdf = new SimpleDateFormat("M/dd/Y h:mm:ss a");
            Date created = new Date(utc * 1000);
            String date = sdf.format(created);

            commentSubreddits.add(subreddit.substring(1,subreddit.length()-1));
            commentMap.put(id, body.substring(1,body.length()-1));

            List commentsArray = new ArrayList<>();
            commentsArray.add(body.substring(1,body.length()-1)); // [0]
            commentsArray.add(title.substring(1,title.length()-1)); // [1]
            commentsArray.add(subreddit.substring(1,subreddit.length()-1)); // [2]
            commentsArray.add(ups); // [3]
            commentsArray.add(downs); // [4]
            commentsArray.add(nsfw); // [5]
            commentsArray.add(date); // [6]
            commentsArray.add(permalink.substring(1,subreddit.length()-1)); // [7]
            commentsArray.add(author.substring(1,author.length()-1)); // [8]
            userComments.put(id, commentsArray);
        }
    }

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

    public double getScore() {
        return compareScore(commentMap);
    }

    /**
     * Send results to string
     * @return commentTotalScore, popularCommentSubreddit, commentSubredditCount, and commentSubreddits
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
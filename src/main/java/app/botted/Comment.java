package app.botted;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.*;

public class Comment extends User {

    /**
     * Private variables
     */
    private double commentTotalScore;
    private String popularCommentSubreddit = "";
    private int commentSubredditCount = 0;
    private ArrayList commentSubreddits = new ArrayList<>();
    private String commentSubs = "";

    /**
     * Default constructor
     * @throws IOException
     * @throws InterruptedException
     */
    public Comment() throws IOException, InterruptedException {
        super();
    }

    /**
     * Constructor with parameters
     * @param user The username
     * @throws IOException
     * @throws InterruptedException
     */
    public Comment(String user) throws IOException, InterruptedException {
        super(user);
        analyzeComment();
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
    public Comment(String subreddit, String name, String id, String user, Boolean verified, Boolean has_verified_email, Boolean is_gold, Boolean is_mod, Boolean is_employee, int awardee_karma, int awarder_karma, int link_karma, int comment_karma, int total_karma, Date created, String comment, boolean upvote, boolean downvote, double commentTotalScore, String popularCommentSubreddit, int commentSubredditCount, ArrayList commentSubreddits) throws IOException, InterruptedException {
        super(subreddit, name, id, user, verified, has_verified_email, is_gold, is_mod, is_employee, awardee_karma, awarder_karma, link_karma, comment_karma, total_karma, created, comment, upvote, downvote);
        this.commentTotalScore = commentTotalScore;
        this.popularCommentSubreddit = popularCommentSubreddit;
        this.commentSubredditCount = commentSubredditCount;
        this.commentSubreddits = commentSubreddits;
    }

    //getters

    public double getCommentTotalScore() {
        return commentTotalScore;
    }

    public String getPopularCommentSubreddit() {
        return popularCommentSubreddit;
    }

    public int getCommentSubredditCount() {
        return commentSubredditCount;
    }

    public ArrayList getCommentSubreddits() {
        return commentSubreddits;
    }

    //setters

    public void setCommentTotalScore(double commentTotalScore) {
        this.commentTotalScore = commentTotalScore;
    }

    public void setPopularCommentSubreddit(String popularCommentSubreddit) {
        this.popularCommentSubreddit = popularCommentSubreddit;
    }

    public void setCommentSubredditCount(int commentSubredditCount) {
        this.commentSubredditCount = commentSubredditCount;
    }

    public void setCommentSubreddits(ArrayList commentSubreddits) {
        this.commentSubreddits = commentSubreddits;
    }

    /**
     * Finds most active subreddit user comments in
     * Calculates similarities between comments
     * @throws IOException
     * @throws InterruptedException
     */
    public void analyzeComment() throws IOException, InterruptedException {
        JsonObject comments = useEndpoint("/user/" + user + "/comments");
        JsonObject data = comments.getAsJsonObject("data");
        JsonArray children = data.getAsJsonArray("children");

        Map<String, String> commentMap = new LinkedHashMap<>();
        for (JsonElement item : children) {
            JsonObject dat = (JsonObject) item.getAsJsonObject().get("data");
            String id = String.valueOf(dat.getAsJsonObject().get("id"));
            String body = String.valueOf(dat.getAsJsonObject().get("body"));
            commentMap.put(id, body);
        }

        if (commentMap.size() <= 1) {
            //do nothing
        } else {
            for (JsonElement item : children) {
                JsonObject dat = (JsonObject) item.getAsJsonObject().get("data");
                String subreddit = String.valueOf(dat.getAsJsonObject().get("subreddit_name_prefixed"));
                commentSubreddits.add(subreddit);
            }

            for (Object commentSubreddit : commentSubreddits) {
                if (Collections.frequency(commentSubreddits, commentSubreddit) > commentSubredditCount) {
                    commentSubredditCount = Collections.frequency(commentSubreddits, commentSubreddit);
                    popularCommentSubreddit = (String) commentSubreddit;
                }
            }
        }

        if (commentMap.size() <= 1) {
            //do nothing
        } else {
            double commentScore = 0;
            int commentScoreCount = 0;
            for (Map.Entry<String, String> comm : commentMap.entrySet()) {
                for (Map.Entry<String, String> comment : commentMap.entrySet()) {
                    if (Objects.equals(comment.getKey(), comm.getKey())) {
                        // do nothing
                    } else {
                        commentScore += (findSimilarity(comment.getValue(), comm.getValue()));
                        commentScoreCount++;
                    }
                }
            }
            commentTotalScore = commentScore / commentScoreCount;
        }
    }

    public void commentSubredditsList() {
        for (Object comment : commentSubreddits)
            commentSubs += comment.toString().replace("\"","") + ", ";
    }

    /**
     * Send results to string
     * @return commentTotalScore, popularCommentSubreddit, commentSubredditCount, and commentSubreddits
     */
    @Override
    public String toString() {
        commentSubredditsList();
        return "<h4 style=\"font-family:system-ui;color:#d7dadc;\">Comments</h4><span style=\"font-family:system-ui;color:#eb5528;\">" +
                "<span style=\"color:#d7dadc;\">comment score: </span>" + commentTotalScore + "<br>" +
                "<span style=\"color:#d7dadc;\">comments compared: </span>" + commentSubreddits.size() + "<br>" +
                "<span style=\"color:#d7dadc;\">popular subreddit: </span>" + popularCommentSubreddit.replace("\"","") + "<br>" +
                "<span style=\"color:#d7dadc;\">popular subreddit count: </span>" + commentSubredditCount + "<br>" +
                "<span style=\"color:#d7dadc;\">comment subreddits: </span>" + commentSubs + "</spam>";
    }
}
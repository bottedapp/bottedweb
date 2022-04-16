package app.botted;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.*;

public class Comment extends User {

    private double commentTotalScore;
    private String popularCommentSubreddit = "";
    private int commentSubredditCount = 0;
    private ArrayList commentSubreddits = new ArrayList<>();
    private String commentSubs = "";

    public Comment() throws IOException, InterruptedException {
        super();
    }

    public Comment(String user) throws IOException, InterruptedException {
        super(user);
        analyzeComment();
    }

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
        //find most active subreddit user comments in
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
        //calculate similarities between comments
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
    public void commentSubredditss() {
        for (Object comment : commentSubreddits)
            commentSubs += comment.toString().replace("\"","") + ", ";
    }
    @Override
    public String toString() {
        commentSubredditss();
        return "<h3 style=\"font-family:system-ui;\";>Comments</h3><h4 style=\"font-family:system-ui;color:#fccfa6;\">" +
                "<span style=\"color:#000000;\">comment score: </span>" + commentTotalScore + "<br>" +
                "<span style=\"color:#000000;\">comments compared: </span>" + commentSubreddits.size() + "<br>" +
                "<span style=\"color:#000000;\">popular subreddit: </span>" + popularCommentSubreddit.replace("\"","") + "<br>" +
                "<span style=\"color:#000000;\">popular subreddit count: </span>" + commentSubredditCount + "<br>" +
                "<span style=\"color:#000000;\">comment subreddits: </span>" + commentSubs + "</h4>";
    }
}
package app.botted;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Comment extends User {

    /**
     * Private variables
     */
    private double commentTotalScore;
    private String popularCommentSubreddit = "";
    private String commentSubs = "";
    private String commentList = "";
    private int commentSubredditCount = 0;
    private int upvotes, downvotes;
    private final SimpleDateFormat sdf = new SimpleDateFormat("M/dd/Y h:mm:ss a");
    private ArrayList commentSubreddits = new ArrayList<>();
    Map<String, String> commentMap = new LinkedHashMap<>();
    Map<String, String> linkMap = new LinkedHashMap<>();
    Map<String, String> titleMap = new LinkedHashMap<>();
    Map<String, String> createdMap = new LinkedHashMap<>();
    Map<String, String> subredditMap = new LinkedHashMap<>();
    Map<String, String> authorMap = new LinkedHashMap<>();
    Map<String, Integer> upvotesMap = new LinkedHashMap<>();
    Map<String, Integer> downvotesMap = new LinkedHashMap<>();
    Map<String, Boolean> isOriginalMap = new LinkedHashMap<>();
    Map<String, Boolean> nsfwMap = new LinkedHashMap<>();
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

    public void setPopularCommentSubreddit(String popularCommentSubreddit) { this.popularCommentSubreddit = popularCommentSubreddit; }

    public void setCommentSubredditCount(int commentSubredditCount) { this.commentSubredditCount = commentSubredditCount; }

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

        for (JsonElement item : children) {
            JsonObject dat = (JsonObject) item.getAsJsonObject().get("data");
            String id = String.valueOf(dat.getAsJsonObject().get("id"));
            String subreddit = String.valueOf(dat.getAsJsonObject().get("subreddit_name_prefixed"));
            String body = String.valueOf(dat.getAsJsonObject().get("body"));
            String author = String.valueOf(dat.getAsJsonObject().get("link_author"));
            String permalink = String.valueOf(dat.getAsJsonObject().get("permalink"));
            String title = StringEscapeUtils.unescapeJava(String.valueOf(dat.getAsJsonObject().get("link_title")));
            long utc = Long.parseLong(String.valueOf(dat.get("created").getAsInt()));
            int ups = Integer.valueOf(String.valueOf(dat.getAsJsonObject().get("ups")));
            int downs = Integer.valueOf(String.valueOf(dat.getAsJsonObject().get("downs")));
            boolean isOriginal = Boolean.valueOf(String.valueOf(dat.getAsJsonObject().get("is_original_content")));
            boolean nsfw = Boolean.valueOf(String.valueOf(dat.getAsJsonObject().get("over_18")));

            created = new Date(utc * 1000);
            String date = sdf.format(created);
            commentSubreddits.add(subreddit);
            commentMap.put(id, body.substring(1,body.length()-1));
            linkMap.put(id, permalink);
            titleMap.put(id, title.substring(1,title.length()-1));
            createdMap.put(id, date);
            subredditMap.put(id, subreddit.substring(1,subreddit.length()-1));
            authorMap.put(id, author.substring(1,author.length()-1));
            upvotesMap.put(id, ups);
            downvotesMap.put(id, downs);
            isOriginalMap.put(id, isOriginal);
            nsfwMap.put(id, nsfw);
            upvotes += ups;
            downvotes += downs;
        }

        if (commentMap.size() < 1) {
            //do nothing
        } else {
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
        int i = 1;
        for (Object subreddit : commentSubreddits) {
            if (i == commentSubreddits.size()) {
                commentSubs += "<a href=\"http://reddit.com/" + subreddit.toString().replace("\"", "") + "\" target=\"_blank\">" + subreddit.toString().replace("\"", "") + "</a>";
            } else {
                commentSubs += "<a href=\"http://reddit.com/" + subreddit.toString().replace("\"", "") + "\" target=\"_blank\">" + subreddit.toString().replace("\"", "") + "</a>, ";
            }
            i++;
        }
    }

    public void commentsList() {
        commentList = "<table style=\"width:100%;max-width:100%;display:block;word-wrap:break-word;\"><tbody style=\"width: 100%;max-width: 100%;display: block;word-wrap: break-word;\">";
        for (Map.Entry<String, String> comment : commentMap.entrySet()) {
            commentList += "<tr style=\"display:block; border-bottom: #363636 solid 15px;\"\">" +
                    "<td style=\"background:#1A1A1B;width: 100%;max-width: 100%;display:block;word-wrap: break-word;color:#d7dadc;border: #d7dadc solid 1px;\">" +
                    "<strong>" + titleMap.get(comment.getKey()) + "</strong> by <a href=\"http://reddit.com/user/" + authorMap.get(comment.getKey()) + "\" target=\"_blank\">u/" + authorMap.get(comment.getKey()) + "</a><br><br>" +
                    StringEscapeUtils.unescapeJava(comment.getValue()).replace("\n", "<br>").replace("\\", "") + "<br><br>" +
                    "upvotes: " + upvotesMap.get(comment.getKey()) + " | downvotes: " + downvotesMap.get(comment.getKey()) + " | nsfw: " + nsfwMap.get(comment.getKey()) + "<br>" +
                    "<a href=\"https://reddit.com" + subredditMap.get(comment.getKey()) + "\" target=\"_blank\">" + subredditMap.get(comment.getKey()) + "</a> | " + createdMap.get(comment.getKey()) + " | <a href=\"https://reddit.com" + linkMap.get(comment.getKey()).replace("\"", "") + "\" target=\"_blank\">permalink</a></td></tr>";
        }
        commentList += "</tbody></table>";
    }

    /**
     * Send results to string
     * @return commentTotalScore, popularCommentSubreddit, commentSubredditCount, and commentSubreddits
     */
    @Override
    public String toString() {
        commentSubredditsList();
        commentsList();
        return "<h4 style=\"font-family:system-ui;color:#d7dadc;\">Comments</h4><span style=\"font-family:system-ui;color:#eb5528;\">" +
                "<span style=\"color:#d7dadc;\">comment score: </span>" + commentTotalScore + "<br>" +
                "<span style=\"color:#d7dadc;\">comments compared: </span>" + commentSubreddits.size() + "<br>" +
                "<span style=\"color:#d7dadc;\">popular subreddit: </span><a href=\"https://reddit.com/" + popularCommentSubreddit.replace("\"", "") + "\" target=\"_blank\">" + popularCommentSubreddit.replace("\"", "") + "</a><br>" +
                "<span style=\"color:#d7dadc;\">popular subreddit count: </span>" + commentSubredditCount + "<br>" +
                "<span style=\"color:#d7dadc;\">comment upvotes: </span>" + upvotes + "<br>" +
                "<span style=\"color:#d7dadc;\">comment downvotes: </span>" + downvotes + "<br>" +
                "<span style=\"color:#d7dadc;\">comment subreddits: </span>" + commentSubs + "<br>" +
                "<span style=\"color:#d7dadc;\">comments: </span>" + commentList + "</span>";
    }
}
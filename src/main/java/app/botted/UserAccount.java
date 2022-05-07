package app.botted;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.sql.SQLException;
/**
 * User Account class
 */
public class UserAccount extends RedditComponent {

    /**
     * Protected variables
     */

    /**
     * The name associated with the account
     */
    protected String name;
    /**
     * The account ID
     */
    protected String id;
    /**
     * The username
     */
    protected String user;
    /**
     * Avatar of user
     */
    protected String icon;
    /**
     * Url of reddit profile
     */
    protected String url;
    /**
     * Users description
     */
    protected String description;
    /**
     * Date account was created
     */
    protected String created;
    /**
     * Comments/submissions
     */
    protected String post;
    /**
     * If account is verified
     */
    protected boolean verified;
    /**
     * If E-mail has been verified on account
     */
    protected boolean has_verified_email;
    /**
     * If account has gold status
     */
    protected boolean is_gold;
    /**
     * Is user a moderator
     */
    protected boolean is_mod;
    /**
     * Is user an employee
     */
    protected boolean is_employee;
    /**
     * An upvote on a post or comment
     */
    protected boolean upvote;
    /**
     * A downvote on a post or comment
     */
    protected boolean downvote;
    /**
     * Karma gained from awardee
     */
    protected int awardee_karma;
    /**
     * arma gained from awarder
     */
    protected int awarder_karma;
    /**
     * Karma gained from link
     */
    protected int link_karma;
    /**
     * Karma gained from comment
     */
    protected int comment_karma;
    /**
     * Total karma on account
     */
    protected int total_karma;

    /**
     * Default constructor
     */
    public UserAccount() {
        super();
    }

    /**
     * Constructor with user parameter
     * @param user The username
     * @throws IOException I/O Exception
     * @throws InterruptedException Interrupted Exception
     * @throws SQLException SQL Exception
     */
    public UserAccount(String user) throws IOException, InterruptedException, SQLException {
        this.user = user;
        analyze();
    }

    /**
     * Constructor for human user
     * @param subreddit The specific subreddit
     * @param comment A comment on a post
     * @param upvote An upvote on a post or comment
     * @param downvote A downvote on a post or comment
     */
    public UserAccount(String subreddit, String comment, boolean upvote, boolean downvote) { // human
        super();
        this.post = comment;
        this.upvote = upvote;
        this.downvote = downvote;
    }

    /**
     * Constructor with parameters
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
     */
    public UserAccount(String subreddit, String name, String id, String user, boolean verified, boolean has_verified_email, boolean is_gold, boolean is_mod, boolean is_employee, int awardee_karma, int awarder_karma, int link_karma, int comment_karma, int total_karma, String created, String comment, boolean upvote, boolean downvote) {
        super(subreddit);
        this.name = name;
        this.id = id;
        this.user = user;
        this.verified = verified;
        this.has_verified_email = has_verified_email;
        this.is_gold = is_gold;
        this.is_mod = is_mod;
        this.is_employee = is_employee;
        this.awardee_karma = awardee_karma;
        this.awarder_karma = awarder_karma;
        this.link_karma = link_karma;
        this.comment_karma = comment_karma;
        this.total_karma = total_karma;
        this.created = created;
        this.post = comment;
        this.upvote = upvote;
        this.downvote = downvote;
    }

    /**
     * Getters
     */

    /**
     * Get The name associated with the account
     * @return The name associated with the account
     */
    public String getName() {
        return name;
    }
    /**
     * Get the account ID
     * @return The account ID
     */
    public String getId() {
        return id;
    }
    /**
     * Get the username
     * @return The username
     */
    public String getUser() {
        return user;
    }
    /**
     * Check if account is verified
     * @return If account is verified
     */
    public boolean getVerified() {
        return verified;
    }
    /**
     * Check if e-mail has been verified on account
     * @return If e-mail has been verified on account
     */
    public boolean getHas_verified_email() {
        return has_verified_email;
    }
    /**
     * Check if account has gold status
     * @return If account has gold status
     */
    public boolean getIs_gold() {
        return is_gold;
    }
    /**
     * Check if user is a moderator
     * @return If user is a moderator
     */
    public boolean getIs_mod() {
        return is_mod;
    }
    /**
     * Check if user is an employee
     * @return If user is an employee
     */
    public boolean getIs_employee() {
        return is_employee;
    }
    /**
     * Get karma gained from awardee
     * @return Karma gained from awardee
     */
    public int getAwardee_karma() {
        return awardee_karma;
    }
    /**
     * Get karma gained from awardee
     * @return Karma gained from awardee
     */
    public int getAwarder_karma() {
        return awarder_karma;
    }
    /**
     * Get karma gained from link
     * @return Karma gained from link
     */
    public int getLink_karma() {
        return link_karma;
    }
    /**
     * Get karma gained from post
     * @return Karma gained from post
     */
    public int getComment_karma() {
        return comment_karma;
    }
    /**
     * Get total karma on account
     * @return Total karma on account
     */
    public int getTotal_karma() {
        return total_karma;
    }
    /**
     * Get date account was created
     * @return Date account was created
     */
    public String getCreated() {
        return created;
    }
    /**
     * Get a comment on a post
     * @return comment on a post
     */
    public String getComment() {
        return post;
    }
    /**
     * Upvote on a post or comment
     * @return Upvote on a post or comment
     */
    public boolean isUpvote() {
        return upvote;
    }
    /**
     * Downvote on a post or comment
     * @return Downvote on a post or comment
     */
    public boolean isDownvote() {
        return downvote;
    }

    /**
     * Setters
     */

    /**
     * Set the name associated with the account
     * @param name The name associated with the account
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Set the account ID
     * @param id The account ID
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * Set the username
     * @param user The username
     */
    public void setUser(String user) {
        this.user = user;
    }
    /**
     * Set if account is verified
     * @param verified If account is verified
     */
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    /**
     * Set if e-mail has been verified on account
     * @param has_verified_email If e-mail has been verified on account
     */
    public void setHas_verified_email(boolean has_verified_email) {
        this.has_verified_email = has_verified_email;
    }
    /**
     * Set if account has gold status
     * @param is_gold If account has gold status
     */
    public void setIs_gold(boolean is_gold) {
        this.is_gold = is_gold;
    }
    /**
     * Set if user is a moderator
     * @param is_mod If user is a moderator
     */
    public void setIs_mod(boolean is_mod) {
        this.is_mod = is_mod;
    }
    /**
     * Set if user is an employee
     * @param is_employee If user is an employee
     */
    public void setIs_employee(boolean is_employee) {
        this.is_employee = is_employee;
    }
    /**
     * Set karma gained from awardee
     * @param awardee_karma Karma gained from awardee
     */
    public void setAwardee_karma(int awardee_karma) {
        this.awardee_karma = awardee_karma;
    }
    /**
     * Set karma gained from awarder
     * @param awarder_karma Karma gained from awarder
     */
    public void setAwarder_karma(int awarder_karma) {
        this.awarder_karma = awarder_karma;
    }
    /**
     * Set karma gained from link
     * @param link_karma Karma gained from link
     */
    public void setLink_karma(int link_karma) {
        this.link_karma = link_karma;
    }
    /**
     * Set karma gained from comment
     * @param comment_karma Karma gained from comment
     */
    public void setComment_karma(int comment_karma) {
        this.comment_karma = comment_karma;
    }
    /**
     * Set total karma on account
     * @param total_karma Total karma on account
     */
    public void setTotal_karma(int total_karma) {
        this.total_karma = total_karma;
    }
    /**
     * Set date when the account was created
     * @param created Date when the account was created
     */
    public void setCreated(String created) {
        this.created = created;
    }
    /**
     * Set a comment on a post
     * @param comment A comment on a post
     */
    public void setComment(String comment) {
        this.post = comment;
    }
    /**
     * Set an upvote on a post or comment
     * @param upvote An upvote on a post or comment
     */
    public void setUpvote(boolean upvote) {
        this.upvote = upvote;
    }
    /**
     * Set a downvote on a post or comment
     * @param downvote A downvote on a post or comment
     */
    public void setDownvote(boolean downvote) {
        this.downvote = downvote;
    }


    /**
     * Collects information about user and user account
     */
    public void analyze() {
        JsonObject about = useEndpoint("/user/" + user +"/about");
        JsonObject data = (JsonObject) about.get("data");

        name = String.valueOf(data.get("name")).replace("\"","");
        id = String.valueOf(data.get("id")).replace("\"","");
        icon = String.valueOf(data.get("icon_img"));
        description = String.valueOf(data.get("public_description"));
        verified = Boolean.valueOf(String.valueOf(data.get("verified")));
        has_verified_email = Boolean.valueOf(String.valueOf(data.get("has_verified_email")));
        is_gold = Boolean.valueOf(String.valueOf(data.get("is_gold")));
        is_mod = Boolean.valueOf(String.valueOf(data.get("is_mod")));
        is_employee = Boolean.valueOf(String.valueOf(data.get("is_employee")));
        awardee_karma = Integer.parseInt(String.valueOf(data.get("awardee_karma")));
        awarder_karma = Integer.parseInt(String.valueOf(data.get("awarder_karma")));
        link_karma = Integer.parseInt(String.valueOf(data.get("link_karma")));
        comment_karma = Integer.parseInt(String.valueOf(data.get("comment_karma")));
        total_karma = Integer.parseInt(String.valueOf(data.get("total_karma")));
        url = "http://reddit.com/user/" + name;
        long utc = Long.parseLong(String.valueOf(data.get("created_utc").getAsInt()));
        Date date = new Date(utc * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy");
        created = sdf.format(date);
        this.user = name;
    }


    /**
     * Compares text from each comment and post to determine similarity score
     * @param scoreMap Map of comments/submissions to compare
     * @return Similarity score
     */
    public double compareScore(Map<String, String> scoreMap) {
        double score = 0;
        int scoreCount = 0;
        if (scoreMap.size() <= 1) {
            return 0.0;
        } else {
            for (Map.Entry<String, String> posts : scoreMap.entrySet()) {
                for (Map.Entry<String, String> post : scoreMap.entrySet()) {
                    if (Objects.equals(post.getKey(), posts.getKey())) {
                        // do nothing
                    } else {
                        score += (findSimilarity(post.getValue(), posts.getValue()));
                        scoreCount++;
                    }
                }
            }
        }
        return score / scoreCount;
    }

    /**
     * Calculate total upvotes from all comments/submissions
     * @param postMap Map of comments or submissions data
     * @return total upvotes from all comments/submissions
     */
    public int upvotes(Map<String, List<Object>> postMap) {
        int ups = 0;
        for (Map.Entry<String, List<Object>> comment : postMap.entrySet())
            ups += Integer.valueOf(String.valueOf(postMap.get(comment.getKey()).get(3)));
        return ups;
    }

    /**
     * Calculate total downvotes from all comments/submissions
     * @param postMap Map of comments or submissions data
     * @return all downvotes from all comments/submissions
     */
    public int downvotes(Map<String, List<Object>> postMap) {
        int downs = 0;
        for (Map.Entry<String, List<Object>> comment : postMap.entrySet())
            downs += Integer.valueOf(String.valueOf(postMap.get(comment.getKey()).get(4)));
        return downs;
    }

    /**
     * List of subreddits the user has commented and posted in
     * @param userSubreddits  Map of comments or submissions data
     * @return List of subreddits the user has commented and posted in
     */
    public String subredditsList(Map<String, List<Object>> userSubreddits) {
        int i = 1;
        String userSubs = "";
        for (Map.Entry<String, List<Object>> subreddit : userSubreddits.entrySet()) {
            if (i == userSubreddits.size()) {
                userSubs += "<a href=\"http://reddit.com/" + userSubreddits.get(subreddit.getKey()).get(2) + "\" target=\"_blank\">" + userSubreddits.get(subreddit.getKey()).get(2) + "</a>";
            } else {
                userSubs += "<a href=\"http://reddit.com/" + userSubreddits.get(subreddit.getKey()).get(2) + "\" target=\"_blank\">" + userSubreddits.get(subreddit.getKey()).get(2) + "</a>, ";
            }
            i++;
        }
        return userSubs;
    }

    /**
     * Finds most frequent subreddit user comments/posts in
     * @param subreddits List of subreddits
     * @return most popular subreddit of user
     */
    public String popularSubreddit(List subreddits) {
        String popularSubreddit = "";
        int subredditCount = 0;
        if (subreddits.size() < 1) {
            //do nothing
        } else {
            for (Object postSubreddit : subreddits) {
                if (Collections.frequency(subreddits, postSubreddit) > subredditCount) {
                    popularSubreddit = (String) postSubreddit;
                    subredditCount = Collections.frequency(subreddits, postSubreddit);
                }
            }
        }
        return popularSubreddit;
    }

    /**
     * Determines number of times user has commented/posted in most frequent subreddit
     * @param subreddits List of subreddits
     * @return times posted in most popular subreddit of user
     */
    public int popularSubredditCount(List subreddits) {
        int subredditCount = 0;
        if (subreddits.size() < 1) {
            //do nothing
        } else {
            for (Object postSubreddit : subreddits) {
                if (Collections.frequency(subreddits, postSubreddit) > subredditCount) {
                    subredditCount = Collections.frequency(subreddits, postSubreddit);
                }
            }
        }
        return subredditCount;
    }

    /**
     * Finding similarities between comments/submissions (referenced as Strings)
     * @param x String 1 to compare to
     * @param y String 2 to compare to
     * @return Similarity score between comments/submissions
     */
    public static double findSimilarity(String x, String y) {
        double maxLength = Double.max(x.length(), y.length());
        if (maxLength > 0)
            return (maxLength - StringUtils.getLevenshteinDistance(x, y)) / maxLength;
        return 0.0;
    }

    /**
     * Generate random user from reddit
     * @return random username of reddit user
     */
    public String randomUser() {
        JsonObject random = useEndpoint("/r/all/comments?sort=random");
        JsonObject data = (JsonObject) random.get("data");
        JsonArray children = data.getAsJsonArray("children");
        String author = null;
        for (JsonElement item : children) {
            JsonObject dat = (JsonObject) item.getAsJsonObject().get("data");
            author = String.valueOf(dat.getAsJsonObject().get("author"));
        }
        return author.substring(1,author.length()-1);
    }

    /**
     * Send results to string
     * @return name, id, user, verified, has_verified_email, is_gold, is_mod, is_employee, awardee_karma, awarder_karma, link_karma, created, sdf, comment, upvote, and downvote
     */
    @Override
    public String toString() {
        return "<h4 style=\"font-family:system-ui;color:#d7dadc;\">User</h4><span style=\"font-family:system-ui;color:#eb5528;\">" +
                "<img src=" + icon + " width=\"50px\"><br>" +
                "<span style=\"color:#d7dadc;\">user: </span><a href=\"" + url + "\" target=\"_blank\">" + name + "</a><br>" +
                "<span style=\"color:#d7dadc;\">id: </span>" + id + "<br>" +
                "<span style=\"color:#d7dadc;\">verified: </span>" + verified + "<br>" +
                "<span style=\"color:#d7dadc;\">has verified email: </span>" + has_verified_email + "<br>" +
                "<span style=\"color:#d7dadc;\">premium: </span>" + is_gold + "<br>" +
                "<span style=\"color:#d7dadc;\">mod: </span>" + is_mod + "<br>" +
                "<span style=\"color:#d7dadc;\">employee: </span>" + is_employee + "<br>" +
                "<span style=\"color:#d7dadc;\">awardee karma: </span>" + awardee_karma + "<br>" +
                "<span style=\"color:#d7dadc;\">awarder karma: </span>" + awarder_karma + "<br>" +
                "<span style=\"color:#d7dadc;\">link karma: </span>" + link_karma + "<br>" +
                "<span style=\"color:#d7dadc;\">comment karma: </span>" + comment_karma + "<br>" +
                "<span style=\"color:#d7dadc;\">total karma: </span>" + total_karma + "<br>" +
                "<span style=\"color:#d7dadc;\">created: </span>" + created + "</span>";
    }
}
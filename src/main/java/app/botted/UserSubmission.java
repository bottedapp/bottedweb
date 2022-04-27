package app.botted;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserSubmission extends UserAccount {

    /**
     * Protected and private variables
     */
    private ArrayList subSubreddits = new ArrayList();
    Map<String, String> submissionMap = new LinkedHashMap<>();
    Map<String, List<Object>> userSubmissions = new HashMap<>();

    /**
     * Constructor with parameters
     * @param user The username
     * @throws IOException
     * @throws InterruptedException
     */
    public UserSubmission(String user) throws IOException, InterruptedException {
        super(user);
        analyzeSubmissions();
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
     * @param submissionTotalScore Total karma gained from particular subreddit
     * @param popularSubmissionSubreddit Most popular submission in particular subreddit
     * @param submissionSubredditCount How many submissions in particular subreddit
     * @param freeKarma Subreddit called FreeKarma4U
     * @param commentSubreddits Subreddits commented in
     * @throws IOException
     * @throws InterruptedException
     */
    public UserSubmission(String subreddit, String name, String id, String user, Boolean verified, Boolean has_verified_email, Boolean is_gold, Boolean is_mod, Boolean is_employee, int awardee_karma, int awarder_karma, int link_karma, int comment_karma, int total_karma, Date created, String comment, boolean upvote, boolean downvote, double submissionTotalScore, String popularSubmissionSubreddit, int submissionSubredditCount, int freeKarma, ArrayList commentSubreddits) throws IOException, InterruptedException {
        super(subreddit, name, id, user, verified, has_verified_email, is_gold, is_mod, is_employee, awardee_karma, awarder_karma, link_karma, comment_karma, total_karma, created, comment, upvote, downvote);
        this.subSubreddits = commentSubreddits;
    }

    //getters

    public ArrayList getSubSubreddits() {
        return subSubreddits;
    }

    //setters

    public void setSubSubreddits(ArrayList commentSubreddits) {
        this.subSubreddits = commentSubreddits;
    }

    /**
     * Finds the most active subreddit a user posts in
     * Checks if posted in r/FreeKarma4U
     * Calculates similarities between submissions
     * @throws IOException
     * @throws InterruptedException
     */
    public void analyzeSubmissions() throws IOException, InterruptedException {
        JsonObject submitted = useEndpoint("/user/" + user + "/submitted");
        JsonObject data = (JsonObject) submitted.get("data");
        JsonArray children = (JsonArray) data.get("children");

        for (JsonElement item : children) {
            JsonObject dat = (JsonObject) item.getAsJsonObject().get("data");
            String id = String.valueOf(dat.getAsJsonObject().get("id"));
            String subreddit = String.valueOf(dat.getAsJsonObject().get("subreddit_name_prefixed"));
            String body = String.valueOf(dat.getAsJsonObject().get("selftext"));
            String permalink = String.valueOf(dat.getAsJsonObject().get("permalink"));
            String title = String.valueOf(dat.getAsJsonObject().get("title"));
            String url = String.valueOf(dat.getAsJsonObject().get("url_overridden_by_dest"));
            int ups = Integer.valueOf(String.valueOf(dat.getAsJsonObject().get("ups")));
            int downs = Integer.valueOf(String.valueOf(dat.getAsJsonObject().get("downs")));
            double upvoteRatio = Double.valueOf(String.valueOf(dat.getAsJsonObject().get("upvote_ratio")));
            int numComments = Integer.valueOf(String.valueOf(dat.getAsJsonObject().get("num_comments")));
            boolean isOriginal = Boolean.valueOf(String.valueOf(dat.getAsJsonObject().get("is_original_content")));
            int crossposts = Integer.valueOf(String.valueOf(dat.getAsJsonObject().get("num_crossposts")));
            boolean nsfw = Boolean.valueOf(String.valueOf(dat.getAsJsonObject().get("over_18")));
            long utc = Long.parseLong(String.valueOf(dat.get("created").getAsInt()));
            SimpleDateFormat sdf = new SimpleDateFormat("M/dd/Y h:mm:ss a");
            Date created = new Date(utc * 1000);
            String date = sdf.format(created);

            if (body.length() > 2 && body != null)
                submissionMap.put(id, body.substring(1, body.length() - 1));
            else if (url != "null" && url.endsWith(".jpg\"") == false && url.endsWith(".png\"") == false && url.endsWith(".gif\"") == false)
                submissionMap.put(id, url.substring(1, url.length() - 1));
            else if (url.endsWith(".jpg\"") == true || url.endsWith(".png\"") == true || url.endsWith(".gif\"") == true)
                submissionMap.put(id, "<img src=" + url + " width=\"100%\"></img>");
            else
                submissionMap.put(id, "");

            List submissionArray = new ArrayList<>();
            submissionArray.add(body.substring(1, body.length() - 1)); // [0]
            submissionArray.add(title.substring(1, title.length() - 1)); // [1]
            submissionArray.add(subreddit.substring(1, subreddit.length() - 1)); // [2]
            submissionArray.add(ups); // [3]
            submissionArray.add(downs); // [4]
            submissionArray.add(nsfw); // [5]
            submissionArray.add(date); // [6]
            submissionArray.add(permalink); // [7]
            submissionArray.add(upvoteRatio); // [8]
            submissionArray.add(isOriginal); // [9]
            submissionArray.add(crossposts); // [10]
            submissionArray.add(numComments); // [11]
            userSubmissions.put(id, submissionArray);
        }
    }

    public String submissionsList(Map<String, List<Object>> userSubmissions) {
        String submissionList = "<table style=\"width:100%;max-width:100%;display:block;word-wrap:break-word;\"><tbody style=\"width: 100%;max-width: 100%;display: block;word-wrap: break-word;\">";
        for (Map.Entry<String, List<Object>> post : userSubmissions.entrySet()) {
            submissionList += "<tr style=\"display:block;border-bottom: #363636 solid 15px;\"\">" +
                    "<td style=\"background:#1A1A1B;width: 100%;max-width: 100%;display:block;word-wrap: break-word;color:#d7dadc;border: #d7dadc solid 1px;\">" +
                    "<strong>" + userSubmissions.get(post.getKey()).get(1) + "</strong><br><br>" +
                    StringEscapeUtils.unescapeJava((String) userSubmissions.get(post.getKey()).get(0)).replace("\n", "<br>").replace("\\", "") + "<br><br>" +
                    "original: " + userSubmissions.get(post.getKey()).get(9) + " | " + "crossposts: " + userSubmissions.get(post.getKey()).get(10) + " | comments: " + userSubmissions.get(post.getKey()).get(11) + "<br>" +
                    "upvotes: " + userSubmissions.get(post.getKey()).get(3) + " | downvotes: " + userSubmissions.get(post.getKey()).get(4) + " | ratio: " + userSubmissions.get(post.getKey()).get(8) + " | nsfw: " + userSubmissions.get(post.getKey()).get(5) + "<br>" +
                    "<a href=\"https://reddit.com/" + userSubmissions.get(post.getKey()).get(2) + "\" target=\"_blank\">" + userSubmissions.get(post.getKey()).get(2) + "</a> | " + userSubmissions.get(post.getKey()).get(6) + " | <a style=\"color:#eb5528\" href=\"https://reddit.com" + userSubmissions.get(post.getKey()).get(7) + "\" target=\"_blank\">permalink</a></td></tr>";
        }
        submissionList += "</tbody></table>";
        return submissionList;
    }

    @Override
    public String toString() {
            return "<h4 style=\"font-family:system-ui;color:#d7dadc;\">Submissions</h4><span style=\"font-family:system-ui;color:#eb5528;\">" +
                    "<span style=\"color:#d7dadc;\">submission score: </span>" + compareScore(submissionMap) + "<br>" +
                    "<span style=\"color:#d7dadc;\">submissions compared: </span>" + submissionMap.size() + "<br>" +
                    "<span style=\"color:#d7dadc;\">popular subreddit: </span><a href=\"https://reddit.com/" + popularSubreddit(subSubreddits) + "\" target=\"_blank\">" + popularSubreddit(subSubreddits) + "</a><br>" +
                    "<span style=\"color:#d7dadc;\">popular subreddit count: </span>" + popularSubredditCount(subSubreddits) + "<br>" +
                    "<span style=\"color:#d7dadc;\">submission upvotes: </span>" + upvotes(userSubmissions) + "<br>" +
                    "<span style=\"color:#d7dadc;\">submission downvotes: </span>" + downvotes(userSubmissions) + "<br>" +
                    "<span style=\"color:#d7dadc;\">submission subreddits: </span>" + subredditsList(userSubmissions) + "<br>" +
                    "<span style=\"color:#d7dadc;\">submissions: </span>" + submissionsList(userSubmissions) + "</span>";
    }

    public String getResponse(String[] keyPhrase) {
        return "Submission class - Insert response here.";
    }

}
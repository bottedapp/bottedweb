package app.botted;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.text.StringEscapeUtils;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;

public class UserSubmissions extends UserAccount {

    /**
     * private variables
     */
    private List submissionSubreddits;
    private Map<String, String> submissionMap;
    private Map<String, List<Object>> userSubmissions;

    /**
     * Default constructor
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public UserSubmissions() throws IOException, InterruptedException {
        super();
    }

    /**
     * Constructor with parameters
     *
     * @param user The username
     * @throws IOException
     * @throws InterruptedException
     */
    public UserSubmissions(String user) throws IOException, InterruptedException {
        super(user);
        analyze();
    }

    /**
     * Constructor
     *
     * @param user                 The username
     * @param submissionSubreddits Subreddits commented in
     * @param submissionMap        25 latest submissions by user
     * @param userSubmissions      Map of all user submission data
     * @throws IOException
     * @throws InterruptedException
     */
    public UserSubmissions(String user, List submissionSubreddits, Map<String, String> submissionMap, Map<String, List<Object>> userSubmissions) throws IOException, InterruptedException {
        super(user);
        this.submissionSubreddits = submissionSubreddits;
        this.submissionMap = submissionMap;
        this.userSubmissions = userSubmissions;
    }

    //getters

    public List getSubmissionSubreddits() {
        return submissionSubreddits;
    }

    public Map<String, String> getSubmissionMap() {
        return submissionMap;
    }

    public Map<String, List<Object>> getUserSubmissions() {
        return userSubmissions;
    }

    //setters

    public void setSubmissionSubreddits(List submissionSubreddits) {
        this.submissionSubreddits = submissionSubreddits;
    }

    public void setSubmissionMap(Map<String, String> submissionMap) {
        this.submissionMap = submissionMap;
    }

    public void setUserSubmissions(Map<String, List<Object>> userSubmissions) {
        this.userSubmissions = userSubmissions;
    }

    /**
     * Retrieves and assigns all submission data from Reddit API
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void analyze() throws IOException, InterruptedException {
        submissionSubreddits = new ArrayList();
        submissionMap = new HashMap<>();
        userSubmissions = new HashMap<>();

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
            Date date = new Date(utc * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("M/dd/Y h:mm:ss a");
            String created = sdf.format(date);

            submissionSubreddits.add(subreddit.substring(1, subreddit.length() - 1));

            List submissionArray = new ArrayList<>();
            if (body.length() > 2 && body != null) {
                submissionMap.put(id, body.substring(1, body.length() - 1));
                submissionArray.add(body.substring(1, body.length() - 1));
            } else if (url != "null" && url.endsWith(".jpg\"") == false && url.endsWith(".png\"") == false && url.endsWith(".gif\"") == false) {
                submissionMap.put(id, url.substring(1, url.length() - 1));
                submissionArray.add(url.substring(1, url.length() - 1));
            } else if (url.endsWith(".jpg\"") == true || url.endsWith(".png\"") == true || url.endsWith(".gif\"") == true) {
                submissionMap.put(id, url.substring(1, url.length() - 1));
                submissionArray.add("<img src=" + url + " width=\"100%\"></img>");
            } else {
                submissionMap.put(id, "");
                submissionArray.add(body.substring(1, body.length() - 1)); // [0]
            }
            submissionArray.add(title.substring(1, title.length() - 1)); // [1]
            submissionArray.add(subreddit.substring(1, subreddit.length() - 1)); // [2]
            submissionArray.add(ups); // [3]
            submissionArray.add(downs); // [4]
            submissionArray.add(nsfw); // [5]
            submissionArray.add(created); // [6]
            submissionArray.add(permalink.substring(1, permalink.length() - 1)); // [7]
            submissionArray.add(upvoteRatio); // [8]
            submissionArray.add(isOriginal); // [9]
            submissionArray.add(crossposts); // [10]
            submissionArray.add(numComments); // [11]
            userSubmissions.put(id, submissionArray);
        }
    }

    /**
     * @param userSubmissions Map of all user submission data
     * @return List of all user submissions
     */
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
    /**
     * Send results to string
     * @return All data of analyzed submissions
     */
    @Override
    public String toString() {
        return "<h4 style=\"font-family:system-ui;color:#d7dadc;\">Submissions</h4><span style=\"font-family:system-ui;color:#eb5528;\">" +
                "<span style=\"color:#d7dadc;\">submission score: </span>" + compareScore(submissionMap) + "<br>" +
                "<span style=\"color:#d7dadc;\">submissions compared: </span>" + submissionMap.size() + "<br>" +
                "<span style=\"color:#d7dadc;\">popular subreddit: </span><a href=\"https://reddit.com/" + popularSubreddit(submissionSubreddits) + "\" target=\"_blank\">" + popularSubreddit(submissionSubreddits) + "</a><br>" +
                "<span style=\"color:#d7dadc;\">popular subreddit count: </span>" + popularSubredditCount(submissionSubreddits) + "<br>" +
                "<span style=\"color:#d7dadc;\">submission upvotes: </span>" + upvotes(userSubmissions) + "<br>" +
                "<span style=\"color:#d7dadc;\">submission downvotes: </span>" + downvotes(userSubmissions) + "<br>" +
                "<span style=\"color:#d7dadc;\">submission subreddits: </span>" + subredditsList(userSubmissions) + "<br>" +
                "<span style=\"color:#d7dadc;\">submissions: </span>" + submissionsList(userSubmissions) + "</span>";
    }
}
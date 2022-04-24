package app.botted;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Submission extends User {

    /**
     * Protected and private variables
     */
    private double submissionTotalScore;
    private String popularSubmissionSubreddit = "";
    private String input, result;
    private String subSubs = "";
    private String submissionList;
    private int submissionSubredditCount, freeKarma = 0;
    private int upvotes, downvotes;
    private final SimpleDateFormat sdf = new SimpleDateFormat("M/dd/Y h:mm:ss a");
    private ArrayList subSubreddits = new ArrayList();
    Map<String, String> submissionMap = new LinkedHashMap<>();
    Map<String, String> linkMap = new LinkedHashMap<>();
    Map<String, String> titleMap = new LinkedHashMap<>();
    Map<String, String> createdMap = new LinkedHashMap<>();
    Map<String, String> subredditMap = new LinkedHashMap<>();
    Map<String, Integer> commentsMap = new LinkedHashMap<>();
    Map<String, Integer> upvotesMap = new LinkedHashMap<>();
    Map<String, Integer> downvotesMap = new LinkedHashMap<>();
    Map<String, Double> upvoteRatioMap = new LinkedHashMap<>();
    Map<String, Boolean> isOriginalMap = new LinkedHashMap<>();
    Map<String, Integer> crosspostsMap = new LinkedHashMap<>();
    Map<String, Boolean> nsfwMap = new LinkedHashMap<>();

    /**
     * Constructor with parameters
     * @param user The username
     * @throws IOException
     * @throws InterruptedException
     */
    public Submission(String user) throws IOException, InterruptedException {
        super(user);
        analyzeSubmission();
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
    public Submission(String subreddit, String name, String id, String user, Boolean verified, Boolean has_verified_email, Boolean is_gold, Boolean is_mod, Boolean is_employee, int awardee_karma, int awarder_karma, int link_karma, int comment_karma, int total_karma, Date created, String comment, boolean upvote, boolean downvote, double submissionTotalScore, String popularSubmissionSubreddit, int submissionSubredditCount, int freeKarma, ArrayList commentSubreddits) throws IOException, InterruptedException {
        super(subreddit, name, id, user, verified, has_verified_email, is_gold, is_mod, is_employee, awardee_karma, awarder_karma, link_karma, comment_karma, total_karma, created, comment, upvote, downvote);
        this.submissionTotalScore = submissionTotalScore;
        this.popularSubmissionSubreddit = popularSubmissionSubreddit;
        this.submissionSubredditCount = submissionSubredditCount;
        this.freeKarma = freeKarma;
        this.subSubreddits = commentSubreddits;
        this.input = input;
        this.result = result;
    }

    //getters

    public double getSubmissionTotalScore() {
        return submissionTotalScore;
    }

    public String getPopularSubmissionSubreddit() {
        return popularSubmissionSubreddit;
    }

    public int getSubmissionSubredditCount() {
        return submissionSubredditCount;
    }

    public int getFreeKarma() {
        return freeKarma;
    }

    public ArrayList getSubSubreddits() {
        return subSubreddits;
    }

    public String getInput() {
        return input;
    }

    public String getResult() {
        return result;
    }

    //setters

    public void setSubmissionTotalScore(double submissionTotalScore) { this.submissionTotalScore = submissionTotalScore; }

    public void setPopularSubmissionSubreddit(String popularSubmissionSubreddit) { this.popularSubmissionSubreddit = popularSubmissionSubreddit; }

    public void setSubmissionSubredditCount(int submissionSubredditCount) { this.submissionSubredditCount = submissionSubredditCount; }

    public void setFreeKarma(int freeKarma) {
        this.freeKarma = freeKarma;
    }

    public void setSubSubreddits(ArrayList commentSubreddits) {
        this.subSubreddits = commentSubreddits;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Finds the most active subreddit a user posts in
     * Checks if posted in r/FreeKarma4U
     * Calculates similarities between submissions
     * @throws IOException
     * @throws InterruptedException
     */
    public void analyzeSubmission() throws IOException, InterruptedException {
        JsonObject submitted = useEndpoint("/user/" + user + "/submitted");
        JsonObject data = (JsonObject) submitted.get("data");
        JsonArray children = (JsonArray) data.get("children");

        for (JsonElement item : children) {
            //posts
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
            long utc = Long.parseLong(String.valueOf(dat.get("created").getAsInt()));
            int numComments = Integer.valueOf(String.valueOf(dat.getAsJsonObject().get("num_comments")));
            boolean isOriginal = Boolean.valueOf(String.valueOf(dat.getAsJsonObject().get("is_original_content")));
            int crossposts = Integer.valueOf(String.valueOf(dat.getAsJsonObject().get("num_crossposts")));
            boolean nsfw = Boolean.valueOf(String.valueOf(dat.getAsJsonObject().get("over_18")));


            if (body.length() > 3 || body != "\"ul\"")
                submissionMap.put(id, body.substring(1,body.length()-1));
            else
                submissionMap.put(id, url.substring(1,url.length()-1));
            created = new Date(utc * 1000);
            String date = sdf.format(created);
            linkMap.put(id, permalink);
            titleMap.put(id, title.substring(1,title.length()-1));
            createdMap.put(id, date);
            subredditMap.put(id, subreddit.substring(1,subreddit.length()-1));
            upvotesMap.put(id, ups);
            downvotesMap.put(id, downs);
            upvoteRatioMap.put(id, upvoteRatio);
            commentsMap.put(id, numComments);
            isOriginalMap.put(id, isOriginal);
            crosspostsMap.put(id, crossposts);
            nsfwMap.put(id, nsfw);
            upvotes += ups;
            downvotes += downs;
        }

        if (submissionMap.size() < 1) {
            //do nothing
        } else {
            for (JsonElement item : children) {
                JsonObject dat = (JsonObject) item.getAsJsonObject().get("data");
                String subreddit = String.valueOf(dat.getAsJsonObject().get("subreddit_name_prefixed"));
                subSubreddits.add(subreddit);
            }
            for (Object a : subSubreddits) {
                if (a.toString().contains("r/FreeKarma4You"))
                    freeKarma = Collections.frequency(subSubreddits, a);
                if (Collections.frequency(subSubreddits, a) > submissionSubredditCount) {
                    submissionSubredditCount = Collections.frequency(subSubreddits, a);
                    popularSubmissionSubreddit = (String) a;
                }
            }
        }
        if (submissionMap.size() <= 1) {
            //do nothing
        } else {
            double postScore = 0;
            int postScoreCount = 0;
            for (Map.Entry<String, String> posts : submissionMap.entrySet()) {
                for (Map.Entry<String, String> post : submissionMap.entrySet()) {
                    if (Objects.equals(post.getKey(), posts.getKey())) {
                        //do nothing
                    } else {
                        postScore += (findSimilarity(post.getValue(), posts.getValue()));
                        postScoreCount++;
                    }
                }
            }
            submissionTotalScore = postScore / postScoreCount;
        }
    }

    public void subSubredditslist() {
        int i = 1;
        for (Object subreddit : subSubreddits) {
            if (i == subSubreddits.size()) {
                subSubs += "<a href=\"http://reddit.com/" + subreddit.toString().replace("\"", "") + "\" target=\"_blank\">" + subreddit.toString().replace("\"", "") + "</a>";
            } else {
                subSubs += "<a href=\"http://reddit.com/" + subreddit.toString().replace("\"", "") + "\" target=\"_blank\">" + subreddit.toString().replace("\"", "") + "</a>, ";
            }
            i++;
        }
    }

    public void submissionsList() {
        submissionList = "<table style=\"width:100%;max-width:100%;display:block;word-wrap:break-word;\"><tbody style=\"width: 100%;max-width: 100%;display: block;word-wrap: break-word;\">";
        for (Map.Entry<String, String> post : submissionMap.entrySet()) {
            submissionList += "<tr style=\"display:block;border-bottom: #363636 solid 15px;\"\">" +
                    "<td style=\"background:#1A1A1B;width: 100%;max-width: 100%;display:block;word-wrap: break-word;color:#d7dadc;border: #d7dadc solid 1px;\">" +
                    "<strong>" + titleMap.get(post.getKey()) + "</strong><br><br>" +
                    StringEscapeUtils.unescapeJava(post.getValue()).replace("\n", "<br>").replace("\\", "") + "<br><br>" +
                    "original: " + isOriginalMap.get(post.getKey()) + " | " + "crossposts: " + crosspostsMap.get(post.getKey()) + " | comments: " + commentsMap.get(post.getKey()) + "<br>" +
                    "upvotes: " + upvotesMap.get(post.getKey()) + " | downvotes: " + downvotesMap.get(post.getKey()) + " | ratio: " + upvoteRatioMap.get(post.getKey()) + " | nsfw: " + nsfwMap.get(post.getKey()) + "<br>" +
                    "<a href=\"https://reddit.com/" + subredditMap.get(post.getKey()) + "\" target=\"_blank\">" + subredditMap.get(post.getKey()) + "</a> | " + createdMap.get(post.getKey()) + " | <a style=\"color:#eb5528\" href=\"https://reddit.com" + linkMap.get(post.getKey()).replace("\"", "") + "\" target=\"_blank\">permalink</a></td></tr>";
        }
        submissionList += "</tbody></table>";
    }

    @Override
    public String toString() {
        subSubredditslist();
        submissionsList();
        return "<h4 style=\"font-family:system-ui;color:#d7dadc;\">Submissions</h4><span style=\"font-family:system-ui;color:#eb5528;\">" +
                "<span style=\"color:#d7dadc;\">submission score: </span>" + submissionTotalScore + "<br>" +
                "<span style=\"color:#d7dadc;\">submissions compared: </span>" + subSubreddits.size() + "<br>" +
                "<span style=\"color:#d7dadc;\">popular subreddit: </span><a href=\"https://reddit.com/" + popularSubmissionSubreddit.replace("\"", "") + "\" target=\"_blank\">" + popularSubmissionSubreddit.replace("\"", "") + "</a><br>" +
                "<span style=\"color:#d7dadc;\">popular subreddit count: </span>" + submissionSubredditCount + "<br>" +
                "<span style=\"color:#d7dadc;\">submissions in r/FreeKarma4You: </span>" + freeKarma + "<br>" +
                "<span style=\"color:#d7dadc;\">submission upvotes: </span>" + upvotes + "<br>" +
                "<span style=\"color:#d7dadc;\">submission downvotes: </span>" + downvotes + "<br>" +
                "<span style=\"color:#d7dadc;\">submission subreddits: </span>" + subSubs + "<br>" +
                "<span style=\"color:#d7dadc;\">submissions: </span>" + submissionList + "</span>";
    }

    public String getResponse(String[] keyPhrase) {
        return "Submission class - Insert response here.";
    }

}
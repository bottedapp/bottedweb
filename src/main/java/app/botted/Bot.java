package app.botted;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Bot extends UserAccount {

    /**
     * Private variables
     */
    private boolean bot;
    private boolean goodBot;
    private boolean badBot;
    private static ArrayList keyPhrase = new ArrayList();

    public Bot() throws IOException, InterruptedException {
        super();
        keyPhrase.add("This is a good bot!");
        keyPhrase.add("This is a bad bot!");
        keyPhrase.add("This is a human!");
        keyPhrase.add("Undetermined.");
    }
    public static String isBot(double score) {
        if (score >= 0.4)
            return " is a bot";
        if (score > 0.2 && score < 0.4)
            return " might be a bot";
        if (score <= 0.2)
            return " is not a bot";
        return "";
    }
    /**
     * Constructor
     * @param subreddit The specific subreddit
     * @param comment A comment on a post
     * @param upvote An upvote on a post or comment
     * @param downvote A downvote on a post or comment
     * @param bot Whether this is a bot
     * @param goodBot Whether the bot is good
     * @param badBot Whether the bot is bad
     */
    public Bot(String subreddit, String name, String id, String user, Boolean verified, Boolean has_verified_email, Boolean is_gold, Boolean is_mod, Boolean is_employee, int awardee_karma, int awarder_karma, int link_karma, int comment_karma, int total_karma, Date created, String comment, boolean upvote, boolean downvote, boolean bot, boolean goodBot, boolean badBot) throws IOException, InterruptedException {
        super(subreddit, name, id, user, verified, has_verified_email, is_gold, is_mod, is_employee, awardee_karma, awarder_karma, link_karma, comment_karma, total_karma, created, comment, upvote, downvote);
        this.bot = bot;
        this.goodBot = goodBot;
        this.badBot = badBot;
    }

    //getters


    public boolean isBot() {
        return bot;
    }

    public boolean isGoodBot() {
        return goodBot;
    }

    public boolean isBadBot() {
        return badBot;
    }

    //setters

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public void setGoodBot(boolean goodBot) {
        this.goodBot = goodBot;
    }

    public void setBadBot(boolean badBot) {
        this.badBot = badBot;
    }

    @Override
    public String toString() {
        return "Bot{" +
                "bot=" + bot +
                ", goodBot=" + goodBot +
                ", badBot=" + badBot +
                '}';
    }

    public String[] responses(boolean goodBot, boolean badBot, ArrayList<String> keyPhrase) {
        if (goodBot = true) {
            return new String[]{keyPhrase.get(0)};
        }
        else if (badBot = true) {
            return new String[]{keyPhrase.get(1)};
        }
        else {
            return new String[]{keyPhrase.get(3)};
        }
    }

}
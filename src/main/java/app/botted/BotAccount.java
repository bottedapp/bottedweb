package app.botted;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class BotAccount extends UserAccount {

    /**
     * Private variables
     */
    private boolean bot;
    static ArrayList keyPhrase = new ArrayList();

    /**
     * Default constructor
     * @throws IOException
     * @throws InterruptedException
     */
    public BotAccount() throws IOException, InterruptedException {
        super();
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
     * @param bot Whether this is a bot
     * @throws IOException
     * @throws InterruptedException
     */
    public BotAccount(String subreddit, String name, String id, String user, Boolean verified, Boolean has_verified_email, Boolean is_gold, Boolean is_mod, Boolean is_employee, int awardee_karma, int awarder_karma, int link_karma, int comment_karma, int total_karma, String created, String comment, boolean upvote, boolean downvote, boolean bot, boolean goodBot, boolean badBot) throws IOException, InterruptedException {
        super(subreddit, name, id, user, verified, has_verified_email, is_gold, is_mod, is_employee, awardee_karma, awarder_karma, link_karma, comment_karma, total_karma, created, comment, upvote, downvote);
        this.bot = bot;
    }

    public static String isBot(String name, double commentScore, double submissionScore) {
        String user = name.toLowerCase(Locale.ROOT);
        if (user.contains("_bot") || user.contains("bot_"))
            return name + " is a bot";
        if (commentScore >= 0.35 || submissionScore >= 0.6)
            return name + " is a bot";
        if (commentScore > 0.25 && commentScore < 0.4 || submissionScore >= 0.35 && submissionScore >= 0.6)
            return name + " might be a bot";
        if (commentScore <= 0.25 && submissionScore <= 0.4)
            return name + " is not a bot";
        return "";
    }
    public static String BotOrNot(String name, double commentScore, double submissionScore) {
        keyPhrase.add("Hi! Thank you for summoning me! It would appear that " + name + " is run by a bot!" +
                "\nHere is a link to my webpage if you would like a more detailed analysis!" +
                "\nhttps://botted.app/?u=" + name);
        keyPhrase.add("Hi! Thank you for summoning me! It would appear that " + name + " might be run by a bot!" +
                "\nHere is a link to my webpage if you would like a more detailed analysis!" +
                "\nhttps://botted.app/?u=" + name);
        keyPhrase.add("Hi! Thank you for summoning me! It would appear that " + name + " is run by a human!" +
                "\nHere is a link to my webpage if you would like a more detailed analysis!" +
                "\nhttps://botted.app/?u=" + name);
        keyPhrase.add("Hi! Thank you for summoning me! Hm... my apologies, for some reason I am unsure whether or not this account is run by a bot." +
                "\nHere is a link to my webpage if you would like a more detailed analysis!" +
                "\nhttps://botted.app/?u=" + name);
        String user = name.toLowerCase(Locale.ROOT);
        if (user.contains("_bot") || user.contains("bot_"))
            return (String) keyPhrase.get(0);
        if (commentScore >= 0.35 || submissionScore >= 0.6)
            return (String) keyPhrase.get(0);
        if (commentScore > 0.25 && commentScore < 0.4 || submissionScore >= 0.35 && submissionScore >= 0.6)
            return (String) keyPhrase.get(1);
        if (commentScore <= 0.25 && submissionScore <= 0.4)
            return (String) keyPhrase.get(2);
        return (String) keyPhrase.get(3);
    }

    //getters

    public boolean isBot() {
        return bot;
    }

    //setters

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    /**
     * Return correct response according to results
     * @param keyPhrase Phrase to signal to our bot for an action
     * @return
     */
    @Override
    public String responses(ArrayList<String> keyPhrase) {

        String response = keyPhrase.get(0);

        return response;
    }

    public static Object responses() {
        return responses();
    }

    /**
     * Send results to string
     * @return bot, goodBot, and badBot
     */
    @Override
    public String toString() {
        return "Bot{" +
                "bot=" + bot +
                '}';
    }

}
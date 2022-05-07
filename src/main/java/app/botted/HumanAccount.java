package app.botted;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Human Account class
 */
public class HumanAccount extends UserAccount {

    /**
     * Private variables
     */

    /**
     * The username
     */
    private String user;
    /**
     * Whether it is a human account
     */
    private boolean human;


    /**
     * Default constructor
     * @throws IOException I/O Exception
     * @throws InterruptedException Interrupted Exception
     */
    public HumanAccount() throws IOException, InterruptedException {
        super();
    }

    /**
     * Constructor with parameters
     * @param subreddit The specific subreddit
     * @param comment A comment on a post
     * @param upvote An upvote on a post or comment
     * @param downvote A downvote on a post or comment
     * @param human Whether it is a human account
     * @throws IOException I/O Exception
     * @throws InterruptedException Interrupted Exception
     */
    public HumanAccount(String subreddit, String comment, boolean upvote, boolean downvote, boolean human) throws IOException, InterruptedException {
        super(subreddit, comment, upvote, downvote);
        this.human = human;
    }

    /**
     * Getter
     */

    /**
     * Boolean value to determine if is user is a human
     * @return Whether it is a human account
     */
    public boolean isHuman() {
        return human;
    }

    /**
     * Setter
     */

    /**
     * Set boolean value to determine if is user is a human
     * @param human Whether it is a human account
     */
    public void setHuman(boolean human) {
        this.human = human;
    }

    /**
     * Return correct response according to results
     * @param keyPhrase Phrase to signal to our bot for an action
     * @return Human keyphrase
     */
    public String responses(ArrayList<String> keyPhrase) {
        return keyPhrase.get(2);
    }

    /**
     * Send results to string
     * @return human
     */
    @Override
    public String toString() {
        return "Human{" +
                "human=" + human +
                '}';
    }

}

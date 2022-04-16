package app.botted;

import java.io.IOException;
import java.util.ArrayList;

public class Human extends User {

    private boolean human;
    public Human() throws IOException, InterruptedException {
        super();

    }
    public Human(String subreddit, String comment, boolean upvote, boolean downvote, boolean human) throws IOException, InterruptedException {
        super(subreddit, comment, upvote, downvote);
        this.human = human;
    }

    //getter

    public boolean isHuman() {
        return human;
    }

    //setter

    public void setHuman(boolean human) {
        this.human = human;
    }

    public String[] responses(ArrayList<String> keyPhrase) {
        return new String[]{keyPhrase.get(2)};
    }

    @Override
    public String toString() {
        return "Human{" +
                "human=" + human +
                '}';
    }

}
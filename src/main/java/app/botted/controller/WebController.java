package app.botted.controller;

import app.botted.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class WebController {

    @RequestMapping("/")
    public String index(Model m, @RequestParam(value = "u", required = false) String name) throws IOException, InterruptedException {
        if (name == null)
            return "index";
        else {
            ArrayList keyPhrase = new ArrayList();
            keyPhrase.add("This is a good bot!");
            keyPhrase.add("This is a bad bot!");
            keyPhrase.add("This is a human!");
            keyPhrase.add("Undetermined.");

            Reddit reddit = new Reddit();
            String redditor = reddit.readInput(name);
            Reddit user = new User(redditor);
            User comments = new Comment(redditor);
            User submissions = new Submission(redditor);
            Reddit bot = new Bot();
            Reddit human = new Human();
            String isaBot = "<h1 style=\"font-family:system-ui;color:#ffffff\">" + ((User) user).getName() + Bot.isBot(((Comment) comments).getCommentTotalScore()) + "</h1>";

            m.addAttribute("uname", name);
            m.addAttribute("user", user);
            m.addAttribute("comments", comments);
            m.addAttribute("submissions", submissions);
            m.addAttribute("isBot", isaBot);

            return "result";
        }
    }
}


package app.botted.controller;

import app.botted.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Spring Boot Web Controller class
 */
@Controller
public class WebController {
    /**
     * SQL Database source
     */
    @Autowired
    private DataSource dataSource;

    /**
     * Index page / Result page
     * @param m model
     * @param name the username
     * @param random generate random username
     * @return index page or result page
     * @throws IOException I/O Exception
     * @throws InterruptedException Interrupted Exception
     * @throws SQLException SQL Exception
     */
    @RequestMapping("/")
    public String index(Model m, @RequestParam(value = "u", required = false) String name, String random) throws IOException, InterruptedException, SQLException {

        if (name == null && random == null) {
            return "index";
        } else if (random !=  null) {
            RedditComponent reddit = new RedditComponent();
            String redditor = new UserAccount().randomUser();
            RedditComponent user = new UserAccount(redditor);
            UserAccount comments = new UserComments(redditor);
            UserAccount submissions = new UserSubmissions(redditor);
            String isaBot = "<h1 style=\"font-family:system-ui;color:#ffffff\">" + BotAccount.BotOrNot(((UserAccount) user).getName(), ((UserComments) comments).getScore(), ((UserSubmissions) submissions).getScore()) + "</h1>";

            m.addAttribute("uname", name);
            m.addAttribute("user", user);
            m.addAttribute("comments", comments);
            m.addAttribute("submissions", submissions);
            m.addAttribute("isBot", isaBot);

            return "result";
        } else {
            RedditComponent reddit = new RedditComponent();
            String redditor = reddit.readInput(name);
            RedditComponent user = new UserAccount(redditor);
            UserAccount comments = new UserComments(redditor);
            UserAccount submissions = new UserSubmissions(redditor);
            BotAccount bot = new BotAccount();
            UserAccount human = new HumanAccount();
            String isaBot = "<h1 style=\"font-family:system-ui;color:#ffffff\">" + BotAccount.BotOrNot(((UserAccount) user).getName(), ((UserComments) comments).getScore(), ((UserSubmissions) submissions).getScore()) + "</h1>";

            m.addAttribute("uname", name);
            m.addAttribute("user", user);
            m.addAttribute("comments", comments);
            m.addAttribute("submissions", submissions);
            m.addAttribute("isBot", isaBot);
            m.addAttribute("db", "");

            return "result";
        }
    }

    /**
     * About page
     * @return about page
     */
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    /**
     * Javadoc Page
     * @return Javadoc Documentation
     */
    @GetMapping("/javadoc")
    public String javadoc() {
        return "javadoc";
    }

}


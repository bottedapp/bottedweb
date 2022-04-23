package app.botted.controller;

import app.botted.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Controller
public class WebController {
    @Autowired
    private DataSource dataSource;

    @RequestMapping("/")
    public String index(Model m, @RequestParam(value = "u", required = false) String name) throws IOException, InterruptedException, SQLException {
        if (name == null)
            return "index";
        else {
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
            m.addAttribute("db", "");
            //db();
            return "result";
        }
    }


    private void db() throws SQLException {
            Statement stmt = dataSource.getConnection().createStatement();
            //stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
            //stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
            ResultSet rs = stmt.executeQuery("SELECT * FROM comments");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
    }
}


package app.botted.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class WebController {
    @GetMapping({"/"})
    public String index(Model model, @RequestParam(value="name", required=false, defaultValue="Bot") String name) {
        model.addAttribute("name", name);
        return "index";
    }
}


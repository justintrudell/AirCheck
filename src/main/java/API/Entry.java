package API;

import spark.ModelAndView;
import spark.template.jade.JadeTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by vishalkuo on 2016-04-22.
 */
public class Entry {
    public static void main(String[] args){
        Map<String, String> map = new HashMap<>();
        map.put("message", "");

        get("/", (req, res) -> new ModelAndView(map, "hello"), new JadeTemplateEngine());


        get("/data", (req, res) -> {
            res.redirect("/");
            map.replace("message", "hey");
        return null;});
    }
}

package API;

import Models.Monoxide;
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

        // Route for forms
        get("/data", (request, response) -> {
            System.out.println(request.body());

            double latitude = Double.parseDouble(request.queryParams("latitude"));
            double longitude = Double.parseDouble(request.queryParams("longitude"));
            System.out.println(latitude);
            System.out.println(longitude);
            Monoxide mon = GetMonoxide.GetMonoxide(longitude, latitude);
            String rvm = mon != null ? String.valueOf(mon.getValue()) : "not found!";
            map.put("rvm", rvm);
            return null;
        });
    }

    public static String testMonoxide(){
        Monoxide m;
        try{
            m = GetMonoxide.GetMonoxide(0.0, 10.0);
        } catch(Exception e){
            return "";
        }
        return String.valueOf(m.getValue());
    }

}

package API;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by vishalkuo on 2016-04-22.
 */
public class test {
    static OkHttpClient client = new OkHttpClient();
    public static void main(String[] args){
        try{
            String test = run("http://api.openweathermap.org/pollution/v1/co/0.0,10.0/current.json?appid=2e5380c813086bfa4cff625b78e8996b");
//            System.out.println(test);
            JsonParser p = new JsonParser();
            JsonObject o= p.parse(test).getAsJsonObject();
            System.out.println(o.get("time"));

        } catch (Exception e){
            return;
        }

    }

    static String run(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private class myclass{
        public double test;

        public myclass(double test) {
            this.test = test;
        }


    }
}

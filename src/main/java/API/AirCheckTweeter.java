package API;

import Helpers.AirCheckConstants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import static org.eclipse.jetty.http.HttpParser.LOG;

/**
 * Created by Justin on 23/04/2016.
 */
public class AirCheckTweeter {

    static OkHttpClient client = new OkHttpClient();

    public static void TweetMessage(String message) throws Exception {
        Twitter twitter = TwitterFactory.getSingleton();
        Status status = twitter.updateStatus(message);
    }
}

package API;

import Helpers.AirCheckConstants;
import Models.Monoxide;
import Models.UserFeelings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Iterator;
import java.util.Map;

import static DAO.DataAccessObject.processUsers;

/**
 * Created by Justin on 23/04/2016.
 */
public class GetUser {

    public static void ProcessUserFeelings() throws Exception {
        String json = processUsers();
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
        for(JsonElement e : jsonArray) {
            System.out.println(e.toString());
        }
    }
}

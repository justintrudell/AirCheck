package API;

import Helpers.AirCheckConstants;
import Models.Coordinate;
import Models.Monoxide;
import Models.UserFeelings;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static DAO.DataAccessObject.processUsers;

public class AirCheckUser {

    public static ArrayList<UserFeelings> GetListOfUsers() throws Exception {
        String json = processUsers();
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
        ArrayList<UserFeelings> feels = new ArrayList<>();
        for(JsonElement e : jsonArray) {
            feels.add(new UserFeelings(e.getAsJsonObject().get("coughLevel").getAsInt(),
                    e.getAsJsonObject().get("howIsBreath").getAsInt(),
                    e.getAsJsonObject().get("wheezing").getAsInt(),
                    e.getAsJsonObject().get("sneezing").getAsInt(),
                    e.getAsJsonObject().get("noseBlock").getAsBoolean(),
                    e.getAsJsonObject().get("itchyEyes").getAsBoolean(),
                    e.getAsJsonObject().get("city").getAsString(),
                    e.getAsJsonObject().get("longitude").getAsDouble(),
                    e.getAsJsonObject().get("latitude").getAsDouble()));
        }
        return feels;
    }
}

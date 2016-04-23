package API;

import Models.Coordinate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import static DAO.DataAccessObject.processUsers;

public class AirCheckUser {

    public static ArrayList<Coordinate> GetListOfCoordinates() throws Exception {
        String json = processUsers();
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
        ArrayList<Coordinate> coordinateList = new ArrayList<>();
        for(JsonElement e : jsonArray) {
            coordinateList.add(new Coordinate(e.getAsJsonObject().get("longitude").getAsDouble(),
                    e.getAsJsonObject().get("latitude").getAsDouble()));
        }
        return coordinateList;
    }
}

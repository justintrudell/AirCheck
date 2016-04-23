package API;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetLocation {

    static OkHttpClient client = new OkHttpClient();

    public static String CoordsToCity(double longitude, double latitude) throws Exception {
        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&sensor=true";
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(response.body().string()).getAsJsonObject();
        JsonArray array = object.getAsJsonArray("results");
        JsonElement e = array.get(0);
        object = e.getAsJsonObject();
        array = object.getAsJsonArray("address_components");
        JsonElement finalElement = null;
        boolean complete = false;
        int j = 0;
        for(JsonElement element : array) {
            JsonArray arr = element.getAsJsonObject().getAsJsonArray("types");
            for(int i = 0; i < arr.size() - 1; i++) {
                if(arr.get(i).getAsString().equals("locality") && arr.get(i + 1).getAsString().equals("political")) {
                    finalElement = array.get(j);
                    break;
                }
            }
            if(complete) {
                break;
            }
            j++;
        }
        if(finalElement != null) {
            return finalElement.getAsJsonObject().getAsJsonPrimitive("long_name").getAsString();
        }
        return null;
    }
}

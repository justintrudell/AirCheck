package Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by jerry on 2016-04-22.
 */
public class Weather {
    public Weather(double temp, double humidity, double pressure, double longitude, double latitude) {
        this.temp = temp - 273;
        this.humidity = humidity;
        this.pressure = pressure;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // Getters
    public double getTemp() {
        return temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    // Setters
    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    private double temp;
    private double humidity;
    private double pressure;
    private double latitude;
    private double longitude;

    public void save(String city, double latitude, double longitude){

        try{
            Connection c = DriverManager.getConnection("jdbc:sqlite:userEntries.db");
            Statement stmt = c.createStatement();

            String sql = "select intensity from Cities where city = \"" + city + "\"";
            ResultSet rs = stmt.executeQuery(sql);
            double intensity = rs.next()? rs.getDouble("intensity"): 0;


            sql= "INSERT OR REPLACE INTO Cities (city, latitude, longitude, temp, humidity, pressure, intensity) VALUES(" +
                    "\""+city+"\","
                    + latitude + ","
                    + longitude + ","
                    +this.temp+","
                    +this.humidity+","
                    +this.pressure+ ", "
                    +intensity+")";

            System.out.println(sql);
            stmt.executeUpdate(sql);




            stmt.close();
            c.close();
        }catch(Exception e){
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}

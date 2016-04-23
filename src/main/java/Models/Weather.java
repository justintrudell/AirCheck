package Models;

/**
 * Created by jerry on 2016-04-22.
 */
public class Weather {
    public Weather(double temp, double humidity, double pressure, double longitude, double latitude) {
        this.temp = temp;
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
}

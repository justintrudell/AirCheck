package Models;

/**
 * Created by Justin on 22/04/2016.
 */
public class Monoxide {

    public Monoxide(double precision, double pressure, double value) {
        this.precision = precision;
        this.pressure = pressure;
        this.value = value;
    }

    // Takes in a parts-per-million value for carbon monoxide and
    // returns a string indicating air quality
    public static String ppmToQuality(double ppm) {
        String quality = "";
        if(ppm < 0) {
            quality = "Negative ppm?";
        }
        else if(ppm < 0.5) {
            quality = "Excellent";
        }
        else if(ppm < 1) {
            quality = "Good";
        }
        else if(ppm < 2) {
            quality = "Mediocre";
        }
        else if(ppm < 9) {
            quality = "Low";
        }
        else {
            quality = "Unsafe";
        }

        return quality;
    }

    private double precision;
    private double pressure;
    private double value;

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    // Get rmv
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

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
        double quality;
        if(ppm < 0.5) {
            quality = 0;
        }
        else if(ppm < 1) {
            quality = 1;
        }
        else if(ppm < 2) {
            quality = 2;
        }
        else if(ppm < 9) {
            quality = 3;
        }
        else {
            quality = 4;
        }

        return String.valueOf(quality);
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

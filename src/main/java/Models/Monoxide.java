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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

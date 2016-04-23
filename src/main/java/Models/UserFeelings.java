package Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DecimalFormat;

/**
 * Created by vishalkuo on 2016-04-22.
 */
public class UserFeelings {
    private int coughLevel;
    private int howIsBreath;
    private int wheezing;
    private int sneezing;
    private boolean noseBlock;
    private boolean itchyEyes;
    private String city;
    private double longitude;
    private double latitude;
    private double intensity;

    public UserFeelings(int coughLevel, int howIsBreath, int wheezing, int sneezing, boolean noseBlock, boolean itchyEyes, String city,
                        double longitude, double latitude) {
        this.coughLevel = coughLevel;
        this.howIsBreath = howIsBreath;
        this.wheezing = wheezing;
        this.sneezing = sneezing;
        this.noseBlock = noseBlock;
        this.itchyEyes = itchyEyes;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
        this.intensity = GetSymptomIntensity();
    }

    public void Save() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:userEntries.db");

            int _noseBlock = this.noseBlock ? 1 : 0;
            int _itchyEyes = this.itchyEyes ? 1 : 0;
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Users (" +
                    "cough_level int, " +
                    "breath int, " +
                    "wheezing int, " +
                    "sneezing int, " +
                    "nose_block boolean, " +
                    "itchy_eyes boolean, " +
                    "city text, " +
                    "longitude double, " +
                    "latitude double, " +
                    "intensity double)";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Users values(" +
                    this.coughLevel + "," +
                    this.howIsBreath + "," +
                    this.wheezing + "," +
                    this.sneezing + "," +
                    _noseBlock + "," +
                    _itchyEyes + ",'" +
                    this.city + "'," +
                    new DecimalFormat("#.###").format(this.longitude) + "," +
                    new DecimalFormat("#.###").format(this.latitude) + "," +
                    this.intensity +
                    ")";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public double GetSymptomIntensity() {
        double total = coughLevel + howIsBreath +
                wheezing*0.8 + sneezing*0.8;
        total += noseBlock ? 6 : 0;
        total += itchyEyes ? 6 : 0;
        return total;
    }

    public int getCoughLevel() {
        return coughLevel;
    }

    public int getHowIsBreath() {
        return howIsBreath;
    }

    public int getWheezing() {
        return wheezing;
    }

    public int getSneezing() {
        return sneezing;
    }

    public boolean isNoseBlock() {
        return noseBlock;
    }

    public boolean isItchyEyes() {
        return itchyEyes;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

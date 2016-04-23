package Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

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

    public UserFeelings(int coughLevel, int howIsBreath, int wheezing, int sneezing, boolean noseBlock, boolean itchyEyes, String city) {
        this.coughLevel = coughLevel;
        this.howIsBreath = howIsBreath;
        this.wheezing = wheezing;
        this.sneezing = sneezing;
        this.noseBlock = noseBlock;
        this.itchyEyes = itchyEyes;
        this.city = city;
    }

    public void Save() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");

            int _noseBlock = this.noseBlock ? 1 : 0;
            int _itchyEyes = this.itchyEyes ? 1 : 0;
            stmt = c.createStatement();
            String sql = "INSERT INTO Users values(" +
                    this.coughLevel + "," +
                    this.howIsBreath + "," +
                    this.wheezing + "," +
                    this.sneezing + "," +
                    _noseBlock + "," +
                    _itchyEyes + ", \'" +
                    this.city + "\'" +
                    ")";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
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
}

package Models;

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
    private double latitude;
    private double longitude;

    public UserFeelings(int coughLevel, int howIsBreath, int wheezing, int sneezing, boolean noseBlock, boolean itchyEyes, double latitude, double longitude) {
        this.coughLevel = coughLevel;
        this.howIsBreath = howIsBreath;
        this.wheezing = wheezing;
        this.sneezing = sneezing;
        this.noseBlock = noseBlock;
        this.itchyEyes = itchyEyes;
        this.latitude = latitude;

        this.longitude = longitude;
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

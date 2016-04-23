package API;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by vishalkuo on 2016-04-22.
 */
public class test {
   public static void main(String args[]){
       Connection c = null;
       try {
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection("jdbc:sqlite:test.db");
       } catch ( Exception e ) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
       }
       System.out.println("Opened database successfully");
   }
}

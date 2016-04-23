package DAO;

/**
 * Created by vishalkuo on 2016-04-23.
 */
public class DAOEntryMethodCaller {
    public static void main(String[] args){
        try{
//            new DataGeneration().GenerateData(2);
            createTables();
        }catch (Exception e){
            System.out.println("lol");
        }

    }

    public static void createTables(){
        try{
            DataAccessObject.createUsers();
            DataAccessObject.createCities();
        }catch(Exception e){
            return;
        }

    }
}

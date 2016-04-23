package DAO;

import Helpers.DataGeneration;

/**
 * Created by vishalkuo on 2016-04-23.
 */
public class DAOEntryMethodCaller {
    public static void main(String[] args){
        try{
            new DataGeneration().GenerateData(10);
        }catch (Exception e){
            System.out.println("lol");
        }

    }
}

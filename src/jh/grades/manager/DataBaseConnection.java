package jh.grades.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

class DataBaseConnection {

    private static final Connection conn;
    private static final String DB_NAME = "file.db";

    static {
        Connection aux = null;
        try{
            Class.forName("org.sqlite.JDBC");
            aux = DriverManager.getConnection("jdbc:sqlite:file.db");
        }catch (Exception e){
            e.printStackTrace(); // by now :)
        }
        conn = aux;
    }

    public static Connection getConnection(){
        return conn;
    }
}

package jh.grades.manager;

import java.sql.Connection;
import java.sql.DriverManager;

class DataBaseConnection {

    private static final Connection conn;
    private static final String DB_NAME = "file.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

    static {
        Connection aux = null;
        try{
            Class.forName("org.sqlite.JDBC");
            aux = DriverManager.getConnection(DB_URL);
        }catch (Exception e){
            e.printStackTrace(); // by now :)
        }
        conn = aux;
    }

    public static Connection getConnection(){
        return conn;
    }
}

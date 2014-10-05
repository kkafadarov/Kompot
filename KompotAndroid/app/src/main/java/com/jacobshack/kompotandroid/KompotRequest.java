package com.jacobshack.kompotandroid;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by kkafadarov on 10/4/14.
 */
public class KompotRequest {
    private static Connection connection;


    public static void sendRequest(String requestType, String requestData) {
        // Send an actual request to the server!
        return;
    }

    public static boolean authenticate(String name, String pass){

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Cant load driver?");
            e.printStackTrace();
        }


        try {
            connection = DriverManager
                    .getConnection("jdbc:mysql://54.77.217.249/", "root", "H4ck4R007");

        } catch (SQLException e) {
            System.out.println("Cant connect");
            e.printStackTrace();
        }

        try {
            Statement statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Cant get statement");
        }


        return true;
    }





    public static int pairStudentAndExam(String imageFilePath, String uniqueId) {
        return 0;
    }

}

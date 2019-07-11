import javax.swing.*;
import java.sql.*;

class ResponseDB {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/";

    private static final String USER = "root";
    private static final String PASSWORD = "di-engg-response";

    private static final String DB_NAME = "RESPONSE_DB";
    private static final String TABLE_NAME = "RESPONSES";
    private static final String COL_PLC = "plc";
    private static final String COL_RESPONSE = "response";

    private static final String CREATE_DB = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COL_PLC + " INT, "
            + COL_RESPONSE + " TEXT)";
    private static final String INSERT_RESPONSE = "INSERT INTO " + TABLE_NAME + " (" + COL_PLC + "," + COL_RESPONSE +
            ") VALUES (?, ?)";
    private static final String SELECT_PLACEMENTS = "SELECT " + COL_PLC + " FROM " + TABLE_NAME;
    private static final String DELETE_PLACEMENT = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_PLC + " = ?";
    private static final String SELECT_PLACEMENT = "SELECT " + COL_RESPONSE + " FROM " + TABLE_NAME + " WHERE " + COL_PLC + " = ?";

    ResponseDB() throws ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        createDBIfNotExists();
    }

    private void createDBIfNotExists() {
        try {
            System.out.println("Connecting to DB ...");
            final Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            System.out.println("Creating DB ...");
            final Statement statement = connection.createStatement();

            statement.execute(CREATE_DB);
            System.out.println("Database created successfully ...");

            statement.close();
            connection.close();

            createTableIfNotExists();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTableIfNotExists() {
        try {
            System.out.println("Connecting to DB ...");
            final Connection connection = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASSWORD);
            final Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_TABLE);
            System.out.println("Table create successfully");

            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean addResponse(String plc, String response) {
        int result = 0;

        try {
            System.out.println("Connecting to DB ...");
            final Connection connection = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASSWORD);

            System.out.println("Creating Query ...");
            final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_RESPONSE);
            preparedStatement.setString(1, plc);
            preparedStatement.setString(2, response);
            result = preparedStatement.executeUpdate();
            System.out.println("Response addition result : " + result);

            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result > 0;
    }

    DefaultListModel<String> getPlacements() {
        DefaultListModel<String> placements = new DefaultListModel<>();
        try {
            System.out.println("Connecting to DB ...");
            final Connection connection = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASSWORD);
            final Statement statement = connection.createStatement();

            System.out.println("Creating Query ...");
            ResultSet resultSet = statement.executeQuery(SELECT_PLACEMENTS);
            while (resultSet.next()) {
                placements.addElement(resultSet.getString(COL_PLC));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return placements;
    }

    String getResponse(String placement) {
        String response = "";

        try {
            System.out.println("Connecting to DB ...");
            final Connection connection = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASSWORD);
            final Statement statement = connection.createStatement();

            System.out.println("Creating Query ...");
            final PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PLACEMENT);
            preparedStatement.setString(1, placement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                response = resultSet.getString(COL_RESPONSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    boolean removePlacement(String placement) {
        int result = 0;
        try {
            System.out.println("Connecting to DB ...");
            final Connection connection = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASSWORD);

            System.out.println("Creating Query ...");
            final PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PLACEMENT);
            preparedStatement.setString(1, placement);
            result = preparedStatement.executeUpdate();
            System.out.println("Response deletion result : " + result);

            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result > 0;
    }
}

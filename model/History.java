package logic;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by Поздяев on 31.05.2015.
 */
public class History {

    private String driver;
    private Connection connection;
    private String url;
    private String name;
    private String password;

    private Connection getConnection () throws SQLException {
        return DriverManager.getConnection(url, name, password);
    }

    public History() {
        Properties props = new Properties();

        try {
            FileInputStream in = new FileInputStream(System.getProperty("user.dir")+ "\\model\\config.properties");
            props.load(in);
            in.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        driver = props.getProperty("Database.Driver");
        url = props.getProperty("Database.DataURL");
        name = props.getProperty("Database.Prop.user");
        password = props.getProperty("Database.Prop.password");

    }

    public void addStory(String login, String text){
        try {
            // Load driver
            Class.forName(driver);
            // Create connection
            connection = getConnection();

            Statement stat = connection.createStatement();

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Date today = Calendar.getInstance().getTime();

            String reportDate = df.format(today);

            String sql = "insert into history(login, data, text) values ('" + login
                    + "', to_date('" + reportDate + "', 'MM.dd.yyyy'), '"+ text + "')";

            stat.execute(sql);

        }
        catch (SQLException sqle){
            sqle.printStackTrace();
        }
        catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }
    }

    public HashMap<Date, String> getHistory(String login){

        HashMap<Date, String> history = new HashMap<Date, String>();

        try {
            // Load driver
            Class.forName(driver);
            // Create connection
            connection = getConnection();

            Statement stat = connection.createStatement();

            String sql = "SELECT h.date, h.text FROM history h WHERE h.login = '" + login + "'";

            try (ResultSet result = stat.executeQuery(sql)) {

                while (result.next()) {

                    history.put(result.getDate("date"), result.getString("text"));
                }
            }


        }
        catch (SQLException sqle){
            sqle.printStackTrace();
        }
        catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }


        return history;
    }
}
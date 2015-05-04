package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.*;
import view.*;


public class Container {

    final static private String driver = "oracle.jdbc.OracleDriver";

    private List<Object> objects;
    private Connection connection;
    private String url;
    private String name;
    private String password;

    public Container(){
        connection = null;
        url = "jdbc:oracle:thin:@edu-netcracker.com:1520:XE";
        name = "project_lab";
        password = "project_lab1";

        objects = new ArrayList<Object>();
    }

    public Container(String _url, String _name, String _password){
        connection = null;
        url = _url;
        name = _name;
        password = _password;

        objects = new ArrayList<Object>();
    }

    private Connection getConnection () throws SQLException{
        return DriverManager.getConnection(url, name, password);
    }

    public List<Object> getObjects(String condition){

        try {
            //Загружаем драйвер
            Class.forName(driver);
            //Создаём соединение
            connection = getConnection();


            // Отбираем объекты по фильтру
            String sql = "SELECT o.object_id, o.object_type_id, o.name, ot.name t_name " +
                    " FROM objects o, object_types ot, references r, attributes a, params p " +
                    " WHERE o.object_id = r.object_id " +
                    " AND r.attribute_id = a.attribute_id " +
                    " AND o.object_type_id = ot.object_type_id " +
                    " AND o.object_id = p.object_id " +
                    " AND a.attribute_id = p.attribute_id " + condition;

            Statement stat = connection.createStatement();
            try (ResultSet result = stat.executeQuery(sql))
            {
                while (result.next()){
                    Object tmp = new Object(result.getInt("object_id"), result.getInt("object_type_id"),
                            result.getString("name"), result.getString("t_name"));
                    objects.add(tmp);
                }
            }

            // Получаем параметры объектов
            String sql1 = "SELECT a.name, a_t.value_type, p.number_value, p.text_value, p.date_value, p.data_value " +
                    " FROM objects o, references r, attributes a, attribute_types a_t, params p " +
                    " WHERE o.object_id = ?" +
                    " AND o.object_id = r.object_id " +
                    " AND r.attribute_id = a.attribute_id " +
                    " AND a.attribute_type_id = a_t.attribute_type_id " +
                    " AND o.object_id = p.object_id " +
                    " AND a.attribute_id = p.attribute_id ";
            PreparedStatement preparedStatement = null;

            for (Object currObj: objects) {
                preparedStatement = connection.prepareStatement(sql1);
                preparedStatement.setInt(1, currObj.getObject_id());
                try (ResultSet result1 = preparedStatement.executeQuery()) {
                    while (result1.next()) {
                        currObj.addParams(result1.getString("name"), result1.getString(result1.getInt(2) + 2));
                    }
                }
            }

        }
        catch (SQLException sqle){
                sqle.printStackTrace();
            }
        catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }

        return objects;
    }

    public List<String> getClubNames(){
        List<String> names = new ArrayList<String>();

        try {
            //Загружаем драйвер
            Class.forName(driver);
            //Создаём соединение
            connection = getConnection();


            // Отбираем клуб
            String sql = "SELECT o.name " +
                    " FROM objects o, object_types ot" +
                    " WHERE o.object_type_id = ot.object_type_id " +
                    " AND ot.name = 'Фитнес-клуб'";


            Statement stat = connection.createStatement();
            try (ResultSet result = stat.executeQuery(sql)) {
                while (result.next()) {
                    names.add(result.getString(1));
                }
            }
        }
        catch (SQLException sqle){
            sqle.printStackTrace();
        }
        catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }

        return names;
    }

    public List<Club> getClubs(ClubFilter filter){
        List<Club> clubs = new ArrayList<Club>();

        try {
        // Load driver
        Class.forName(driver);
        // Create connection
        connection = getConnection();

        // SQL query for clubs
        String sql = "SELECT DISTINCT o.object_id, o.name, o.description" +
                " FROM objects o, object_types ot, references r, attributes a, params p " +
                " WHERE o.object_type_id = ot.object_type_id" +
                " AND o.object_id = r.object_id " +
                " AND r.attribute_id = a.attribute_id " +
                " AND o.object_id = p.object_id " +
                " AND a.attribute_id = p.attribute_id " +
                " AND ot.name = 'Фитнес-клуб'" + filter.toString();

            // SQL query for club's address&phone
            String sqlFilter = "SELECT p.text_value" +
                    " FROM objects o, references r, attributes a, params p" +
                    " WHERE o.object_id = ?" +
                    " AND o.object_id = r.object_id" +
                    " AND r.attribute_id = a.attribute_id" +
                    " AND o.object_id = p.object_id" +
                    " AND a.attribute_id = p.attribute_id ";
            String sqlAddress = " AND a.name = 'Адрес'";
            String sqlPhone = " AND a.name = 'Телефон' ";

            // SQL query for club's statistics
            String sqlAvg = "select club_pack.avgPriceCard(?) from dual";
            String sqlNum = "select club_pack.numberOfCard(?) from dual";


            String address = "", phone = "";
            int avg = 0, num = 0;

            Statement stat = connection.createStatement();
            PreparedStatement preparedStatement = null;


            try (ResultSet result = stat.executeQuery(sql)) {

                while (result.next()) {

                    int id = result.getInt("object_id");

                    preparedStatement = connection.prepareStatement(sqlFilter+sqlAddress);
                    preparedStatement.setInt(1, id);

                    try (ResultSet result1 = preparedStatement.executeQuery();) {
                        while (result1.next()) {
                            address = result1.getString(1);
                        }
                    }

                    preparedStatement = connection.prepareStatement(sqlFilter+sqlPhone);
                    preparedStatement.setInt(1, id);

                    try (ResultSet result1 = preparedStatement.executeQuery();) {
                        while (result1.next()) {
                            phone = result1.getString(1);
                        }
                    }

                    preparedStatement = connection.prepareStatement(sqlAvg);
                    preparedStatement.setInt(1, id);

                    try (ResultSet result1 = preparedStatement.executeQuery();) {
                        while (result1.next()) {
                            avg = result1.getInt(1);
                        }
                    }

                    preparedStatement = connection.prepareStatement(sqlNum);
                    preparedStatement.setInt(1, id);
                    //result1 = preparedStatement.executeQuery();
                    try (ResultSet result1 = preparedStatement.executeQuery();) {
                        while (result1.next()) {
                            num = result1.getInt(1);
                        }
                    }


                    clubs.add(new Club(result.getString("name"), address, phone, avg, num, result.getString("description")));
                }
            }



    }
    catch (SQLException sqle){
        sqle.printStackTrace();
    }
    catch (ClassNotFoundException cnfe){
        cnfe.printStackTrace();
    }

        // Сортируем клубы по пулярности
        if (filter.isPopularity()) Collections.sort(clubs);

        return clubs;
    }

    public List<Card> getCards(CardFilter filter){
        List<Card> cards = new ArrayList<Card>();

        try {
            // Load driver
            Class.forName(driver);
            // Create connection
            connection = getConnection();

            // SQL query for cards
            String sql = "SELECT DISTINCT o.object_id, o.name, o.description" +
                    " FROM objects o, object_types ot, references r, attributes a, params p " +
                    " WHERE o.object_type_id = ot.object_type_id" +
                    " AND o.object_id = r.object_id " +
                    " AND r.attribute_id = a.attribute_id " +
                    " AND o.object_id = p.object_id " +
                    " AND a.attribute_id = p.attribute_id " +
                    " AND ot.name = 'Абонемент' " + filter.toString();

            // SQL query for card's information
            String sqlFilter = " FROM objects o, references r, attributes a, params p" +
                    " WHERE o.object_id = ?" +
                    " AND o.object_id = r.object_id" +
                    " AND r.attribute_id = a.attribute_id" +
                    " AND o.object_id = p.object_id" +
                    " AND a.attribute_id = p.attribute_id ";
            String sqlPrice = " AND a.name = 'Цена(руб.)'";
            String sqlValidity = " AND a.name = 'Срок действия' ";
            String sqlHavePool = " AND a.name = 'Наличие бассейна' ";
            String sqlType = " AND a.name = 'Тип карты' ";


            int price = 0, validity = 0;
            boolean pool = false;
            String type = "";

            Statement stat = connection.createStatement();
            PreparedStatement preparedStatement = null;


            try (ResultSet result = stat.executeQuery(sql)) {

                while (result.next()) {
                    // Get card's id
                    int id = result.getInt("object_id");
                    // Get price
                    preparedStatement = connection.prepareStatement("SELECT p.number_value" + sqlFilter + sqlPrice);
                    preparedStatement.setInt(1, id);

                    try (ResultSet result1 = preparedStatement.executeQuery();) {
                        while (result1.next()) {
                            price = result1.getInt(1);
                        }
                    }
                    // Get validity(Срок действия)
                    preparedStatement = connection.prepareStatement("SELECT p.number_value" + sqlFilter + sqlValidity);
                    preparedStatement.setInt(1, id);

                    try (ResultSet result1 = preparedStatement.executeQuery();) {
                        while (result1.next()) {
                            validity = result1.getInt(1);
                        }
                    }
                    // Get information about swimming pool
                    preparedStatement = connection.prepareStatement("SELECT p.text_value" + sqlFilter + sqlHavePool);
                    preparedStatement.setInt(1, id);

                    try (ResultSet result1 = preparedStatement.executeQuery();) {
                        while (result1.next()) {
                            if (result1.getString(1).equals("Да")) pool = true;
                        }
                    }
                    // Get card's type
                    preparedStatement = connection.prepareStatement("SELECT p.text_value" + sqlFilter+sqlType);
                    preparedStatement.setInt(1, id);
                    //result1 = preparedStatement.executeQuery();
                    try (ResultSet result1 = preparedStatement.executeQuery();) {
                        while (result1.next()) {
                            type = result1.getString(1);
                        }
                    }

                    // Add new card to list
                    cards.add(new Card(id, result.getString("name"), price, validity, pool, type, result.getString("description")));
                }
            }



        }
        catch (SQLException sqle){
            sqle.printStackTrace();
        }
        catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }

        // Сортируем карты по пулярности
        if (filter.isPopularity()) Collections.sort(cards);

        return cards;

    }
}

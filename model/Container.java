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

            // Вычисляем диапазон выводимого результата
            int startPage = (filter.getPage()-1)*filter.getCountOnPage() + 1;
            int finishPage = startPage + filter.getCountOnPage() - 1;

            // SQL query for clubs
            String sql = "SELECT qw.* " +
                    " from " +
                    " (" +
                    " SELECT o.object_id id, " +
                    "       o.name name, " +
                    "       address.text_value address, " +
                    "       phone.text_value phone, " +
                    "       (select club_pack.avgPriceCard(o.object_id) from dual ) avgPr, " +
                    "       (select club_pack.numberOfCard(o.object_id) from dual ) numCr, " +
                    "       o.description descr, " +
                    "       row_number() over (order by o.name) rn " +
                    " from objects o " +
                    " join params address " +
                    "     on address.object_id = o.object_id " +
                    "     and address.attribute_id = 21 " +
                    " join params phone " +
                    "     on phone.object_id = o.object_id " +
                    "     and phone.attribute_id = 22 " +
                    " join params district " +
                    "     on district.object_id = o.object_id " +
                    "     and district.attribute_id = 41 " +
                    " where 1=1 "
                    + filter.toString() +
                    " ) qw " +
                    " where qw.rn between " + startPage + " and " + finishPage;

            if (filter.isPopularity()) sql+= " order by qw.name, qw.avgPr, qw.numCr ";



            Statement stat = connection.createStatement();


            try (ResultSet result = stat.executeQuery(sql)) {

                while (result.next()) {

                    clubs.add(new Club(result.getString("name"), result.getString("address"),
                            result.getString("phone"), result.getInt("avgPr"),
                            result.getInt("numCr"), result.getString("descr")));
                }
            }



        }
        catch (SQLException sqle){
            sqle.printStackTrace();
        }
        catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }

        return clubs;
    }

    public List<Card> getCards(CardFilter filter){

        List<Card> cards = new ArrayList<Card>();

        try {
            // Load driver
            Class.forName(driver);
            // Create connection
            connection = getConnection();

            // Вычисляем диапазон выводимого результата
            int startPage = (filter.getPage()-1)*filter.getCountOnPage() + 1;
            int finishPage = startPage + filter.getCountOnPage() - 1;

            // SQL query for cards
            String sql = "SELECT qw.*" +
                    "from " +
                    "     (" +
                    "       SELECT o.object_id id, " +
                    "              o.name name, " +
                    "              cost.number_value cost, " +
                    "              validity.number_value validity, " +
                    "              pool.text_value pool, " +
                    "              o.description descr, " +
                    "              district.text_value distr, " +
                    "              club.name club_name," +
                    "              row_number() over (order by o.name) rn " +
                    "      from objects o " +
                    "       join params cost " +
                    "        on cost.object_id = o.object_id" +
                    "        and cost.attribute_id = 13" +
                    "       join params validity " +
                    "        on validity.object_id = o.object_id " +
                    "        and validity.attribute_id = 40" +
                    "       join params pool " +
                    "        on pool.object_id = o.object_id " +
                    "        and pool.attribute_id = 61" +
                    "       join params district " +
                    "        on district.object_id = o.parent_id" +
                    "        and district.attribute_id = 41" +
                    "       join objects club" +
                    "        on club.object_id = o.parent_id" +
                    "      where 1=1 "
                    + filter.toString() +
                    " ) qw " +
                    " where qw.rn between " + startPage + " and " + finishPage;

            if (filter.isPopularity()) sql+= " order by qw.name, qw.cost, qw.validity ";



            Statement stat = connection.createStatement();


            try (ResultSet result = stat.executeQuery(sql)) {

                while (result.next()) {

                    boolean pool = false;
                    if (filter.isHavePool()) pool = true;

                    cards.add(new Card(result.getInt("id"), result.getString("name"),
                            result.getInt("cost"), result.getInt("validity"), pool
                            , "", result.getString("descr")));
                }
            }



        }
        catch (SQLException sqle){
            sqle.printStackTrace();
        }
        catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }

        return cards;
    }
}

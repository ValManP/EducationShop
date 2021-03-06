package logic;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.sql.*;
import view.*;


public class Container {

    private String driver;
    private Connection connection;
    private String url;
    private String name;
    private String password;

    public Container() throws IOException {

        Properties props = new Properties();

        try {
            FileInputStream in = new FileInputStream(System.getProperty("user.dir")+ "\\src\\logic\\config.properties");
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

    public Container(String _url, String _name, String _password){
        connection = null;
        url = _url;
        name = _name;
        password = _password;

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
                    "       district.text_value distr, " +
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
                            result.getInt("numCr"), result.getString("descr"), result.getString("distr")));
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

                    cards.add(new Card(result.getInt("id"), result.getString("name"),result.getString("club_name"),
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

    public void addCard(Card newCard){

        try {
            //Загружаем драйвер
            Class.forName(driver);
            //Создаём соединение
            connection = getConnection();

            Statement stat = connection.createStatement();


            // Отбираем клуб
            String sql = "begin " +
                    " object_pack.add_obj('Абонемент', '" + newCard.getName()  + "', '" +
                    newCard.getClubName() + "'); " +
                    " end;";



            stat.execute(sql);

            String sqlParams = " begin object_pack.add_obj_params(?, ?, ?, ?); end;";

            PreparedStatement param = connection.prepareStatement(sqlParams);

            param.setString(1, newCard.getName());

            // Price
            param.setString(2, "Цена(руб.)");
            param.setInt(3, newCard.getPrice());
            param.setNull(4, 0);
            param.execute();

            // Validity
            param.setString(2, "Срок действия"); param.setInt(3, newCard.getValidity()); param.setNull(4, 0);
            param.execute();

            // Pool
            String pool; if (newCard.isHavePool()) pool = "Да"; else pool = "Нет";
            param.setString(2, "Бассейн");
            param.setNull(3, 0); param.setString(4, pool);
            param.execute();


        }
        catch (SQLException sqle){
            sqle.printStackTrace();
        }
        catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }
    }

    public void addClub(Club newClub){

        try {
            //Загружаем драйвер
            Class.forName(driver);
            //Создаём соединение
            connection = getConnection();

            Statement stat = connection.createStatement();


            // Отбираем клуб
            String sql = "begin " +
                    " object_pack.add_obj('Фитнес-клуб', '" + newClub.getName() + "'); " +
                    " end;";



            stat.execute(sql);

            String sqlParams = " begin object_pack.add_obj_params(?, ?, ?, ?); end;";

            PreparedStatement param = connection.prepareStatement(sqlParams);

            param.setString(1, newClub.getName());

            // Address
            param.setString(2, "Адрес");
            param.setNull(3, 0);
            param.setString(4, newClub.getAddress());
            param.execute();

            // Phone
            param.setString(2, "Телефон");
            param.setNull(3, 0);
            param.setString(4, newClub.getPhone());
            param.execute();

            // District
            param.setString(2, "Район");
            param.setNull(3, 0);
            param.setString(4, newClub.getDistrict());
            param.execute();

        }
        catch (SQLException sqle){
            sqle.printStackTrace();
        }
        catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }
    }

    public Map<Integer, String> getUserAttributes(String role){
        Map<Integer, String> m = new HashMap<>();

        try {
            // Load driver
            Class.forName(driver);
            // Create connection
            connection = getConnection();

            String sql = "SELECT a.attribute_id, a.name FROM attributes a, attribute_bind ab " +
                    "WHERE  a.attribute_id = ab.attribute_id AND ab.object_type_id = ";
            if (role.equals("ROLE_USER")) sql+="4"; else sql+="2";

            Statement stat = connection.createStatement();

            try (ResultSet result = stat.executeQuery(sql)) {

                while (result.next()) {
                    m.put(result.getInt(1),result.getString(2));
                }
            }

        }
        catch (SQLException sqle){
            sqle.printStackTrace();
        }
        catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }

        return m;
    }

    public void userRegistration(Map<Integer, String> data, String role){
        try {
            // Load driver
            Class.forName(driver);
            // Create connection
            connection = getConnection();

            int obj_type_id = 0, obj_id = 0;
            String name = data.get(5);
            // Создаем новый объект(клуб и пользователь)
            if (role.equals("ROLE_USER")) obj_type_id=4; else obj_type_id = 2;
            String sql = "insert into objects(object_type_id, name)" +
                    " values (" + obj_type_id + ", '" + name+"')";

            Statement stat = connection.createStatement();
            stat.execute(sql);

            sql = "select o.object_id from objects o where o.name = '" + name +"'";
            try (ResultSet result = stat.executeQuery(sql)) {

                while (result.next()) {
                    obj_id = result.getInt(1);
                }
            }

            String sqlParams = "begin object_pack.add_obj_params(?, ?, ?, ?); end;";

            PreparedStatement param = connection.prepareStatement(sqlParams);

            param.setString(1, name);
            param.setNull(3, 0);
            param.setNull(4, 0);

            String tmp = "SELECT a.name, a_t.value_type FROM attributes a, attribute_types a_t" +
                    " WHERE a_t.attribute_type_id = a.attribute_type_id" +
                    " AND a.attribute_id = " ;
            //Set<Map.Entry> s = new HashSet<>();
            int type = 0;

            for (Map.Entry currObj: data.entrySet()){

                try (ResultSet result = stat.executeQuery(tmp+currObj.getKey())) {
                    while (result.next()) {
                        name = result.getString(1);
                        type = result.getInt(2);
                    }
                }

                param.setString(2, name);
                param.setNull(3, 0);
                param.setNull(4, 0);

                if (type == 1) param.setInt(3, (int)currObj.getValue());
                else param.setString(4,currObj.getValue().toString());

                param.execute();
            }
            // Set "enabled"
            param.setString(2, "enabled");
            param.setNull(3, 0);
            param.setString(4, "TRUE");
            param.execute();

            // Set "authority"
            param.setString(2, "authority");
            param.setNull(3, 0);
            if (role.equals("ROLE_USER")) param.setString(4, "ROLE_USER");
            else param.setString(4, "ROLE_CLUB");
            param.execute();

        }
        catch (SQLException sqle){
            sqle.printStackTrace();
        }
        catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }
    }
}

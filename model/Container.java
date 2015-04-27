package logic;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;


public class Container {

    final static private String driver = "oracle.jdbc.OracleDriver";

    private List<Object> objects;
    private Connection connection;
    private String url;
    private String name;
    private String password;

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

    public List<Club> getClubs(){
        List<Club> clubs = new ArrayList<Club>();

        try {
        //Загружаем драйвер
        Class.forName(driver);
        //Создаём соединение
        connection = getConnection();

        // Получаем параметры объектов
        String sql = "SELECT o.object_id, o.name, o.description, p.text_value" +
                " FROM objects o, object_types ot, references r, attributes a, params p" +
                " WHERE o.object_id = r.object_id" +
                " AND r.attribute_id = a.attribute_id" +
                " AND o.object_type_id = ot.object_type_id" +
                " AND o.object_id = p.object_id" +
                " AND a.attribute_id = p.attribute_id";
            String sqlAddress = " AND a.name = 'Адрес'";
            String sqlPhone = " AND a.name = 'Телефон'";
            String sqlAvg = "select club_pack.avgPriceCard(?) from dual";
            String sqlNum = "select club_pack.numberOfCard(?) from dual";

            Statement stat = connection.createStatement();
            PreparedStatement preparedStatement = null;
            //try (ResultSet result = stat.executeQuery(sql1))

                ResultSet resultAddress = stat.executeQuery(sql+sqlAddress);
                ResultSet resultPhone = stat.executeQuery(sql+sqlPhone);
            while (resultAddress.next()) {
                //resultPhone.next();
                int id = resultAddress.getInt("object_id");
                String avg = "", num = "";

                preparedStatement = connection.prepareStatement(sqlAvg);
                preparedStatement.setInt(1, id);

                try (ResultSet result1 = preparedStatement.executeQuery();) {
                    while (result1.next()) {
                        avg = result1.getString(1);
                    }
                }

                preparedStatement = connection.prepareStatement(sqlNum);
                preparedStatement.setInt(1, id);
                //result1 = preparedStatement.executeQuery();
                try (ResultSet result1 = preparedStatement.executeQuery();) {
                    while (result1.next()) {
                        num = result1.getString(1);
                    }
                }


                clubs.add(new Club(resultAddress.getString("name"), resultAddress.getString("text_value"),
                        "LOL", avg, num, resultAddress.getString("description")));
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
}

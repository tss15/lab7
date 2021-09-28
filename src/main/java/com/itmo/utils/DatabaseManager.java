package com.itmo.utils;

import com.itmo.client.User;
import com.itmo.collection.*;
//import com.itmo.server.url.SshConnection;
import com.itmo.server.url.UrlGetterDirectly;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DatabaseManager implements MyCRUD  {

    private static final String DB_URL = new UrlGetterDirectly().getUrl() ;
    private static String USER;
    private static String PASS;
    private static final String COLLECTION_TABLE = "labworks";
    private static final String FILE_WITH_ACCOUNT = "account";
    private static final String USERS_TABLE = "users";
    private static final String pepper = "1@#$&^%$)3";


    //we read the account data to enter the connection to the database, we are looking for a driver
    static {
        try (FileReader fileReader = new FileReader(FILE_WITH_ACCOUNT);
             BufferedReader reader = new BufferedReader(fileReader)) {
            USER = "s313320";
            PASS = "fsg890";
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Connection to PostgreSQL JDBC");
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL JDBC Driver successfully connected");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path");
            e.printStackTrace();
        }
    }

    private Connection connection;
    private PassEncoder passEncoder;

    public DatabaseManager(String dbUrl, String user, String pass) {
        try {
            connection = DriverManager.getConnection(dbUrl, user, pass);
            passEncoder = new PassEncoder();
            System.out.println("Database initialized: " + dbUrl);
        } catch (SQLException e) {
            System.out.println("Connection to database failed");
            e.printStackTrace();
        }
    }

    public DatabaseManager(String address, int port, String dbName, String user, String pass) {
        this("jdbc:postgresql://" + address + ":" + port + "/" + dbName, user, pass);
    }

    public DatabaseManager() {
        this(DB_URL, USER, PASS);
    }

    public boolean insertLabWork(LabWork d)  {
        try {
            String drtype = null;

            if (d.getType() != null) drtype = d.getType().name();
            String date =d.getCreationDateInFormat();
            String state = "INSERT INTO " + COLLECTION_TABLE +
                    "(labwork_name," +
                    " reg_date," +
                    " weight," +
                    " minimalpoint," +
                    " hair_color," +
                    " difficulty, " +
                    "owner)\n" +
                    "VALUES (?, '" + date + "', ?, ?, ?::hair_color, ?::difficulty, ?)";
            PreparedStatement labWorkItself = connection.prepareStatement(state);

            labWorkItself.setString(1, d.getName());
            labWorkItself.setInt(2, d.getWeight());
            labWorkItself.setFloat(3, d.getMinimalpoint());
            labWorkItself.setString(4, drtype);
            labWorkItself.setString(5, d.getDifficulty().name());
            labWorkItself.setString(6, d.getOwnerName());
            labWorkItself.executeUpdate();

            PreparedStatement labWorkCoords = connection.prepareStatement(
                    "INSERT INTO coordinates(labwork_id,x,y)" +
                            "VALUES (currval('generate_id'), ?, ?)");
            labWorkCoords.setLong(1, d.getCoordinates().getX());
            labWorkCoords.setDouble(2, d.getCoordinates().getY());
            labWorkCoords.executeUpdate();

            Person person = d.getPerson();
            if (person != null) {
                String hair = person.getHairColor() == null ?
                        "NULL" : person.getHairColor().name();
                String nati = person.getNationality() == null ?
                        "NULL" : person.getNationality().name();
                PreparedStatement labWorkAuthor =
                        connection.prepareStatement(
                                "INSERT INTO authors(labwork_id, author_name, birthday, color, country)" +
                                        "VALUES (currval('generate_id'), ?,'"
                                        +person.getBirthdayInFormat() + "', ?::color, ?::country)"
                        );
                labWorkAuthor.setString(1, person.getName());
                labWorkAuthor.setString(2, hair);
                labWorkAuthor.setString(3, nati);
                labWorkAuthor.executeUpdate();

                Location loc = person.getLocation();
                PreparedStatement AuthorLoc =
                        connection.prepareStatement("INSERT INTO locations( labwork_id, x,y,z,loc_name)\n" +
                                "VALUES (currval('generate_id'), ?, ?, ?, ?)");
                AuthorLoc.setInt(1, loc.getX());
                AuthorLoc.setLong(2, loc.getY());
                AuthorLoc.setFloat(3, loc.getZ());
                AuthorLoc.setString(4, loc.getName());
                AuthorLoc.executeUpdate();
            }
            return true;
        }catch (SQLException e){
            System.out.println("An error occurred while adding an item to the database.");
            e.printStackTrace();
            return false;
        }
    }




    public Set<LabWork> getCollectionFromDatabase() throws SQLException {
        PreparedStatement statement =
                connection.prepareStatement(
                        "SELECT * FROM "+ COLLECTION_TABLE + " ds\n" +
                                "    inner join coordinates dc\n" +
                                "on ds.id = dc.labwork_id\n" +
                                "    left outer join authors dk\n" +
                                "    on dk.labwork_id = ds.id\n" +
                                "    left outer join locations kl\n" +
                                "    on kl.labwork_id=ds.id"
                );
        ResultSet resultSet = statement.executeQuery();
        HashSet<LabWork> labWorks = new HashSet<>();
        while (resultSet.next()) {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("labwork_name");
            Date date = resultSet.getDate("reg_date");
            int age = resultSet.getInt("weight");
            float wingspan = resultSet.getFloat("minimalpoint");
            String strType = resultSet.getString("hair_color");
            HairColor type = strType == null ? null : Enum.valueOf(HairColor.class, strType);
            Difficulty character = Enum.valueOf(Difficulty.class, resultSet.
                    getString("difficulty"));
            String ownerName = resultSet.getString("owner");
            Coordinates coordinates = new Coordinates(resultSet.getInt("x"), resultSet.getLong("y"));
            Person person = null;
            if(resultSet.getString("author_name")!=null){
                person = new Person(
                        resultSet.getString("author_name"),
                        DateTimeAdapter.convertToLocalDateViaInstant(resultSet.getDate("birthday")),
                        Enum.valueOf(com.itmo.collection.Color.class, resultSet.getString("color")),
                        Enum.valueOf(Country.class, resultSet.getString("country")),
                        new Location(
                                resultSet.getInt("x"),
                                resultSet.getLong("y"),
                                resultSet.getFloat("z"),
                                resultSet.getString("loc_name"))
                );
            }
            LabWork labWork = new LabWork(name, coordinates, date, age, wingspan, type, character, person);
            labWork.setOwnerName(ownerName);
            labWork.setId(id);
            User user = new User(ownerName);
            labWork.setUser(user);
            labWorks.add(labWork);
        }
        return (Collections.synchronizedSet(labWorks));
    }


    @Override
    public boolean deleteLabWorkById(long id) {
        String sqlRequest =
                "DELETE FROM " + COLLECTION_TABLE + " WHERE id=" + id;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlRequest);
            statement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public long getIdOflabWork(LabWork d){
        String sqlRequest =
                "select id FROM " + COLLECTION_TABLE +
                        " WHERE owner=? " +
                        "and labWork_name=?" +
                        " and wingspan=?" +
                        " and labWork_character=?::labWork_character" +
                        " and age=?" +
                        " and reg_date='"; //+ d.getCreationDateInFormat() + "'";

        try {
            PreparedStatement statement = connection.prepareStatement(sqlRequest);
            statement.setString(1, d.getOwnerName());
            statement.setString(2, d.getName());
            statement.setFloat(3, d.getMinimalpoint());
            statement.setString(4, d.getDifficulty().name());
            statement.setInt(5, d.getWeight());
            ResultSet set = statement.executeQuery();
            if (set.next()){
                return set.getInt("id");
            }
            return 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public boolean containsUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from " + USERS_TABLE + " where login = ?");
            statement.setString(1, user.getName());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return false;
            String salt = resultSet.getString("salt");
            String hash = passEncoder.getHash(user.getHashPass() + salt, "1@#$&^%$)3");
            statement = connection.prepareStatement("select * from " + USERS_TABLE + " where login = ? " +
                    "and hashpass = ? and salt=?");
            statement.setString(1, user.getName());
            statement.setString(2, hash);
            statement.setString(3, salt);
            return statement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insertUser(String login, String passHash){
        try {
            String salt = new SimplePasswordGenerator(true, true, true, true)
                    .generate(10,10);
            String hash = passEncoder.getHash(passHash + salt, pepper);
            PreparedStatement statement = connection.prepareStatement("insert into " +
                    USERS_TABLE + " (login, hashpass, salt)" + " VALUES ( '"+login+"', '" + hash +"', '" + salt +"' )");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void insertUser(User user){
        insertUser(user.getName(), user.getHashPass());
    }

    //looking for a user only by name
    public boolean containsUserName(String login) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from " + USERS_TABLE + " where login = ?");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
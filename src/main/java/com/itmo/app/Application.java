package com.itmo.app;

import com.itmo.collection.LabWorkCollection;
import com.itmo.server.ActiveUsersHandler;
import com.itmo.utils.DatabaseManager;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;

public class Application implements Serializable {
    @Setter
    @Getter
    private LabWorkCollection collection;
    private Date date;
    public DatabaseManager manager;

    public ActiveUsersHandler activeUsers;

    public Application() throws SQLException {
        manager = new DatabaseManager();
        collection = new LabWorkCollection(manager.getCollectionFromDatabase());
        date = new Date();
        activeUsers = ActiveUsersHandler.getInstance();
    }



    public void syncWithDB(){
        try{
            this.collection = new LabWorkCollection(manager.getCollectionFromDatabase());

        }catch (SQLException e){
            System.out.println("error in database"+ e.getSQLState());
            e.printStackTrace();
        }
    }
    public Application(LabWorkCollection collection) {
        this.collection = collection;
    }

}

package com.itmo.utils;

import com.itmo.collection.LabWork;

import java.sql.SQLException;
import java.util.Set;

public interface MyCRUD {
    boolean insertLabWork(LabWork d);
    void insertUser(String login, String passHash);
    Set<LabWork> getCollectionFromDatabase() throws SQLException;
    boolean deleteLabWorkById(long id);
}

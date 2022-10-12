package com.project.RestifyDB.Service;

import com.project.RestifyDB.property.DatabaseProperties;
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



@Service
public class DataRetrievalService {

    @Autowired
    DatabaseProperties databaseProperties;
    public JSONArray getRow(String path, String id) throws Exception {
        String tableName =  getTableNameFromUrl(path);
        String columnName = getColumnNameFromUrl(path,tableName);
        String dataBaseType = databaseProperties.getDataBaseType().toLowerCase(Locale.ROOT);
        if(dataBaseType.equals("mysql")) {
            return getJsonArrayFromDatabase(registerDriver(new com.mysql.jdbc.Driver()), tableName, id,columnName);
        }
        else if(dataBaseType.equals("postgresql")) {
            return getJsonArrayFromDatabase(registerDriver(new org.postgresql.Driver()), tableName, id,columnName);
        }
        else{
            throw new Exception("Please provide suitable database");
        }
    }

   public String  getTableNameFromUrl(String path) {
        String tableName = null;
        Pattern pattern = Pattern.compile("/tables/([^/]+)");
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            tableName = matcher.group(1);
        }
        return tableName;
    }

    public String  getColumnNameFromUrl(String path,String tableName) {
        String columnName = null;
        Pattern pattern = Pattern.compile("/tables/"+tableName+"/([^/]+)");
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            columnName = matcher.group(1);
        }
        return columnName;
    }

    public JSONArray getJsonArrayFromDatabase(Connection connection, String tableName, String id, String columnName) throws Exception {

        DatabaseMetaData databaseMetaData = connection.getMetaData();
        Statement statement = connection.createStatement();
        String query = createQuery(tableName,id,columnName);
        ResultSet resultSet = statement.executeQuery(query);
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            JSONObject obj = new JSONObject();
            int total_rows = resultSet.getMetaData().getColumnCount();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    public Connection registerDriver(Driver driver) throws SQLException {
        DriverManager.registerDriver(driver);
        Connection connection = DriverManager.getConnection(databaseProperties.getUrl(),
                databaseProperties.getUsername(), databaseProperties.getPassword());
        return connection;
    }

    public String createQuery(String tableName, String id, String columnName){
        if(databaseProperties.getDataBaseType().equals("postgresql")) {
            return "select * from " + databaseProperties.getSchemaName() + "." + tableName + " where " + columnName
                    + " = '" + id + "'";
        } else {
            return "select * from "+ tableName + " where " + columnName
                    + " = '" + id + "'";
        }
    }
}

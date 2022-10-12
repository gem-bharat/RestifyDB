package com.project.RestifyDB.Service;

import com.project.RestifyDB.property.DatabaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.sql.*;
import java.util.Locale;

@Service
public class ScanTables {

    @Autowired
    DatabaseProperties databaseProperties;
    public ResultSet getTables() throws Exception {
        String statementType = "";
        String dataBaseType = databaseProperties.getDataBaseType().toLowerCase(Locale.ROOT);
        if (dataBaseType.equals("mysql")) {
            statementType += "Show Tables";
            return registerDriver(new com.mysql.jdbc.Driver()).executeQuery(statementType);
        } else if (dataBaseType.equals("postgresql")) {
            statementType += "SELECT table_name FROM information_schema.tables WHERE table_schema = '"+databaseProperties.getSchemaName() + "'";
            return registerDriver(new org.postgresql.Driver()).executeQuery(statementType);
        } else {
            throw new Exception("Please provide suitable database");
        }
    }

    public ResultSet getColumns(String tableName) throws Exception {
        String statementType = "";
        String dataBaseType = databaseProperties.getDataBaseType().toLowerCase(Locale.ROOT);

        if (dataBaseType.equals("mysql")) {
            statementType += "SHOW COLUMNS FROM "+databaseProperties.getDatabaseName()+"."+tableName;
            return registerDriver(new com.mysql.jdbc.Driver()).executeQuery(statementType);
        } else if (dataBaseType.equals("postgresql")) {
            statementType += "select column_name from INFORMATION_SCHEMA.columns where TABLE_NAME='" + tableName + "'";
            return registerDriver(new org.postgresql.Driver()).executeQuery(statementType);
        } else {
            throw new Exception("Please provide suitable database");
        }
    }

    public Statement registerDriver(Driver driver) throws SQLException {
        DriverManager.registerDriver(driver);
        Connection connection = DriverManager.getConnection(databaseProperties.getUrl(),
                databaseProperties.getUsername(), databaseProperties.getPassword());
        return connection.createStatement();
    }
}

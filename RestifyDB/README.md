# RestifyDB
    ResdtifyDB is a Java Spring boot application which restifies the given database.

## Description
    We use JDBC and spring web framework to accomplish restification of the given database
    by only creating Get Request urls. Using JDBC scanning of all the tables in the database is done 
    basis on what type of database(Mysql, PostGres) is being connected to. Dynamic urls
    are created for all the tables present in the database using the spring web framework. 
    The url endpoint goes like, Get Request : "/rest/tables/tableName/columnName/{columnValue}".
    Need to plug the tableName and columnName to get the corrsponding table record.
    
 ## Test Run 
    For now in order to run the application, need to provide these database properties:
          1) url: database url to connect to, like jdbc:mysql://localhost:3306/employeedb
          2) username
          3) password
          4) databaseType: mySql, PostGreSQL
          5) schema : only for postgres
          6) databaseName : name of your database
    Execute on postman/browser by hitting the url : "/rest/tables/tableName/columnName/{columnValue}"


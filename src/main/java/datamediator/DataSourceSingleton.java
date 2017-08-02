/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamediator;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author antonio
 */
public class DataSourceSingleton {
    private static BasicDataSource connectionPool = null;
    public static BasicDataSource getConnectionPool ()
            throws SQLException, URISyntaxException {
        if (DataSourceSingleton.connectionPool == null){
            DataSourceSingleton.dataSource();
        }
        return DataSourceSingleton.connectionPool;
    }
    
    private DataSourceSingleton(){
    }
    
    private static void dataSource() throws SQLException, URISyntaxException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":"
                + dbUri.getPort() + dbUri.getPath();
        DataSourceSingleton.connectionPool = new BasicDataSource();

        if (dbUri.getUserInfo() != null) {
          DataSourceSingleton.connectionPool.setUsername(dbUri.getUserInfo().split(":")[0]);
          DataSourceSingleton.connectionPool.setPassword(dbUri.getUserInfo().split(":")[1]);
        }
        DataSourceSingleton.connectionPool.setDriverClassName("org.postgresql.Driver");
        DataSourceSingleton.connectionPool.setUrl(dbUrl);
        DataSourceSingleton.connectionPool.setInitialSize(1);
    }
    
}

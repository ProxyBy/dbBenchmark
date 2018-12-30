package com.abc.dbConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseConfig {
    private String name;
    private String username;
    private String password;
    private String driver;
    private String url;

    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setPassword(password);
        dataSource.setUsername(username);
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        return dataSource;
    }
}

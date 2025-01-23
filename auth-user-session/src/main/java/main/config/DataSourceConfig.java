package main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Configuration
public class DataSourceConfig {

    // 连接本地嵌入到项目的SQLite数据库
    @Bean
    public DataSource dataSource() throws IOException {
        String dbFilePath = "auth-web-login-session/database/session.db";
        Path dbPath = FileSystems.getDefault().getPath(dbFilePath);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:sqlite:" + dbPath.toAbsolutePath());
        return dataSource;
    }
}

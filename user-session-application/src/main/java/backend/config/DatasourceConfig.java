package backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Configuration
public class DatasourceConfig {

    // TODO. SQLite DataSource: 使用本地嵌入式数据库来持久化UserDetails用户信息
    @Bean
    public DataSource dataSource() throws IOException {
        String dbFilePath = "user-session-backend/database/session.db";
        Path dbPath = FileSystems.getDefault().getPath(dbFilePath);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:sqlite:" + dbPath.toAbsolutePath());
        return dataSource;
    }
}

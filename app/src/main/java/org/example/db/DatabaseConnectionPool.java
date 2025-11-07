package org.example.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionPool.class);
    private static HikariDataSource dataSource;

    static {
        try {
            // Default configuration, replace with environment variables or config file in production
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
            config.setUsername("postgres");
            config.setPassword("");
            config.setDriverClassName("org.postgresql.Driver");

            // Connection pool configuration
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(20000);
            config.setValidationTimeout(5000);

            // Optional: Additional pool settings
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
            logger.info("Database connection pool initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize database connection pool", e);
            throw new RuntimeException("Database connection pool initialization failed", e);
        }
    }

    private DatabaseConnectionPool() {
        // Prevent instantiation
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = dataSource.getConnection();
            logger.debug("Database connection acquired");
            return connection;
        } catch (SQLException e) {
            logger.error("Failed to acquire database connection", e);
            throw e;
        }
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Database connection pool closed");
        }
    }

    // Shutdown hook to properly close the connection pool
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseConnectionPool::closeDataSource));
    }
}
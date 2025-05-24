package org.example.researchassistant.utils;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/*
    该工具类提供了数据库连接池相关功能以及统一的数据库操作模板，
    可通过传入不同的SQL语句来执行查询、删除、更新等操作，无需为每条语句单独编写打开连接等过程。
*/
public class JDBCUtils {

    private static DataSource dataSource;

    // 数据库连接相关配置信息
    private static String driverClassName;
    private static String url;
    private static String username;
    private static String password;

    private static final String CONFIG_FILE_PATH = "src/main/resources/druid.properties";



    private static Properties properties;

    // 静态代码块，在类加载时执行，用于初始化数据库连接相关配置和数据源
    static {
        try {
            // 加载配置文件
           // InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("D:\\So\\SoWellLife\\src\\main\\resources\\druid.properties");
            Properties properties = new Properties();
            InputStream is = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(is);

            // 读取配置文件中的数据库连接信息
            driverClassName = properties.getProperty("driverClassName");
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");

            // 注册JDBC驱动，在较新的JDBC版本中，这一步可能不是必需的，但为了兼容性这里保留
            try {
                Class.forName(driverClassName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            // 创建数据源，这里简单创建连接对象，实际可使用连接池库来优化
            dataSource = new DataSource() {
                @Override
                public Connection getConnection() throws SQLException {
                    return DriverManager.getConnection(url, username, password);
                }

                @Override
                public Connection getConnection(String username, String password) throws SQLException {
                    return DriverManager.getConnection(url, username, password);
                }

                // 以下方法可根据需要实现，这里暂不做完整实现，因为主要关注连接获取功能
                @Override
                public PrintWriter getLogWriter() throws SQLException {
                    return null;
                }

                @Override
                public void setLogWriter(PrintWriter out) throws SQLException {
                }

                @Override
                public int getLoginTimeout() throws SQLException {
                    return 0;
                }

                @Override
                public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                    return null;
                }

                @Override
                public void setLoginTimeout(int seconds) throws SQLException {
                }

                @Override
                public <T> T unwrap(Class<T> iface) throws SQLException {
                    return null;
                }

                @Override
                public boolean isWrapperFor(Class<?> iface) throws SQLException {
                    return false;
                }


            };

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取数据源的方法
    public static DataSource getDataSource() {
        return dataSource;
    }

    // 获取数据库连接的方法
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // 关闭资源的方法，可处理连接、语句和结果集
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs!= null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (stmt!= null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn!= null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 重载关闭方法，可处理连接和语句
    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }

    // 统一的数据库操作模板方法，可执行查询、删除、更新等操作
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            // 设置参数，如果有传入参数的话
            if (params!= null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }

            // 执行更新操作（包括插入、更新、删除等），返回受影响的行数
            return preparedStatement.executeUpdate();
        } finally {
            close(connection, preparedStatement);
        }
    }

    // 统一的查询操作模板方法，可执行查询操作并返回结果集
    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            // 设置参数，如果有传入参数的话
            if (params!= null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }

            // 执行查询操作，返回结果集
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } finally {
            close(connection, preparedStatement, resultSet);
        }
    }
}
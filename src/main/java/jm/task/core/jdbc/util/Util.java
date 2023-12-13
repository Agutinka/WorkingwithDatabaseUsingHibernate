package jm.task.core.jdbc.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String PROPERTIES_FILE = "db.properties"; // Константа PROPERTIES_FILE содержит имя файла
    private static final Properties properties = new Properties(); // переменная properties используется для хранения параметров подключения

    static {
        try (InputStream input = Util.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Не удалось найти " + PROPERTIES_FILE);
            }
            // попытка загрузить файл свойств
            properties.load(input);
        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
            throw new RuntimeException("Ошибка загрузки свойств БД, файл не найден", e);
        }
    }

    // метод getConnection загружает параметры подключения из файла db.properties
    // файл должен содержать url БД, username и password
    // Возвращает объект Connection (готовое соединение с БД), если соединение установлено успешно
    public static Connection getConnection() {
        try {
            loadDatabaseDriver();
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось установить соединение с БД", e);
        }
    }

    // метод loadDatabaseDriver для загрузки драйвера базы данных (вызывается только один раз)
    private static void loadDatabaseDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Не удалось загрузить драйвер БД", e);
        }
    }
}

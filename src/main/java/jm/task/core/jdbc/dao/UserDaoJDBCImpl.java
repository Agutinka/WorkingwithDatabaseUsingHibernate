package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection;
    public UserDaoJDBCImpl() {
        this.connection = Util.getConnection(); // метод Util.getConnection() - для получения соединения с БД
    }

    //переопределяю метод для создания таблицы
    @Override
//    public void createUsersTable() throws SQLException {
    public void createUsersTable() {
//        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
//                "id INT PRIMARY KEY AUTO_INCREMENT," +
//                "name VARCHAR(255)," +
//                "lastName VARCHAR(255)," +
//                "age TINYINT)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255)," +
                    "lastName VARCHAR(255)," +
                    "age TINYINT)");
            System.out.println("Таблица users создана успешно");
        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new SQLException(e);
            handleSQLException("Ошибка при создании таблицы", e);
        }
    }

    //переопределяю метод для удаления таблицы
    @Override
    public void dropUsersTable() {
//        String dropTableSQL = "DROP TABLE IF EXISTS users";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
            System.out.println("Таблица users удалена успешно");
        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new SQLException(e);
            handleSQLException("Ошибка при удалении таблицы", e);
        }
    }

    //переопределяю метод для сохранения пользователя
    @Override
    public void saveUser(String name, String lastName, byte age) {
//        String insertUserSQL = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";

        //public interface PreparedStatement (extends Statement) - объект, представляющий предварительно скомпилированный оператор SQL.
        //Инструкция SQL предварительно компилируется и сохраняется в объекте PreparedStatement.
        // Затем этот объект можно использовать для эффективного выполнения этого оператора несколько раз.
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            int rowsNew = preparedStatement.executeUpdate();
            if (rowsNew > 0) {
                // Получаю добавленного пользователя
                User newUser = getUserByName(name);
                System.out.println(newUser.toString() );
            } else {
                System.out.println("Не удалось добавить пользователя в БД");
            }
        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new SQLException(e);
            handleSQLException("Ошибка при сохранении пользователя", e);
        }
    }

    // метод для получения пользователя по имени
    private User getUserByName(String name) {
//        String selectUserSQL = "SELECT * FROM users WHERE name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE name = ?")) {
            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("name"));
                    user.setLastName(resultSet.getString("lastName"));
                    user.setAge(resultSet.getByte("age"));
                    return user;
                }
            }
        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new SQLException(e);
            handleSQLException("Ошибка при получении пользователя по имени", e);
        }
        return null;
    }

    public void removeUserById(long id) {
//        String removeUserSQL = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //переопределяю метод для получения всех пользователей
    @Override
    public List<User> getAllUsers() {
//        String selectAllUsersSQL = "SELECT * FROM users";
        LinkedList<User> userList = new LinkedList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }

        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new SQLException(e);
            handleSQLException("Ошибка при получении списка пользователей", e);
        }

        return userList;
    }

    //переопределяю метод для очистки таблицы пользователей
    @Override
    public void cleanUsersTable() {
//        String truncateTableSQL = "TRUNCATE TABLE users";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE users");
            System.out.println("Таблица users очищена успешно");
        } catch (SQLException e) {
//            e.printStackTrace();
//        throw new SQLException(e);
            handleSQLException("Ошибка при очистке таблицы пользователей", e);
        }
    }

    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
    }
}

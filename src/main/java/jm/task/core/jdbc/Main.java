package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        // реализуйте алгоритм здесь

        // Создаю экземпляр UserService, чтобы использовать методы для взаимодействия с БД
        UserService userService = new UserServiceImpl();

        // Вызываю метод createUsersTable() для создания таблицы пользователей в БД
        userService.createUsersTable();

        // Вызываю метод saveUser для добавления пользователя в БД
        userService.saveUser("Иван", "Иванов", (byte) 25);
        userService.saveUser("Петя", "Петров", (byte) 30);
        userService.saveUser("Сидор", "Сидоров", (byte) 35);
        userService.saveUser("Маша", "Медведева", (byte) 40);

        // Вызываю метод getAllUsers() для получения списка всех пользователей
        List<User> userList = userService.getAllUsers();
        // и вывожу их в консоль через forEach
        System.out.println("Все пользователи: ");
        for (User user : userList) {
            System.out.println(user);
        }

        // Вызываю метод removeUserById() для удаления из БД пользователя по id
        userService.removeUserById(1);

        // Вызываю метод cleanUsersTable() для удаления всех записей из таблицы пользователей
        // при этом, структура таблицы не изменяется
        userService.cleanUsersTable();

        // Вызываю метод dropUsersTable() для удаления из БД таблицы пользователей
        userService.dropUsersTable();
    }

}

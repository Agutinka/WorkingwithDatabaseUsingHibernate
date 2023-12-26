package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            // Выполнение SQL-запроса для создания таблицы
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users (" +
                            "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                            "name VARCHAR(255) NOT NULL," +
                            "lastName VARCHAR(255) NOT NULL," +
                            "age INT NOT NULL)")
                    .addEntity(User.class)
                    .executeUpdate();
            transaction.commit();
            System.out.println("Таблица users создана успешно");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
//        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            // Выполнение SQL-запроса для удаления таблицы
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            transaction.commit();
            System.out.println("Таблица users удалена успешно");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при удалении таблицы: " + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            // начало транзакции
            transaction = session.beginTransaction();
            //сохранение нового объекта user
            session.save(new User(name, lastName, age));
            // подтверждение транзакции
            transaction.commit();
            System.out.println("User с именем: " + name + " добавлен в БД");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.err.println("Ошибка при сохранении пользователя: " + e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            // получаю пользователя по id перед удалением
            User userToRemove = session.get(User.class, id);

            // удаляю пользователя по id
            session.delete(userToRemove);
            System.out.println("Пользователь с id " + id + " удалён успешно");

//  С явным использованием HQL
//            session.createQuery("delete User where id = :id")
////                    .setParameter("id", id)
//                    .executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при удалении пользователя: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            userList = session.createQuery("from User", User.class).getResultList();
            transaction.commit();

        } catch (Exception e) {
            if (transaction == null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.err.println("Ошибка при получении списка пользователей: " + e.getMessage());
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            transaction.commit();
            System.out.println("Таблица users очищена успешно");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.err.println("Ошибка при очистке таблицы пользователей: " + e.getMessage());
        }
    }
}
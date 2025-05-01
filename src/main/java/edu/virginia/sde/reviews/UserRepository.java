package edu.virginia.sde.reviews;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.SelectionQuery;

import java.util.Collections;
import java.util.List;

public class UserRepository {
    public void createUser(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            System.err.println("Error creating user: " + user.getUsername());
            System.err.println(e.getMessage());
        }
    }

    public void changeUserPassword(User user, String newPassword) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "UPDATE User SET password = :password WHERE username = :username";
            tx = session.beginTransaction();
            MutationQuery query = session.createMutationQuery(hql);
            query.setParameter("password", newPassword);
            query.setParameter("username", user.getUsername());
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            System.err.println("Error updating user password: " + user.getUsername());
            System.err.println(e.getMessage());
        }
    }

    public User getUserByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User WHERE username = :username";
            SelectionQuery<User> query = session.createSelectionQuery(hql, User.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        } catch (HibernateException e) {
          System.err.println("Failed to get user by username: " + username);
          System.err.println(e.getMessage());
          return null;
        }
    }

    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User";
            SelectionQuery<User> query = session.createSelectionQuery(hql, User.class);
            return query.list();
        } catch (HibernateException e) {
            System.err.println("Failed to get all users: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void deleteAllUsers() {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "DELETE FROM User";
            tx = session.beginTransaction();
            session.createMutationQuery(hql).executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            System.err.println("Error deleting all users: " + e.getMessage());
        }
    }
}

package edu.virginia.sde.reviews;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.util.Collections;
import java.util.List;

public class CourseRepository {
    public void createCourse(Course course) {
        Transaction tx = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(course);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            System.err.println("Course creation failed: " + course.getName());
            System.err.println(e.getMessage());
        }
    }

    public List<Course> getAllCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Course";
            SelectionQuery<Course> query = session.createQuery(hql, Course.class);
            return query.list();
        } catch (HibernateException e) {
            System.err.println("Failed to get all courses: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    boolean courseExists(String catalog, String name, String instructor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Course WHERE catalog = :catalog AND name = :name AND instructor = :instructor";
            SelectionQuery<Course> query = session.createQuery(hql, Course.class);
            query.setParameter("catalog", catalog);
            query.setParameter("name", name);
            query.setParameter("instructor", instructor);
            return !query.list().isEmpty();
        } catch (HibernateException e) {
            System.err.println("Failed to check if course exists: " + e.getMessage());
            return true;
        }
    }
}

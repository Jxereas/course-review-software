package edu.virginia.sde.reviews;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReviewRepository {
    public void createReview(Review review) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(review);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            System.err.println("Failed to create review: " + review);
            System.err.println(e.getMessage());
        }
    }

    public List<Review> getReviewsByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Review WHERE user.id = :userId";
            SelectionQuery<Review> query = session.createQuery(hql, Review.class);
            query.setParameter("userId", user.getId());
            return query.list();
        } catch (HibernateException e) {
            System.err.println("Failed to get reviews by user: " + user);
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Review> getReviewsByCourse(Course course) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Review WHERE course.id = :courseId";
            SelectionQuery<Review> query = session.createSelectionQuery(hql, Review.class);
            query.setParameter("courseId", course.getId());
            return query.list();
        } catch (HibernateException e) {
            System.err.println("Failed to get reviews for course: " + course);
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public Review getReviewByUserAndCourse(User user, Course course) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Review WHERE user.id = :userId and course.id = :courseId";
            SelectionQuery<Review> query = session.createQuery(hql, Review.class);
            query.setParameter("userId", user.getId());
            query.setParameter("courseId", course.getId());
            return query.uniqueResult();
        } catch (HibernateException e) {
            System.err.println("Failed to get review for user: " + user + " and course: " + course);
            System.err.println(e.getMessage());
            return null;
        }
    }

    public void updateReview(Review review) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            review.setTimestamp(LocalDateTime.now());
            session.merge(review);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            System.err.println("Failed to update review: " + review);
            System.err.println(e.getMessage());
        }
    }

    public void deleteReview(Review review) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(review);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            System.err.println("Failed to delete review: " + review);
            System.err.println(e.getMessage());
        }
    }

    public boolean hasUserReviewedCourse(User user, Course course) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT count(r) FROM Review r WHERE r.user.id = :userId AND r.course.id = :courseId";
            SelectionQuery<Long> query = session.createSelectionQuery(hql, Long.class);
            query.setParameter("userId", user.getId());
            query.setParameter("courseId", course.getId());

            return query.uniqueResult() > 0;
        } catch (HibernateException e) {
            System.err.println("Failed to get if user: " + user + " reviewed course: " + course);
            System.err.println(e.getMessage());
            return true;
        }
    }
}
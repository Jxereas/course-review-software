# Course Review Application

A JavaFX desktop application that allows users to create accounts, search for university courses, and write anonymous course reviews with persistent storage.

This project implements a full CRUD workflow and was developed as part of a software development capstone.

---

## Overview

The application allows users to:
- Create an account and log in
- Add and search for university courses
- Write, edit, and delete anonymous course reviews
- View average course ratings and review history
- Persist all data using a local SQLite database

The system enforces constraints such as unique usernames, one review per user per course, and automatic database reconstruction if the database is missing.

---

## Features

- User authentication (login and account creation)
- Course search by subject, number, or title
- Add new courses with input validation
- Anonymous course reviews with ratings and optional comments
- Edit and delete existing reviews
- View all reviews written by the logged-in user
- Automatic calculation of average course ratings
- Persistent storage using SQLite
- Hibernate ORM for database interaction
- JavaFX-based GUI with multiple scenes

---

## Tech Stack

- Java 21
- JavaFX 21
- SQLite
- Hibernate ORM
- Gradle
- JUnit (basic repository testing)

---

## Running the Application

The main entry point is:

CourseReviewsApplication.java


To run the application, ensure JavaFX is available and pass the required VM arguments:

--module-path [PATH_TO_JAVAFX_LIB] --add-modules javafx.controls,javafx.fxml


The application will initialize the database automatically if it does not already exist.

---

## Notes

This project was originally developed as part of coursework at the University of Virginia.  

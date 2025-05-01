package edu.virginia.sde.reviews;

import jakarta.persistence.*;

@Entity
@Table(name = "COURSES")
public class Course {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "CATALOG", nullable = false)
    private String catalog;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "INSTRUCTOR", nullable = false)
    private String instructor;

    public Course() {

    }

    public Course(String catalog, String name, String instructor) {
        this.catalog = catalog;
        this.name = name;
        this.instructor = instructor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return catalog + " - " + name + " (Instructor: " + instructor + ")";
    }
}

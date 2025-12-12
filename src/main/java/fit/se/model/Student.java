package fit.se.model;

import java.io.Serializable;

public class Student implements Serializable {
    private String id;
    private String name;
    private int age;
    private String email;
    private double score;

    // Constructor
    public Student(String id, String name, int age, String email, double score) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.score = score;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Age: %d, Email: %s, Score: %.2f", id, name, age, email, score);
    }
}

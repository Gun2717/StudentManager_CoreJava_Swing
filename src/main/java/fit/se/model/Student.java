package fit.se.model;

import java.io.Serializable;
import java.util.Objects;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private int age;
    private String email;
    private double score;

    // Constructor đầy đủ
    public Student(String id, String name, int age, String email, double score) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.score = score;
    }

    // Constructor mặc định
    public Student() {
        this("", "", 18, "", 0.0);
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

    // Phương thức lấy học lực
    public String getAcademicLevel() {
        if (score >= 8.0) return "GIỎI";
        if (score >= 6.5) return "KHÁ";
        if (score >= 5.0) return "TRUNG BÌNH";
        return "YẾU";
    }

    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Age: %d, Email: %s, Score: %.2f, Level: %s",
                id, name, age, email, score, getAcademicLevel());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Clone method
    public Student clone() {
        return new Student(this.id, this.name, this.age, this.email, this.score);
    }
}

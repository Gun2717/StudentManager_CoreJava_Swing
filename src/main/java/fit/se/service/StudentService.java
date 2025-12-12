package fit.se.service;

import fit.se.model.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentService {
    private List<Student> students = new ArrayList<>();
    private List<Student> history = new ArrayList<>(); // Để hỗ trợ undo

    public void addStudent(Student student) {
        students.add(student);
        history.add(student);
    }

    public void updateStudent(String id, Student newStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                history.add(students.get(i)); // Lưu lại cho undo
                students.set(i, newStudent);
                break;
            }
        }
    }

    public void deleteStudent(String id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                history.add(student); // Lưu lại cho undo
                students.remove(student);
                break;
            }
        }
    }

    public List<Student> search(String name) {
        List<Student> foundStudents = new ArrayList<>();
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(name.toLowerCase())) {
                foundStudents.add(student);
            }
        }
        return foundStudents;
    }

    public List<Student> getAll() {
        return students;
    }

    public void undo() {
        if (!history.isEmpty()) {
            Student lastAction = history.remove(history.size() - 1);
            students.remove(lastAction);
        }
    }

    // Các phương thức sắp xếp
    public void sortByName() {
        students.sort(Comparator.comparing(Student::getName));
    }

    public void sortByScore() {
        students.sort(Comparator.comparingDouble(Student::getScore));
    }

    // Lọc học lực
    public List<Student> filterByScore(double minScore, double maxScore) {
        List<Student> filteredStudents = new ArrayList<>();
        for (Student student : students) {
            if (student.getScore() >= minScore && student.getScore() <= maxScore) {
                filteredStudents.add(student);
            }
        }
        return filteredStudents;
    }

    // Thống kê
    public double getAverageScore() {
        return students.stream().mapToDouble(Student::getScore).average().orElse(0.0);
    }

    public int getTotalStudents() {
        return students.size();
    }
}

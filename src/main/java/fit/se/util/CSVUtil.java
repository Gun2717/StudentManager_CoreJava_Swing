package fit.se.util;

import fit.se.model.Student;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {
    private static final String CSV_FILE = "src/data/student.csv";
    private static final String CSV_HEADER = "ID,Name,Age,Email,Score";
    private static final String CSV_SEPARATOR = ",";

    // Khởi tạo file CSV nếu chưa tồn tại
    public void initializeCSV() {
        try {
            Path path = Paths.get(CSV_FILE);
            Path parentDir = path.getParent();

            // Tạo thư mục nếu chưa có
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            // Tạo file với header nếu chưa có
            if (!Files.exists(path)) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
                    writer.write(CSV_HEADER);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi khởi tạo file CSV: " + e.getMessage());
        }
    }

    // Thêm sinh viên vào CSV
    public boolean addStudentToCSV(Student student) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            String line = String.format("%s,%s,%d,%s,%.2f",
                    student.getId(),
                    student.getName(),
                    student.getAge(),
                    student.getEmail(),
                    student.getScore());
            writer.write(line);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Lỗi ghi CSV: " + e.getMessage());
            return false;
        }
    }

    // Đọc tất cả sinh viên từ CSV
    public List<Student> readAllFromCSV() {
        List<Student> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                // Bỏ qua header
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] fields = line.split(CSV_SEPARATOR);
                if (fields.length == 5) {
                    Student student = new Student(
                            fields[0].trim(),
                            fields[1].trim(),
                            Integer.parseInt(fields[2].trim()),
                            fields[3].trim(),
                            Double.parseDouble(fields[4].trim())
                    );
                    students.add(student);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File CSV không tồn tại: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Lỗi đọc CSV: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Lỗi định dạng dữ liệu: " + e.getMessage());
        }

        return students;
    }

    // Ghi đè toàn bộ danh sách sinh viên vào CSV
    public boolean writeAllToCSV(List<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            // Ghi header
            writer.write(CSV_HEADER);
            writer.newLine();

            // Ghi từng sinh viên
            for (Student student : students) {
                String line = String.format("%s,%s,%d,%s,%.2f",
                        student.getId(),
                        student.getName(),
                        student.getAge(),
                        student.getEmail(),
                        student.getScore());
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Lỗi ghi CSV: " + e.getMessage());
            return false;
        }
    }

    // Import từ file CSV khác
    public List<Student> importFromCSV(String filePath) {
        List<Student> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] fields = line.split(CSV_SEPARATOR);
                if (fields.length == 5) {
                    Student student = new Student(
                            fields[0].trim(),
                            fields[1].trim(),
                            Integer.parseInt(fields[2].trim()),
                            fields[3].trim(),
                            Double.parseDouble(fields[4].trim())
                    );
                    students.add(student);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Lỗi import CSV: " + e.getMessage());
        }

        return students;
    }

    // Export ra file CSV khác
    public boolean exportToCSV(List<Student> students, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(CSV_HEADER);
            writer.newLine();

            for (Student student : students) {
                String line = String.format("%s,%s,%d,%s,%.2f",
                        student.getId(),
                        student.getName(),
                        student.getAge(),
                        student.getEmail(),
                        student.getScore());
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Lỗi export CSV: " + e.getMessage());
            return false;
        }
    }
}

package fit.se.util;

import fit.se.model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class để xử lý file serialization
 * Lưu/đọc danh sách sinh viên dưới dạng binary
 */
public class FileUtil {

    /**
     * Lưu danh sách sinh viên vào file (binary serialization)
     */
    public void saveToFile(List<Student> students, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(students);
            System.out.println("FileUtil: Đã lưu " + students.size() + " sinh viên vào " + filename);
        } catch (IOException e) {
            System.err.println("FileUtil: Lỗi lưu file - " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Đọc danh sách sinh viên từ file (binary deserialization)
     */
    @SuppressWarnings("unchecked")
    public List<Student> loadFromFile(String filename) {
        List<Student> students = new ArrayList<>();

        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("FileUtil: File không tồn tại - " + filename);
            return students;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            students = (List<Student>) ois.readObject();
            System.out.println("FileUtil: Đã đọc " + students.size() + " sinh viên từ " + filename);
        } catch (IOException e) {
            System.err.println("FileUtil: Lỗi đọc file - " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("FileUtil: Không tìm thấy class Student - " + e.getMessage());
            e.printStackTrace();
        }

        return students;
    }

    /**
     * Kiểm tra file có tồn tại không
     */
    public boolean fileExists(String filename) {
        return new File(filename).exists();
    }

    /**
     * Xóa file
     */
    public boolean deleteFile(String filename) {
        try {
            File file = new File(filename);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("FileUtil: Đã xóa file - " + filename);
                }
                return deleted;
            }
            return false;
        } catch (Exception e) {
            System.err.println("FileUtil: Lỗi xóa file - " + e.getMessage());
            return false;
        }
    }

    /**
     * Backup danh sách sinh viên
     */
    public boolean backupData(List<Student> students, String backupPath) {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String filename = backupPath + "/backup_" + timestamp + ".dat";
            saveToFile(students, filename);
            System.out.println("FileUtil: Đã backup dữ liệu vào " + filename);
            return true;
        } catch (Exception e) {
            System.err.println("FileUtil: Lỗi backup - " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy kích thước file (bytes)
     */
    public long getFileSize(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            return file.length();
        }
        return 0;
    }

    /**
     * Lấy thời gian sửa đổi cuối cùng
     */
    public long getLastModified(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            return file.lastModified();
        }
        return 0;
    }
}

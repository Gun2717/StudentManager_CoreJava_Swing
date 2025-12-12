package fit.se.service;

import fit.se.model.Student;
import fit.se.util.CSVUtil;

import java.util.*;
import java.util.stream.Collectors;

public class StudentService {
    private List<Student> students = new ArrayList<>();
    private Stack<Action> undoStack = new Stack<>();
    private CSVUtil csvUtil;

    public StudentService() {
        this.csvUtil = new CSVUtil();
        // Khởi tạo file CSV
        csvUtil.initializeCSV();
        // Load dữ liệu từ CSV khi khởi động
        loadFromCSV();
    }

    /**
     * Load dữ liệu từ CSV
     */
    public void loadFromCSV() {
        students = csvUtil.readAllFromCSV();
        System.out.println("Service: Đã load " + students.size() + " sinh viên từ CSV.");
    }

    /**
     * Lưu toàn bộ dữ liệu vào CSV
     */
    private void saveToCSV() {
        csvUtil.writeAllToCSV(students);
    }

    /**
     * Thêm sinh viên
     */
    public boolean addStudent(Student student) {
        // Kiểm tra ID trùng
        if (isIdExists(student.getId())) {
            System.err.println("ID đã tồn tại: " + student.getId());
            return false;
        }

        // Kiểm tra email trùng
        if (isEmailExists(student.getEmail())) {
            System.err.println("Email đã tồn tại: " + student.getEmail());
            return false;
        }

        students.add(student);
        // Ghi ngay vào CSV
        csvUtil.addStudentToCSV(student);

        // Lưu action để undo
        undoStack.push(new Action(ActionType.ADD, student, null));
        System.out.println("Service: Đã thêm sinh viên " + student.getId());
        return true;
    }

    /**
     * Cập nhật sinh viên
     */
    public boolean updateStudent(String id, Student newStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                Student oldStudent = students.get(i).clone();

                // Kiểm tra email trùng (nếu thay đổi email)
                if (!oldStudent.getEmail().equals(newStudent.getEmail()) &&
                        isEmailExists(newStudent.getEmail())) {
                    System.err.println("Email mới đã tồn tại: " + newStudent.getEmail());
                    return false;
                }

                students.set(i, newStudent);

                // Cập nhật CSV
                saveToCSV();

                // Lưu action để undo
                undoStack.push(new Action(ActionType.UPDATE, newStudent, oldStudent));
                System.out.println("Service: Đã cập nhật sinh viên " + id);
                return true;
            }
        }
        System.err.println("Không tìm thấy sinh viên với ID: " + id);
        return false;
    }

    /**
     * Xóa sinh viên
     */
    public boolean deleteStudent(String id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                Student deletedStudent = student.clone();
                students.remove(student);

                // Cập nhật CSV
                saveToCSV();

                // Lưu action để undo
                undoStack.push(new Action(ActionType.DELETE, null, deletedStudent));
                System.out.println("Service: Đã xóa sinh viên " + id);
                return true;
            }
        }
        System.err.println("Không tìm thấy sinh viên với ID: " + id);
        return false;
    }

    /**
     * Tìm sinh viên theo ID
     */
    public Student findById(String id) {
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Tìm kiếm sinh viên theo tên
     */
    public List<Student> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>(students);
        }

        String searchTerm = name.toLowerCase().trim();
        return students.stream()
                .filter(s -> s.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Tìm kiếm nâng cao
     */
    public List<Student> advancedSearch(String id, String name, Integer minAge, Integer maxAge,
                                        Double minScore, Double maxScore) {
        return students.stream()
                .filter(s -> id == null || id.isEmpty() ||
                        s.getId().toLowerCase().contains(id.toLowerCase()))
                .filter(s -> name == null || name.isEmpty() ||
                        s.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(s -> minAge == null || s.getAge() >= minAge)
                .filter(s -> maxAge == null || s.getAge() <= maxAge)
                .filter(s -> minScore == null || s.getScore() >= minScore)
                .filter(s -> maxScore == null || s.getScore() <= maxScore)
                .collect(Collectors.toList());
    }

    /**
     * Lấy tất cả sinh viên
     */
    public List<Student> getAll() {
        return new ArrayList<>(students);
    }

    /**
     * Undo thao tác gần nhất
     */
    public boolean undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Service: Không có thao tác để hoàn tác.");
            return false;
        }

        Action action = undoStack.pop();

        switch (action.type) {
            case ADD:
                // Hủy thêm = xóa
                students.removeIf(s -> s.getId().equals(action.newData.getId()));
                System.out.println("Service: Đã hoàn tác thêm sinh viên " + action.newData.getId());
                break;

            case UPDATE:
                // Hủy sửa = khôi phục dữ liệu cũ
                for (int i = 0; i < students.size(); i++) {
                    if (students.get(i).getId().equals(action.newData.getId())) {
                        students.set(i, action.oldData);
                        System.out.println("Service: Đã hoàn tác sửa sinh viên " + action.newData.getId());
                        break;
                    }
                }
                break;

            case DELETE:
                // Hủy xóa = thêm lại
                students.add(action.oldData);
                System.out.println("Service: Đã hoàn tác xóa sinh viên " + action.oldData.getId());
                break;
        }

        // Cập nhật CSV
        saveToCSV();
        return true;
    }

    /**
     * Sắp xếp theo tên
     */
    public void sortByName(boolean ascending) {
        if (ascending) {
            students.sort(Comparator.comparing(Student::getName));
        } else {
            students.sort(Comparator.comparing(Student::getName).reversed());
        }
        System.out.println("Service: Đã sắp xếp theo tên (" + (ascending ? "tăng" : "giảm") + ")");
    }

    /**
     * Sắp xếp theo điểm
     */
    public void sortByScore(boolean ascending) {
        if (ascending) {
            students.sort(Comparator.comparingDouble(Student::getScore));
        } else {
            students.sort(Comparator.comparingDouble(Student::getScore).reversed());
        }
        System.out.println("Service: Đã sắp xếp theo điểm (" + (ascending ? "tăng" : "giảm") + ")");
    }

    /**
     * Sắp xếp theo tuổi
     */
    public void sortByAge(boolean ascending) {
        if (ascending) {
            students.sort(Comparator.comparingInt(Student::getAge));
        } else {
            students.sort(Comparator.comparingInt(Student::getAge).reversed());
        }
        System.out.println("Service: Đã sắp xếp theo tuổi (" + (ascending ? "tăng" : "giảm") + ")");
    }

    /**
     * Lọc theo học lực
     */
    public List<Student> filterByAcademicLevel(String level) {
        return switch (level.toUpperCase()) {
            case "GIỎI" -> students.stream()
                    .filter(s -> s.getScore() >= 8.0)
                    .collect(Collectors.toList());
            case "KHÁ" -> students.stream()
                    .filter(s -> s.getScore() >= 6.5 && s.getScore() < 8.0)
                    .collect(Collectors.toList());
            case "TRUNG BÌNH" -> students.stream()
                    .filter(s -> s.getScore() >= 5.0 && s.getScore() < 6.5)
                    .collect(Collectors.toList());
            case "YẾU" -> students.stream()
                    .filter(s -> s.getScore() < 5.0)
                    .collect(Collectors.toList());
            default -> new ArrayList<>(students);
        };
    }

    /**
     * Lọc theo khoảng điểm
     */
    public List<Student> filterByScore(double minScore, double maxScore) {
        return students.stream()
                .filter(s -> s.getScore() >= minScore && s.getScore() <= maxScore)
                .collect(Collectors.toList());
    }

    /**
     * Lọc theo khoảng tuổi
     */
    public List<Student> filterByAge(int minAge, int maxAge) {
        return students.stream()
                .filter(s -> s.getAge() >= minAge && s.getAge() <= maxAge)
                .collect(Collectors.toList());
    }

    /**
     * Thống kê đầy đủ
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        int total = students.size();
        stats.put("total", total);

        if (total == 0) {
            stats.put("average", 0.0);
            stats.put("maxScore", 0.0);
            stats.put("minScore", 0.0);
            stats.put("excellent", 0L);
            stats.put("good", 0L);
            stats.put("average_level", 0L);
            stats.put("weak", 0L);
            stats.put("avgAge", 0.0);
            return stats;
        }

        // Thống kê điểm
        DoubleSummaryStatistics scoreStats = students.stream()
                .mapToDouble(Student::getScore)
                .summaryStatistics();

        stats.put("average", scoreStats.getAverage());
        stats.put("maxScore", scoreStats.getMax());
        stats.put("minScore", scoreStats.getMin());

        // Thống kê tuổi
        double avgAge = students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
        stats.put("avgAge", avgAge);

        // Thống kê theo học lực
        long excellent = students.stream().filter(s -> s.getScore() >= 8.0).count();
        long good = students.stream().filter(s -> s.getScore() >= 6.5 && s.getScore() < 8.0).count();
        long average = students.stream().filter(s -> s.getScore() >= 5.0 && s.getScore() < 6.5).count();
        long weak = students.stream().filter(s -> s.getScore() < 5.0).count();

        stats.put("excellent", excellent);
        stats.put("good", good);
        stats.put("average_level", average);
        stats.put("weak", weak);

        // Phần trăm
        stats.put("excellentPercent", (excellent * 100.0) / total);
        stats.put("goodPercent", (good * 100.0) / total);
        stats.put("averagePercent", (average * 100.0) / total);
        stats.put("weakPercent", (weak * 100.0) / total);

        return stats;
    }

    /**
     * Export CSV
     */
    public boolean exportToCSV(String filePath) {
        return csvUtil.exportToCSV(students, filePath);
    }

    /**
     * Import CSV
     */
    public boolean importFromCSV(String filePath) {
        List<Student> importedStudents = csvUtil.importFromCSV(filePath);

        if (importedStudents.isEmpty()) {
            System.err.println("Service: Không có sinh viên nào được import.");
            return false;
        }

        // Thêm các sinh viên mới (không trùng ID)
        int addedCount = 0;
        int duplicateCount = 0;

        for (Student student : importedStudents) {
            if (!isIdExists(student.getId())) {
                students.add(student);
                addedCount++;
            } else {
                duplicateCount++;
            }
        }

        System.out.println("Service: Import hoàn tất. Thêm: " + addedCount + ", Trùng: " + duplicateCount);

        // Cập nhật CSV
        if (addedCount > 0) {
            saveToCSV();
            return true;
        }

        return false;
    }

    /**
     * Kiểm tra ID có tồn tại không
     */
    public boolean isIdExists(String id) {
        return students.stream().anyMatch(s -> s.getId().equals(id));
    }

    /**
     * Kiểm tra Email có tồn tại không
     */
    public boolean isEmailExists(String email) {
        return students.stream().anyMatch(s -> s.getEmail().equals(email));
    }

    /**
     * Xóa tất cả sinh viên
     */
    public void clearAll() {
        students.clear();
        undoStack.clear();
        saveToCSV();
        System.out.println("Service: Đã xóa tất cả sinh viên.");
    }

    /**
     * Lấy số lượng sinh viên
     */
    public int getCount() {
        return students.size();
    }

    /**
     * Kiểm tra có undo không
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    // Inner class để lưu action cho undo
    private static class Action {
        ActionType type;
        Student newData;
        Student oldData;

        public Action(ActionType type, Student newData, Student oldData) {
            this.type = type;
            this.newData = newData;
            this.oldData = oldData;
        }
    }

    private enum ActionType {
        ADD, UPDATE, DELETE
    }
}

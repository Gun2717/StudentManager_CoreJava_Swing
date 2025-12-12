package fit.se.view;

import fit.se.model.Student;
import fit.se.service.StudentService;
import fit.se.service.Validator;
import fit.se.util.FileUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainView {
    private JTextField idField, nameField, ageField, emailField, scoreField;
    private JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnImport, btnExport, btnUndo;
    private JButton btnSortByName, btnSortByScore, btnFilter, btnStatistics;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private StudentService studentService;
    private Validator validator;
    private FileUtil fileUtil;

    public MainView() {
        studentService = new StudentService();
        validator = new Validator();
        fileUtil = new FileUtil();

        // Tạo khung giao diện
        JFrame frame = new JFrame("Quản Lý Sinh Viên");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Chạy full màn hình
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        // Tạo bảng sinh viên
        String[] columnNames = {"ID", "Name", "Age", "Email", "Score"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 14));
        studentTable.setBackground(Color.WHITE);
        studentTable.setRowHeight(25);
        studentTable.setFillsViewportHeight(true);

        // Tạo và tùy chỉnh các thành phần nhập liệu
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        inputPanel.setBackground(new Color(220, 220, 220));

        // Tạo các trường nhập
        idField = createInputField("ID:");
        nameField = createInputField("Name:");
        ageField = createInputField("Age:");
        emailField = createInputField("Email:");
        scoreField = createInputField("Score:");

        inputPanel.add(idField);
        inputPanel.add(nameField);
        inputPanel.add(ageField);
        inputPanel.add(emailField);
        inputPanel.add(scoreField);

        // Tạo và tùy chỉnh nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 5, 10, 10));
        buttonPanel.setBackground(new Color(220, 220, 220));

        // Tạo nút
        btnAdd = createButton("Thêm");
        btnUpdate = createButton("Cập nhật");
        btnDelete = createButton("Xóa");
        btnSearch = createButton("Tìm kiếm");
        btnImport = createButton("Nhập");
        btnExport = createButton("Xuất");
        btnUndo = createButton("Quay lại");
        btnSortByName = createButton("Sắp xếp theo Tên");
        btnSortByScore = createButton("Sắp xếp theo Điểm");
        btnFilter = createButton("Lọc Học Lực");
        btnStatistics = createButton("Thống kê");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnImport);
        buttonPanel.add(btnExport);
        buttonPanel.add(btnUndo);
        buttonPanel.add(btnSortByName);
        buttonPanel.add(btnSortByScore);
        buttonPanel.add(btnFilter);
        buttonPanel.add(btnStatistics);

        // Bảng sinh viên
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tổng hợp mọi thành phần
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Thiết lập modal ActionListener cho các nút
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnSearch.addActionListener(e -> searchStudent());
        btnImport.addActionListener(e -> importStudents());
        btnExport.addActionListener(e -> exportStudents());
        btnUndo.addActionListener(e -> undoAction());
        btnSortByName.addActionListener(e -> sortByName());
        btnSortByScore.addActionListener(e -> sortByScore());
        btnFilter.addActionListener(e -> filterStudents());
        btnStatistics.addActionListener(e -> showStatistics());

        // Cập nhật sự kiện khi hàng được chọn trong bảng
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && studentTable.getSelectedRow() != -1) {
                loadSelectedStudent();
            }
        });

        frame.setVisible(true);
    }

    private JTextField createInputField(String label) {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createTitledBorder(label));
        return textField;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }

    private void addStudent() {
        if (validateInput()) {
            Student student = new Student(idField.getText(), nameField.getText(), Integer.parseInt(ageField.getText()),
                    emailField.getText(), Double.parseDouble(scoreField.getText()));
            studentService.addStudent(student);
            updateTable();
            clearInputFields();
        }
    }

    private void updateStudent() {
        if (studentTable.getSelectedRow() != -1 && validateInput()) {
            String id = idField.getText();
            Student newStudent = new Student(id, nameField.getText(), Integer.parseInt(ageField.getText()),
                    emailField.getText(), Double.parseDouble(scoreField.getText()));
            studentService.updateStudent(id, newStudent);
            updateTable();
            clearInputFields();
        }
    }

    private void deleteStudent() {
        if (studentTable.getSelectedRow() != -1) {
            String id = idField.getText();
            studentService.deleteStudent(id);
            updateTable();
            clearInputFields();
        }
    }

    private void searchStudent() {
        String name = nameField.getText();
        List<Student> foundStudents = studentService.search(name);
        updateTable(foundStudents);
    }

    private void importStudents() {
        // Mở hộp thoại chọn file để nhập dữ liệu
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            List<Student> importedStudents = fileUtil.loadFromFile(filePath);
            for (Student student : importedStudents) {
                studentService.addStudent(student);
            }
            updateTable();
        }
    }

    private void exportStudents() {
        // Mở hộp thoại chọn file để xuất dữ liệu
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            fileUtil.saveToFile(studentService.getAll(), filePath);
            JOptionPane.showMessageDialog(null, "Xuất dữ liệu thành công!");
        }
    }

    private boolean validateInput() {
        String id = idField.getText();
        String name = nameField.getText();
        int age;
        double score;

        try {
            age = Integer.parseInt(ageField.getText());
            score = Double.parseDouble(scoreField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Nhập tuổi và điểm là số hợp lệ!");
            return false;
        }

        // Kiểm tra ID có trùng
        if (studentService.getAll().stream().anyMatch(student -> student.getId().equals(id))) {
            JOptionPane.showMessageDialog(null, "ID đã tồn tại! Vui lòng nhập ID khác.");
            return false;
        }

        // Kiểm tra độ tuổi (tối thiểu 18 tuổi)
        if (age < 18) {
            JOptionPane.showMessageDialog(null, "Tuổi phải từ 18 trở lên.");
            return false;
        }

        // Kiểm tra định dạng email
        if (!validator.validateEmail(emailField.getText())) {
            JOptionPane.showMessageDialog(null, "Email không hợp lệ!");
            return false;
        }

        // Kiểm tra email có trùng
        if (studentService.getAll().stream().anyMatch(student -> student.getEmail().equals(emailField.getText()))) {
            JOptionPane.showMessageDialog(null, "Email đã tồn tại! Vui lòng nhập email khác.");
            return false;
        }

        // Kiểm tra các trường khác
        if (!validator.validateID(id) || !validator.validateAge(age) || !validator.validateScore(score)) {
            JOptionPane.showMessageDialog(null, "Thông tin không hợp lệ!");
            return false;
        }

        return true;
    }

    private void undoAction() {
        studentService.undo();
        updateTable();
    }

    private void sortByName() {
        studentService.sortByName();
        updateTable();
    }

    private void sortByScore() {
        studentService.sortByScore();
        updateTable();
    }

    private void filterStudents() {
        String minScoreStr = JOptionPane.showInputDialog("Nhập điểm thấp nhất:");
        String maxScoreStr = JOptionPane.showInputDialog("Nhập điểm cao nhất:");
        double minScore = Double.parseDouble(minScoreStr);
        double maxScore = Double.parseDouble(maxScoreStr);

        List<Student> filteredStudents = studentService.filterByScore(minScore, maxScore);
        updateTable(filteredStudents);
    }

    private void showStatistics() {
        int totalStudents = studentService.getTotalStudents();
        double averageScore = studentService.getAverageScore();
        JOptionPane.showMessageDialog(null, "Tổng số sinh viên: " + totalStudents + "\nĐiểm trung bình: " + averageScore);
    }

    private void loadSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        ageField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        emailField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        scoreField.setText(tableModel.getValueAt(selectedRow, 4).toString());
    }

    private void updateTable() {
        updateTable(studentService.getAll());
    }

    private void updateTable(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            tableModel.addRow(new Object[]{student.getId(), student.getName(), student.getAge(),
                    student.getEmail(), student.getScore()});
        }
    }

    private void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        emailField.setText("");
        scoreField.setText("");
    }

    public static void main(String[] args) {
        new MainView();
    }
}

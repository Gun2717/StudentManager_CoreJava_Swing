package fit.se.view;

import fit.se.model.Student;
import fit.se.service.StudentService;
import fit.se.service.Validator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import java.util.Map;

public class MainView {
    // Giữ nguyên các khai báo biến cũ
    private JTextField idField, nameField, ageField, emailField, scoreField;
    private JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnImport, btnExport, btnUndo;
    private JButton btnSortByName, btnSortByScore, btnFilter, btnStatistics, btnAdvancedSearch, btnRefresh;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private StudentService studentService;
    private Validator validator;
    private JFrame frame;

    // Màu sắc chủ đạo (Modern Palette)
    private final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private final Color SECONDARY_COLOR = new Color(44, 62, 80);
    private final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private final Color ACCENT_COLOR = new Color(236, 240, 241);

    public MainView() {
        studentService = new StudentService();
        validator = new Validator();

        frame = new JFrame("Hệ Thống Quản Lý Sinh Viên Pro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        // Panel bao quanh toàn bộ với Padding lớn
        JPanel container = new JPanel(new BorderLayout(20, 20));
        container.setBackground(BACKGROUND_COLOR);
        container.setBorder(new EmptyBorder(25, 25, 25, 25));

        // 1. HEADER
        JLabel lblHeader = new JLabel("QUẢN LÝ THÔNG TIN SINH VIÊN", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblHeader.setForeground(SECONDARY_COLOR);
        lblHeader.setBorder(new EmptyBorder(0, 0, 10, 0));

        // 2. PHẦN NHẬP LIỆU (Input Card)
        JPanel inputCard = createModernInputPanel();

        // 3. PHẦN BẢNG
        JPanel tablePanel = createTablePanel();

        // 4. PHẦN NÚT CHỨC NĂNG
        JPanel buttonPanel = createButtonPanel();

        // Ghép các phần lại
        JPanel leftPanel = new JPanel(new BorderLayout(20, 20));
        leftPanel.setOpaque(false);
        leftPanel.add(inputCard, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.CENTER);

        container.add(lblHeader, BorderLayout.NORTH);
        container.add(leftPanel, BorderLayout.WEST);
        container.add(tablePanel, BorderLayout.CENTER);

        frame.add(container);
        setupActionListeners();
        updateTable();
        frame.setVisible(true);
    }

    private JPanel createModernInputPanel() {
        // Sử dụng GridBagLayout để căn chỉnh chuẩn xác hơn
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(450, 350));

        // Tạo viền bo góc và bóng đổ nhẹ (giả lập bằng LineBorder)
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        // Khởi tạo các fields với Style mới
        idField = createStyledTextField("Nhập mã SV...");
        nameField = createStyledTextField("Nhập họ tên...");
        ageField = createStyledTextField("Nhập tuổi...");
        emailField = createStyledTextField("Nhập email...");
        scoreField = createStyledTextField("Nhập điểm (0-10)...");

        // Thêm các thành phần vào Panel
        addInputRow(panel, gbc, 0, "Mã sinh viên:", idField);
        addInputRow(panel, gbc, 1, "Họ và tên:", nameField);
        addInputRow(panel, gbc, 2, "Tuổi:", ageField);
        addInputRow(panel, gbc, 3, "Email:", emailField);
        addInputRow(panel, gbc, 4, "Điểm tổng kết:", scoreField);

        return panel;
    }

    private void addInputRow(JPanel p, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(70, 70, 70));
        p.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        p.add(field, gbc);
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(200, 40));
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1, true),
                new EmptyBorder(5, 12, 5, 12)
        ));

        // Hiệu ứng Focus
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(PRIMARY_COLOR, 2, true),
                        new EmptyBorder(4, 11, 4, 11)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(210, 210, 210), 1, true),
                        new EmptyBorder(5, 12, 5, 12)
                ));
            }
        });
        return textField;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String[] columnNames = {"ID", "HỌ TÊN", "TUỔI", "EMAIL", "ĐIỂM"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);

        // Style cho Table
        studentTable.setRowHeight(35);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentTable.setGridColor(new Color(230, 230, 230));
        studentTable.setSelectionBackground(new Color(232, 244, 253));
        studentTable.setSelectionForeground(Color.BLACK);
        studentTable.setShowVerticalLines(false);

        // Style cho Header Table
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentTable.getTableHeader().setBackground(new Color(255, 255, 255));
        studentTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        ((JLabel)studentTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230)));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        // Tăng số hàng/cột để chứa đủ tất cả các nút của bạn
        JPanel panel = new JPanel(new GridLayout(0, 2, 12, 12));
        panel.setOpaque(false);

        // --- KHỞI TẠO TẤT CẢ CÁC NÚT (Đảm bảo không cái nào bị null) ---
        btnAdd = createButton("Thêm mới", new Color(46, 204, 113));
        btnUpdate = createButton("Cập nhật", new Color(52, 152, 219));
        btnDelete = createButton("Xóa bỏ", new Color(231, 76, 60));

        // Những nút này ở bản trước bị thiếu dẫn đến lỗi NullPointerException:
        btnSearch = createButton("Tìm kiếm", new Color(155, 89, 182));
        btnAdvancedSearch = createButton("TK Nâng cao", new Color(142, 68, 173));

        btnSortByName = createButton("Sắp xếp Tên", new Color(41, 128, 185));
        btnSortByScore = createButton("Sắp xếp Điểm", new Color(41, 128, 185));

        btnFilter = createButton("Lọc Học lực", new Color(243, 156, 18));
        btnStatistics = createButton("Thống kê", new Color(230, 126, 34));
        btnRefresh = createButton("Làm mới", new Color(149, 165, 166));

        btnImport = createButton("Import CSV", new Color(22, 160, 133));
        btnExport = createButton("Export CSV", new Color(39, 174, 96));
        btnUndo = createButton("Hoàn tác", new Color(192, 57, 43));

        // Thêm nút Clear (Xóa trắng)
        JButton btnClear = createButton("Xóa trắng", new Color(127, 140, 141));
        btnClear.addActionListener(e -> clearInputFields());

        // --- ADD VÀO PANEL ---
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnSearch);
        panel.add(btnAdvancedSearch);
        panel.add(btnSortByName);
        panel.add(btnSortByScore);
        panel.add(btnFilter);
        panel.add(btnStatistics);
        panel.add(btnRefresh);
        panel.add(btnImport);
        panel.add(btnExport);
        panel.add(btnUndo);
        panel.add(btnClear);

        return panel;
    }

    private JButton createButton(String text, Color baseColor) {
        JButton btn = new JButton(text.toUpperCase());
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(baseColor);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hiệu ứng Hover mượt mà
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(baseColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(baseColor);
            }
        });
        return btn;
    }

    private void setupActionListeners() {
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnSearch.addActionListener(e -> searchStudent());
        btnAdvancedSearch.addActionListener(e -> advancedSearch());
        btnImport.addActionListener(e -> importStudents());
        btnExport.addActionListener(e -> exportStudents());
        btnUndo.addActionListener(e -> undoAction());
        btnSortByName.addActionListener(e -> sortByName());
        btnSortByScore.addActionListener(e -> sortByScore());
        btnFilter.addActionListener(e -> filterStudents());
        btnStatistics.addActionListener(e -> showStatistics());
        btnRefresh.addActionListener(e -> updateTable());

        // Sự kiện chọn hàng trong bảng
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && studentTable.getSelectedRow() != -1) {
                loadSelectedStudent();
            }
        });
    }

    private void addStudent() {
        if (!validateInput(false)) {
            return;
        }

        Student student = new Student(
                idField.getText().trim(),
                nameField.getText().trim(),
                Integer.parseInt(ageField.getText().trim()),
                emailField.getText().trim(),
                Double.parseDouble(scoreField.getText().trim())
        );

        if (studentService.addStudent(student)) {
            JOptionPane.showMessageDialog(frame,
                    "Thêm sinh viên thành công!\nDữ liệu đã được lưu vào CSV.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            updateTable();
            clearInputFields();
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Mã sinh viên đã tồn tại!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        if (studentTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(frame,
                    "Vui lòng chọn sinh viên cần sửa!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateInput(true)) {
            return;
        }

        String id = idField.getText().trim();
        Student newStudent = new Student(
                id,
                nameField.getText().trim(),
                Integer.parseInt(ageField.getText().trim()),
                emailField.getText().trim(),
                Double.parseDouble(scoreField.getText().trim())
        );

        if (studentService.updateStudent(id, newStudent)) {
            JOptionPane.showMessageDialog(frame,
                    "Cập nhật sinh viên thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            updateTable();
            clearInputFields();
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Không tìm thấy sinh viên!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        if (studentTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(frame,
                    "Vui lòng chọn sinh viên cần xóa!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame,
                "Bạn có chắc muốn xóa sinh viên này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String id = idField.getText().trim();
            if (studentService.deleteStudent(id)) {
                JOptionPane.showMessageDialog(frame,
                        "Xóa sinh viên thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                updateTable();
                clearInputFields();
            }
        }
    }

    private void searchStudent() {
        String name = nameField.getText().trim();
        List<Student> foundStudents = studentService.searchByName(name);

        if (foundStudents.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Không tìm thấy sinh viên!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        updateTable(foundStudents);
    }

    private void advancedSearch() {
        JDialog dialog = new JDialog(frame, "Tìm kiếm nâng cao", true);
        dialog.setLayout(new GridLayout(7, 2, 10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(frame);

        JTextField searchId = new JTextField();
        JTextField searchName = new JTextField();
        JTextField searchMinAge = new JTextField();
        JTextField searchMaxAge = new JTextField();
        JTextField searchMinScore = new JTextField();
        JTextField searchMaxScore = new JTextField();

        dialog.add(new JLabel("Mã SV:"));
        dialog.add(searchId);
        dialog.add(new JLabel("Tên:"));
        dialog.add(searchName);
        dialog.add(new JLabel("Tuổi từ:"));
        dialog.add(searchMinAge);
        dialog.add(new JLabel("Tuổi đến:"));
        dialog.add(searchMaxAge);
        dialog.add(new JLabel("Điểm từ:"));
        dialog.add(searchMinScore);
        dialog.add(new JLabel("Điểm đến:"));
        dialog.add(searchMaxScore);

        JButton btnSearchAdvanced = new JButton("Tìm kiếm");
        btnSearchAdvanced.addActionListener(e -> {
            try {
                String id = searchId.getText().trim().isEmpty() ? null : searchId.getText().trim();
                String name = searchName.getText().trim().isEmpty() ? null : searchName.getText().trim();
                Integer minAge = searchMinAge.getText().trim().isEmpty() ? null : Integer.parseInt(searchMinAge.getText().trim());
                Integer maxAge = searchMaxAge.getText().trim().isEmpty() ? null : Integer.parseInt(searchMaxAge.getText().trim());
                Double minScore = searchMinScore.getText().trim().isEmpty() ? null : Double.parseDouble(searchMinScore.getText().trim());
                Double maxScore = searchMaxScore.getText().trim().isEmpty() ? null : Double.parseDouble(searchMaxScore.getText().trim());

                List<Student> results = studentService.advancedSearch(id, name, minAge, maxAge, minScore, maxScore);
                updateTable(results);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(btnSearchAdvanced);
        dialog.setVisible(true);
    }

    private void importStudents() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file CSV để import");
        int returnValue = fileChooser.showOpenDialog(frame);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            if (studentService.importFromCSV(filePath)) {
                JOptionPane.showMessageDialog(frame,
                        "Import dữ liệu thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                updateTable();
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Import thất bại hoặc không có dữ liệu mới!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportStudents() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file CSV");
        int returnValue = fileChooser.showSaveDialog(frame);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.endsWith(".csv")) {
                filePath += ".csv";
            }

            if (studentService.exportToCSV(filePath)) {
                JOptionPane.showMessageDialog(frame,
                        "Export dữ liệu thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Export thất bại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateInput(boolean isUpdate) {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        // Kiểm tra rỗng
        if (id.isEmpty() || name.isEmpty() || email.isEmpty() ||
                ageField.getText().trim().isEmpty() || scoreField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Vui lòng điền đầy đủ thông tin!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        int age;
        double score;

        try {
            age = Integer.parseInt(ageField.getText().trim());
            score = Double.parseDouble(scoreField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                    "Tuổi và điểm phải là số hợp lệ!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra tuổi
        if (age < 18) {
            JOptionPane.showMessageDialog(frame,
                    "Tuổi phải từ 18 trở lên!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra email
        if (!validator.validateEmail(email)) {
            JOptionPane.showMessageDialog(frame,
                    "Email không hợp lệ!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra điểm
        if (!validator.validateScore(score)) {
            JOptionPane.showMessageDialog(frame,
                    "Điểm phải từ 0 đến 10!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void undoAction() {
        if (studentService.undo()) {
            JOptionPane.showMessageDialog(frame,
                    "Hoàn tác thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            updateTable();
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Không có thao tác để hoàn tác!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void sortByName() {
        String[] options = {"Tăng dần", "Giảm dần"};
        int choice = JOptionPane.showOptionDialog(frame,
                "Chọn kiểu sắp xếp:",
                "Sắp xếp theo tên",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice != -1) {
            studentService.sortByName(choice == 0);
            updateTable();
        }
    }

    private void sortByScore() {
        String[] options = {"Tăng dần", "Giảm dần"};
        int choice = JOptionPane.showOptionDialog(frame,
                "Chọn kiểu sắp xếp:",
                "Sắp xếp theo điểm",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice != -1) {
            studentService.sortByScore(choice == 0);
            updateTable();
        }
    }

    private void filterStudents() {
        String[] levels = {"TẤT CẢ", "GIỎI", "KHÁ", "TRUNG BÌNH", "YẾU"};
        String level = (String) JOptionPane.showInputDialog(frame,
                "Chọn học lực:",
                "Lọc theo học lực",
                JOptionPane.QUESTION_MESSAGE,
                null, levels, levels[0]);

        if (level != null) {
            if (level.equals("TẤT CẢ")) {
                updateTable();
            } else {
                List<Student> filtered = studentService.filterByAcademicLevel(level);
                updateTable(filtered);
            }
        }
    }

    private void showStatistics() {
        Map<String, Object> stats = studentService.getStatistics();

        String message = String.format(
                "=== THỐNG KÊ SINH VIÊN ===\n\n" +
                        "Tổng số sinh viên: %d\n" +
                        "Điểm trung bình: %.2f\n" +
                        "Điểm cao nhất: %.2f\n" +
                        "Điểm thấp nhất: %.2f\n\n" +
                        "--- Phân loại học lực ---\n" +
                        "Giỏi (≥ 8.0): %d sinh viên\n" +
                        "Khá (6.5 - 7.9): %d sinh viên\n" +
                        "Trung bình (5.0 - 6.4): %d sinh viên\n" +
                        "Yếu (< 5.0): %d sinh viên",
                stats.get("total"),
                stats.get("average"),
                stats.get("maxScore"),
                stats.get("minScore"),
                stats.get("excellent"),
                stats.get("good"),
                stats.get("average"),
                stats.get("weak")
        );

        JOptionPane.showMessageDialog(frame, message,
                "Thống kê", JOptionPane.INFORMATION_MESSAGE);
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
            tableModel.addRow(new Object[]{
                    student.getId(),
                    student.getName(),
                    student.getAge(),
                    student.getEmail(),
                    String.format("%.2f", student.getScore())
            });
        }
    }

    private void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        emailField.setText("");
        scoreField.setText("");
        studentTable.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainView());
    }
}

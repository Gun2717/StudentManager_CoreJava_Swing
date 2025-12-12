# 🎓 Student Management System - Hệ thống Quản lý Sinh viên

## 📋 Giới thiệu

Hệ thống Quản lý Sinh viên được xây dựng bằng **Java Swing**, cho phép quản lý thông tin sinh viên một cách hiệu quả với giao diện thân thiện và các tính năng đầy đủ.

## ✨ Tính năng

### 🔥 Tính năng chính
- ✅ **Thêm sinh viên** - Thêm sinh viên mới với validation đầy đủ
- ✅ **Sửa sinh viên** - Cập nhật thông tin sinh viên
- ✅ **Xóa sinh viên** - Xóa sinh viên với xác nhận
- ✅ **Tìm kiếm** - Tìm kiếm theo tên
- ✅ **Tìm kiếm nâng cao** - Tìm kiếm theo nhiều tiêu chí
- ✅ **Sắp xếp** - Theo tên, điểm, tuổi (tăng/giảm)
- ✅ **Lọc học lực** - Giỏi, Khá, Trung bình, Yếu
- ✅ **Thống kê** - Số liệu đầy đủ về sinh viên
- ✅ **Import/Export CSV** - Nhập xuất dữ liệu
- ✅ **Undo** - Hoàn tác thao tác
- ✅ **Auto-save to CSV** - Tự động lưu khi thêm/sửa/xóa

### 📊 Tiêu chí đánh giá học lực
- **Giỏi**: Điểm ≥ 8.0
- **Khá**: Điểm từ 6.5 - 7.9
- **Trung bình**: Điểm từ 5.0 - 6.4
- **Yếu**: Điểm < 5.0

## 🏗️ Cấu trúc dự án

```
StudentManager/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── fit/
│   │   │       └── se/
│   │   │           ├── Main.java                  # Entry point
│   │   │           ├── model/
│   │   │           │   └── Student.java           # Model sinh viên
│   │   │           ├── service/
│   │   │           │   ├── StudentService.java    # Business logic
│   │   │           │   └── Validator.java         # Validation
│   │   │           ├── util/
│   │   │           │   ├── CSVUtil.java           # Xử lý CSV
│   │   │           │   └── FileUtil.java          # Xử lý File
│   │   │           └── view/
│   │   │               └── MainView.java          # Giao diện
│   │   └── resources/
│   └── data/
│       └── student.csv                            # Database CSV
├── pom.xml                                        # Maven config
└── README.md
```

## 🚀 Cài đặt và Chạy

### Yêu cầu hệ thống
- **Java**: JDK 17 trở lên (khuyến nghị JDK 23)
- **Maven**: 3.6 trở lên
- **IDE**: IntelliJ IDEA, Eclipse, hoặc VS Code

### Cách 1: Chạy với Maven

```bash
# Clone hoặc giải nén project

# Compile
mvn clean compile

# Chạy ứng dụng
mvn exec:java -Dexec.mainClass="fit.se.Main"
```

### Cách 2: Chạy với IDE

1. Mở project trong IDE
2. Đợi Maven tải dependencies
3. Chạy file `Main.java`

### Cách 3: Build JAR

```bash
# Build JAR file
mvn clean package

# Chạy JAR
java -jar target/StudentManager-1.0-SNAPSHOT.jar
```

## 📖 Hướng dẫn sử dụng

### 1. Thêm sinh viên
1. Nhập đầy đủ thông tin: ID, Tên, Tuổi, Email, Điểm
2. Click nút **"Thêm SV"**
3. Dữ liệu tự động lưu vào `src/data/student.csv`

### 2. Sửa sinh viên
1. Click chọn sinh viên trong bảng
2. Sửa thông tin trong các ô nhập liệu
3. Click nút **"Sửa SV"**

### 3. Xóa sinh viên
1. Click chọn sinh viên trong bảng
2. Click nút **"Xóa SV"**
3. Xác nhận xóa

### 4. Tìm kiếm
- **Tìm kiếm đơn giản**: Nhập tên vào ô "Tên SV" → Click **"Tìm kiếm"**
- **Tìm kiếm nâng cao**: Click **"TK Nâng cao"** → Nhập tiêu chí → Tìm kiếm

### 5. Sắp xếp
- Click **"Sắp xếp Tên"** hoặc **"Sắp xếp Điểm"**
- Chọn tăng dần hoặc giảm dần

### 6. Lọc học lực
- Click **"Lọc Học lực"**
- Chọn: Tất cả / Giỏi / Khá / Trung bình / Yếu

### 7. Thống kê
- Click **"Thống kê"**
- Xem số liệu: Tổng số SV, điểm TB, phân loại học lực

### 8. Import/Export
- **Import**: Click **"Import CSV"** → Chọn file CSV
- **Export**: Click **"Export CSV"** → Chọn nơi lưu

### 9. Undo
- Click **"Hoàn tác"** để hoàn tác thao tác gần nhất

### 10. Làm mới
- Click **"Làm mới"** để hiển thị lại toàn bộ danh sách

## 🎨 Giao diện

<img width="1919" height="1020" alt="image" src="https://github.com/user-attachments/assets/f7ee3a38-adeb-4df3-9cab-44f59d0792c4" />


### Màu sắc nút
- 🟢 **Xanh lá**: Thêm sinh viên
- 🔵 **Xanh dương**: Sửa, Sắp xếp
- 🔴 **Đỏ**: Xóa, Hoàn tác
- 🟣 **Tím**: Tìm kiếm
- 🟠 **Cam**: Lọc, Thống kê
- 🟤 **Xanh ngọc**: Import/Export
- ⚪ **Xám**: Làm mới, Xóa trắng

## 📝 Validation Rules

### ID
- Không được rỗng
- Chỉ chứa chữ cái và số
- Không được trùng

### Tên
- Không được rỗng
- Chỉ chứa chữ cái và khoảng trắng

### Tuổi
- Từ 18 đến 100

### Email
- Định dạng hợp lệ: `example@domain.com`
- Không được trùng

### Điểm
- Từ 0 đến 10

## 🗂️ Định dạng CSV

```csv
ID,Name,Age,Email,Score
SV001,Nguyen Van A,20,a@email.com,8.5
SV002,Tran Thi B,21,b@email.com,7.2
```

## 🐛 Xử lý lỗi

### File CSV không tồn tại
- Hệ thống tự động tạo file mới tại `src/data/student.csv`

### Import file lỗi
- Kiểm tra định dạng CSV
- Đảm bảo có header: `ID,Name,Age,Email,Score`

### Lỗi validation
- Đọc thông báo lỗi và sửa lại thông tin

## 💡 Tips

1. **Auto-save**: Mọi thay đổi tự động lưu vào CSV
2. **Undo**: Có thể hoàn tác nhiều thao tác
3. **Backup**: Nên export CSV thường xuyên để backup
4. **Import**: Khi import, sinh viên có ID trùng sẽ bị bỏ qua

## 🔧 Công nghệ sử dụng

- **Java 23** với preview features
- **Swing** cho giao diện
- **Maven** quản lý dependencies
- **CSV** làm database đơn giản
- **Object Serialization** cho backup

## 📚 Design Patterns

- **MVC Pattern**: Model-View-Controller
- **Service Layer Pattern**: Business logic
- **Utility Pattern**: Helper classes
- **Command Pattern**: Undo mechanism

## 🤝 Đóng góp

Mọi đóng góp đều được chào đón! Hãy tạo Pull Request hoặc Issue.

## 📄 License

MIT License - Tự do sử dụng cho mục đích học tập và thương mại.

## 👥 Tác giả

**FIT-SE Team**
- Email: caovanbao6815@gmail.com
- GitHub: https://github.com/Gun2717/

## 📞 Hỗ trợ

Nếu gặp vấn đề, vui lòng:
1. Kiểm tra phần **Xử lý lỗi** trong README
2. Xem console log để biết chi tiết lỗi
3. Tạo Issue trên GitHub

[Diagrams_StudentManager_CoreJava_Swing.docx](https://github.com/user-attachments/files/24133855/Diagrams_StudentManager_CoreJava_Swing.docx)

---

**Happy Coding! 🚀**

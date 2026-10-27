# Ứng dụng Quản lý Cửa hàng Thức ăn Nhanh & Giải khát

Khung dự án (template) cho bài tập OOP bằng Java: quản lý sản phẩm, khách hàng, nhân viên, nhà cung cấp, order/hóa đơn, nhập hàng và nhật ký kho.

> Trạng thái: **template** — toàn bộ file trong `src/` chỉ còn khai báo package, class/interface rỗng để nhóm tự triển khai.

## Yêu cầu

- JDK 8 trở lên
- Không dùng thư viện ngoài

## Cấu trúc dự án

```text
Ung-Dung-Quan-Ly-Cua-Hang-Thuc-An-Nhanh-Giai-Khat-branch-back-up/
├── README.md
├── PROJECT_TREE.md
├── Quy_uoc_Lien_ket_Method_OOP_Nhom.docx
└── src/
    ├── Main.java
    ├── interfaces/
    │   ├── IDiscountable.java
    │   ├── IPayable.java
    │   ├── IStatusable.java
    │   └── IStorable.java
    ├── model/
    │   ├── Admin.java
    │   ├── Customer.java
    │   ├── Drink.java
    │   ├── Employee.java
    │   ├── FastFood.java
    │   ├── ImportItem.java
    │   ├── ImportOrder.java
    │   ├── MenuItem.java
    │   ├── Order.java
    │   ├── OrderItem.java
    │   ├── Person.java
    │   ├── StockLog.java
    │   ├── Store.java
    │   └── Supplier.java
    ├── service/
    │   ├── CustomerManager.java
    │   ├── EmployeeManager.java
    │   ├── ImportManager.java
    │   ├── MenuManager.java
    │   ├── OrderManager.java
    │   ├── StockLogManager.java
    │   └── SupplierManager.java
    └── utils/
        ├── FileHandler.java
        └── InputValidator.java
```

## Kiến trúc

- `interfaces/` — hợp đồng dùng chung: lưu file, giảm giá, thanh toán, trạng thái.
- `model/` — thực thể nghiệp vụ; `Person` và `MenuItem` là abstract class.
- `service/` — các manager quản lý danh sách thực thể (thêm/xóa/tìm/đọc/ghi).
- `utils/` — tiện ích đọc ghi file và kiểm tra dữ liệu nhập.
- `Main.java` — điểm vào, menu console và đăng nhập admin.

## Biên dịch và chạy

PowerShell (Windows):

```powershell
javac -d out (Get-ChildItem -Recurse -File src -Filter *.java).FullName
java -cp out Main
```

macOS/Linux:

```bash
javac -d out $(find src -name "*.java")
java -cp out Main
```

## Ghi chú

- Thư mục `data/` (dữ liệu mẫu) và `diagrams/` (sơ đồ lớp) đã được gỡ bỏ khi reset về template.
- Quy ước đặt tên/liên kết method của nhóm nằm trong `Quy_uoc_Lien_ket_Method_OOP_Nhom.docx`.

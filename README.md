## Phan Cong Nhiem Vu

| Thanh Vien | Lop / Interface Phu Trach | Vai Tro & Trach Nhiem |
| :--- | :--- | :--- |
| **Nguyen Minh Tuan** |  |
| **Hoang Phuc** |  | 
| **Nguyen Gia Bao** |  | 
| **Nguyen Huu Dang Qui** |  | 
| **Lam Phoi Phoi** |  | 

---

## Cach Bien Dich & Chay (Geany / VS Code)

Chay tu thu muc chua thu muc `src/`:

```bash
# Cach 1 - bien dich ngay trong src/ (Geany compile current file)
cd src
javac -encoding UTF-8 Main.java
java -Dfile.encoding=UTF-8 Main

# Cach 2 - bien dich ra thu muc out/ tu goc du an
javac -encoding UTF-8 -sourcepath src -d out src/Main.java
java -Dfile.encoding=UTF-8 -cp out Main
```

> Luu y: file du lieu se duoc ghi vao thu muc `data/` theo thu muc ma ban chay lenh.

---

## Cau Truc Du An

```text
OOP_T2/
└── src/
    ├── interfaces/
    │   ├── IDiscountable.java
    │   ├── IPayable.java
    │   └── IStorable.java
    ├── model/
    │   ├── Admin.java
    │   ├── Customer.java
    │   ├── Drink.java
    │   ├── Employee.java
    │   ├── FastFood.java
    │   ├── MenuItem.java
    │   ├── Order.java
    │   ├── OrderItem.java
    │   └── Person.java
    ├── service/
    │   ├── CustomerManager.java
    │   ├── EmployeeManager.java
    │   ├── MenuManager.java
    │   └── OrderManager.java
    ├── utils/
    │   ├── FileHandler.java
    │   └── InputValidator.java
    └── Main.java
    class IStorable {
        <<interface>>
        +toFileString() : String
        +fromFileString(line : String) : void
    }
    class IDiscountable {
        <<interface>>
        +getDiscountRate() : double
        +setDiscountRate(rate : double) : void
        +getDiscountedPrice() : double
    }
    class IPayable {
        <<interface>>
        +calculateTotal() : double
    }
    class IStatusable {
        <<interface>>
        +isStatus() : boolean
        +setStatus(status : boolean) : void
        +updateStatus() : void
    }
    class Person {
        <<abstract>>
        #id : String
        #name : String
        #phoneNumber : String
        #status : boolean
        +Person()
        +Person(id : String, name : String, phoneNumber : String)
        +displayInfo()* : void
        +updateStatus()* : void
        +setID(id : String) : void
        +setName(name : String) : void
        +setPhoneNumber(phoneNumber : String) : void
        +setStatus(status : boolean) : void
        +getID() : String
        +getName() : String
        +getPhoneNumber() : String
        +isStatus() : boolean
    }
    class Customer {
        -memberType : String
        -loyaltyPoints : int
        +Customer()
        +Customer(id : String, name : String, phoneNumber : String, memberType : String, loyaltyPoints : int)
        +addPoints(points : int) : void
        +displayInfo() : void
        +updateStatus() : void
        +toFileString() : String
        +fromFileString(line : String) : void
        +setMemberType(memberType : String) : void
        +setLoyaltyPoints(points : int) : void
        +getMemberType() : String
        +getLoyaltyPoints() : int
    }
    class Employee {
        -role : String
        -salary : double
        +Employee()
        +Employee(id : String, name : String, phoneNumber : String, role : String, salary : double)
        +displayInfo() : void
        +updateStatus() : void
        +toFileString() : String
        +fromFileString(line : String) : void
        +setRole(role : String) : void
        +setSalary(salary : double) : void
        +getRole() : String
        +getSalary() : double
    }
    class Admin {
        -username : String
        -password : String
        +Admin()
        +Admin(id : String, name : String, phoneNumber : String, username : String, password : String)
        +Admin(id : String, name : String, phoneNumber : String, role : String, salary : double, username : String, password : String)
        +login(username : String, password : String) : boolean
        +displayInfo() : void
        +toFileString() : String
        +fromFileString(line : String) : void
        +setUsername(username : String) : void
        +setPassword(password : String) : void
        +getUsername() : String
        +getPassword() : String
    }
    class Supplier {
        -supplierID : String
        -supplierName : String
        -phoneNumber : String
        -address : String
        -status : boolean
        +Supplier()
        +Supplier(supplierID : String, supplierName : String, phoneNumber : String, address : String)
        +displayInfo() : void
        +updateStatus() : void
        +toFileString() : String
        +fromFileString(line : String) : void
        +setSupplierID(supplierID : String) : void
        +setSupplierName(supplierName : String) : void
        +setPhoneNumber(phoneNumber : String) : void
        +setAddress(address : String) : void
        +setStatus(status : boolean) : void
        +getSupplierID() : String
        +getSupplierName() : String
        +getPhoneNumber() : String
        +getAddress() : String
        +isStatus() : boolean
    }
    class MenuItem {
        <<abstract>>
        -itemID : String
        -itemName : String
        -price : double
        -stock : int
        -discountRate : double
        -status : boolean
        +displayInfo()* : void
        +increaseStock(quantity : int) : void
        +decreaseStock(quantity : int) : boolean
        +updateStatus() : void
        +setItemID(itemID : String) : void
        +setItemName(itemName : String) : void
        +setPrice(price : double) : void
        +setStock(stock : int) : void
        +setDiscountRate(discountRate : double) : void
        +setStatus(status : boolean) : void
        +getItemID() : String
        +getItemName() : String
        +getPrice() : double
        +getStock() : int
        +getDiscountRate() : double
        +getDiscountedPrice() : double
        +isStatus() : boolean
    }
    class Drink {
        -size : String
        -isIced : boolean
        +Drink()
        +Drink(itemID : String, itemName : String, price : double, stock : int)
        +Drink(itemID : String, itemName : String, price : double, stock : int, size : String, isIced : boolean)
        +displayInfo() : void
        +toFileString() : String
        +fromFileString(line : String) : void
        +setSize(size : String) : void
        +setIsIced(isIced : boolean) : void
        +getSize() : String
        +isIced() : boolean
    }
    class FastFood {
        -calories : double
        -shelfLifeHours : double
        +FastFood()
        +FastFood(itemID : String, itemName : String, price : double, stock : int, calories : double, shelfLifeHours : double)
        +displayInfo() : void
        +toFileString() : String
        +fromFileString(line : String) : void
        +setCalories(calories : double) : void
        +setShelfLifeHours(hours : double) : void
        +getCalories() : double
        +getShelfLifeHours() : double
    }
    class Order {
        <<export>>
        -orderID : String
        -orderDate : String
        -cashier : Employee
        -customer : Customer
        -items : OrderItem[]
        -count : int
        -status : boolean
        +Order()
        +Order(orderID : String, orderDate : String, cashier : Employee, customer : Customer)
        -resize() : void
        +addItem(item : OrderItem) : void
        +removeItem(itemID : String) : boolean
        +findItem(itemID : String) : OrderItem
        +calculateTotal() : double
        +confirm(logManager : StockLogManager) : boolean
        +updateStatus() : void
        +displayOrder() : void
        +toFileString() : String
        +fromFileString(line : String) : void
        +fromFileString(line : String, mm : MenuManager, em : EmployeeManager, cm : CustomerManager) : void
        +setOrderID(orderID : String) : void
        +setOrderDate(orderDate : String) : void
        +setCashier(cashier : Employee) : void
        +setCustomer(customer : Customer) : void
        +setStatus(status : boolean) : void
        +getOrderID() : String
        +getOrderDate() : String
        +getCashier() : Employee
        +getCustomer() : Customer
        +getItems() : OrderItem[]
        +getCount() : int
        +isStatus() : boolean
    }
    class OrderItem {
        -item : MenuItem
        -quantity : int
        +OrderItem(item : MenuItem, quantity : int)
        +getSubtotal() : double
        +displayItem() : void
        +toFileString() : String
        +fromFileString(line : String) : void
        +setItem(item : MenuItem) : void
        +setQuantity(quantity : int) : void
        +getItem() : MenuItem
        +getQuantity() : int
    }
    class ImportOrder {
        <<import>>
        -importID : String
        -importDate : String
        -staff : Employee
        -supplier : Supplier
        -items : ImportItem[]
        -count : int
        -status : boolean
        +ImportOrder()
        +ImportOrder(importID : String, importDate : String, staff : Employee, supplier : Supplier)
        -resize() : void
        +addItem(item : ImportItem) : void
        +removeItem(itemID : String) : boolean
        +findItem(itemID : String) : ImportItem
        +calculateTotal() : double
        +confirm(logManager : StockLogManager) : boolean
        +updateStatus() : void
        +displayImport() : void
        +toFileString() : String
        +fromFileString(line : String) : void
        +fromFileString(line : String, mm : MenuManager, em : EmployeeManager, sm : SupplierManager) : void
        +setImportID(importID : String) : void
        +setImportDate(importDate : String) : void
        +setStaff(staff : Employee) : void
        +setSupplier(supplier : Supplier) : void
        +setStatus(status : boolean) : void
        +getImportID() : String
        +getImportDate() : String
        +getStaff() : Employee
        +getSupplier() : Supplier
        +getItems() : ImportItem[]
        +getCount() : int
        +isStatus() : boolean
    }
    class ImportItem {
        -item : MenuItem
        -quantity : int
        -importPrice : double
        +ImportItem(item : MenuItem, quantity : int, importPrice : double)
        +getSubtotal() : double
        +displayItem() : void
        +toFileString() : String
        +fromFileString(line : String) : void
        +setItem(item : MenuItem) : void
        +setQuantity(quantity : int) : void
        +setImportPrice(importPrice : double) : void
        +getItem() : MenuItem
        +getQuantity() : int
        +getImportPrice() : double
    }
    class StockLog {
        -logID : String
        -logTime : String
        -item : MenuItem
        -changeType : String
        -quantity : int
        -oldStock : int
        -newStock : int
        -refID : String
        -performedBy : Employee
        -status : boolean
        +StockLog()
        +StockLog(logID : String, logTime : String, item : MenuItem, changeType : String, quantity : int, oldStock : int, newStock : int, refID : String, performedBy : Employee)
        +displayLog() : void
        +updateStatus() : void
        +toFileString() : String
        +fromFileString(line : String) : void
        +fromFileString(line : String, mm : MenuManager, em : EmployeeManager) : void
        +getLogID() : String
        +getLogTime() : String
        +getItem() : MenuItem
        +getChangeType() : String
        +getQuantity() : int
        +getOldStock() : int
        +getNewStock() : int
        +getRefID() : String
        +getPerformedBy() : Employee
        +isStatus() : boolean
        +setStatus(status : boolean) : void
    }
    class CustomerManager {
        -customers : Customer[]
        -count : int
        +CustomerManager()
        -resize() : void
        +addCustomer(c : Customer) : boolean
        +findCustomer(id : String) : Customer
        +removeCustomer(id : String) : boolean
        +displayAllCustomers() : void
        +saveToFile(fileName : String) : void
        +loadFromFile(fileName : String) : void
        +getCustomers() : Customer[]
        +getCount() : int
    }
    class EmployeeManager {
        -employees : Employee[]
        -count : int
        +EmployeeManager()
        -resize() : void
        +addEmployee(e : Employee) : boolean
        +findEmployee(id : String) : Employee
        +removeEmployee(id : String) : boolean
        +displayAllEmployees() : void
        +findHighestPaidEmployee() : Employee
        +searchByRole(role : String) : Employee[]
        +saveToFile(fileName : String) : void
        +loadFromFile(fileName : String) : void
        +getCount() : int
        +getEmployees() : Employee[]
    }
    class SupplierManager {
        -suppliers : Supplier[]
        -count : int
        +SupplierManager()
        -resize() : void
        +addSupplier(s : Supplier) : boolean
        +findSupplier(id : String) : Supplier
        +removeSupplier(id : String) : boolean
        +displayAllSuppliers() : void
        +saveToFile(fileName : String) : void
        +loadFromFile(fileName : String) : void
        +getCount() : int
        +getSuppliers() : Supplier[]
    }
    class MenuManager {
        -items : MenuItem[]
        -count : int
        +MenuManager()
        -resize() : void
        +addItem(item : MenuItem) : boolean
        +findItem(id : String) : MenuItem
        +removeItem(id : String) : boolean
        +displayAllItems() : void
        +displayAvailableItems() : void
        +findMostExpensiveItem() : MenuItem
        +saveToFile(fileName : String) : void
        +loadFromFile(fileName : String) : void
        +getCount() : int
        +getItems() : MenuItem[]
    }
    class OrderManager {
        -orders : Order[]
        -count : int
        +OrderManager()
        -resize() : void
        +addOrder(order : Order) : boolean
        +findOrder(orderID : String) : Order
        +removeOrder(orderID : String) : boolean
        +displayAllOrders() : void
        +calculateTotalRevenue() : double
        +findHighestOrderValue() : Order
        +printReceipt(order : Order) : void
        +saveToFile(fileName : String) : void
        +loadFromFile(fileName : String, mm : MenuManager, em : EmployeeManager, cm : CustomerManager) : void
        +getCount() : int
        +getOrders() : Order[]
    }
    class ImportManager {
        -imports : ImportOrder[]
        -count : int
        +ImportManager()
        -resize() : void
        +addImport(imp : ImportOrder) : boolean
        +findImport(importID : String) : ImportOrder
        +removeImport(importID : String) : boolean
        +displayAllImports() : void
        +calculateTotalImportCost() : double
        +saveToFile(fileName : String) : void
        +loadFromFile(fileName : String, mm : MenuManager, em : EmployeeManager, sm : SupplierManager) : void
        +getCount() : int
        +getImports() : ImportOrder[]
    }
    class StockLogManager {
        -logs : StockLog[]
        -count : int
        +StockLogManager()
        -resize() : void
        +addLog(log : StockLog) : boolean
        +logImport(item : MenuItem, qty : int, importID : String, staff : Employee) : void
        +logExport(item : MenuItem, qty : int, orderID : String, cashier : Employee) : void
        +findLogsByItem(itemID : String) : StockLog[]
        +findLogsByType(changeType : String) : StockLog[]
        +displayAllLogs() : void
        +displayLogsByItem(itemID : String) : void
        +saveToFile(fileName : String) : void
        +loadFromFile(fileName : String, mm : MenuManager, em : EmployeeManager) : void
        +getCount() : int
        +getLogs() : StockLog[]
    }
    class Store {
        -storeName : String
        -storeAddress : String
        -storePhoneNumber : String
        +Store()
        +Store(storeName : String, storeAddress : String, storePhoneNumber : String)
        +setStoreName(storeName : String) : void
        +setStoreAddress(storeAddress : String) : void
        +setStorePhoneNumber(phone : String) : void
        +getStoreName() : String
        +getStoreAddress() : String
        +getStorePhoneNumber() : String
    }
    class Main {
        -menuManager : MenuManager
        -customerManager : CustomerManager
        -employeeManager : EmployeeManager
        -supplierManager : SupplierManager
        -orderManager : OrderManager
        -importManager : ImportManager
        -stockLogManager : StockLogManager
        -store : Store
        -sc : Scanner
        +main(args : String[])$ void
        -productMenu() : void
        -customerMenu() : void
        -employeeMenu() : void
        -supplierMenu() : void
        -orderMenu() : void
        -importMenu() : void
        -stockLogMenu() : void
        -addProductFromInput() : void
        -createOrderFromInput() : void
        -addItemToOrderFromInput() : void
        -createImportFromInput() : void
        -addItemToImportFromInput() : void
        -saveAllData() : void
        -loadAllData() : void
    }
    class FileHandler {
        <<utility>>
        +saveToFile(fileName : String, items : IStorable[], count : int)$ boolean
        +countLines(fileName : String)$ int
        +readLines(fileName : String)$ String[]
    }
    class InputValidator {
        <<utility>>
        +getInt(sc : Scanner, prompt : String)$ int
        +getDouble(sc : Scanner, prompt : String)$ double
        +getNonEmptyString(sc : Scanner, prompt : String)$ String
        +getBoolean(sc : Scanner, prompt : String)$ boolean
    }

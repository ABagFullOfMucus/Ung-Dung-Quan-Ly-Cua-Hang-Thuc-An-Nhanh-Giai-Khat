import model.Admin;
import model.Customer;
import model.Employee;
import model.FullTimeEmployee;
import model.MemberTier;
import model.PartTimeEmployee;
import model.Permission;
import model.Role;
import model.Supplier;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Store Management Demo ===");

        Customer customer = new Customer("C001", "Nguyen Van A", "0901234567",
                "Ho Chi Minh City", 0, MemberTier.NONE);
        customer.earnPoints(250000);
        System.out.println("Customer: " + customer.getName()
                + " | points=" + customer.getLoyaltyPoints()
                + " | tier=" + customer.getTier());

        Employee fullTime = new FullTimeEmployee("E001", "Tran Thi B", "0912345678",
                "Ho Chi Minh City", Role.MANAGER, 15000000);
        Employee partTime = new PartTimeEmployee("E002", "Le Van C", "0923456789",
                "Ho Chi Minh City", Role.WAITER, 30000, 80);
        System.out.println("Full-time: " + fullTime.getName() + " | pay=" + fullTime.calculatePay());
        System.out.println("Part-time: " + partTime.getName() + " | pay=" + partTime.calculatePay());

        Admin admin = new Admin("A001", "System Admin", "0900000000",
                "Ho Chi Minh City", 20000000, "admin", "123456");
        System.out.println("Admin login: " + admin.login("admin", "123456")
                + " | can manage staff: " + admin.getRole().can(Permission.MANAGE_STAFF));

        Supplier supplier = new Supplier("S001", "Nguyen Van Supplier", "0934567890",
                "Ho Chi Minh City", "ABC Beverage Co.", "contact@abc.vn");
        System.out.println("Supplier: " + supplier.getCompanyName()
                + " | email=" + supplier.getEmail());
    }
}

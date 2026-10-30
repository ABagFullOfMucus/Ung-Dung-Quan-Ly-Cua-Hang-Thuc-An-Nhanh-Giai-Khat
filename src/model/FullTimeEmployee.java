package model;

public class FullTimeEmployee extends Employee {
    private double monthlySalary;

    public FullTimeEmployee() {
    }

    public FullTimeEmployee(String id, String name, String phoneNumber, String address,
                            Role role, double monthlySalary) {
        super(id, name, phoneNumber, address, role);
        this.monthlySalary = monthlySalary;
    }

    @Override
    public double calculatePay() {
        return monthlySalary;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }
}

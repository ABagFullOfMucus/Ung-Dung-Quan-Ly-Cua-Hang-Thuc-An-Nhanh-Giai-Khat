package model;

import interfaces.IPayable;

public abstract class Employee extends Person implements IPayable {
    private Role role;

    public Employee() {
        this.role = Role.WAITER;
    }

    public Employee(String id, String name, String phoneNumber, String address, Role role) {
        super(id, name, phoneNumber, address);
        this.role = role == null ? Role.WAITER : role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role == null ? Role.WAITER : role;
    }

    @Override
    public abstract double calculatePay();
}

package model.employee;

import interfaces.IPayable;
import model.base.Person;

public abstract class Employee extends Person implements IPayable {
    private Role role;
    private Account account;

    public Employee() {
        this.role = Role.WAITER;
        this.account = new Account();
    }

    public Employee(String id, String name, String phoneNumber, String address, Role role) {
        super(id, name, phoneNumber, address);
        this.role = role == null ? Role.WAITER : role;
        this.account = new Account();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role == null ? Role.WAITER : role;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getUsername() {
        return account == null ? null : account.getUsername();
    }

    public void setUsername(String username) {
        if (account != null) {
            account.setUsername(username);
        }
    }

    @Override
    public abstract double calculatePay();
}

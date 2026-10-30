package model;

public class Admin extends FullTimeEmployee {
    private Account account;

    public Admin() {
        super();
        setRole(Role.ADMIN);
        this.account = new Account();
    }

    public Admin(String id, String name, String phoneNumber, String address,
                 double monthlySalary, String username, String password) {
        super(id, name, phoneNumber, address, Role.ADMIN, monthlySalary);
        this.account = new Account(username, password);
    }

    public boolean login(String username, String password) {
        return account != null
                && account.getUsername() != null
                && account.getUsername().equals(username)
                && account.authenticate(password);
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

    public String getPasswordHash() {
        return account == null ? null : account.getPasswordHash();
    }
}

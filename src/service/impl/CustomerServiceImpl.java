package service.impl;

import common.Result;
import model.customer.Customer;
import persistence.Repository;
import service.base.CrudService;
import service.interfaces.ICustomerService;
import utils.Validator;

public class CustomerServiceImpl extends CrudService<Customer> implements ICustomerService {

    public CustomerServiceImpl(Repository<Customer> repository) {
        super(repository);
    }

    @Override
    protected String idPrefix() {
        return "KH";
    }

    @Override
    protected Customer[] newArray(int size) {
        return new Customer[size];
    }

    @Override
    protected String validate(Customer customer) {
        if (!Validator.isNotBlank(customer.getName())) {
            return "Tên khách hàng không được để trống";
        }
        if (!Validator.isSafeText(customer.getName())) {
            return "Tên khách hàng chứa ký tự không hợp lệ (| hoặc xuống dòng)";
        }
        if (!Validator.isValidPhone(customer.getPhoneNumber())) {
            return "Số điện thoại khách hàng không hợp lệ (10 số bắt đầu bằng 0 hoặc +84)";
        }
        if (!Validator.isSafeText(customer.getAddress())) {
            return "Địa chỉ khách hàng chứa ký tự không hợp lệ (| hoặc xuống dòng)";
        }
        if (customer.getLoyaltyPoints() < 0) {
            return "Điểm tích lũy không được âm";
        }

        if (isPhoneUsedByOtherActive(normalizePhone(customer.getPhoneNumber()), customer)) {
            return "Số điện thoại đã được đăng ký cho khách hàng khác";
        }
        return null;
    }

    private boolean isPhoneUsedByOtherActive(String normalizedPhone, Customer current) {
        Customer[] all = getAll();
        for (int i = 0; i < all.length; i++) {
            Customer existing = all[i];
            if (existing != null
                    && existing.isActive()
                    && !isSameId(existing, current)
                    && existing.getPhoneNumber() != null
                    && normalizePhone(existing.getPhoneNumber()).equals(normalizedPhone)) {
                return true;
            }
        }
        return false;
    }

    private String normalizePhone(String phone) {
        String trimmed = phone.trim();
        if (trimmed.startsWith("+84")) {
            return "0" + trimmed.substring(3);
        }
        return trimmed;
    }

    @Override
    public Result<Customer> earnPoints(String customerId, double amountSpent) {
        if (!Validator.isNotBlank(customerId)) {
            return Result.fail("Mã khách hàng không hợp lệ");
        }
        if (!Double.isFinite(amountSpent) || amountSpent <= 0) {
            return Result.fail("Số tiền chi tiêu phải lớn hơn 0");
        }

        String cleanId = customerId.trim();
        Customer customer = getRepository().findById(cleanId);
        if (customer == null) {
            return Result.fail("Không tìm thấy khách hàng có mã " + cleanId);
        }
        if (!customer.isActive()) {
            return Result.fail("Khách hàng đã bị vô hiệu hóa, không thể cộng điểm");
        }

        int pointsBefore = customer.getLoyaltyPoints();
        customer.earnPoints(amountSpent);
        int pointsAdded = customer.getLoyaltyPoints() - pointsBefore;
        if (pointsAdded == 0) {
            return Result.ok(customer);
        }

        if (!getRepository().update(customer)) {
            customer.redeemPoints(pointsAdded);
            return Result.fail("Không thể lưu điểm thưởng cho khách hàng");
        }
        return Result.ok(customer);
    }
}
package service.impl;

import model.supplier.Supplier;
import persistence.Repository;
import service.base.CrudService;
import service.interfaces.ISupplierService;
import utils.Validator;

public class SupplierServiceImpl extends CrudService<Supplier> implements ISupplierService {

    public SupplierServiceImpl(Repository<Supplier> repository) {
        super(repository);
    }

    @Override
    protected String idPrefix() {
        return "NCC";
    }

    @Override
    protected Supplier[] newArray(int size) {
        return new Supplier[size];
    }

    @Override
    protected String validate(Supplier supplier) {
        if (!Validator.isNotBlank(supplier.getName())) {
            return "Tên người liên hệ nhà cung cấp không được để trống";
        }
        if (!Validator.isSafeText(supplier.getName())) {
            return "Tên nhà cung cấp chứa ký tự không hợp lệ (| hoặc xuống dòng)";
        }
        if (!Validator.isNotBlank(supplier.getCompanyName())) {
            return "Tên công ty nhà cung cấp không được để trống";
        }
        if (!Validator.isSafeText(supplier.getCompanyName())) {
            return "Tên công ty chứa ký tự không hợp lệ (| hoặc xuống dòng)";
        }
        if (!Validator.isValidPhone(supplier.getPhoneNumber())) {
            return "Số điện thoại nhà cung cấp không hợp lệ (10 số bắt đầu bằng 0 hoặc +84)";
        }
        if (!Validator.isValidEmail(supplier.getEmail())) {
            return "Email nhà cung cấp không hợp lệ";
        }
        if (!Validator.isSafeText(supplier.getAddress())) {
            return "Địa chỉ nhà cung cấp chứa ký tự không hợp lệ (| hoặc xuống dòng)";
        }
        return null;
    }
}
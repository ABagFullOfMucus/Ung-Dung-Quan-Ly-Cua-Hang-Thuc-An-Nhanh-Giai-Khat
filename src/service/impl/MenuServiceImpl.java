package service.impl;

import model.menu.Drink;
import model.menu.FastFood;
import model.menu.MenuItem;
import persistence.Repository;
import service.base.CrudService;
import service.interfaces.IMenuService;
import utils.Validator;

public class MenuServiceImpl extends CrudService<MenuItem> implements IMenuService {

    public MenuServiceImpl(Repository<MenuItem> repository) {
        super(repository);
    }

    @Override
    protected String idPrefix() {
        return "MN";
    }

    @Override
    protected MenuItem[] newArray(int size) {
        return new MenuItem[size];
    }

    @Override
    protected String validate(MenuItem item) {
        if (!Validator.isNotBlank(item.getName())) {
            return "Tên món không được để trống";
        }
        if (!Validator.isSafeText(item.getName())) {
            return "Tên món chứa ký tự không hợp lệ (| hoặc xuống dòng)";
        }
        if (!Validator.isSafeText(item.getDescription())) {
            return "Mô tả món chứa ký tự không hợp lệ (| hoặc xuống dòng)";
        }
        double price = item.getPrice();
        if (!Double.isFinite(price) || price <= 0) {
            return "Giá món phải là số hữu hạn lớn hơn 0";
        }

        if (isNameUsedByOtherActive(item.getName().trim(), item)) {
            return "Tên món đã tồn tại trong thực đơn đang bán";
        }
        return validateDetails(item);
    }

    private boolean isNameUsedByOtherActive(String normalizedName, MenuItem current) {
        MenuItem[] all = getAll();
        for (int i = 0; i < all.length; i++) {
            MenuItem existing = all[i];
            if (existing != null
                    && existing.isActive()
                    && !isSameId(existing, current)
                    && existing.getName() != null
                    && existing.getName().trim().equalsIgnoreCase(normalizedName)) {
                return true;
            }
        }
        return false;
    }

    private String validateDetails(MenuItem item) {
        if (item instanceof Drink) {
            Drink drink = (Drink) item;
            if (drink.getSize() == null) {
                return "Đồ uống phải có kích cỡ (S, M, L)";
            }
            return null;
        }
        if (item instanceof FastFood) {
            FastFood fastFood = (FastFood) item;
            double calories = fastFood.getCalories();
            if (!Double.isFinite(calories) || calories < 0) {
                return "Lượng calo phải lớn hơn hoặc bằng 0";
            }
            return null;
        }
        return "Loại món không được hỗ trợ";
    }
}
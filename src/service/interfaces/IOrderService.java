package service.interfaces;

import common.Result;
import model.employee.Employee;
import model.transaction.Order;

public interface IOrderService extends IReadService<Order> {
    Result<Order> createOrder(String customerId, Employee employee);

    Result<Order> addItem(String orderId, String itemId, int qty);

    Result<Order> removeItem(String orderId, String itemId);

    Result<Order> confirmOrder(String orderId);

    Result<Order> completeOrder(String orderId);

    Result<Order> cancelOrder(String orderId);
}
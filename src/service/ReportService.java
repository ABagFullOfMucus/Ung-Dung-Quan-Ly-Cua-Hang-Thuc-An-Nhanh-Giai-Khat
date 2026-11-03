package service;

import model.menu.MenuItem;
import model.transaction.Order;
import model.transaction.TransactionStatus;
import persistence.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ReportService {
    private final Repository<Order> orderRepository;
    private final Repository<MenuItem> menuRepository;
    private final InventoryService inventoryService;

    public ReportService(Repository<Order> orderRepository,
                         Repository<MenuItem> menuRepository,
                         InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.inventoryService = inventoryService;
    }

    public double revenueBetween(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            return 0;
        }
        double total = 0;
        for (Order order : orderRepository.findAll()) {
            if (order.getStatus() != TransactionStatus.COMPLETED) {
                continue;
            }
            LocalDate date = order.getTime().toLocalDate();
            if (!date.isBefore(from) && !date.isAfter(to)) {
                total += order.getFinalTotal();
            }
        }
        return total;
    }

    public List<MenuItem> lowStockItems(int threshold) {
        List<MenuItem> items = menuRepository.findAll();
        MenuItem[] found = new MenuItem[items.size()];
        int count = 0;
        for (MenuItem item : items) {
            if (item.isActive() && inventoryService.getQuantity(item.getId()) <= threshold) {
                found[count++] = item;
            }
        }
        return Arrays.asList(Arrays.copyOf(found, count));
    }
}
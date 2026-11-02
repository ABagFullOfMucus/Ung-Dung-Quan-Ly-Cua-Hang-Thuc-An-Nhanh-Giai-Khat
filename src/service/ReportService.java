package service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import interfaces.Repository;
import model.MenuItem;
import model.Order;
import model.StockEntry;
import model.TransactionStatus;

public class ReportService {
    private Repository<Order> orderRepository;
    private Repository<StockEntry> stockEntryRepository;
    private Repository<MenuItem> menuItemRepository;

    public ReportService( Repository<Order> orderRepository,
            Repository<StockEntry> stockEntryRepository,
            Repository<MenuItem> menuItemRepository) {
        this.orderRepository = orderRepository;
        this.stockEntryRepository = stockEntryRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public double revenueBetween(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            return 0.0;
        }

        List<Order> orders = orderRepository.findAll();
        double revenue = 0.0;

        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);

            if (order == null || order.getTime() == null) {
                continue;
            }

            if (order.getStatus() != TransactionStatus.COMPLETED) {
                continue;
            }

            LocalDate orderDate = order.getTime().toLocalDate();

            if (!orderDate.isBefore(from) && !orderDate.isAfter(to)) {
                revenue += order.getFinalTotal();
            }
        }

        return revenue;
    }

    public List<MenuItem> lowStockItems(int threshold) {
        List<StockEntry> entries = stockEntryRepository.findAll();

        MenuItem[] lowStock = new MenuItem[entries.size()];
        int count = 0;

        for (int i = 0; i < entries.size(); i++) {
            StockEntry entry = entries.get(i);

            if (entry == null) {
                continue;
            }

            if (entry.getQuantity() >= threshold) {
                continue;
            }

            if (entry.getItem() == null) {
                continue;
            }

            MenuItem item = menuItemRepository.findById( entry.getItem().getId());

            if (item != null) {
                lowStock[count] = item;
                count++;
            }
        }

        return Arrays.asList(Arrays.copyOf(lowStock, count));
    }
}

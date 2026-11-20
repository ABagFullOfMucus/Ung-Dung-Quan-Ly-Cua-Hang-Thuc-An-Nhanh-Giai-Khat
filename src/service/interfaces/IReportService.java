package service.interfaces;

import java.time.LocalDate;
import model.menu.MenuItem;

public interface IReportService {
    double revenueBetween(LocalDate from, LocalDate to);

    MenuItem[] lowStockItems(int threshold);
}
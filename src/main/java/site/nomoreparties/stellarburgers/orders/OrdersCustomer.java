package site.nomoreparties.stellarburgers.orders;

public class OrdersCustomer {
    private boolean success;
    private Orders[] orders;
    private int total;
    private int totalToday;

    public boolean isSuccess() {
        return success;
    }

    public Orders[] getOrders() {
        return orders;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalToday() {
        return totalToday;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setOrders(Orders[] orders) {
        this.orders = orders;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setTotalToday(int totalToday) {
        this.totalToday = totalToday;
    }
}
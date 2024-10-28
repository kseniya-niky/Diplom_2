package site.nomoreparties.stellarburgers.orders;

public class OrderCreated {
    private String name;
    private Order order;
    private boolean success;

    public String getName() {
        return name;
    }

    public Order getOrder() {
        return order;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
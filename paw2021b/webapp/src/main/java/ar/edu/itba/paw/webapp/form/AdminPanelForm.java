package ar.edu.itba.paw.webapp.form;

public class AdminPanelForm {
    private int page;
    private String order;
    private int pageA;
    private String orderA;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getPageA() {
        return pageA;
    }

    public void setPageA(int page_a) {
        this.pageA = page_a;
    }

    public String getOrderA() {
        return orderA;
    }

    public void setOrderA(String order_a) {
        this.orderA = order_a;
    }
}

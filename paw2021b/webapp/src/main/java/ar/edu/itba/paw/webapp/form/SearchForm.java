package ar.edu.itba.paw.webapp.form;

import java.util.List;

public class SearchForm {
    private String query;
    private List<Integer> categoriesQuery;
    private int difficulty;
    private String order;
    private String orderU;
    private int pageU;
    private int page;
    private Boolean highlighted;
    private Boolean admins;
    private Boolean rhighlighted;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Integer> getCategoriesQuery() {
        return categoriesQuery;
    }

    public void setCategoriesQuery(List<Integer> categoriesQuery) {
        this.categoriesQuery = categoriesQuery;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getOrderU() {
        return orderU;
    }

    public void setOrderU(String orderU) {
        this.orderU = orderU;
    }

    public int getPageU() {
        return pageU;
    }

    public void setPageU(int pageU) {
        this.pageU = pageU;
    }

    public Boolean getHighlighted() {
        return highlighted;
    }

    public void setHighlighted(Boolean highlighted) {
        this.highlighted = highlighted;
    }

    public Boolean getAdmins() {
        return admins;
    }

    public void setAdmins(Boolean admins) {
        this.admins = admins;
    }

    public Boolean getRhighlighted() {
        return rhighlighted;
    }

    public void setRhighlighted(Boolean rhighlighted) {
        this.rhighlighted = rhighlighted;
    }
}

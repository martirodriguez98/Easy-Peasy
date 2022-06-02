package ar.edu.itba.paw.models;

import java.util.Collection;
import java.util.List;

public class PaginatedResult<T> {

    private String order, query, orderU;
    private List<Integer> categoriesQuery;
    private int difficulty, page, itemsPerPage, totalPages;
    private long totalItems;
    private boolean isFirstPage, isLastPage;
    private Boolean highlighted;
    private Boolean admins;
    private Boolean rHighlighted;
    private long id;

    private Collection<T> results;

    public PaginatedResult() {
    }


    //for search

    public PaginatedResult(String order, String query, List<Integer> categoriesQuery, int difficulty, int page, int itemsPerPage, long totalItems, Collection<T> results) {
        this.order = order;
        this.query = query;
        this.categoriesQuery = categoriesQuery;
        this.difficulty = difficulty;
        this.page = page;
        this.itemsPerPage = itemsPerPage;
        this.totalItems = totalItems;
        this.results = results;
        this.totalPages = (int) Math.ceil((float) totalItems / itemsPerPage);
        this.isFirstPage = page == 0;
        this.isLastPage = page == totalPages - 1;
//        this.isLastPage = itemsPerPage * page + results.size() > itemsPerPage * (totalPages - 1) && itemsPerPage * page + results.size() <= itemsPerPage * totalPages;
    }
    //for profile recipes and comments

    public PaginatedResult(int page, int itemsPerPage, long totalItems, long id, Collection<T> results) {
        this.page = page;
        this.itemsPerPage = itemsPerPage;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((float) totalItems / itemsPerPage);
        this.id = id;
        this.results = results;
        this.isFirstPage = page == 0;
        this.isLastPage = page == totalPages - 1;
//        this.isLastPage = itemsPerPage * page + results.size() > itemsPerPage * (totalPages - 1) && itemsPerPage * page + results.size() <= itemsPerPage * totalPages;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public Collection<T> getResults() {
        return results;
    }

    public void setResults(Collection<T> results) {
        this.results = results;
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

    public String getOrderU() {
        return orderU;
    }

    public void setOrderU(String orderU) {
        this.orderU = orderU;
    }

    public Boolean getrHighlighted() {
        return rHighlighted;
    }

    public void setrHighlighted(Boolean rHighlighted) {
        this.rHighlighted = rHighlighted;
    }

    @Override
    public String toString() {
        return "PaginatedResult{" +
                "order='" + order + '\'' +
                ", query='" + query + '\'' +
                ", categoriesQuery=" + categoriesQuery +
                ", difficulty=" + difficulty +
                ", page=" + page +
                ", itemsPerPage=" + itemsPerPage +
                ", totalItems=" + totalItems +
                ", totalPages=" + totalPages +
                ", isFirstPage=" + isFirstPage +
                ", isLastPage=" + isLastPage +
                ", id=" + id +
                ", results=" + results +
                '}';
    }
}

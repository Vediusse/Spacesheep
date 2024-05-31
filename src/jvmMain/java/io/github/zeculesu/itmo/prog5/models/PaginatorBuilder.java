package io.github.zeculesu.itmo.prog5.models;

import java.util.*;

public class PaginatorBuilder<Element extends Cordinatable> {

    private List<Element> listOfElement = new ArrayList<>();
    private int amountPerPage = 3;
    private int pageNumber = 1;

    private Long xCords = null;
    private Float yCords = null;

    public PaginatorBuilder() {
    }

    public PaginatorBuilder<Element> listPerPage(List<Element> listOfElement) {
        this.listOfElement = listOfElement;
        return this;
    }

    public PaginatorBuilder<Element> amountPerPage(int amountPerPage) {
        this.amountPerPage = amountPerPage;
        return this;
    }

    public PaginatorBuilder<Element> pageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public PaginatorBuilder<Element> xCords(Long xCords) {
        this.xCords = xCords;
        return this;
    }

    public PaginatorBuilder<Element> yCords(Float yCords) {
        this.yCords = yCords;
        return this;
    }

    private List<Element> getPageElements(List<Element> elements, int pageNumber, int amountPerPage) {
        int startIndex = (pageNumber - 1) * amountPerPage;
        int endIndex = Math.min(startIndex + amountPerPage, elements.size());
        return new ArrayList<>(elements.subList(startIndex, endIndex));
    }

    public Paginator<Element> paginate() {
        int totalElements = this.listOfElement.size();
        int pageAmount = (totalElements + amountPerPage - 1) / amountPerPage;
        boolean hasNext = pageNumber < pageAmount;
        boolean isFirst = pageNumber == 1;
        boolean isLast = pageNumber == pageAmount;

        List<Element> pageElements = getPageElements(this.listOfElement, pageNumber, amountPerPage);

        return new Paginator<>(pageElements, hasNext, isLast, isFirst, pageNumber);

    }

    public Paginator<Element> paginateWithCords(){
        if ((xCords == null) && (yCords == null)) {return paginate();}
        List<Element> filteredElements = new ArrayList<>();

        for (Element element : listOfElement) {
            boolean matches = true;
            if (xCords != null && !xCords.equals(element.getCoordinates().getX())) {
                matches = false;
            }
            if (yCords != null && !yCords.equals(element.getCoordinates().getY())) {
                matches = false;
            }
            if (matches) {
                filteredElements.add(element);
            }
        }


        int totalElements = filteredElements.size();
        int pageAmount = (totalElements + amountPerPage - 1) / amountPerPage;
        boolean hasNext = pageNumber < pageAmount;
        boolean isFirst = pageNumber == 1;
        boolean isLast = pageNumber == pageAmount;

        List<Element> pageElements = getPageElements(filteredElements, pageNumber, amountPerPage);

        return new Paginator<>(pageElements, hasNext, isLast, isFirst, pageNumber);
    }
}

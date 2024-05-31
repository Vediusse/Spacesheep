package io.github.zeculesu.itmo.prog5.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Paginator<Element> implements Serializable {

    @Serial
    private static final long serialVersionUID = 378997L;

    private final List<Element> listOfElement;
    private final boolean hasNext;
    private final boolean isLast;
    private final boolean isFirst;
    private final int pageNumber;


    public Paginator(List<Element> listOfElement, boolean hasNext, boolean isLast, boolean isFirst, int pageNumber) {
        this.listOfElement = listOfElement;
        this.hasNext = hasNext;
        this.isLast = isLast;
        this.isFirst = isFirst;
        this.pageNumber = pageNumber;

    }

    public List<Element> getListOfElement() {
        return listOfElement;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean isLast() {
        return isLast;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public String toString() {
        return "Paginator{" +
                "listOfElement=" + listOfElement +
                '}';
    }
}

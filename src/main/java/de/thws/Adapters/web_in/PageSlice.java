package de.thws.Adapters.web_in;

import java.util.List;

public final class PageSlice<T> {
    private final List<T> items;
    private final int page;
    private final int size;
    private final boolean hasNext;
    private final boolean hasPrev;

    private PageSlice(List<T> items, int page, int size, boolean hasNext, boolean hasPrev) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.hasNext = hasNext;
        this.hasPrev = hasPrev;
    }

    public static <T> PageSlice<T> from(List<T> items, Integer page, Integer size) {
        List<T> safeItems = items == null ? List.of() : items;
        int safePage = page == null ? 0 : Math.max(0, page);
        int safeSize = size == null ? 20 : Math.max(1, size);
        int fromIndex = safePage * safeSize;
        if (fromIndex >= safeItems.size()) {
            return new PageSlice<>(List.of(), safePage, safeSize, false, safePage > 0);
        }
        int toIndex = Math.min(safeItems.size(), fromIndex + safeSize);
        boolean hasNext = toIndex < safeItems.size();
        boolean hasPrev = safePage > 0;
        return new PageSlice<>(safeItems.subList(fromIndex, toIndex), safePage, safeSize, hasNext, hasPrev);
    }

    public List<T> getItems() {
        return items;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public boolean hasPrev() {
        return hasPrev;
    }
}

package de.thws.Adapters.web_in.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class ItemWithSelfLink<T> {
    private final T item;
    private final String self;

    public ItemWithSelfLink(T item, String self) {
        this.item = item;
        this.self = self;
    }

    @JsonUnwrapped
    public T getItem() {
        return item;
    }

    public String getSelf() {
        return self;
    }
}

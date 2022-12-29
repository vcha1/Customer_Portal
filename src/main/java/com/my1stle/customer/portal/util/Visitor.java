package com.my1stle.customer.portal.util;

@FunctionalInterface
public interface Visitor<T> {

    void visit(T t);

}
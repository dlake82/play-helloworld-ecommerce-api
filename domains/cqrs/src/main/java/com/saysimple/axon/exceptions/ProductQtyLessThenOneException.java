package com.saysimple.axon.exceptions;

public class ProductQtyLessThenOneException extends IllegalStateException {

    public ProductQtyLessThenOneException(Integer qty) {
        super("Product quantity is less than one. [" + qty + "]");
    }
}

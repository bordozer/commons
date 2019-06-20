package com.bordozer.commons.batcher;

@FunctionalInterface
public interface Applicable<T> {

    void apply(Batch<T> batch);
}

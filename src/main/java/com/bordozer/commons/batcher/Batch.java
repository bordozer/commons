package com.bordozer.commons.batcher;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Batch<T> implements Serializable {
    private final int number;
    private final List<T> items;
}

package com.bordozer.commons.batcher;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class Batch<T> {
    private final int number;
    private final List<T> items;
}

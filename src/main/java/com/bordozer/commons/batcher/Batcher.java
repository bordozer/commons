package com.bordozer.commons.batcher;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.collect.Lists.newArrayList;

public final class Batcher {

    private static final int ITEMS_IN_BATCH = 10;

    private Batcher() {
    }

    public static <T> List<Batch<T>> split(final List<T> list) {
        return split(list, ITEMS_IN_BATCH);
    }

    public static <T> List<Batch<T>> split(final List<T> list, final int itemsPerBatch) {
        if (CollectionUtils.isEmpty(list)) {
            return newArrayList();
        }
        final int batchCount = (int) Math.ceil(list.size() / (float) itemsPerBatch);
        return IntStream.range(0, batchCount)
                .mapToObj(batchNumber -> extractBatch(list, batchNumber, itemsPerBatch))
                .collect(Collectors.toList());
    }

    private static <T> Batch<T> extractBatch(final List<T> list, final int batchNumber, final int itemsPerBatch) {
        final int startIndex = batchNumber * itemsPerBatch;
        final int finalIndex = (startIndex + (itemsPerBatch - 1));
        if (finalIndex > list.size() - 1) {
            return new Batch<>(batchNumber, list.subList(startIndex, list.size()));
        }
        return new Batch<>(batchNumber, list.subList(startIndex, finalIndex + 1));
    }
}

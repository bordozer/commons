package com.bordozer.commons.batcher;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

class BatcherTest {

    @Test
    void shouldSplitOnBatches1() {
        // given
        final List<String> data = buildList(0);

        // when
        final List<Batch<String>> actual = Batcher.split(data);

        // then
        assertThat(actual).hasSize(0);
    }

    @Test
    void shouldSplitOnBatches2() {
        // given
        final List<String> data = buildList(8);

        // when
        final List<Batch<String>> actual = Batcher.split(data, 10);

        // then
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getNumber()).isEqualTo(0);
        assertThat(actual.get(0).getItems()).containsAll(newArrayList("0", "1", "2", "3", "4", "5", "6", "7"));
    }

    @Test
    void shouldSplitOnBatches3() {
        // given
        final List<String> data = buildList(8);

        // when
        final List<Batch<String>> actual = Batcher.split(data, 3);

        // then
        assertThat(actual).hasSize(3);
        assertThat(actual.get(0).getNumber()).isEqualTo(0);
        assertThat(actual.get(0).getItems()).containsAll(newArrayList("0", "1", "2"));
        assertThat(actual.get(1).getNumber()).isEqualTo(1);
        assertThat(actual.get(1).getItems()).containsAll(newArrayList("3", "4", "5"));
        assertThat(actual.get(2).getNumber()).isEqualTo(2);
        assertThat(actual.get(2).getItems()).containsAll(newArrayList("6", "7"));
    }

    @Test
    void shouldSplitOnBatches4() {
        // given
        final List<String> data = buildList(8);

        // when
        final List<Batch<String>> actual = Batcher.split(data, 4);

        // then
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0).getNumber()).isEqualTo(0);
        assertThat(actual.get(0).getItems()).containsAll(newArrayList("0", "1", "2", "3"));
        assertThat(actual.get(1).getNumber()).isEqualTo(1);
        assertThat(actual.get(1).getItems()).containsAll(newArrayList("4", "5", "6", "7"));
    }

    private static List<String> buildList(final int count) {
        return IntStream.range(0, count)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
    }
}
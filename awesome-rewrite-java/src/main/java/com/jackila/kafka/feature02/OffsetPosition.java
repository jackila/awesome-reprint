package com.jackila.kafka.feature02;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: jackila
 * @Date: 18:34 2020-08-19
 */
@Data
@AllArgsConstructor
public class OffsetPosition {

    private Long offset;
    private Integer position;
}

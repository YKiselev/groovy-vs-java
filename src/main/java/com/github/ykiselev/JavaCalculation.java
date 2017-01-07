package com.github.ykiselev;

import com.github.ykiselev.model.Component;
import com.github.ykiselev.model.Group;
import com.github.ykiselev.model.Position;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
public final class JavaCalculation {

    public Component calculate(List<Position> positions) {
        final List<Group> groups = positions.stream()
                .filter(p -> !StringUtils.equals(p.getType(), "Z"))
                .collect(Collectors.groupingBy((Position p) -> p.getType()))
                .entrySet()
                .stream()
                .map(e -> new Group(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        return new Component(
                groups.stream()
                        .map((Group g) -> g.getName())
                        .collect(Collectors.joining("|")),
                groups
        );
    }

}

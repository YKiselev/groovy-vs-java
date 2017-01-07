package com.github.ykiselev;

import com.github.ykiselev.model.Component;
import com.github.ykiselev.model.Group;
import com.github.ykiselev.model.Position;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
public final class JavaCalculation {

    public static Predicate<Position> filter() {
        return p -> !StringUtils.equals(p.getType(), "Z");
    }

    public static Function<Position, String> classifier() {
        return Position::getType;
    }

    public static Function<Map.Entry<String, List<Position>>, Group> toGroup() {
        return e -> new Group(e.getKey(), e.getValue());
    }

    public static Function<Group, String> groupName() {
        return Group::getName;
    }

    public Component calculate(List<Position> positions) {
        final List<Group> groups = positions.stream()
                .filter(p -> !StringUtils.equals(p.getType(), "Z"))
                .collect(Collectors.groupingBy(Position::getType))
                .entrySet()
                .stream()
                .map(e -> new Group(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        return new Component(
                groups.stream()
                        .map(Group::getName)
                        .collect(Collectors.joining("|")),
                groups
        );
    }

}

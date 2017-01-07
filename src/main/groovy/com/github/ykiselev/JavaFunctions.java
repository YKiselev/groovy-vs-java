package com.github.ykiselev;

import com.github.ykiselev.model.Group;
import com.github.ykiselev.model.Position;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
public final class JavaFunctions {

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

}

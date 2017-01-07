package com.github.ykiselev

import com.github.ykiselev.model.Component
import com.github.ykiselev.model.Group
import com.github.ykiselev.model.Position
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked

import java.util.function.Function
import java.util.stream.Collectors

/**
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
@TypeChecked
@CompileStatic
class GroovyCalculation {

    Component calculate(List<Position> positions) {
        final List<Group> groups = positions.stream()
                .filter({ p -> p != 'Z' })
                .collect(Collectors.groupingBy({ Position p -> p.type } as Function))
                .entrySet()
                .stream()
                .map({ Map.Entry<String, List<Position>> e -> new Group(e.getKey(), e.getValue()) })
                .collect(Collectors.toList())
        String name = groups.stream()
                .map({ Group g -> g.name } as Function)
                .collect(Collectors.joining('|'))
        return new Component(name, groups)
    }

    Component calculate2(List<Position> positions) {
        final List<Group> groups = positions.stream()
                .filter(JavaFunctions.filter())
                .collect(Collectors.groupingBy(JavaFunctions.classifier()))
                .entrySet()
                .stream()
                .map(JavaFunctions.toGroup())
                .collect(Collectors.toList())
        String name = groups.stream()
                .map(JavaFunctions.groupName() as Function)
                .collect(Collectors.joining('|'))
        return new Component(name, groups)
    }

}

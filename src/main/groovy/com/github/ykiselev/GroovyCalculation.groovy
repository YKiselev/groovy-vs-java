package com.github.ykiselev

import com.github.ykiselev.model.Component
import com.github.ykiselev.model.Group
import com.github.ykiselev.model.Position
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.apache.commons.lang3.StringUtils

import java.util.function.Function
import java.util.stream.Collectors

/**
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
//@TypeChecked
@CompileStatic
class GroovyCalculation {

    @CompileStatic
    Component calculate(List<Position> positions) {
        final List<Group> groups = positions.stream()
                .filter({ Position p -> p.type != 'Z' })
                //.filter(JavaFunctions.filter())
                .collect(Collectors.groupingBy({ Position p -> p.type } as Function))
                //.collect(Collectors.groupingBy(JavaFunctions.classifier()))
                .entrySet()
                .stream()
                .map({ Map.Entry<String, List<Position>> e -> new Group(e.getKey(), e.getValue()) })
                //.map(JavaFunctions.toGroup())
                .collect(Collectors.toList())
        String name = groups.stream()
                .map({ Group g -> g.name } as Function)
                //.map(JavaFunctions.groupName() as Function)
                .collect(Collectors.joining('|'))
        return new Component(name, groups)
    }

}

package com.github.ykiselev

import com.github.ykiselev.model.Component
import com.github.ykiselev.model.Group
import com.github.ykiselev.model.Position
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked

/**
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
//@TypeChecked
@CompileStatic
class GroovyCalculation {

    Component calculate(List<Position> positions) {
        def groups = positions.findAll {
            Position p -> p.type != 'Z'
        }.groupBy {
            Position p -> p.type
        }.entrySet()
                .collect {
            Map.Entry<String, List<Position>> e -> new Group(e.getKey(), e.getValue())
        }
        def name = groups.collect {
            Group g -> g.name
        }.join('|')
        return new Component(name, groups)
    }

}

var Collectors = Java.type("java.util.stream.Collectors")
var Group = Java.type("com.github.ykiselev.model.Group")
var Component = Java.type("com.github.ykiselev.model.Component")

var calculation = function(positions){
    var groups = positions.stream()
                .filter(function(p) { return p.getType() != 'Z'; })
                .collect(Collectors.groupingBy(function(p){ return p.getType(); }))
                .entrySet()
                .stream()
                .map(function(e){ return new Group(e.getKey(), e.getValue()); })
                .collect(Collectors.toList());
    var name = groups.stream()
            .map(function(g){ return g.getName(); })
            .collect(Collectors.joining('|'));
    return new Component(name, groups);
};
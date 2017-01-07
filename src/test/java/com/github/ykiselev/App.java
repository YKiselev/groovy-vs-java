package com.github.ykiselev;

import com.github.ykiselev.model.Component;
import com.github.ykiselev.model.Group;
import com.github.ykiselev.model.Position;
import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
public final class App {

    private static final int COUNT = 15_000;

    public static void main(String[] args) {
        new App().run();
    }

    private void doTest(List<Position> positions, Function<List<Position>, Component> calculator, String name) {
        final Stopwatch sw = Stopwatch.createStarted();
        int calcs = 0;
        long hash = 0;
        while (sw.elapsed(TimeUnit.SECONDS) <= 15) {
            final Component c = calculator.apply(positions);
            hash |= Objects.hash(c.name(), c.groups().size());
            for (Group group : c.groups()) {
                hash |= Objects.hash(group.getName(), group.getPositions().size());
            }

            calcs++;
        }
        sw.stop();
        final long ms = sw.elapsed(TimeUnit.MILLISECONDS);
        final double speed = calcs > 0
                ? 1000.0 * calcs / (double) ms
                : 0;
        System.out.println(name + ": " + calcs + " calculations in " + sw + ", speed (comps/sec)=" + speed + ", hash=" + hash);
    }

    Function<List<Position>, Component> jsCalculation() {
        final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        Objects.requireNonNull(engine);
        try {
            try (Reader r = new InputStreamReader(getClass().getResourceAsStream("/calculation.js"))) {
                engine.eval(r);
            }
        } catch (ScriptException | IOException e) {
            throw Throwables.propagate(e);
        }
        return (p) -> {
            try {
                return Component.class.cast(((Invocable) engine).invokeFunction("calculation", p));
            } catch (ScriptException | NoSuchMethodException e) {
                throw Throwables.propagate(e);
            }
        };
    }

    private void run() {
        final GroovyCalculation groovy = new GroovyCalculation();
        final JavaCalculation java = new JavaCalculation();
        final Function<List<Position>, Component> javaCode = java::calculate;
        final Function<List<Position>, Component> groovyCode = groovy::calculate;
        Function<List<Position>, Component> jsCode = jsCalculation();
        System.out.println("Starting test...");
        final List<Position> positions = Positions.prepare(COUNT);
        for (int iteration = 1; iteration < 5; iteration++) {
            doTest(positions, jsCode, "Nashorn");
            doTest(positions, javaCode, "Java");
            doTest(positions, groovyCode, "Groovy");
        }
        System.out.println("Done.");
    }
}

package com.github.ykiselev;

import com.github.ykiselev.model.Component;
import com.github.ykiselev.model.Group;
import com.github.ykiselev.model.Position;
import com.google.common.base.Stopwatch;

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

    private void doTest(List<Position> positions, Function<List<Position>, Component> calculator) {
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
        System.out.println(calcs + " calculations in " + sw + ", speed (comps/sec)=" + speed + ", " + (byte) (hash & 0xf));
    }

    private void run() {
        final GroovyCalculation groovy = new GroovyCalculation();
        final JavaCalculation java = new JavaCalculation();
        final Function<List<Position>, Component> javaCode = java::calculate;
        final Function<List<Position>, Component> groovyCode = groovy::calculate2;
        System.out.println("Starting test...");
        final List<Position> positions = Positions.prepare(COUNT);
        for (int iteration = 1; iteration < 5; iteration++) {
            doTest(positions, javaCode);
            doTest(positions, groovyCode);
        }
        System.out.println("Done.");
    }
}

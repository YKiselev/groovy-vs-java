package com.github.ykiselev;

import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
class PredicateVsClosure {

    public static void main(String[] args) {
        new PredicateVsClosure().run();
    }

    private static List<String> create(int count) {
        final List<String> src = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            src.add("#" + i);
        }
        Collections.shuffle(src);
        return src;
    }

    private void run() {
        final List<String> src = create(5_000);
        final Function<List<String>, List<String>> javaFn = PredicateVsClosure::select;
        final Function<List<String>, List<String>> groovyFn = Closures::select;
        final Function<List<String>, List<String>> groovyWithCSFn = Closures::selectWithCS;
        final Function<List<String>, List<String>> groovyNopmFn = Closures::selectNopm;
        final Function<List<String>, List<String>> groovyNopmWithCSFn = Closures::selectNopmWithCS;
        for (int i = 1; i < 4; i++) {
            doTest(src, javaFn, "Java");
            doTest(src, groovyFn, "Groovy closure");
            doTest(src, groovyWithCSFn, "Groovy closure+CS");
            doTest(src, groovyNopmFn, "Groovy closure(no params)");
            doTest(src, groovyNopmWithCSFn, "Groovy closure(no params)+CS");
        }
    }

    private void doTest(List<String> src, Function<List<String>, List<String>> fn, String name) {
        final Stopwatch sw = Stopwatch.createStarted();
        int count = 0;
        long hash = 0;
        while (sw.elapsed(TimeUnit.SECONDS) <= 10) {
            final List<String> filtered = fn.apply(src);
            count++;
            hash |= filtered.get(count % filtered.size()).hashCode();
        }
        sw.stop();
        final double speed = 1000.0 * count / (double) sw.elapsed(TimeUnit.MILLISECONDS);
        System.out.println(name + String.format(": %.2f (calls/sec), %h", speed, hash));
    }

    private static List<String> select(List<String> src) {
        //final Function<String, Boolean> badPredicate = (String it) -> it.length() > Integer.valueOf(3);
        return src.stream()
                .filter(s -> s.length() > 3)
                .collect(Collectors.toList());
    }

}

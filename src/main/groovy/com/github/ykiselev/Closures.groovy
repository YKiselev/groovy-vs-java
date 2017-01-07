package com.github.ykiselev

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

/**
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
class Closures {

    @CompileStatic
    static List<String> selectNopmWithCS(List<String> src) {
        src.findAll {
            it.length() > 3
        }
    }

    @CompileStatic
    static List<String> selectWithCS(List<String> src) {
        src.findAll {
            String it -> it.length() > 3
        }
    }

    @CompileStatic(value = TypeCheckingMode.SKIP)
    static List<String> selectNopm(List<String> src) {
        src.findAll {
            it.length() > 3
        }
    }

    @CompileStatic(value = TypeCheckingMode.SKIP)
    static List<String> select(List<String> src) {
        src.findAll {
            String it -> it.length() > 3
        }
    }

}

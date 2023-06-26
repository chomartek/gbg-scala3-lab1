package org.ditw.pise5.misc;

import java.util.*;
import java.util.function.Function;

public class ClosureTestJ {
    // https://stackoverflow.com/questions/750486/javascript-closure-inside-loops-simple-practical-example
    public static void main(String[] args) {
        int more = 10;
        Function<Integer, Integer> closure1 = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x + more;
            }
        };

        // more = 20;

        System.out.println(closure1.apply(2));
    }
}

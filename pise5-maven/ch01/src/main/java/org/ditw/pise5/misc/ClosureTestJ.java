package org.ditw.pise5.misc;

import java.util.*;

public class ClosureTestJ {
    // https://stackoverflow.com/questions/750486/javascript-closure-inside-loops-simple-practical-example
    public static void main(String[] args) {
        List<String> verbs = Arrays.asList("Hi", "Hey");
        List<String> names = Arrays.asList("John", "Jane");
        names.stream().forEach(name -> {
            verbs.stream().forEach(verb -> System.out.printf("%s %s!\n", verb, name));
        });

//        String name;
//        for (String n : names) {
//            name = n;
//            verbs.stream().forEach(verb -> System.out.printf("%s %s!\n", verb, name));
//        }
    }
}

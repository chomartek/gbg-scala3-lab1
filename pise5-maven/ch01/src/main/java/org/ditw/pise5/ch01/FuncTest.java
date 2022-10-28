package org.ditw.pise5.ch01;

import java.util.*;

public class FuncTest {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Abc", "AA");

        list.stream().forEach(System.out::println);
    }
}

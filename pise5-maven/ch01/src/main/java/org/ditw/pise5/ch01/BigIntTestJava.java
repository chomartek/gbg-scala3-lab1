package org.ditw.pise5.ch01;

import scala.Tuple2;
import scala.math.BigInt;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BigIntTestJava {
    public static void main(String[] args) {
        BigInt bi = BigInt.apply(40);
        BigInt bi2 = bi.$times(BigInt.apply(10));
        System.out.println(bi2.toString());
    }
}

package org.ditw.pise5.ch01;

import scala.math.BigInt;

import java.math.BigInteger;

public class BigIntTestJava {
    public static void main(String[] args) {
        BigInt bi = BigInt.apply(40);
        BigInt bi2 = bi.$times(BigInt.apply(10));
        System.out.println(bi2.toString());
    }
}

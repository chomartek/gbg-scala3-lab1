package org.ditw.pise5.ch01;

import scala.Tuple2;

import java.util.*;
import java.util.stream.Collectors;
//import org.ditw.pise5.ch01.Person;

public class CountryPeopleJ {
//    public static void main(String[] args) {
//        List<Person> people = Arrays.asList(
//                Person.apply("Jiaji", "CN"),
//                Person.apply("Bin", "CN"),
//                Person.apply("Per", "SV"),
//                Person.apply("Marta", "PL"),
//                Person.apply("Mythili", "IN"),
//                Person.apply("Arindam", "IN"),
//                Person.apply("Arthur", "BR")
//        );
//
//        Map<String, List<Person>> ccToPerson = people.stream().collect(
//                Collectors.groupingBy(Person::countryCode)
//        );
//
//        Map<String, List<Person>> ccToSorted = ccToPerson.keySet().stream().map(k -> {
//            List<Person> sortedByName = ccToPerson.get(k);
//            Collections.sort(sortedByName, Comparator.comparing(Person::name));
//            return new Tuple2<String, List<Person>>(k, sortedByName);
//        }).collect(
//                Collectors.toMap(Tuple2::_1, Tuple2::_2)
//        );
//
//        List<String> sortedCC = new ArrayList<>(ccToSorted.keySet());
//        Collections.sort(sortedCC);
//
//        for (String cc : sortedCC) {
//            System.out.printf("%s:\n", cc);
//            for (Person p : ccToSorted.get(cc)) {
//                System.out.printf("\t%s\n", p);
//            }
//        }
//    }
}

import First.Alternative;
import First.Pareto;
import Second.Electro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    public static void main(String[] args)  {
        second();
    }

    public static void first() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<Alternative> alternatives = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<Boolean> heights = new ArrayList<>();
        List<String> importance = new ArrayList<>();
        names.add("Температура");
        names.add("Плотность");
        names.add("Вода");
        names.add("Вулканы");
        names.add("Атмосфера");
        List<Alternative.Criteria> criteriaToNarrowing = new ArrayList<>();
        for (String name : names) {
            System.out.println("Хотите задать верхнюю границу для критерия " + name + " (1-да, 2-нет)?");
            if (Integer.parseInt(reader.readLine()) == 1) {
                System.out.println("Задайте значение верхней границы");
                criteriaToNarrowing.add(new Alternative.Criteria(name, Integer.parseInt(reader.readLine()), true));
            }
            System.out.println("Хотите задать нижнюю границу для критерия(1-да, 2-нет)?");
            if (Integer.parseInt(reader.readLine()) == 1) {
                System.out.println("Задайте значение нижней границы");
                criteriaToNarrowing.add(new Alternative.Criteria(name, Integer.parseInt(reader.readLine()), false));
            }
        }
        System.out.println("Напишите название критерия для добавления его в приоритетность или exit для продолжения");
        String impStr = reader.readLine();
        while (!impStr.equals("exit")) {
            importance.add(impStr);
            impStr = reader.readLine();
        }
        heights.add(false);
        heights.add(true);
        heights.add(true);
        heights.add(false);
        heights.add(true);
        List<Double> counts = new ArrayList<>();
        counts.add(427.);
        counts.add(5.43);
        counts.add(0.);
        counts.add(423.);
        counts.add(0.);
        alternatives.add(new Alternative("Меркурий", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(600.);
        counts.add(5.42);
        counts.add(0.);
        counts.add(1023.);
        counts.add(90.);
        alternatives.add(new Alternative("Венера", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(20.);
        counts.add(5.5);
        counts.add(5000.);
        counts.add(56.);
        counts.add(1.);
        alternatives.add(new Alternative("Земля", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(-40.);
        counts.add(5.4);
        counts.add(5.);
        counts.add(100.);
        counts.add(2.);
        alternatives.add(new Alternative("Марс", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(-108.);
        counts.add(0.6);
        counts.add(300.);
        counts.add(0.);
        counts.add(3000.);
        alternatives.add(new Alternative("Юпитер", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(-109.);
        counts.add(0.7);
        counts.add(300.);
        counts.add(0.);
        counts.add(3000.);
        alternatives.add(new Alternative("Сатурн", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(-100.);
        counts.add(0.8);
        counts.add(100.);
        counts.add(0.);
        counts.add(2000.);
        alternatives.add(new Alternative("Уран", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(-150.);
        counts.add(0.8);
        counts.add(200.);
        counts.add(0.);
        counts.add(2500.);
        alternatives.add(new Alternative("Нептун", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(-150.);
        counts.add(5.1);
        counts.add(0.);
        counts.add(40.);
        counts.add(0.5);
        alternatives.add(new Alternative("Плутон", names, heights, counts));
        Pareto.out(alternatives);
        System.out.println();
        Pareto.narrowing(alternatives, criteriaToNarrowing);
        System.out.println();
        Pareto.out(alternatives);
        System.out.println();
        Pareto.paretoMethod(alternatives);
        System.out.println();
        Pareto.out(alternatives);
        System.out.println();
        Pareto.importance(alternatives, importance);
        System.out.println();
        System.out.println(alternatives);
    }

    public static void second() {
        List<Alternative> alternatives = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<Boolean> heights = new ArrayList<>();
        names.add("Температура");
        names.add("Плотность");
        names.add("Вода");
        names.add("Вулканы");
        names.add("Атмосфера");
        heights.add(false);
        heights.add(true);
        heights.add(true);
        heights.add(false);
        heights.add(true);
        List<Double> counts = new ArrayList<>();
        counts.add(15.);
        counts.add(2.);
        counts.add(5.);
        counts.add(15.);
        counts.add(5.);
        alternatives.add(new Alternative("Меркурий", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(15.);
        counts.add(2.);
        counts.add(10.);
        counts.add(15.);
        counts.add(15.);
        alternatives.add(new Alternative("Венера", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(10.);
        counts.add(2.);
        counts.add(15.);
        counts.add(10.);
        counts.add(15.);
        alternatives.add(new Alternative("Земля", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(5.);
        counts.add(2.);
        counts.add(10.);
        counts.add(10.);
        counts.add(10.);
        alternatives.add(new Alternative("Марс", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(5.);
        counts.add(1.);
        counts.add(10.);
        counts.add(5.);
        counts.add(15.);
        alternatives.add(new Alternative("Юпитер", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(5.);
        counts.add(1.);
        counts.add(10.);
        counts.add(5.);
        counts.add(15.);
        alternatives.add(new Alternative("Сатурн", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(5.);
        counts.add(1.);
        counts.add(5.);
        counts.add(5.);
        counts.add(15.);
        alternatives.add(new Alternative("Уран", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(5.);
        counts.add(1.);
        counts.add(15.);
        counts.add(5.);
        counts.add(15.);
        alternatives.add(new Alternative("Нептун", names, heights, counts));
        counts = new ArrayList<>();
        counts.add(5.);
        counts.add(2.);
        counts.add(10.);
        counts.add(5.);
        counts.add(10.);
        alternatives.add(new Alternative("Плутон", names, heights, counts));
        Pareto.out(alternatives);
        System.out.println();
        Map<String, Integer> map = new HashMap<>();
        map.put("Температура", 5);
        map.put("Плотность", 1);
        map.put("Вода", 4);
        map.put("Атмосфера", 5);
        map.put("Вулканы", 2);
        Electro.calculate(alternatives, map);
    }
}

package First;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Pareto {
    public static class ParetoHash {
        public List<Alternative.Criteria> criteriaToNarrowing;
        public List<String> importance;
        public List<Alternative> alternatives;

        public ParetoHash(List<Alternative.Criteria> criteriaToNarrowing, List<String> importance, List<Alternative> alternatives) {
            this.criteriaToNarrowing = criteriaToNarrowing;
            this.importance = importance;
            this.alternatives = alternatives;
        }
    }

    public static ParetoHash in() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<Alternative> alternatives = new ArrayList<>();
        System.out.println("Введите количество критериев");
        int n = Integer.parseInt(reader.readLine());
        List<String> names = new ArrayList<>();
        List<Boolean> heights = new ArrayList<>();
        List<Alternative.Criteria> criteriaToNarrowing = new ArrayList<>();
        List<String> importance = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.println("Введите имя для критерия");
            names.add(reader.readLine());
            System.out.println("Критерий положительный или отрицательный(true/false)?");
            heights.add(Boolean.parseBoolean(reader.readLine()));
            System.out.println("Хотите задать верхнюю границу для критерия(1-да, 2-нет)?");
            if (Integer.parseInt(reader.readLine()) == 1) {
                System.out.println("Задайте значение верхней границы");
                criteriaToNarrowing.add(new Alternative.Criteria(names.get(names.size() - 1), Integer.parseInt(reader.readLine()), true));
            }
            System.out.println("Хотите задать нижнюю границу для критерия(1-да, 2-нет)?");
            if (Integer.parseInt(reader.readLine()) == 1) {
                System.out.println("Задайте значение нижней границы");
                criteriaToNarrowing.add(new Alternative.Criteria(names.get(names.size() - 1), Integer.parseInt(reader.readLine()), false));
            }
        }
        System.out.println("Напишите название критерия для добавления его в приоритетность или exit для продолжения");
        String impStr = reader.readLine();
        while (!impStr.equals("exit")) {
            importance.add(impStr);
            impStr = reader.readLine();
        }
        System.out.println("Введите количество альтернатив");
        n = Integer.parseInt(reader.readLine());
        for (int i = 0; i < n; i++) {
            System.out.println("Введите название варианта");
            String name = reader.readLine();
            List<Double> counts = new ArrayList<>();
            for (String na : names) {
                System.out.println("Введите значение для критерия " + na);
                counts.add(Double.parseDouble(reader.readLine()));
            }
            alternatives.add(new Alternative(name, names, heights, counts));
        }
        return new ParetoHash(criteriaToNarrowing, importance, alternatives);
    }

    public static void paretoMethod(List<Alternative> alternatives) {
        label:
        for (int i = 0; i < alternatives.size(); ) {
            for (int j = i + 1; j < alternatives.size(); ) {
                int res = alternatives.get(i).compareTo(alternatives.get(j));
                if (res < 0) {
                    System.out.println(alternatives.get(j).getName() + " доминирует над " + alternatives.get(i).getName());
                    alternatives.remove(i);
                    continue label;
                } else if (res > 0) {
                    System.out.println(alternatives.get(i).getName() + " доминирует над " + alternatives.get(j).getName());
                    alternatives.remove(j);
                    continue;
                }
                j++;
            }
            i++;
        }
    }

    public static void narrowing(List<Alternative> alternatives, List<Alternative.Criteria> criteries) {
        for (int i = 0; i < alternatives.size(); ) {
            if (alternatives.get(i).checkToNarrowing(criteries)) {
                System.out.println("Вариант " + alternatives.get(i).getName() + " исключается в результате сужения");
                alternatives.remove(alternatives.get(i));
            } else
                i++;
        }
    }

    public static void importance(List<Alternative> alternatives, List<String> criteries) {
        for (String criteria : criteries) {
            if (alternatives.size() == 1)
                return;
            boolean height = alternatives.get(0).getHeight(criteria);
            List<Alternative> best = new ArrayList<>();
            for (int i = 0; i < alternatives.size(); ) {
                Alternative alternative = alternatives.get(i);
                if (best.isEmpty() || (height && alternative.getCount(criteria) > best.get(0).getCount(criteria)) || (!height && alternative.getCount(criteria) < best.get(0).getCount(criteria))) {
                    alternatives.removeAll(best);
                    if (!best.isEmpty()) {
                        StringBuilder builder = new StringBuilder("Варианты ");
                        for (Alternative alternative1 : best) {
                            builder.append(alternative1.getName()).append(", ");
                            alternatives.remove(alternative1);
                        }
                        builder.append(" проигрывают варианту ").append(alternative.getName()).append(" по критерию ").append(criteria);
                        System.out.println(builder);
                    }
                    best = new ArrayList<>();
                    best.add(alternative);
                    i = 1;
                } else if ((height && alternative.getCount(criteria) < best.get(0).getCount(criteria)) || (!height && alternative.getCount(criteria) > best.get(0).getCount(criteria))) {
                    System.out.println("Вариант " + alternative.getName() + " проигрывает варианту " + best.get(0).getName());
                    alternatives.remove(alternative);
                }
                else {
                    best.add(alternative);
                    i++;
                }
            }
        }
    }

    public static void out(List<Alternative> alternatives) {
        for (Alternative alternative : alternatives)
            System.out.println(alternative);
    }
}
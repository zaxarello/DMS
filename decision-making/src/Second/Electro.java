package Second;

import First.Alternative;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Electro {
    private final Map<String, Integer> weight;

    private Electro(Map<String, Integer> weight) {
        this.weight = weight;
    }

    public double compare(Alternative alternative1, Alternative alternative2) {
        int first = 0;
        int second = 0;
        for (Map.Entry<String, Integer> entry : weight.entrySet()) {
            boolean height = alternative1.getHeight(entry.getKey());
            if (height && alternative1.getCount(entry.getKey()) > alternative2.getCount(entry.getKey()) || !height && alternative1.getCount(entry.getKey()) < alternative2.getCount(entry.getKey()))
                first += entry.getValue();
            else if (height && alternative1.getCount(entry.getKey()) < alternative2.getCount(entry.getKey()) || !height && alternative1.getCount(entry.getKey()) > alternative2.getCount(entry.getKey()))
                second += entry.getValue();
        }
        if (first == second)
            return 1;
        else if (second == 0)
            return -1;
        return (double) first / second;
    }

    public static void calculate(List<Alternative> alternatives, Map<String, Integer> weight) {
        Electro electro = new Electro(weight);
        TreeMap<Integer, String> map = new TreeMap<>();
        double[][] matrix = new double[alternatives.size()][alternatives.size()];
        for (int i = 0; i < alternatives.size(); i++) {
            matrix[i][i] = -2;
            for (int j = i; j < alternatives.size(); j++) {
                double result = electro.compare(alternatives.get(i), alternatives.get(j));
                if (result == -1 || result > 1) {
                    matrix[i][j] = result;
                    matrix[j][i] = -2;
                } else if (result == 1) {
                    matrix[i][j] = 1;
                    matrix[j][i] = 1;
                } else {
                    matrix[i][j] = -2;
                    matrix[j][i] = 1 / result;
                }
            }
        }
        for (double[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
        for (int i = 0; i < matrix.length; i++) {
            int count = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == -1 || matrix[i][j] >= 1)
                    count++;
            }
            String name = alternatives.get(i).getName();
            map.computeIfPresent(count, (a, b) -> b + ", " + name);
            map.putIfAbsent(count, name);
        }
        System.out.println();
        System.out.println("Результат: ");
        for (String value : map.descendingMap().values()) {
            System.out.println(value);
        }
    }
}
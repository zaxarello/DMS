package First;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Alternative implements Comparable<Alternative> {
    private final String name;
    private final List<Criteria> criterias = new ArrayList<>();

    public String getName() {
        return name;
    }

    public Alternative(String name, List<String> nCriterias, List<Boolean> bCriterias, List<Double> iCriterias) {
        this.name = name;
        for (int i = 0; i < nCriterias.size(); i++)
            criterias.add(new Criteria(nCriterias.get(i), iCriterias.get(i), bCriterias.get(i)));
    }

    public boolean checkToNarrowing(List<Criteria> criterias2) {
        for (Criteria criteria : criterias2) {
            if (criteria.height && criterias.get(criterias.indexOf(criteria)).count > criteria.count)
                return true;
            if (!criteria.height && criterias.get(criterias.indexOf(criteria)).count < criteria.count)
                return true;
        }
        return false;
    }

    public double getCount(String name) {
        for (Criteria criteria : criterias)
            if (criteria.criteriaName.equals(name))
                return criteria.count;
        return 0;
    }

    public boolean getHeight(String name) {
        for (Criteria criteria : criterias)
            if (criteria.criteriaName.equals(name))
                return criteria.height;
        return false;
    }

    public int compareTo(Alternative o) {
        int res = 0;
        for (int i = 0; i < criterias.size(); i++) {
            if (criterias.get(i).height && criterias.get(i).count > o.criterias.get(i).count || !criterias.get(i).height && criterias.get(i).count < o.criterias.get(i).count) {
                if (res < 0)
                    return 0;
                res++;
            } else if (criterias.get(i).height && criterias.get(i).count < o.criterias.get(i).count || !criterias.get(i).height && criterias.get(i).count > o.criterias.get(i).count)
                if (res > 0)
                    return 0;
            res--;
        }
        return res;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Вариант ").append(name).append(":");
        for (Criteria criteria : criterias)
            builder.append(", ").append(criteria.criteriaName).append(" = ").append(criteria.count);
        return builder.toString();
    }


    public static class Criteria {
        private final String criteriaName;
        private final double count;
        private final boolean height;


        public Criteria(String criteriaName, double count, boolean height) {
            this.criteriaName = criteriaName;
            this.count = count;
            this.height = height;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Criteria criteria = (Criteria) o;
            return Objects.equals(criteriaName, criteria.criteriaName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(criteriaName);
        }

    }
}
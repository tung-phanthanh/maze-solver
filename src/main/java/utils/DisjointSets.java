package utils;

import java.util.*;

public class DisjointSets {
    private List<Map<Integer, Set<Integer>>> disjointSet;

    public DisjointSets() {
        this.disjointSet = new ArrayList<>();
    }

    public void createSet(int element) {
        Map<Integer, Set<Integer>> map = new HashMap<>();
        Set<Integer> set = new HashSet<>();

        set.add(element);
        map.put(element, set);
        disjointSet.add(map);
    }

    public int findSet(int element) {
        for (Map<Integer, Set<Integer>> map : disjointSet) {
            Set<Integer> keySet = map.keySet();

            for (Integer key : keySet) {
                Set<Integer> set = map.get(key);
                if (set.contains(element)) {
                    return key;
                }
            }
        }
        return -1;
    }

    public void unionSet(int element1, int element2) {
        int firstReprentationSet = findSet(element1);
        int secondReprentationSet = findSet(element2);
        Set<Integer> firstSet = null;
        Set<Integer> secondSet = null;

        for (Map<Integer, Set<Integer>> map : disjointSet) {
            if (map.containsKey(firstReprentationSet)) {
                firstSet = map.get(firstReprentationSet);
            } else if (map.containsKey(secondReprentationSet)) {
                secondSet = map.get(secondReprentationSet);
            }
        }

        if (firstSet != null && secondSet != null) {
            firstSet.addAll(secondSet);
        }

        for (int i = 0; i < disjointSet.size(); i++) {
            Map<Integer, Set<Integer>> map = disjointSet.get(i);
            if (map.containsKey(firstReprentationSet)) {
                map.put(firstReprentationSet, firstSet);
            } else if (map.containsKey(secondReprentationSet)) {
                map.remove(secondReprentationSet);
                disjointSet.remove(i);
            }
        }
    }

    public int getNumberOfDisjointSets() {
        return disjointSet.size();
    }
}

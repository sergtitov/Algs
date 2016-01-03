package titov;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Merge {

    public static void print(Object o) {
        System.out.println(o);
    }

    public static <E> void merge(List<E> source, int start, int mid, int end, List<E> target) {
        int leftIndex = 0;
        int rightIndex = 0;
        int resIndex = start;

        List<E> left = source.subList(start, mid);
        List<E> right = source.subList(mid, end);

        while(leftIndex < left.size() && rightIndex < right.size()) {
            E l = left.get(leftIndex);
            E r = right.get(rightIndex);

            // unchecked
            if (((Comparable<? super E>)l).compareTo(r) < 0) {
                target.set(resIndex++, l);
                leftIndex++;
            }
            else {
                target.set(resIndex++, r);
                rightIndex++;
            }
        }

        if (leftIndex == left.size()) {
            for (E e: right.subList(rightIndex, right.size()))
                target.set(resIndex++, e);
        }

        if (rightIndex == right.size()) {
            for (E e: left.subList(leftIndex, left.size()))
                target.set(resIndex++, e);

        }
    }

    public static <E> List<E> sort(List<E> source) {
        List<E> target = new ArrayList<>(source);
        return sort1(source, 0, source.size(), target);
    }

    public static <E> List<E> sort1(List<E> source, int start, int end, List<E> target) {
        List<E> list = source.subList(start, end);
        if (list.size() == 0)
            return source;

        if (list.size() == 1) {
            target.set(start, list.get(0));
            return target;
        }

        int mid = (start + end) / 2;
        List<E> leftRes = sort1(source, start, mid, target);
        List<E> rightRes = sort1(source, mid, end, target);
        if (leftRes != rightRes) {
            for (int i = start; i < mid; i++)
                rightRes.set(i, leftRes.get(i));
        }
        List<E> res = rightRes == source ? target : source;
        merge(rightRes, start, mid, end, res);

        return res;
    }

    public static void main(String[] args) {

        int size = 10_000_000;
        long before1 = System.nanoTime();
        List<Integer> list = new ArrayList<>(size);
        for (int i = size; i >= 0; i--)
            list.add((int)(Math.random() * (double)i));
        long after1 = System.nanoTime();
        List<Integer> copy = new ArrayList<>(list);
        print("Filled in " + TimeUnit.NANOSECONDS.toMillis(after1 - before1) + "ms");

        //print(list);

        long before = System.nanoTime();
        List<Integer> sorted = sort(list);
        long after = System.nanoTime();
        print("Custom Merge Sort In " + TimeUnit.NANOSECONDS.toMillis(after - before) + "ms");
        //print(sorted);
        //print(list);

        // quicksort
        long beforeQ = System.nanoTime();
        //copy.sort((a, b) -> a - b);
        Collections.sort(copy);
        long afterQ = System.nanoTime();
        print("Quicksort In " + TimeUnit.NANOSECONDS.toMillis(afterQ - beforeQ) + "ms");
        //print(copy);

        print(copy.equals(sorted));
    }
}

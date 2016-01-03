package titov;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Merge {

    public static void print(Object o) {
        System.out.println(o);
    }

    public static <E> void merge(List<E> source, int start, int mid, int end, List<E> target) {
        int leftIndex = start;
        int rightIndex = mid;
        int resIndex = start;

        while(leftIndex < mid && rightIndex < end) {
            E l = source.get(leftIndex);
            E r = source.get(rightIndex);

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

        if (leftIndex == mid) {
            for (int i = rightIndex; i < end; i++)
                target.set(resIndex++, source.get(i));
        }

        if (rightIndex == end) {
            for (int i = leftIndex; i < mid; i++)
                target.set(resIndex++, source.get(i));
        }
    }

    public static <E> List<E> sort(List<E> source) {
        List<E> target = new ArrayList<>(source);
        return sort1(source, 0, source.size(), target);
    }

    public static <E> List<E> sort1(List<E> source, int start, int end, List<E> target) {
        if (end - start <= 1) {
            return source;
        }

        if (end - start == 2) {
            E e1 = source.get(start);
            E e2 = source.get(start + 1);
            if (((Comparable<? super E>)e1).compareTo(e2) > 0) {
                source.set(start, e2);
                source.set(start + 1, e1);
            }
            return source;
        }

        int mid = (start + end) / 2;
        List<E> leftRes = sort1(source, start, mid, target);
        List<E> rightRes = sort1(source, mid, end, target);

        // if results of the sort are in a different array, copy left to right (right is usually one item bigger)
        if (leftRes != rightRes) {
            for (int i = start; i < mid; i++)
                rightRes.set(i, leftRes.get(i));
        }

        List<E> res = rightRes == source ? target : source;

        // avoid merge if biggest left is smaller than smallest right
        if (((Comparable<? super E>)rightRes.get(mid - 1)).compareTo(rightRes.get(mid)) <= 0)
            return rightRes;

        merge(rightRes, start, mid, end, res);

        return res;
    }

    public static void main(String[] args) {

        int size = 10_000_000;
        long before1 = System.nanoTime();
        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            list.add((int)(Math.random() * (double)size));
        long after1 = System.nanoTime();
        List<Integer> copy = new ArrayList<>(list);
        print("Filled in " + TimeUnit.NANOSECONDS.toMillis(after1 - before1) + "ms");

        //print(list);

        long before = System.nanoTime();
        List<Integer> sorted = sort(list);
        long after = System.nanoTime();
        print("Custom Merge Sort in " + TimeUnit.NANOSECONDS.toMillis(after - before) + "ms");
        //print(sorted);
        //print(list);

        // quicksort
        long beforeQ = System.nanoTime();
        //copy.sort((a, b) -> a - b);
        Collections.sort(copy);
        long afterQ = System.nanoTime();
        print("Collections.sort in " + TimeUnit.NANOSECONDS.toMillis(afterQ - beforeQ) + "ms");
        //print(copy);

        print(copy.equals(sorted));
    }
}

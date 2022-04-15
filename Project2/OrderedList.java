package Project2;

import java.util.*;

public class OrderedList {

    public static Boolean checkSorted(ArrayList<Polynomial> list) {
        int result;
        for (int i = 1; i < list.size(); i++) {
            result = list.get(i - 1).compareTo(list.get(i));

            if (result < 0) {
                return false;
            }
        }
        return true;
    }

    public static Boolean checkSorted(ArrayList<Polynomial> list, Comparator<Polynomial> comparator) {
        int result;
        for (int i = 1; i < list.size(); i++) {
            result = comparator.compare(list.get(i - 1), list.get(i));

            if (result < 0) {
                return false;
            }
        }
        return true;
    }
}

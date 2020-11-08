package sample;

import java.util.ArrayList;
import java.util.List;

public class MySample {

    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
        int mask = l.stream().filter(i -> i > 0 && i % 2 == 0).map(i -> -i).mapToInt(i -> i).reduce(0, (a, b) -> a | b);
    }

}

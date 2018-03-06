import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by martin on 12/27/16.
 */
public class UniqueNames {
    Map<String, Integer> names;

    public UniqueNames() {
        this.names = new TreeMap<>();
    }

    public void addName(String name) {
        names.computeIfAbsent(name, freq -> 0);
        names.computeIfPresent(name, (key, freq) -> freq = freq + 1);
    }

    public void printN(int n) {
        for (Map.Entry<String, Integer> entry : names.entrySet()) {
            int freq = entry.getValue();
            if (freq >= n) {
                String name = entry.getKey();
                System.out.format("%s (%d) %d", name, freq, uniqueLetters(name));
                System.out.println();
            }
        }
    }

    public String findName(int len, int x) {
        List<String> uniqueNames = names.keySet()
                .stream()
                .filter(name -> name.length() < len)
                .collect(Collectors.toList());
        System.out.println(uniqueNames);
        return uniqueNames.get(x % uniqueNames.size());
    }

    private int uniqueLetters(String word) {
        Set<Character> uniqueLetters = new HashSet<>();
        for (Character letter : word.toCharArray())
            uniqueLetters.add(Character.toLowerCase(letter));
        return uniqueLetters.size();
    }

}

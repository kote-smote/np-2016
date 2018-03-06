import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Argument: JavaProjects/Advanced Programming/quotes.txt
 */
public class WordCount {

    public static void main(String[] args) {
        StringBuilder result = new StringBuilder();
        
        if (args.length == 0) {
            System.out.println("Provide filename as argument");
            return;
        }
        
        for (String fileName : args) {
            try {
//              String wordCount = processFile(fileName);
//              String wordCount = processWithMapReduce(fileName);
                String wordCount = processFileWithConsumer(fileName);
                result.append(String.format("%s -> %s\n", fileName, wordCount));
                System.out.println(result.toString());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
       
    private static String processFile(String fileName) throws IOException {
        int linesCount = 0;
        int wordsCount = 0;
        int charactersCount = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                linesCount++;
                String[] words = line.split("\\s+");
                wordsCount += words.length;
                charactersCount += line.length() + 1;
            }
        }
        return String.format("%d %d %d", linesCount, wordsCount, charactersCount);
    }
    
    private static String processWithMapReduce(String fileName) throws IOException {
        Pattern word = Pattern.compile("\\s+");
        long[] result = Files.lines(Paths.get(fileName))
                .map(line -> {
                    long words = word.split(line).length;
                    return new long[]{1, words, line.length() + 1};
                })
                .reduce(new long[]{0, 0, 0},
                        (left, right) -> {
                            long[] sum = new long[3];
                            Arrays.setAll(sum, i -> left[i] + right[i]);
                            return sum;
                        });
        return Arrays.stream(result)
                .mapToObj(l -> String.format("%d", l))
                .collect(Collectors.joining(" "));
//      return new String(result[0] + " " + result[1] + " " + result[2]);

    }
    
    /**
     * Solution using {@link Consumer< FileCounts >} function
     */
    private static String processFileWithConsumer(String fileName) throws IOException {
        FileCounts fileCounts = new FileCounts();
        Files.lines(Paths.get(fileName))
        	.forEach(fileCounts);
        return fileCounts.toString();
    }
 
    static class FileCounts implements Consumer<String> {
        private static final Pattern WORD = Pattern.compile("\\s+");
        long lines;
        long chars;
        long words;

        public FileCounts() {
            this.lines = 0;
            this.chars = 0;
            this.words = 0;
        }

        @Override
        public void accept(String line) {
            ++lines;
            this.chars += line.length() + 1; // + the \n character
            this.words += WORD.split(line).length;
        }

        @Override
        public String toString() {
            return String.format("%d %d %d", lines, words, chars);
        }
    }    
}


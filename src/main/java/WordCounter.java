import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class WordCounter {
    public static void main(String[] args) {

        String text = readArticleText();

        String[] wordsToCount = {"I", "Dostoyevsky", "a"};

        Map<String, Integer> wordCountMap = countWords(text, wordsToCount);

        printResults(wordCountMap);

        saveResultsToFile(wordCountMap);
    }
    private static String readArticleText() {
        try {

            return FileUtils.readFileToString(new File("src/main/resources/text.txt"), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    private static Map<String, Integer> countWords(String text, String[] wordsToCount) {
        Map<String, Integer> wordCountMap = new HashMap<>();
        for (String word : wordsToCount) {
            int count = StringUtils.countMatches(text, word);
            wordCountMap.put(word, count);
        }
        return wordCountMap;
    }
    private static void printResults(Map<String, Integer> wordCountMap) {
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
    private static void saveResultsToFile(Map<String, Integer> wordCountMap) {
        try {

            File outputFile = new File("src/main/resources/count_result.txt");

            for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
                FileUtils.write(outputFile, entry.getKey() + " = " + entry.getValue() + "\n", "UTF-8", true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


package dist_spamfilter_brunner_heimgartner_iseli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class FileHandler {
	private static final String SPACE = "\\ ";

	public static Map<String, BigDecimal> readWordsFromDirectory(File direcotry) throws IOException {
		Map<String, BigDecimal> words = new HashMap<>();

		if (direcotry.isDirectory()) {
			File[] files = direcotry.listFiles();
			for (File file : files) {
				Set<String> unicords = readWordsFromFile(file);
				for (String word : unicords) {
					words.put(word, words.get(word) == null ? BigDecimal.ONE : words.get(word).add(BigDecimal.ONE));
				}
				
			}
		}

		return words;
	}

	public static Set<String> readWordsFromFile(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		Set<String> unichorn = new HashSet<>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] potentialWords = line.split(SPACE);
			for (String potentialWord : potentialWords) {
				unichorn.add(potentialWord);
			}
		}
		scanner.close();
		return unichorn;
	}

}

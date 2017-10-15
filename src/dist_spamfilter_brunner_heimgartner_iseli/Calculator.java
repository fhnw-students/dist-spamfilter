package dist_spamfilter_brunner_heimgartner_iseli;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Calculator {

	private static final int PRECISION = 20;


	public static BigDecimal pOfEmail(SpamFilterData data, Set<String> content) {
		List<BigFraction> pOfHamWords = new ArrayList<>();
		List<BigFraction> pOfSpamWords = new ArrayList<>();

		content.forEach(word -> {
			fillPOfWords(data, pOfSpamWords, word, data.getSpamWords());
			fillPOfWords(data, pOfHamWords, word, data.getHamWords());
		});

		BigDecimal numberOfAllMails = data.getNumberOfHamMails().add(data.getNumberOfSpamMails());
		double pSpam = data.getNumberOfSpamMails().divide(numberOfAllMails, PRECISION, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		double pHam = data.getNumberOfHamMails().divide(numberOfAllMails, PRECISION, BigDecimal.ROUND_HALF_UP)
				.doubleValue();

		BigFraction totalProbability = totalProbability(pSpam, pHam, pOfSpamWords, pOfHamWords);
		return totalProbability.toBigDecimal();
	}

	private static void fillPOfWords(SpamFilterData data, List<BigFraction> pOfWords, String word,
			Map<String, BigDecimal> hamWords) {
		BigDecimal occurencesOfWordInHam = hamWords.get(word);
		if (occurencesOfWordInHam == null || occurencesOfWordInHam == BigDecimal.ZERO) {
			occurencesOfWordInHam = data.getAlpha();
		}
		BigDecimal numberOfHamWordsOverall = new BigDecimal(hamWords.size());
		try {
			BigFraction pOfHamWord = new BigFraction(occurencesOfWordInHam, numberOfHamWordsOverall);
			pOfWords.add(pOfHamWord);
		} catch (ArithmeticException e) {
			// hang on!
			System.out.println(occurencesOfWordInHam + " / " + numberOfHamWordsOverall);
		}
	}

	public static BigFraction totalProbability(double pSpam, double pHam, List<BigFraction> pWortSpam,
			List<BigFraction> pWortHam) {
		BigFraction nominator = new BigFraction(pSpam);
		for (int i = 0; i < pWortSpam.size(); ++i) {
			nominator = nominator.multiply(pWortSpam.get(i));
		}
		BigFraction denominator = new BigFraction(pHam);
		for (int i = 0; i < pWortHam.size(); ++i) {
			denominator = denominator.multiply(pWortHam.get(i));
		}
		denominator = denominator.add(nominator);
		return new BigFraction(nominator.toBigDecimal(), denominator.toBigDecimal());
	}

}
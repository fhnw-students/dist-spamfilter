package dist_spamfilter_brunner_heimgartner_iseli;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Calculator {

	private static final int PRECISION = 20;

	public static void main(String[] args) {

		// zahlen nicht als relative Prozentwerte eintragen sondern als absolute
		// Werte --> 3% als 3 und nicht als 0.03
		double[] a = new double[2];
		a[0] = 7;
		a[1] = 8;

		double[] b = new double[2];
		b[0] = 30;
		b[1] = 3;

		System.out.println(gesamtWkeit(50, 50, a, b));

	}

	// wird nicht gebraucht
	public static double wkeitSpamWort(double anzWortInSpam, double anzWortInHam, double anzSpams, double anzHams) {
		double pWort = (anzWortInHam + anzWortInSpam) / (anzHams + anzSpams);
		// double pSpam = anzSpams /(anzHams+anzSpams) ;
		double pSpamWort = (anzWortInSpam / (anzHams + anzSpams)) / pWort;
		return pSpamWort;
	}

	// wird nicht gebraucht
	public static BigDecimal wkeitSpamWort(BigDecimal anzWortInSpam, BigDecimal anzWortInHam, BigDecimal anzSpams,
			BigDecimal anzHams) {
		BigDecimal pWort = anzWortInHam.add(anzWortInSpam).divide((anzHams.add(anzSpams)));
		// double pSpam = anzSpams /(anzHams+anzSpams) ;
		BigDecimal pSpamWort = anzWortInSpam.divide(anzHams.add(anzSpams)).divide(pWort);
		return pSpamWort;
	}

	public static BigDecimal pOfEmail(SpamFilterData data, Set<String> content) {
		List<BigDecimal> pOfHamWords = new ArrayList<>();
		List<BigDecimal> pOfSpamWords = new ArrayList<>();

		content.forEach(word -> {
			fillPOfWords(data, pOfSpamWords, word, data.getSpamWords());
			fillPOfWords(data, pOfHamWords, word, data.getHamWords());
		});

		BigDecimal numberOfAllMails = data.getNumberOfHamMails().add(data.getNumberOfSpamMails());
		double pSpam = data.getNumberOfSpamMails().divide(numberOfAllMails, PRECISION, BigDecimal.ROUND_HALF_UP).doubleValue();
		double pHam = data.getNumberOfHamMails().divide(numberOfAllMails, PRECISION, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		double gesamtWkeit = gesamtWkeit(pSpam, pHam, toDoubleArray(pOfSpamWords), toDoubleArray(pOfHamWords));
		return new BigDecimal(gesamtWkeit);
	}

	private static void fillPOfWords(SpamFilterData data, List<BigDecimal> pOfHamWords, String word,
			Map<String, BigDecimal> hamWords) {
		BigDecimal occurencesOfWordInHam = hamWords.get(word);
		if (occurencesOfWordInHam == null || occurencesOfWordInHam == BigDecimal.ZERO) {
			occurencesOfWordInHam = data.getAlpha();
		}
		BigDecimal numberOfHamWordsOverall = new BigDecimal(hamWords.size());
		try {
			BigDecimal pOfHamWord = occurencesOfWordInHam.divide(numberOfHamWordsOverall, PRECISION, BigDecimal.ROUND_HALF_UP);
			pOfHamWords.add(pOfHamWord);
		} catch (ArithmeticException e) {
			// hang on!
			System.out.println(occurencesOfWordInHam + " / " + numberOfHamWordsOverall);
		}
	}

	private static double[] toDoubleArray(List<BigDecimal> bigDecimals) {
		double[] doubles = new double[bigDecimals.size()];
		for (int i = 0; i < bigDecimals.size(); i++) {
			doubles[i] = bigDecimals.get(i).doubleValue();
		}
		return doubles;
	}

	public static double gesamtWkeit(double pSpam, double pHam, double[] pWortSpam, double[] pWortHam) {
		double factor = 100;
		pSpam *= factor;
		pHam *= factor;
		double zaehler = pSpam;
		for (int i = 0; i < pWortSpam.length; ++i) {
			zaehler *= (pWortSpam[i] * factor);
		}
		double nenner = pHam;
		for (int i = 0; i < pWortSpam.length; ++i) {
			nenner *= (pWortHam[i] * factor);
		}
		nenner += zaehler;

		System.out.println(zaehler);
		System.out.println(nenner);
		return zaehler / nenner;
	}

}
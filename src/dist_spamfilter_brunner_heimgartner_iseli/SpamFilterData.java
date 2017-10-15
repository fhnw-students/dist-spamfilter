package dist_spamfilter_brunner_heimgartner_iseli;

import java.math.BigDecimal;
import java.util.Map;

public class SpamFilterData {

	private BigDecimal numberOfHamMails;
	private BigDecimal numberOfSpamMails;
	private Map<String, BigDecimal> spamWords;
	private Map<String, BigDecimal> hamWords;
	private BigDecimal alpha = BigDecimal.ONE;
	
	public Map<String, BigDecimal> getSpamWords() {
		return spamWords;
	}

	public void setSpamWords(Map<String, BigDecimal> spamWords) {
		this.spamWords = spamWords;
	}

	public Map<String, BigDecimal> getHamWords() {
		return hamWords;
	}

	public void setHamWords(Map<String, BigDecimal> hamWords) {
		this.hamWords = hamWords;
	}

	public BigDecimal getNumberOfHamMails() {
		return numberOfHamMails;
	}

	public void setNumberOfHamMails(BigDecimal numberOfHamsMails) {
		this.numberOfHamMails = numberOfHamsMails;
	}

	public BigDecimal getNumberOfSpamMails() {
		return numberOfSpamMails;
	}

	public void setNumberOfSpamMails(BigDecimal numberOfSpamMails) {
		this.numberOfSpamMails = numberOfSpamMails;
	}

	public void addSpamWordIfNotPresent(String word, BigDecimal amountOfOccurrences) {
		if (!spamWords.containsKey(word)) {
			spamWords.put(word, amountOfOccurrences);
		}
	}

	public void addHamWordIfNotPresent(String word, BigDecimal amountOfOccurrences) {
		if (!hamWords.containsKey(word)) {
			hamWords.put(word, amountOfOccurrences);
		}
	}

	public BigDecimal getAlpha() {
		return alpha;
	}
	
	public void setAlpha(BigDecimal alpha) {
		this.alpha = alpha;
	}

}

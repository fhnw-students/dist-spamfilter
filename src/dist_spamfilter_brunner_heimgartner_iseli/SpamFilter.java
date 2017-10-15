package dist_spamfilter_brunner_heimgartner_iseli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpamFilter {

	private SpamFilterData data;
	private String pathToHamEmails;
	private String pathToSpamEmails;
	private String pathToSpamCalibrationMails;
	private String pathToHamCalibrationMails;

	public SpamFilter(String pathToHams, String pathToSpams, String pathToSpamCalibrationMails,
			String pathToHamCalibrationMails) {
		data = new SpamFilterData();
		this.pathToHamEmails = pathToHams;
		this.pathToSpamEmails = pathToSpams;
		this.pathToSpamCalibrationMails = pathToSpamCalibrationMails;
		this.pathToHamCalibrationMails = pathToHamCalibrationMails;
	}

	/**
	 * 
	 * @param desiredQuality
	 *            a value in [0..1] with the desired quality
	 */
	public void startCalculation(double desiredQuality) {
		// while (desiredQuality < threshold) {
		System.out.println("learning...");
		learn();
		System.out.println("calibrating...");
		calibrate();
	}
	// }

	public BigDecimal checkEmail(File email) {
		Set<String> wordsInEmail = new HashSet<>();
		BigDecimal pOfEmail = BigDecimal.ZERO;
		try {
			wordsInEmail = FileHandler.readWordsFromFile(email);
			pOfEmail = Calculator.pOfEmail(data, wordsInEmail);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return pOfEmail;

	}

	private void learn() {
		try {
			System.out.println("reading for learn...");
			readSpams();
			readHams();

			System.out.println("synchronizing for learn...");
			synchronizeSpamWords();
			synchronizeHamWords();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void calibrate() {
		List<BigDecimal> pOfSpams = calibrateWords(pathToSpamCalibrationMails);
		List<BigDecimal> pOfHams = calibrateWords(pathToHamCalibrationMails);

		double d = 0.5;
		filterForPercentage(pOfSpams, d);
		filterForPercentage(pOfHams, d);
		d = 0.95;
		filterForPercentage(pOfSpams, d);
		filterForPercentage(pOfHams, d);
		d = 0.97;
		filterForPercentage(pOfSpams, d);
		filterForPercentage(pOfHams, d);
		d = 0.99;
		filterForPercentage(pOfSpams, d);
		filterForPercentage(pOfHams, d);

	}

	private void filterForPercentage(List<BigDecimal> ps, double d) {
		System.out.println("filtering for " + d * 100 + "% mails...");
		long amountOf99PercentageSpamClassifications = ps.stream().filter(probability -> {
			return probability.doubleValue() >= d;
		}).count();
		System.out.println(">=" + d * 100 + "%: " + amountOf99PercentageSpamClassifications + " out of " + ps.size());
	}

	private List<BigDecimal> calibrateWords(String pathToCalibrationMails) {
		System.out.println("calibrating words for " + pathToCalibrationMails + "...");
		File[] filesInCalibration = new File(pathToCalibrationMails).listFiles();
		List<BigDecimal> pOfEmails = new ArrayList<>();
		for (int i = 0; i < filesInCalibration.length; i++) {
			BigDecimal pOfEmail = checkEmail(filesInCalibration[i]);
			pOfEmails.add(pOfEmail);
		}
		return pOfEmails;

	}

	private void synchronizeHamWords() {
		data.getSpamWords().forEach((spamWord, amountOfWord) -> {
			data.addHamWordIfNotPresent(spamWord, data.getAlpha());
		});
	}

	private void synchronizeSpamWords() {
		data.getHamWords().forEach((hamWord, amountOfWord) -> {
			data.addSpamWordIfNotPresent(hamWord, data.getAlpha());
		});
	}

	private void readHams() throws IOException {
		String hamEmailsPath = pathToHamEmails;
		File hamFiles = new File(hamEmailsPath);
		data.setHamWords(FileHandler.readWordsFromDirectory(hamFiles));
		int numberOfHamMails = hamFiles.listFiles().length;
		data.setNumberOfHamMails(new BigDecimal(numberOfHamMails));
	}

	private void readSpams() throws IOException {
		String spamEmailsPath = pathToSpamEmails;
		File spamFiles = new File(spamEmailsPath);
		data.setSpamWords(FileHandler.readWordsFromDirectory(spamFiles));
		int numberOfSpamMails = spamFiles.listFiles().length;
		data.setNumberOfSpamMails(new BigDecimal(numberOfSpamMails));
	}

	public SpamFilterData getData() {
		return data;
	}

	public void setData(SpamFilterData data) {
		this.data = data;
	}

}

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

	public SpamFilter(String pathToHams, String pathToSpams, String pathToHamCalibrationMails,
			String pathToSpamCalibrationMails) {
		data = new SpamFilterData();
		this.pathToHamEmails = pathToHams;
		this.pathToSpamEmails = pathToSpams;
		learn();
		calibrate(pathToHamCalibrationMails, pathToSpamCalibrationMails);
	}

	public BigDecimal checkEmail(File email) {
		Set<String> wordsInEmail = new HashSet<>();
		BigDecimal pOfEmail = BigDecimal.ZERO;
		try {
			wordsInEmail = FileHandler.readWordsFromFile(email);
			pOfEmail = Calculator.pOfEmail(data, wordsInEmail);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		wordsInEmail.forEach(word -> {
			System.out.println(word);
		});

		return pOfEmail;

	}

	private void learn() {
		try {
			readSpams();
			readHams();

			synchronizeSpamWords();
			synchronizeHamWords();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void calibrate(String pathToHamCalibrationMails, String pathToSpamCalibrationMails) {
		calibrateWords(pathToSpamCalibrationMails);
		calibrateWords(pathToHamCalibrationMails);
	}

	private void calibrateWords(String pathToCalibrationMails) {
		File[] filesInCalibration = new File(pathToCalibrationMails).listFiles();

		for (int j = 0; j < 5; j++) {
			System.out.println("alpha set to " + data.getAlpha());
			List<BigDecimal> pOfEmails = new ArrayList<>();
			for (int i = 0; i < filesInCalibration.length; i++) {
				BigDecimal pOfEmail = checkEmail(filesInCalibration[i]);
				pOfEmails.add(pOfEmail);
			}
			BigDecimal maxP = pOfEmails.stream().max((b1, b2) -> b1.compareTo(b2)).get();
			BigDecimal minP = pOfEmails.stream().min((b1, b2) -> b1.compareTo(b2)).get();
			System.out.println("max p: " + maxP + " min p: " + minP);
			data.setAlpha(data.getAlpha().divide(BigDecimal.TEN));
		}
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

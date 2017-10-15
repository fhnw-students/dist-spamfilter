package dist_spamfilter_brunner_heimgartner_iseli;


import org.junit.Assert;
import org.junit.Test;


public class CalculatorTest {

	@Test
	public void testSimpleCalculation() {
//		SpamFilterData data = new SpamFilterData();
//		data.setHamWords(hamWords);
//		data.setSpamWords(spamWords);
//		data.setNumberOfHamMails(numberOfHamsMails);
//		data.setNumberOfSpamMails();
//		Set<String> content = new HashSet<>();
//		Calculator.pOfEmail(data, content)
		double[] pWortSpam = {7, 8};
		double[] pWortHam = {30, 3};
		double gesamtWkeit = Calculator.gesamtWkeit(0.9, 0.1, pWortSpam, pWortHam);
		System.out.println(gesamtWkeit);
		Assert.assertEquals(0.85, gesamtWkeit, 0.01);
	}
}

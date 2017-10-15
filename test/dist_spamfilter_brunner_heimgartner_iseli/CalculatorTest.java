package dist_spamfilter_brunner_heimgartner_iseli;


import java.util.ArrayList;
import java.util.List;

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
		List<BigFraction> pWortSpam = new ArrayList<>();
		pWortSpam.add(new BigFraction(7));
		pWortSpam.add(new BigFraction(8));
		
		List<BigFraction> pWortHam = new ArrayList<>();
		pWortHam.add(new BigFraction(30));
		pWortHam.add(new BigFraction(3));
		BigFraction totalProbability = Calculator.totalProbability(0.9, 0.1, pWortSpam, pWortHam);
		Assert.assertEquals(0.85, totalProbability.doubleValue(), 0.01);
	}
}

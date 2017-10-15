package dist_spamfilter_brunner_heimgartner_iseli;

import org.junit.Assert;
import org.junit.Test;


public class SpamFilterTest {

	@Test
	public void testSpamFilter() {
		SpamFilter spamFilter = new SpamFilter(
				"C:\\Users\\Ken\\OneDrive\\7\\dist\\workspace\\dist_spamfilter_brunner_heimgartner_iseli\\resources\\learn\\ham",
				"C:\\Users\\Ken\\OneDrive\\7\\dist\\workspace\\dist_spamfilter_brunner_heimgartner_iseli\\resources\\learn\\spam",
				"C:\\Users\\Ken\\OneDrive\\7\\dist\\workspace\\dist_spamfilter_brunner_heimgartner_iseli\\resources\\calibration\\spam",
				"C:\\Users\\Ken\\OneDrive\\7\\dist\\workspace\\dist_spamfilter_brunner_heimgartner_iseli\\resources\\calibration\\ham");

		SpamFilterData data = spamFilter.getData();
		Assert.assertEquals(data.getHamWords().size(), data.getSpamWords().size());
	}
}

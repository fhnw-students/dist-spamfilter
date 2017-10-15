package dist_spamfilter_brunner_heimgartner_iseli;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class FileHandlerTest {

	@Test
	public void testReadWords() throws IOException {
		Map<String, BigDecimal> words = FileHandler.readWordsFromDirectory(new File(
				"C:\\Users\\Ken\\OneDrive\\7\\dist\\workspace\\dist_spamfilter_brunner_heimgartner_iseli\\resources\\learn\\spam"));

		Assert.assertFalse(words.isEmpty());
		
	}
}

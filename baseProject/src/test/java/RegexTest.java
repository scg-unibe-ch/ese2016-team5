import static org.junit.Assert.*;
import org.junit.Test;

public class RegexTest {
	
	@Test
	public void testEffinBiel() {
		/**
		 * Should return true.
		 */
		assertFalse("2500 - Biel;Bienne".matches("^[0-9]{4} - [-\\w\\s\\u00C0-\\u00FF]*"));
	}
}

package biz.noorlander.batclient.plugins;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mythicscape.batclient.interfaces.ParsedResult;

class MonkSpecialSkillPluginTest {

	private static MonkSpecialSkillPlugin underTest;
	
	@BeforeAll
	static void setupClass() {
		underTest = new MonkSpecialSkillPlugin();
		underTest.loadPlugin();
	}
	@Test
	void testTriggerParsedResult() {
		assertEquals("MonkSpecialSkillPlugin", underTest.getName());
		ParsedResult testCase = new ParsedResult("| avalanche slam                 |  77 | Baptize                        |  90 |");
		ParsedResult result = underTest.trigger(testCase);
		assertNotNull(result);
	}

}

package biz.noorlander.batclient.utils;

import com.mythicscape.batclient.interfaces.ParsedResult;

public class ParsedResultUtil {

	public static ParsedResult gag(ParsedResult input) {
		input.setStrippedText("");
		return input;
	}
}

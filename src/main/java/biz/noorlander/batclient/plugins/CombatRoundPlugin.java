package biz.noorlander.batclient.plugins;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginCommandTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginUtil;
import com.mythicscape.batclient.interfaces.ParsedResult;

import biz.noorlander.batclient.handlers.CombatRoundHandler;

public class CombatRoundPlugin extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginCommandTrigger, BatClientPluginUtil {

	private Pattern combatRoundPattern;
	private CombatRoundHandler combatRoundHandler;
	
	@Override
	public void loadPlugin() {
		// ********************* Round 13 **********************
		// ********************** Round 4 **********************
		// ******************** Round 3 (2) ********************

		this.combatRoundPattern = Pattern.compile("^[*]+ Round ([0-9]+) ([(][0-9]+[)] )?[*]+$");
		this.combatRoundHandler = new CombatRoundHandler(getClientGUI());
	}

	@Override
	public String trigger(String command) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult trigger(ParsedResult output) {
        String text = output.getStrippedText().trim();
        if (text.startsWith("*")) {
        	Matcher roundMatcher = combatRoundPattern.matcher(text);
        	if (roundMatcher.find()) {
        		combatRoundHandler.combatRound(Integer.parseInt(roundMatcher.group(1)));
        		return output;
        	}
        }
		return null;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void clientExit() {
		this.combatRoundHandler.destroyHandler();
	}


}

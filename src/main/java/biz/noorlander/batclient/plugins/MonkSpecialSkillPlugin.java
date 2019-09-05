package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.handlers.MonkSpecialSkillHandler;
import com.mythicscape.batclient.interfaces.*;

public class MonkSpecialSkillPlugin extends BatClientPlugin
		implements BatClientPluginCommandTrigger, BatClientPluginTrigger, BatClientPluginUtil {

	private MonkSpecialSkillHandler monkSpecialSkillHandler;

	@Override
	public void clientExit() {
        System.out.println("--- Unloading MonkSpecialSkillPlugin ---");

	}

	@Override
	public ParsedResult trigger(ParsedResult incoming) {
		return monkSpecialSkillHandler.handleOutputTriggers(incoming);
	}

	@Override
	public String trigger(String command) {
		return monkSpecialSkillHandler.handleCommandTriggers(command);
	}


	@Override
	public String getName() {
		return "MonkSpecialSkillPlugin";
	}

	@Override
	public void loadPlugin() {
		this.monkSpecialSkillHandler = new MonkSpecialSkillHandler(this.getClientGUI());
	}
}

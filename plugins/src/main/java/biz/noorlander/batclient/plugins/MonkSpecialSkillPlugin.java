package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.BatmudPlugin;
import biz.noorlander.batclient.handlers.MonkSpecialSkillHandler;

public class MonkSpecialSkillPlugin extends BatmudPlugin<MonkSpecialSkillHandler> {

	@Override
	public MonkSpecialSkillHandler createHandler() {
		return new MonkSpecialSkillHandler(this.getClientGUI());
	}

}

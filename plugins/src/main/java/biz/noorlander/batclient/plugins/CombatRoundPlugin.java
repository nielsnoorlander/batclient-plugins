package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.BatmudPlugin;

import biz.noorlander.batclient.handlers.CombatRoundHandler;

public class CombatRoundPlugin extends BatmudPlugin<CombatRoundHandler> {

	@Override
	public CombatRoundHandler createHandler() {
		return new CombatRoundHandler(this.getClientGUI());
	}
}

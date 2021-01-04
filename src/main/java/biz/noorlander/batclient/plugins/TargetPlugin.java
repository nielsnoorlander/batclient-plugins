package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.BatmudPlugin;
import biz.noorlander.batclient.handlers.TargetHandler;

public class TargetPlugin extends BatmudPlugin<TargetHandler> {

	@Override
	public TargetHandler createHandler() {
		return new TargetHandler(this.getClientGUI());
	}
}

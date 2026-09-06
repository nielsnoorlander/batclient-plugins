package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.BatmudPlugin;
import biz.noorlander.batclient.handlers.AnimistHandler;

public class AnimistPlugin extends BatmudPlugin<AnimistHandler> {

	@Override
	public AnimistHandler createHandler() {
		return new AnimistHandler(this.getClientGUI());
	}

}

package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.model.PlayerScore;
import com.mythicscape.batclient.interfaces.ClientGUI;

public class PlayerScoreHandler extends AbstractHandler {

	PlayerScore currentScore;

	public PlayerScoreHandler(ClientGUI gui) {
		super(gui, "SCORE");
	}

	@Override
	public void initHandler() {
	}

	@Override
	public void destroyHandler() {
	}
}

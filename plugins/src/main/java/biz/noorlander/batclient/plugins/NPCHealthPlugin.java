package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.BatmudPlugin;
import biz.noorlander.batclient.handlers.NPCHealthHandler;
import biz.noorlander.batclient.utils.CommonPatterns;
import biz.noorlander.batclient.utils.ParsedResultUtil;
import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.ParsedResult;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NPCHealthPlugin extends BatmudPlugin<NPCHealthHandler> {

	@Override
	public NPCHealthHandler createHandler() {
		return new NPCHealthHandler(this.getClientGUI());
	}
}

package biz.noorlander.batclient.plugins;

import biz.noorlander.batclient.handlers.AnimistHandler;
import biz.noorlander.batclient.utils.AttributedMessageBuilder;
import biz.noorlander.batclient.utils.ParsedResultUtil;
import com.mythicscape.batclient.interfaces.*;

import java.awt.*;
import java.util.Optional;
import java.util.regex.Matcher;

public class AnimistPlugin extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginCommandTrigger, BatClientPluginUtil {

	private AnimistHandler animistHandler;
	private boolean souListSparse = true;
	
	@Override
	public void loadPlugin() {
		animistHandler = new AnimistHandler(this.getClientGUI());
		animistHandler.initSoulPanel();
	}

	@Override
	public ParsedResult trigger(ParsedResult output) {
        String text = output.getStrippedText().trim();
        if (text.startsWith("Your soul starts to follow you, as you ordered.")
			|| text.equals("You feel slightly better at fighting with your soul companion.")
			|| text.equals("You feel slightly better at fighting with a soul mount.")
			|| text.equals("The radiance bolts back, striking you. You smile proudly - you and your soul are one again.")
        	|| text.startsWith("You chant, sing and dance, slapping yourself at the important parts.")
			)
        {
        	animistHandler.updateReputation();
			return ParsedResultUtil.gag(output);
		}
        Matcher soulScore = animistHandler.getSoulScoreMatcher(text);
        if (soulScore.find()) {
        	animistHandler.setSoulHealthPercent(Integer.parseInt(soulScore.group(1)));
        	return ParsedResultUtil.gag(output);
		} else {
			Matcher soulReputation = animistHandler.getSoulReputationMatcher(text);
			if (soulReputation.find()) {
				animistHandler.setSoulReputation(Integer.parseInt(soulReputation.group(1)));
				return ParsedResultUtil.gag(output);
			} else {
				Matcher mountReputation = animistHandler.getMountReputationMatcher(text);
				if (mountReputation.find()) {
					animistHandler.setMountReputation(mountReputation.group(1), mountReputation.group(2));
					return ParsedResultUtil.gag(output);
				}
			}
		}
        Matcher soul = animistHandler.getSoulListMatcher(text);
        if (soul.find()) {
			String race = soul.group(4);
			animistHandler.registerSoulStatistic(race, soul.group(3), Integer.parseInt(soul.group(1)));
			if (souListSparse) {
				return ParsedResultUtil.gag(output);
			} else {
				return AttributedMessageBuilder.create().append(String.format("%2s   ", soul.group(1)), Optional.of(Color.CYAN), Optional.empty())
						.append(String.format("%-10s   ", soul.group(3)))
						.append(String.format("%-10s   ", race), Optional.of(Color.WHITE), Optional.empty())
						.append(animistHandler.getAnimistSoul(race).toString(), Optional.of(Color.orange), Optional.empty())
						.build();
			}
		} else {
			Matcher header = animistHandler.getSoulListHeaderMatcher(text);
			if (header.find()) {
				animistHandler.resetSoulsAndStatistics();
				if (souListSparse) {
					return ParsedResultUtil.gag(output);
				} else {
					return AttributedMessageBuilder.create().append(String.format("%2s   ", "ID"), Optional.of(Color.CYAN), Optional.empty())
							.append(String.format("%-10s   ", "Quality"))
							.append(String.format("%-10s   ", "Race"), Optional.of(Color.WHITE), Optional.empty())
							.append("Stats", Optional.of(Color.orange), Optional.empty())
							.build();
				}
			} else {
				Matcher report = animistHandler.getSoulReportMatcher(text);
				if (report.find()) {
					if (souListSparse) {
						animistHandler.reportSoulStatistics(Integer.parseInt(report.group(1)), Integer.parseInt(report.group(2)));
						return ParsedResultUtil.gag(output);
					} else {
						return output;
					}
				} else {
					Matcher select = animistHandler.getSelectSoulMatcher(text);
					if (select.find()) {
						return AttributedMessageBuilder.create().append("Selected: ")
								.append(select.group(1))
								.append(" " + select.group(2) + " ")
								.append(animistHandler.getAnimistSoul(select.group(2)).toString(), Optional.of(Color.orange), Optional.empty())
								.build();
					} else {
						return null;
					}
				}
			}
		}
	}

	@Override
	public String trigger(String command) {
		if ("animist souls long".equalsIgnoreCase(command)) {
			this.souListSparse = false;
			return "animist souls";
		} else if ("animist souls".equalsIgnoreCase(command)) {
			this.souListSparse = true;
			return command;
		} else if (command.startsWith("as ")) {
			return animistHandler.selectSoul(command);
		}
		return null;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void clientExit() {
		this.animistHandler.destroyHandler();
	}
}

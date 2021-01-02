package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.model.CustomConfig;
import biz.noorlander.batclient.model.PlayerStats;
import biz.noorlander.batclient.ui.PlayerStatsFrame;
import biz.noorlander.batclient.utils.ConfigService;
import biz.noorlander.batclient.utils.ParsedResultUtil;
import com.mythicscape.batclient.interfaces.ClientGUI;
import com.mythicscape.batclient.interfaces.ParsedResult;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerStatsHandler extends AbstractWindowedHandler implements ActionListener {
	private final Pattern shortScorePattern;
	private final PlayerStats currentScore;
	private final PlayerStatsFrame playerStatsFrame;
	private final Pattern shortScoreStats;

	public PlayerStatsHandler(ClientGUI gui) {
		super(gui, "PLAYER_STATUS");
        playerStatsFrame = new PlayerStatsFrame(this);
        createWindow("playerStats", "Stats", playerStatsFrame.getPanel(), "PlayerStatsPlugin");
        shortScoreStats = Pattern.compile("^EQ:[a-z0-9]+ STATS: ([A-Z][a-z]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([a-z ]+)/([0-9]+)/([a-z ]+)/([0-9]+)/([ A-z',]*)/([_a-z]*)/");
        currentScore = loadBasePlayerStats();
        this.playerStatsFrame.setBaseStats(loadBasePlayerStats());
		shortScorePattern = Pattern.compile("^EQ:([a-z0-9]+) STATS: ([A-Z][a-z]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([a-z ]+)/([0-9]+)/([a-z ]+)/([0-9]+)/([ A-z',]*)/([_a-z]*)/");
	}

    private PlayerStats loadBasePlayerStats() {
        PlayerStats result = new PlayerStats(CustomConfig.Name.PLAYER_STATS, this.getBaseDir());
        result.setCharisma(100);
        result.setConstitution(100);
        result.setDexterity(100);
        result.setIntelligence(100);
        result.setName("Niliz");
        result.setSize(100);
        result.setStrength(100);
        result.setWisdom(100);

        return ConfigService.getInstance().loadCustomConfig(result);
    }

    @Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("ActionEvent: " + e.getActionCommand());
		if ("saveDefaults".equals(e.getActionCommand())) {
			saveStatsAsDefaults();
			this.playerStatsFrame.setBaseStats(this.currentScore);
		} else {
			PlayerStatsFrame.EqSet eqSet = playerStatsFrame.convertToEqSet(e.getActionCommand());
			switch (eqSet) {
				case COMBAT:
					this.command("dam");
					break;
				case REGEN:
					this.command("re");
					break;
				case BUFF:
					this.command("wis");
					break;
				case NONE:
				default:
					this.command("remove all");
			}
		}
	}

	private void saveStatsAsDefaults() {
		ConfigService.getInstance().saveConfig(this.currentScore);
	}

	public ParsedResult updatePlayerStats(ParsedResult output) {
        String text = output.getStrippedText().trim();
        Matcher matcher = shortScoreStats.matcher(text);
        if (matcher.find()) {
            currentScore.setStrength(Integer.parseInt(matcher.group(2)));
            currentScore.setDexterity(Integer.parseInt(matcher.group(3)));
            currentScore.setConstitution(Integer.parseInt(matcher.group(4)));
            currentScore.setIntelligence(Integer.parseInt(matcher.group(5)));
            currentScore.setWisdom(Integer.parseInt(matcher.group(6)));
            currentScore.setCharisma(Integer.parseInt(matcher.group(7)));
            this.playerStatsFrame.updateStats(currentScore);
            return ParsedResultUtil.gag(output);
        } else {
            System.err.println("Unable to match stats, while eqset did match!");
        }
        return output;
    }
	public void updateEqSet(String eqSet) {
		playerStatsFrame.setEqSet(playerStatsFrame.convertToEqSet(eqSet));
	}

	@Override
	public void destroyHandler() {
		saveWindowsConfig();
	}

	@Override
	public ParsedResult handleOutputTriggers(ParsedResult parsedResult) {
		String text = parsedResult.getStrippedText().trim();
		Matcher matcher = shortScorePattern.matcher(text);
		if (matcher.find()) {
			if ("dam".equals(matcher.group(1))) {
				updateEqSet("combat");
			} else if ("spr".equals(matcher.group(1)) || "hpr".equals(matcher.group(1))) {
				updateEqSet("regen");
			} else if ("wis".equals(matcher.group(1)) || "dex".equals(matcher.group(1))) {
				updateEqSet("buff");
			} else if ("0".equals(matcher.group(1))) {
				updateEqSet("none");
			}
			return updatePlayerStats(parsedResult);
		}
		return null;
	}

	@Override
	public String handleCommandTriggers(String command) {
		return null;
	}
}

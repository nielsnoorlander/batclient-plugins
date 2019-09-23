package biz.noorlander.batclient.handlers;

import biz.noorlander.batclient.model.PlayerStats;
import biz.noorlander.batclient.model.WindowsConfig;
import biz.noorlander.batclient.ui.PlayerStatsFrame;
import biz.noorlander.batclient.utils.ParsedResultUtil;
import com.mythicscape.batclient.interfaces.BatWindow;
import com.mythicscape.batclient.interfaces.ClientGUI;
import com.mythicscape.batclient.interfaces.ParsedResult;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerStatsHandler extends AbstractHandler implements ActionListener {

	private PlayerStats currentScore;
    private PlayerStats baseScore;
	private PlayerStatsFrame playerStatsFrame;
	private BatWindow clientWin;
	private WindowsConfig windowsConfig;
	private Pattern shortScoreStats;

	public PlayerStatsHandler(ClientGUI gui, WindowsConfig windowsConfig) {
		super(gui, "PLAYER_STATUS");
		this.windowsConfig = windowsConfig;
        playerStatsFrame = new PlayerStatsFrame(this);
        clientWin = this.createWindow( "PlayerStatus", windowsConfig);
        clientWin.removeTabAt( 0 );
        clientWin.newTab( "Status", playerStatsFrame.getPanel() );
        clientWin.setVisible( true );
        shortScoreStats = Pattern.compile("^EQ:[a-z0-9]+ STATS: ([A-Z][a-z]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([0-9]+)/([a-z ]+)/([0-9]+)/([a-z ]+)/([0-9]+)/([ A-z',]*)/([_a-z]*)/");
        baseScore = loadBasePlayerStats();
        currentScore = loadBasePlayerStats();
        this.playerStatsFrame.setBaseStats(this.baseScore);
	}

    private PlayerStats loadBasePlayerStats() {
        PlayerStats result = new PlayerStats();
        result.setCharisma(100);
        result.setConstitution(206);
        result.setDexterity(106);
        result.setIntelligence(83);
        result.setName("Niliz");
        result.setSize(65);
        result.setStrength(114);
        result.setWisdom(211);
        return result;
    }

    @Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("ActionEvent: " + e.getActionCommand());
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

	public WindowsConfig getUpdatedWindowsConfig() {
		windowsConfig.setLeft(clientWin.getLocation().x);
		windowsConfig.setTop(clientWin.getLocation().y);
		windowsConfig.setWidth(clientWin.getSize().width);
		windowsConfig.setHeight(clientWin.getSize().height);
		windowsConfig.setVisible(clientWin.isVisible());
		return windowsConfig;
	}

	@Override
	public void initHandler() {
	}

	@Override
	public void destroyHandler() {
	}
}
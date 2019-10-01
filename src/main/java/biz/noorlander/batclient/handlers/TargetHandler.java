package biz.noorlander.batclient.handlers;

import com.mythicscape.batclient.interfaces.ClientGUI;

public class TargetHandler extends AbstractHandler {

	private String targetCommand = "none";
	private String selectedTarget = "none";
	private String considerResult = "unknown";
	private boolean inferior = false;
	
	public TargetHandler(ClientGUI gui) {
		super(gui, "TARGET");
	}

	public void handleTarget(String selectedTarget) {
		setSelectedTarget(selectedTarget);
		command("@party report Target -> " + this.selectedTarget + " (" + this.targetCommand + ") = " 
				+ this.considerResult + (this.inferior ? " #inferior" : ""));
	}
	
	public String getTargetCommand() {
		return targetCommand;
	}

	public void setTargetCommand(String targetCommand) {
		this.inferior = false;
		this.targetCommand = targetCommand;
	}

	public String getSelectedTarget() {
		return selectedTarget;
	}

	public void setSelectedTarget(String selectedTarget) {
		this.selectedTarget = selectedTarget;
	}

	public void isInferion(boolean inferior) {
		this.inferior = inferior;
	}
	
	public String getConsiderResult() {
		return considerResult;
	}

	public void setConsiderResult(String considerResult) {
		this.considerResult = considerResult;
	}

	@Override
	public void destroyHandler() {
	}
}

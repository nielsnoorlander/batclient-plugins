package biz.noorlander.batclient.model;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatWindow;

public class WindowsConfig extends AbstractConfig {
	private static final String PREFIX_WINDOWS = "window-";

	public <T extends BatClientPlugin> WindowsConfig(T plugin) {
		super(plugin);
	}

	public <T extends BatClientPlugin> WindowsConfig(T plugin, BatWindow clientWin) {
		super(plugin);
		this.left = clientWin.getLocation().x;
		this.top = clientWin.getLocation().y;
		this.width = clientWin.getSize().width;
		this.height = clientWin.getSize().height;
		this.visible = clientWin.isVisible();
	}

	int top = 0;
	int left = 0;
	int width = 0;
	int height = 0;
	boolean visible = true;
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public String getPrefix() {
		return PREFIX_WINDOWS;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("\"windowsconfig\" { \n");
		builder.append("    \"left:\", ").append(left).append(",\n");
		builder.append("    \"top:\", ").append(top).append(",\n");
		builder.append("    \"width:\", ").append(width).append(",\n");
		builder.append("    \"height:\", ").append(height).append(",\n");
		builder.append("    \"visible:\", \"").append(visible).append("\"\n}");
		return builder.toString();
	}
}

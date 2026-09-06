package biz.noorlander.batclient.model;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatWindow;

public class WindowsConfig extends AbstractConfig {
	private static final String PREFIX_WINDOWS = "window-";

	public WindowsConfig(String name, String baseDirectory) {
		super(name, baseDirectory);
	}

	private int top = 0;
	private int left = 0;
	private int width = 200;
	private int height = 150;
	private boolean visible = true;
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
		return "\"windowsconfig\" { \n" + "    \"left:\", " + left + ",\n" +
				"    \"top:\", " + top + ",\n" +
				"    \"width:\", " + width + ",\n" +
				"    \"height:\", " + height + ",\n" +
				"    \"visible:\", \"" + visible + "\"\n}";
	}
}

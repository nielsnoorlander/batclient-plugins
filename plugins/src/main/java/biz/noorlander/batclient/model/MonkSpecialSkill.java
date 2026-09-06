package biz.noorlander.batclient.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MonkSpecialSkill {
	
	public enum SkillBranch { ARMOUR, DEFENSE, CONFUSE, MULTITARGET }
	
	private int tier;
	private SkillBranch branch;
	String name;
	List<Pattern> hitMessages;
	List<Pattern> missMessages;
	
	
	public MonkSpecialSkill(int tier, SkillBranch branch, String name) {
		super();
		this.tier = tier;
		this.branch = branch;
		this.name = name;
		this.hitMessages = new ArrayList<>();
		this.missMessages = new ArrayList<>();
	}
	public int getTier() {
		return tier;
	}
	public void setTier(int tier) {
		this.tier = tier;
	}
	public SkillBranch getBranch() {
		return branch;
	}
	public void setBranch(SkillBranch branch) {
		this.branch = branch;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Pattern> getHitMessages() {
		return hitMessages;
	}
	public List<Pattern> getMissMessages() {
		return missMessages;
	}
}

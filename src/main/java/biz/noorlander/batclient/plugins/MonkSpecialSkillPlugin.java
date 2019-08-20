package biz.noorlander.batclient.plugins;

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginCommandTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginUtil;
import com.mythicscape.batclient.interfaces.ParsedResult;

import biz.noorlander.batclient.model.MonkSpecialSkill;
import biz.noorlander.batclient.model.MonkSpecialSkill.SkillBranch;

public class MonkSpecialSkillPlugin extends BatClientPlugin
		implements BatClientPluginCommandTrigger, BatClientPluginTrigger, BatClientPluginUtil {

    private Pattern showSkillsPattern;
    private Pattern monkSpecialSkillPattern;
    private List<MonkSpecialSkill> skills;

	@Override
	public void clientExit() {
        System.out.println("--- Loading MonkSpecialSkillPlugin ---");

	}

	@Override
	public ParsedResult trigger(ParsedResult parsedResult) {        
        for (MonkSpecialSkill monkSkill : this.skills) {
        	ParsedResult skillResult = parseMonkSkill(monkSkill, parsedResult);
        	if (skillResult != null) {
        		return skillResult;
        	}
        }
		return parsedResult;
	}

	private ParsedResult parseMonkSkill(MonkSpecialSkill monkSkill, ParsedResult parsedResult) {
        String text = parsedResult.getStrippedText().trim();
		for (Pattern hitPattern : monkSkill.getHitMessages()) {
			Matcher matcher = hitPattern.matcher(text);
			if (matcher.matches()) {
				parsedResult.addHiliteAttribute(TextAttribute.FOREGROUND, Color.GREEN, 0, text.length());
				return parsedResult;
			}
		}
		for (Pattern missPattern : monkSkill.getMissMessages()) {
			Matcher matcher = missPattern.matcher(text);
			if (matcher.matches()) {
				parsedResult.addHiliteAttribute(TextAttribute.FOREGROUND, Color.RED, 0, text.length());
				return parsedResult;
			}
		}
		return null;
	}

	@Override
	public String trigger(String command) {
		if ("monkskills".equalsIgnoreCase(command)) {
			return "grep -i \"(hydra|lion|dragon|wave|falcon|falling|tsunami|avalanche|elder|earthquake|geyser)\" show skills";
		}
		return command;
	}

	@Override
	public String getName() {
		return "MonkSpecialSkillPlugin";
	}

	@Override
	public void loadPlugin() {
		loadMonkSpecialSkills();
		// Pattern to match: | Avalanche slam                 |  76 | Baptize                        |  90 |
		this.showSkillsPattern = Pattern.compile("^|\\s+([-A-z ]+)\\s+|\\s+([0-9]+)\\s+|\\s+([-A-z ]+)?\\s+|\\s+([0-9]+)?\\s+|$");
	}

	private void loadMonkSpecialSkills() {
		this.skills = new ArrayList<>();

		// Start with the ARMOUR branch skills
		// Tier 1: Falling boulder strike
		MonkSpecialSkill fbs = new MonkSpecialSkill(1, MonkSpecialSkill.SkillBranch.ARMOUR, "falling boulder strike");
		fbs.getHitMessages().add(Pattern.compile("^You ball up a fist and drive your elbow down at [-A-z ']+s shoulder, but only bruise the muscle\\.$"));
		fbs.getHitMessages().add(Pattern.compile("^You ball up a fist and drive your elbow down on [-A-z ']+s shoulder, scoring a solid hit!$"));
		fbs.getHitMessages().add(Pattern.compile("^You ball up a fist and _drive_ your elbow down onto [-A-z ']+s shoulder, and you feel something pop!$"));
		fbs.getHitMessages().add(Pattern.compile("^You ball up a fist and [*]drive[*] your elbow down onto [-A-z ']+s shoulder, and you feel something snap!$"));
		fbs.getHitMessages().add(Pattern.compile("^You ball up a fist and [*]DRIVE[*] it down onto [-A-z ']+s shoulder, and you feel something shatter!$"));
		fbs.getMissMessages().add(Pattern.compile("^You ball up a fist and attempt to drive your elbow down hard onto [-A-z ']+, but only score a glancing blow\\.$"));
		this.skills.add(fbs);

		// Tier 2: Earthquake kick
		MonkSpecialSkill ek = new MonkSpecialSkill(2, MonkSpecialSkill.SkillBranch.ARMOUR, "earthquake kick");
		ek.getHitMessages().add(Pattern.compile("^Your first kick knocks [-A-z ']+ backwards, preventing you from hitting with the others\\.$"));
		ek.getHitMessages().add(Pattern.compile("^You thrust your foot against [-A-z ']+s torso, getting two hits in!$"));
		ek.getHitMessages().add(Pattern.compile("^You thump your foot against [-A-z ']+s chest three times, shaking (its|her|his) whole body!$"));
		ek.getHitMessages().add(Pattern.compile("^You thrash your foot against [-A-z ']+s chest four times, causing (it|her|him) to convulse!$"));
		ek.getHitMessages().add(Pattern.compile("^You manage to score five consecutive kicks against [-A-z ']+s body, driving waves of force all through (it|her|him)!$"));
		ek.getMissMessages().add(Pattern.compile("^You make several quick kicks at Old ogre in rapid succession, but don't get any solid hits\\."));
		this.skills.add(ek);

		// Tier 3:
		MonkSpecialSkill as = new MonkSpecialSkill(3, MonkSpecialSkill.SkillBranch.ARMOUR, "avalanche slam");
		as.getHitMessages().add(Pattern.compile("^You grab and hurl [-A-z ']+ onto the ground, but (it|she|he) manages to land on (its|her|his) butt\\.$"));
		as.getHitMessages().add(Pattern.compile("^You grab and hurl [-A-z ']+ to the ground, but (it|she|he) twists to land on (its|her|his) side\\.$"));
		as.getHitMessages().add(Pattern.compile("^You grab [-A-z ']+ by the right[a-z_]+ and throw (it|she|he) down onto (its|her|his) back!$"));
		as.getHitMessages().add(Pattern.compile("^You grab [-A-z ']+s right[a-z_]+,  pull it over your shoulder and slam (it|her|him) down, landing on (its|her|his) spine!$"));
		as.getMissMessages().add(Pattern.compile("^You grab at [-A-z ']+s outstretched limbs, but miss\\.$"));
		this.skills.add(as);

		// MULTITARGET Branch
		// Tier 1:
		MonkSpecialSkill hfs = new MonkSpecialSkill(1, MonkSpecialSkill.SkillBranch.MULTITARGET, "hydra fang strike");
		hfs.getHitMessages().add(Pattern.compile("^With each [*]JAB[*] you feel your hand dig deeply between the ribs, halfway down to the wrist!$"));
		hfs.getHitMessages().add(Pattern.compile("^As you hit each torso, you manage to [*]jab[*] your knuckles between the ribs!$"));
		hfs.getHitMessages().add(Pattern.compile("^You get a few hits to muscle tissue, shoulders and sides, but nothing deadly\\.$"));
		hfs.getHitMessages().add(Pattern.compile("^You get some hits to the belly, getting some penetration\\.$"));
		hfs.getHitMessages().add(Pattern.compile("^You jab a few ribcages, getting solid impact but not getting your fingers between the ribs like you'd hoped\\.$"));
		hfs.getHitMessages().add(Pattern.compile("^Most of your attacks are partially deflected or blocked\\.$"));
		hfs.getMissMessages().add(Pattern.compile("^You start jabbing your knife-hands rapidly, but [-A-z ']+ backs off and you can't even get started\\.$"));
		this.skills.add(hfs);

		// Tier 2:
		MonkSpecialSkill whk = new MonkSpecialSkill(2, MonkSpecialSkill.SkillBranch.MULTITARGET, "winged horse kick");
		whk.getHitMessages().add(Pattern.compile("^You kick (it|her|him) hard enough to knock (it|her|him) down!$"));
		whk.getHitMessages().add(Pattern.compile("^You kick (it|her|him) hard enough to send (it|her|him) flying!$"));
		whk.getHitMessages().add(Pattern.compile("^You send [-A-z ']+ flying into [-A-z ']+, knocking them both down from the impact!$"));
		whk.getHitMessages().add(Pattern.compile("^Your kick is hard enough to send them all crashing into each oother!"));
		whk.getHitMessages().add(Pattern.compile("^Your kick is hard enough to send them stumbling into each other!$"));
		whk.getHitMessages().add(Pattern.compile("^You knock [-A-z ']+ into [-A-z ']+!$"));
		whk.getHitMessages().add(Pattern.compile("^You kick (it|her|him) stumbling backwards!$"));
		whk.getHitMessages().add(Pattern.compile("^Your kick is true, but not forceful enough to knock anyone around.$"));
		whk.getMissMessages().add(Pattern.compile("^You leap up and make a swinging kick, but [-A-z ']+ blocks it and knocks you to the ground\\.$"));
		this.skills.add(whk);
		
		// Tier 3:
		MonkSpecialSkill dts = new MonkSpecialSkill(3, SkillBranch.MULTITARGET, "dragon tail sweep");
		dts.getHitMessages().add(Pattern.compile("^[A-Z][-A-z ']+ takes your weak sweep on the ankle and barely flinches\\.$"));
		dts.getHitMessages().add(Pattern.compile("^[A-Z][-A-z ']+ is hit in the shin and forced to stagger, but remains standing\\.$"));
		dts.getHitMessages().add(Pattern.compile("^[A-Z][-A-z ']+ is kicked in the back of the leg and stumbles about, off-balance\\.$"));
		dts.getHitMessages().add(Pattern.compile("^You kick both legs out from under [-A-z ']+, who lands on (its|her|his) back with a heavy thud!$"));
		dts.getMissMessages().add(Pattern.compile("^You drop and sweep your leg low, but [-A-z ']+ braces and blocks it\\.$"));
		this.skills.add(dts);
		
		// DEFENSE Branch
		// Tier 1:
		MonkSpecialSkill fts = new MonkSpecialSkill(1, MonkSpecialSkill.SkillBranch.DEFENSE, "falcon talon strike");
		fts.getHitMessages().add(Pattern.compile("^You push off of [-A-z ']+s shoulders hard enough to flip over (its|her|his) head, but can't get a decent claw in\\.$"));
		fts.getHitMessages().add(Pattern.compile("^You push off of [-A-z ']+s shoulders and vault over (its|her|his) head, but can't push hard enough to get into a flip\\.$"));
		fts.getHitMessages().add(Pattern.compile("^You push off [-A-z ']+s shoulders and flip, clawing (it|her|him) in the back with curved fingers!$"));
		fts.getHitMessages().add(Pattern.compile("^You push off [-A-z ']+s shoulders and flip, _claw_ing (it|her|him) in the neck on the way over!$"));
		fts.getHitMessages().add(Pattern.compile("^You push off [-A-z ']+s shoulders and do a fancy flip, [*]claw[*]ing (it|her|him) across the face on the way over!$"));
		fts.getMissMessages().add(Pattern.compile("^You attempt to vault over [-A-z ']+, but are pushed back\\.$"));
		this.skills.add(fts);

		// Tier 2:
		MonkSpecialSkill eck = new MonkSpecialSkill(2, MonkSpecialSkill.SkillBranch.DEFENSE, "elder cobra kick");
		eck.getHitMessages().add(Pattern.compile("^Your forward flip comes too late, and you end up merely slamming your back against [-A-z ']+\\.$"));
		eck.getHitMessages().add(Pattern.compile("^Your forward flip is a little too late, but you manage to club [-A-z ']+ over the shoulder with the heel of your foot\\.$"));
		eck.getMissMessages().add(Pattern.compile("^Your forward flip was too early, leaving you flat on your back!$"));
		this.skills.add(eck);

		// Tier 3:
		MonkSpecialSkill ltt = new MonkSpecialSkill(3, MonkSpecialSkill.SkillBranch.DEFENSE, "lions teeths throw");
		ltt.getHitMessages().add(Pattern.compile("^You fall short, and end up merely kicking (it|her|him) in the face with one foot\\.$"));
		ltt.getMissMessages().add(Pattern.compile("^You hurl your twisting body feet-first through the air at [-A-z ']+, but fall short and land on your side\\.$"));
		this.skills.add(ltt);
		
		// CONFUSE Branch
		// Tier 1:
		MonkSpecialSkill wcs = new MonkSpecialSkill(1, SkillBranch.CONFUSE, "wave crest strike");
		wcs.getHitMessages().add(Pattern.compile("^You manage to swat [-A-z ']+ on the neck, but miss the veins you were aiming for\\.$"));
		wcs.getHitMessages().add(Pattern.compile("^You go for the neck, but end up giving [-A-z ']+ a harsh slap across the jaw\\.$"));
		wcs.getHitMessages().add(Pattern.compile("^You [*]swat[*] [-A-z ']+ on the neck, hitting both arteries and temporarily halting (its|her|his) blood to the brain!$"));
		wcs.getHitMessages().add(Pattern.compile("^You see an excellent opening and [*]SWAT[*] the flat of your hand against [-A-z ']+s neck, hitting both arteries!$"));
		wcs.getHitMessages().add(Pattern.compile("^You open your hand and _swat_ [-A-z ']+ in the neck, hitting one of the arteries and disrupting (its|her|his) blood flow!$"));
		wcs.getHitMessages().add(Pattern.compile("^You can't find a good opening, so you settle for smacking [-A-z ']+ on the side of the head\\.$"));
		wcs.getMissMessages().add(Pattern.compile("^You try to swat at [-A-z ']+, but can't make flesh contact\\.$"));
		this.skills.add(wcs);
		
		// Tier 2:
		MonkSpecialSkill gfk = new MonkSpecialSkill(2, SkillBranch.CONFUSE, "geyser force kick");
		gfk.getHitMessages().add(Pattern.compile("^You run up [-A-z ']+s chest, stomping hard with both feet, and flip backwards onto the ground\\.$"));
		gfk.getHitMessages().add(Pattern.compile("^You land a single kick in the middle of [-A-z ']+s chest, backflip, and land on your feet\\.$"));
		gfk.getHitMessages().add(Pattern.compile("^You jump up and kick [-A-z ']+ in the ribcage, but don't get enough contact to backflip\\.$"));
		gfk.getHitMessages().add(Pattern.compile("^You run up [-A-z ']+s chest, kicking back hard enough to make (it|her|him) cough up blood!$"));
		gfk.getMissMessages().add(Pattern.compile("^You attempt to flip off of [-A-z ']+s chest, but slip and fall down\\.$"));
		this.skills.add(gfk);
		
		// Tier 3:
		MonkSpecialSkill tp = new MonkSpecialSkill(3, SkillBranch.CONFUSE, "tsunami push");
		tp.getHitMessages().add(Pattern.compile("^You shove [-A-z ']+ hard by both shoulders, forcing (it|her|him) to take a few steps back\\.$"));
		tp.getHitMessages().add(Pattern.compile("^You shove both palms against [-A-z ']+s shoulders, but barely manage to move (it|her|him) at all\\.$"));
		tp.getHitMessages().add(Pattern.compile("^You focus chi and shove [-A-z ']+s shoulders, knocking (it|her|him) backwards and off (its|her|his) feet!$"));
		tp.getMissMessages().add(Pattern.compile("^You reach for [-A-z ']+, but (it|she|he) deflects your hands\\.$"));
		this.skills.add(tp);
}

}

package biz.noorlander.batclient.model;

public enum Command {
    MSS_DO_SKILL("ms"),
    MSS_COMBO_SELECT("mc"),
    MSS_SHOW_SKILLS("monkskills");

    public final String mudcommand;
    Command(String mc) {
        this.mudcommand = mc;
    }
}

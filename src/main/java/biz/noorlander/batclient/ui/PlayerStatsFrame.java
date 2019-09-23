package biz.noorlander.batclient.ui;

import biz.noorlander.batclient.model.PlayerStats;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerStatsFrame {
    private static final Color EQSET_SELECTED_COLOR = new Color(240, 240, 240);
    private static final Color EQSET_DESELECTED_COLOR = new Color(180, 180, 180);
    private static final Color EQSET_DESLECTED_BG_COLOR = new Color(70, 70, 70);
    private static final Color EQSET_COMBAT_BG_COLOR = new Color(122, 15, 17);
    private static final Color EQSET_REGEN_BG_COLOR = new Color(22, 109, 188);
    private static final Color EQSET_BUFF_BG_COLOR = new Color(59, 154, 51);
    private static final Color COLOR_PLUS_STAT = new Color(40, 200, 60);
    private static final Color COLOR_MINUS_STAT = new Color(236, 43, 27);
    private static final Color COLOR_EQUAL_STAT = EQSET_DESELECTED_COLOR;

    public enum EqSet {NONE, COMBAT, REGEN, BUFF}

    private EqSet selectedEqSet = EqSet.NONE;
    private PlayerStats baseStats = null;
    public static final Color DEFAULT_STATUS_COLOR = new Color(200, 200, 200);
    private JPanel playerStatsPanel;
    private JButton regenButton;
    private JButton combatButton;
    private JButton buffButton;
    private JLabel strBase;
    private JLabel dexBase;
    private JLabel conBase;
    private JLabel intBase;
    private JLabel wisBase;
    private JLabel chaBase;
    private JLabel chaDiff;
    private JLabel wisDiff;
    private JLabel intDiff;
    private JLabel conDiff;
    private JLabel dexDiff;
    private JLabel strDiff;

    public PlayerStatsFrame(ActionListener listener) {
        super();
        combatButton.addActionListener(listener);
        regenButton.addActionListener(listener);
        buffButton.addActionListener(listener);
    }

    public EqSet convertToEqSet(String eqSetName) {
        return EqSet.valueOf(eqSetName.toUpperCase());
    }

    public void setBaseStats(PlayerStats baseStats) {
        PlayerStats oldBase = this.baseStats;
        this.baseStats = baseStats;
        if (oldBase != null) {
            updateStats(oldBase);
        } else {
            updateStats(baseStats);
        }
        this.strBase.setText(String.valueOf(this.baseStats.getStrength()));
        this.dexBase.setText(String.valueOf(this.baseStats.getDexterity()));
        this.conBase.setText(String.valueOf(this.baseStats.getConstitution()));
        this.intBase.setText(String.valueOf(this.baseStats.getIntelligence()));
        this.wisBase.setText(String.valueOf(this.baseStats.getWisdom()));
        this.chaBase.setText(String.valueOf(this.baseStats.getCharisma()));
    }

    public void updateStats(PlayerStats currentStats) {
        updateStat(this.strBase, this.strDiff, this.baseStats.getStrength(), currentStats.getStrength());
        updateStat(this.dexBase, this.dexDiff, this.baseStats.getDexterity(), currentStats.getDexterity());
        updateStat(this.conBase, this.conDiff, this.baseStats.getConstitution(), currentStats.getConstitution());
        updateStat(this.intBase, this.intDiff, this.baseStats.getIntelligence(), currentStats.getIntelligence());
        updateStat(this.wisBase, this.wisDiff, this.baseStats.getWisdom(), currentStats.getWisdom());
        updateStat(this.chaBase, this.chaDiff, this.baseStats.getCharisma(), currentStats.getCharisma());
    }

    private void updateStat(JLabel baseLabel, JLabel diffLabel, int baseStat, int currentStat) {
        int diff = currentStat - baseStat;
        baseLabel.setText(String.valueOf(currentStat));
        if (diff > 0) {
            diffLabel.setText("+" + diff);
            diffLabel.setForeground(COLOR_PLUS_STAT);
        } else if (diff < 0) {
            diffLabel.setText(String.valueOf(diff));
            diffLabel.setForeground(COLOR_MINUS_STAT);
        } else {
            diffLabel.setText("-");
            diffLabel.setForeground(COLOR_EQUAL_STAT);
        }
    }

    public boolean setEqSet(EqSet eqSet) {
        boolean changed = selectedEqSet != eqSet;
        selectedEqSet = eqSet;
        if (changed) {
            combatButton.setForeground(selectedEqSet == EqSet.COMBAT ? EQSET_SELECTED_COLOR : EQSET_DESELECTED_COLOR);
            combatButton.setBackground(selectedEqSet == EqSet.COMBAT ? EQSET_COMBAT_BG_COLOR : EQSET_DESLECTED_BG_COLOR);
            regenButton.setForeground(selectedEqSet == EqSet.REGEN ? EQSET_SELECTED_COLOR : EQSET_DESELECTED_COLOR);
            regenButton.setBackground(selectedEqSet == EqSet.REGEN ? EQSET_REGEN_BG_COLOR : EQSET_DESLECTED_BG_COLOR);
            buffButton.setForeground(selectedEqSet == EqSet.BUFF ? EQSET_SELECTED_COLOR : EQSET_DESELECTED_COLOR);
            buffButton.setBackground(selectedEqSet == EqSet.BUFF ? EQSET_BUFF_BG_COLOR : EQSET_DESLECTED_BG_COLOR);
        }
        return changed;
    }

    public JPanel getPanel() {
        return this.playerStatsPanel;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("RadioButtonDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new PlayerStatsFrame(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("EVENT: " + e.getActionCommand());
            }
        }).getPanel();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        playerStatsPanel = new JPanel();
        playerStatsPanel.setLayout(new GridBagLayout());
        playerStatsPanel.setBackground(new Color(-14540254));
        playerStatsPanel.setForeground(new Color(-12171706));
        regenButton = new JButton();
        regenButton.setBackground(new Color(-12171706));
        regenButton.setFocusPainted(false);
        regenButton.setFocusable(false);
        regenButton.setForeground(new Color(-4934476));
        regenButton.setText("Regen");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        playerStatsPanel.add(regenButton, gbc);
        buffButton = new JButton();
        buffButton.setBackground(new Color(-12171706));
        buffButton.setFocusPainted(false);
        buffButton.setFocusable(false);
        buffButton.setForeground(new Color(-4934476));
        buffButton.setText("Buff");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        playerStatsPanel.add(buffButton, gbc);
        combatButton = new JButton();
        combatButton.setBackground(new Color(-12171706));
        combatButton.setBorderPainted(true);
        combatButton.setContentAreaFilled(true);
        combatButton.setEnabled(true);
        combatButton.setFocusPainted(false);
        combatButton.setFocusable(false);
        combatButton.setForeground(new Color(-4934476));
        combatButton.setHideActionText(true);
        combatButton.setSelected(false);
        combatButton.setText("Combat");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        playerStatsPanel.add(combatButton, gbc);
        final JLabel label1 = new JLabel();
        label1.setForeground(new Color(-2302756));
        label1.setText("Strength");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 8, 4, 4);
        playerStatsPanel.add(label1, gbc);
        final JLabel label2 = new JLabel();
        label2.setForeground(new Color(-2302756));
        label2.setText("Dexterity");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 8, 4, 4);
        playerStatsPanel.add(label2, gbc);
        final JLabel label3 = new JLabel();
        label3.setForeground(new Color(-2302756));
        label3.setText("Constitution");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 8, 4, 4);
        playerStatsPanel.add(label3, gbc);
        final JLabel label4 = new JLabel();
        label4.setForeground(new Color(-2302756));
        label4.setText("Intelligence");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 8, 4, 4);
        playerStatsPanel.add(label4, gbc);
        final JLabel label5 = new JLabel();
        label5.setForeground(new Color(-2302756));
        label5.setText("Wisdom");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 8, 4, 4);
        playerStatsPanel.add(label5, gbc);
        final JLabel label6 = new JLabel();
        label6.setForeground(new Color(-2302756));
        label6.setText("Charisma");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 8, 4, 4);
        playerStatsPanel.add(label6, gbc);
        strBase = new JLabel();
        strBase.setForeground(new Color(-4934476));
        strBase.setName("");
        strBase.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(4, 4, 4, 4);
        playerStatsPanel.add(strBase, gbc);
        dexBase = new JLabel();
        dexBase.setForeground(new Color(-4934476));
        dexBase.setName("");
        dexBase.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(4, 4, 4, 4);
        playerStatsPanel.add(dexBase, gbc);
        conBase = new JLabel();
        conBase.setForeground(new Color(-4934476));
        conBase.setName("");
        conBase.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(4, 4, 4, 4);
        playerStatsPanel.add(conBase, gbc);
        intBase = new JLabel();
        intBase.setForeground(new Color(-4934476));
        intBase.setName("");
        intBase.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(4, 4, 4, 4);
        playerStatsPanel.add(intBase, gbc);
        wisBase = new JLabel();
        wisBase.setForeground(new Color(-4934476));
        wisBase.setName("");
        wisBase.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(4, 4, 4, 4);
        playerStatsPanel.add(wisBase, gbc);
        chaBase = new JLabel();
        chaBase.setForeground(new Color(-4934476));
        chaBase.setName("");
        chaBase.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(4, 4, 4, 4);
        playerStatsPanel.add(chaBase, gbc);
        strDiff = new JLabel();
        strDiff.setForeground(new Color(-4934476));
        strDiff.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(4, 4, 4, 8);
        playerStatsPanel.add(strDiff, gbc);
        dexDiff = new JLabel();
        dexDiff.setForeground(new Color(-4934476));
        dexDiff.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(4, 4, 4, 8);
        playerStatsPanel.add(dexDiff, gbc);
        conDiff = new JLabel();
        conDiff.setForeground(new Color(-4934476));
        conDiff.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(4, 4, 4, 8);
        playerStatsPanel.add(conDiff, gbc);
        intDiff = new JLabel();
        intDiff.setForeground(new Color(-4934476));
        intDiff.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(4, 4, 4, 8);
        playerStatsPanel.add(intDiff, gbc);
        wisDiff = new JLabel();
        wisDiff.setForeground(new Color(-4934476));
        wisDiff.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(4, 4, 4, 8);
        playerStatsPanel.add(wisDiff, gbc);
        chaDiff = new JLabel();
        chaDiff.setForeground(new Color(-4934476));
        chaDiff.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(4, 4, 4, 8);
        playerStatsPanel.add(chaDiff, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return playerStatsPanel;
    }

}

package biz.noorlander.batclient.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class BatGauge extends JPanel {

	private static final long serialVersionUID = 3148254377785539483L;

	private JProgressBar progressBar;
    private String unit;
    private SortedMap<Integer, Color> boundaryMap;
    private Color defaultColor;
    private int value;

    public BatGauge(Dimension preferredSize, int maximum, String unit, Color defaultColor) {
        super();
        this.unit = unit;
        this.defaultColor = defaultColor;
        this.boundaryMap = new TreeMap<>();
        this.setLayout(new GridBagLayout());
        progressBar = new JProgressBar();
        progressBar.setMaximum(maximum);
        progressBar.setStringPainted(true);
        this.setValue(0);
        this.setPreferredSize(preferredSize);
        this.setBackground( Color.BLACK );
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(progressBar, gbc);
    }

    public void setValue(int value) {
        this.value = value;
        progressBar.setValue(value % (progressBar.getMaximum()));
        progressBar.setString(value + unit);
        Color newColor = this.defaultColor;
        for (Map.Entry<Integer, Color> boundary : boundaryMap.entrySet()) {
            if (value >= boundary.getKey()) {
                newColor = boundary.getValue();
            } else {
                break;
            }
        }
        progressBar.setForeground(newColor);
    }

    public int getValue() {
        return this.value;
    }

    public void addBoundary(Integer value, Color color) {
        boundaryMap.put(value, color);
    }

    public void removeBoundary(Integer value) {
        boundaryMap.remove(value);
    }
}

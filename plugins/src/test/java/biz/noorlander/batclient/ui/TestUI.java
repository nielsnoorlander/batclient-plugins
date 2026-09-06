package biz.noorlander.batclient.ui;

import javax.swing.*;

public class TestUI {

    private static void initAndShowGUI() {
        // This method is invoked on the EDT thread
        JFrame frame = new JFrame("Swing and JavaFX");
//        final SoulPanelController controller = SoulPanelController.initFX();
//        frame.add(controller.getPanel());
//        frame.setSize(300, 200);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        controller.setSoulPoints(1234);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShowGUI();
            }
        });
    }
}


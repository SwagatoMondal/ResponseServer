import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.List;

class Home {
    private ResponseDB responseDB;

    Home() throws ClassNotFoundException {
        responseDB = new ResponseDB();
    }

    void showDialog() {
        JFrame frame = new JFrame("Response Server");
        frame.setSize(800, 500);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        final JLabel placementsLabel = new JLabel("Placements");
        frame.getContentPane().add(BorderLayout.NORTH, placementsLabel);

        final JList<String> placementsList = new JList<>(responseDB.getPlacements());
        frame.getContentPane().add(BorderLayout.CENTER, placementsList);

        final JPanel panel = new JPanel();

        final JButton addResponse = new JButton();
        try {
            final Image image = ImageIO.read(getClass().getResource("/resources/ic_add.png"));
            addResponse.setIcon(new ImageIcon(image.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        addResponse.addActionListener(e -> showResponseInputDialog());
        panel.add(addResponse);

        final JButton refresh = new JButton();
        try {
            final Image image = ImageIO.read(getClass().getResource("/resources/ic_refresh.png"));
            refresh.setIcon(new ImageIcon(image.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh.addActionListener(e -> {
            placementsList.removeAll();
            placementsList.setModel(responseDB.getPlacements());
        });
        panel.add(refresh);

        final JButton view = new JButton();
        try {
            final Image image = ImageIO.read(getClass().getResource("/resources/ic_view.png"));
            view.setIcon(new ImageIcon(image.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.addActionListener(e -> {
            final String toRemove = placementsList.getSelectedValue();
            if (null == toRemove) {
                showMessageDialog("Selection issue", "Please select one placement to view the response");
            } else {
                showResponse(toRemove);
            }
        });
        panel.add(view);

        final JButton remove = new JButton();
        try {
            final Image image = ImageIO.read(getClass().getResource("/resources/ic_remove.png"));
            remove.setIcon(new ImageIcon(image.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        remove.addActionListener(e -> {
            final List<String> toRemove = placementsList.getSelectedValuesList();
            if (toRemove.size() == 0) {
                showMessageDialog("Empty Selection", "Please select placements to remove");
            } else {
                showConfirmDialog(toRemove);
            }
        });
        panel.add(remove);

        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.setVisible(true);
    }

    private void showResponseInputDialog() {
        System.out.println("Add Response clicked");

        final JFrame frame = new JFrame("Response Editor");
        frame.setSize(800, 500);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        final JTextArea responseArea = new JTextArea();

        final JPanel panel = new JPanel();
        final JTextField plc = new JTextField(10);
        final JButton add = new JButton("Save");
        add.addActionListener(e -> {
            final String placement = plc.getText();
            final String response = responseArea.getText();

            if (null == placement || placement.length() == 0) {
                showMessageDialog("Missing text", "Placement is mandatory");
            } else if (null == response || response.length() == 0) {
                showMessageDialog("Missing text", "Response is mandatory");
            } else {
                boolean result = responseDB.addResponse(placement, response);
                showMessageDialog("Response save result",
                        "Response save operation resulted in success=" + result);
                frame.setVisible(false);
            }
        });
        final JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> frame.setVisible(false));
        panel.add(plc);
        panel.add(cancel);
        panel.add(add);

        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, responseArea);
        frame.setVisible(true);
    }

    private void showMessageDialog(String title, String msg) {
        System.out.println("Showing field error dialog ...");

        final JFrame frame = new JFrame();
        final JDialog dialog = new JDialog(frame, title, true);
        dialog.setLayout(new FlowLayout());
        final JLabel message = new JLabel(msg);
        dialog.add(message);
        final JButton ok = new JButton("Ok");
        ok.addActionListener(e -> dialog.setVisible(false));
        dialog.add(BorderLayout.SOUTH, ok);
        dialog.setSize(400, 100);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void showConfirmDialog(final List<String> toRemove) {
        System.out.println("Showing confirmation dialog ...");

        final JFrame frame = new JFrame();
        final JDialog dialog = new JDialog(frame, "Confirmation", true);
        dialog.setLayout(new FlowLayout());

        final JLabel message = new JLabel("Are you sure you want to remove the selected rows?");
        dialog.add(message);

        final JPanel panel = new JPanel();
        final JButton ok = new JButton("Ok");
        ok.addActionListener(e -> {
            boolean result = true;
            for (String placement : toRemove) {
                System.out.println("Removing placement : " + placement);
                result &= responseDB.removePlacement(placement);
            }
            dialog.setVisible(false);
            showMessageDialog("Confirmation", "Remove operation resulted in success=" + result);
        });
        panel.add(ok);
        final JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dialog.setVisible(false));
        panel.add(cancel);
        dialog.getContentPane().add(BorderLayout.SOUTH, panel);

        dialog.setSize(400, 100);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void showResponse(String placement) {
        System.out.println("View Response clicked");

        final JFrame frame = new JFrame("Response Viewer");
        frame.setSize(800, 500);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        final JTextArea responseArea = new JTextArea();
        responseArea.setEnabled(false);
        responseArea.setText(responseDB.getResponse(placement));

        final JPanel panel = new JPanel();
        final JTextField plc = new JTextField(10);
        plc.setEnabled(false);
        plc.setText(placement);
        final JButton cancel = new JButton("Close");
        cancel.addActionListener(e -> frame.setVisible(false));
        panel.add(plc);
        panel.add(cancel);

        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, responseArea);
        frame.setVisible(true);
    }
}

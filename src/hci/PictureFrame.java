package hci;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Aaron on 1/27/2017.
 */
public class PictureFrame extends JFrame {
        private JPanel contentPane = new JPanel();
        JPanel picPanel;
        JPanel bottomPanel;
        JMenuBar menuBar;
        JMenu mFile;
        JMenuItem mItemSelect;
        JMenuItem mItemNext;
        JMenuItem mItemPrevious;
        JMenuItem mItemExit;
        JButton bNext;
        JLabel directoryLabel;
        JFileChooser chooser;
        File directory;

        public PictureFrame() {
            directoryLabel = new JLabel("No directory selected");
            mFile = new JMenu("File");
            mItemSelect = new JMenuItem("Select folder");
            mItemNext = new JMenuItem("Next image");
            mItemPrevious = new JMenuItem("Previous image");
            mItemExit = new JMenuItem("Exit");
            chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            mFile.add(mItemSelect);
            mFile.add(mItemNext);
            mFile.add(mItemPrevious);
            mFile.add(mItemExit);
            menuBar = new JMenuBar();
            menuBar.add(mFile);
            setJMenuBar(menuBar);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            bottomPanel = new JPanel();
            bNext = new JButton("Next");
            bottomPanel.add(bNext);
            bottomPanel.add(directoryLabel);
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
            picPanel = new JPanel();
            mItemExit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PictureFrame.this.dispose();
                }
            });
            mItemSelect.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chooser.showOpenDialog(mItemSelect);
                    directory = chooser.getSelectedFile();
                    directoryLabel.setText(directory.getPath());
                }
            });

            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            contentPane.setLayout(new BorderLayout(0, 0));
            setContentPane(contentPane);
            contentPane.add(picPanel, BorderLayout.CENTER);
            contentPane.add(bottomPanel, BorderLayout.SOUTH);
            this.pack();
        }
}

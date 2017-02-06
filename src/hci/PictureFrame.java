package hci;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileFilter;
import java.sql.Time;

/**
 * Created by Aaron on 1/27/2017.
 */
public class PictureFrame extends JFrame {
        private JPanel contentPane = new JPanel();
        private JPanel picPanel;
        private JPanel bottomPanel;
        private JPanel controlPanel;
        private JMenuBar menuBar;
        private JMenu mFile;
        private JMenuItem mItemSelect;
        private JMenuItem mItemNext;
        private JMenuItem mItemPrevious;
        private JMenuItem mItemExit;
        private JButton bPrevious;
        private JButton bNext;
        private JLabel directoryLabel;
        private JFileChooser chooser;
        private File directory;
        private File[] imageList;
        private FileFilter filter;
        private JLabel imageLabel;
        private ImageIcon icon;
        private ImageIcon scaledImage;
        private final String[] validextensions = ImageIO.getReaderFileSuffixes();
        private int index = 0;
        private int numFiles = 0;

        public PictureFrame() {
            initializeComponents();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            mFile.add(mItemSelect);
            mFile.add(mItemNext);
            mFile.add(mItemPrevious);
            mFile.add(mItemExit);
            menuBar.add(mFile);
            setJMenuBar(menuBar);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            controlPanel.add(bPrevious);
            controlPanel.add(bNext);
            bottomPanel.add(controlPanel);
            bottomPanel.add(directoryLabel);
            imageLabel.setText("No images in directory");
            picPanel.add(imageLabel, BorderLayout.CENTER);
            contentPane.add(picPanel, BorderLayout.CENTER);
            contentPane.add(bottomPanel, BorderLayout.SOUTH);
            setListeners();
            this.setContentPane(contentPane);
            this.pack();
        }
        public void previousImage(){
            if(index > 0){
                index --;
            }else{
                index = numFiles -1;
            }
            setImage();
        }
        public void nextImage(){
            //Loops if the last imageLabel in the folder is reached
            if(index < numFiles-1) {
                index++;
            }else{
                index = 0;
            }
            setImage();
        }
        public void setImage(){
            if (directory!=null && numFiles >0) {
                icon = new ImageIcon(imageList[index].getAbsolutePath());
                imageLabel.setText("");
                imageLabel.setIcon(scaleImage(icon));
            }
        }
        public void initializeComponents(){
            directoryLabel = new JLabel("No directory selected");
            mFile = new JMenu("File");
            mItemSelect = new JMenuItem("Select folder");
            mItemNext = new JMenuItem("Next image");
            mItemPrevious = new JMenuItem("Previous image");
            mItemExit = new JMenuItem("Exit");
            chooser = new JFileChooser();
            menuBar = new JMenuBar();
            bottomPanel = new JPanel();
            bPrevious = new JButton("Previous");
            bNext = new JButton("Next");
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
            picPanel = new JPanel();
            picPanel.setLayout(new CardLayout());
            controlPanel = new JPanel();
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            contentPane.setLayout(new BorderLayout(0, 0));
            imageLabel = new JLabel("");
            icon = new ImageIcon();
        }
        public ImageIcon scaleImage(ImageIcon old){
            if(old.getImage() != null) {
                scaledImage = new ImageIcon(old.getImage().getScaledInstance(picPanel.getWidth(), picPanel.getHeight(), Image.SCALE_FAST));
                return scaledImage;
            }
            return null;
        }
        public String getExtension(File f){
            String name = f.toString();
            if(!name.contains(".")){
                return null;
            }
            return name.substring(name.lastIndexOf(".") +1);
        }
        public void setListeners(){
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
                    imageList = directory.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            String extension = getExtension(pathname);
                            //Check if the extension is in the valid list
                            for(String validext:validextensions){
                                if(validext.equals(extension)){
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                    directoryLabel.setText(directory.getPath());
                    numFiles = imageList.length;
                    setImage();
                }
            });
            bPrevious.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    previousImage();
                }
            });
            bNext.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nextImage();
                }
            });
            mItemNext.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nextImage();
                }
            });
            mItemPrevious.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    previousImage();
                }
            });
            imageLabel.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                        ImageIcon newIcon = scaleImage(icon);
                        imageLabel.setIcon(newIcon);
                }
            });
            imageLabel.addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                            if(e.getWheelRotation() > 0) {
                                nextImage();
                            }else{
                                previousImage();
                            }

                }
            });
            imageLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    nextImage();
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            mItemNext.setAccelerator(KeyStroke.getKeyStroke(39, 0));
            mItemPrevious.setAccelerator(KeyStroke.getKeyStroke(37, 0));

        }
}

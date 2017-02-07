package hci;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileFilter;

/**
 * Created by Aaron on 1/27/2017
 * HCI 2017 Assignment 1
 * Java Swing implementation of Photo Viewer
 */
class PictureFrame extends JFrame {
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
        private JButton bSS;
        private JLabel directoryLabel;
        private JFileChooser chooser;
        private File directory;
        private File[] imageList;
        private JLabel imageLabel;
        private ImageIcon icon;
        private final String[] validextensions = ImageIO.getReaderFileSuffixes();
        private int index = 0;
        private int numFiles = 0;
        private long delay = 0;
        private Timer ssTimer;
        private int prevWindowState;
        //Create the frame and add the components
        PictureFrame() {
            initializeComponents();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            mFile.add(mItemSelect);
            mFile.add(mItemNext);
            mFile.add(mItemPrevious);
            mFile.add(mItemExit);
            menuBar.add(mFile);
            setJMenuBar(menuBar);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            controlPanel.add(bPrevious);
            controlPanel.add(bSS);
            controlPanel.add(bNext);
            bottomPanel.add(controlPanel);
            bottomPanel.add(directoryLabel);
            imageLabel.setText("Use the File menu to select a directory with image files");
            picPanel.add(imageLabel, BorderLayout.CENTER);
            contentPane.add(picPanel, BorderLayout.CENTER);
            contentPane.add(bottomPanel, BorderLayout.SOUTH);
            setListeners();
            this.setContentPane(contentPane);
            this.pack();
        }
        /*Shifts the index and loads the previous image*/
        private void previousImage(){
            if(imageLabel.getIcon()!=null) {
                if (index > 0) {
                    index--;
                } else {
                    index = numFiles - 1;
                }
                setImage();
            }
        }
        /*Shifts the index and loads the next image*/
        private void nextImage(){
            if(imageLabel.getIcon()!=null) {
                if (index < numFiles - 1) {
                    index++;
                } else {
                    index = 0;
                }
                setImage();
            }
        }
        /*Check for valid image files in the directory and set them to the label*/
        private void setImage(){
            if (directory!=null && numFiles >0) {
                icon = new ImageIcon(imageList[index].getAbsolutePath());
                imageLabel.setText("");
                imageLabel.setIcon(scaleImage(icon));
            }
        }
        /*Make the frame full-screen and start the slideshow timer*/
        private void startSlideshow(){
            if(imageLabel.getIcon() != null) {
                prevWindowState = this.getExtendedState();
                this.setExtendedState(JFrame.MAXIMIZED_BOTH);
                ssTimer.start();
            }

        }
        /*Stop the slide-show timer and revert the previous frame size*/
        private void stopSlideshow(){
                if(ssTimer.isRunning()){
                    this.setExtendedState(prevWindowState);
                    ssTimer.stop();
                }
        }
        /*Set up the components*/
        private void initializeComponents(){
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
            bSS = new JButton("Slide-show");
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
            ssTimer = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nextImage();
                }
            });
        }
        /*Scale the image and retain the aspect ratio*/
        private ImageIcon scaleImage(ImageIcon old){
            if(old.getImage() != null) {
                //Get the original dimensions
                double oldWidth = old.getIconWidth();
                double oldHeight = old.getIconHeight();
                //Find the aspect ratio
                double ratio = oldWidth/oldHeight;
                //Set the new width and height based on the ratio
                double width  = Math.min(picPanel.getWidth(), picPanel.getHeight()*ratio);
                double height = Math.min(picPanel.getHeight(), picPanel.getWidth()/ratio);
                return new ImageIcon(old.getImage().getScaledInstance((int) width, (int) height, Image.SCALE_FAST));

            }
            return null;
        }
        /*Returns the extension of a given file*/
        private String getExtension(File f){
            String name = f.toString();
            if(!name.contains(".")){
                return null;
            }
            return name.substring(name.lastIndexOf(".") +1);
        }
        /*Set up listeners to for the user to interact with the components*/
        private void setListeners(){
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
                    index = 0;
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
            bSS.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    startSlideshow();
                }
            });
            mItemNext.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopSlideshow();
                    nextImage();
                }
            });
            mItemPrevious.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopSlideshow();
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
                    /*If time since the last movement is >800 ms
                        Prevents scrolling too fast on a touch-pad
                     */
                    if(delay != 0 && (System.currentTimeMillis() - delay <800)){
                        return;
                    }
                        if (e.getWheelRotation() > 0) {
                            nextImage();
                        } else {
                            previousImage();
                        }
                        delay = System.currentTimeMillis();
                }
            });
            imageLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(ssTimer.isRunning()){
                        stopSlideshow();
                        return;
                    }
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
            //Set accelerators for the right and left arrows to trigger the menu items
            mItemNext.setAccelerator(KeyStroke.getKeyStroke(39, 0));
            mItemPrevious.setAccelerator(KeyStroke.getKeyStroke(37, 0));
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    stopSlideshow();
                }
            });
        }


}


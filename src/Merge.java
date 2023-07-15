// Merging multiple pdf documents here

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import javax.swing.*;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

public class Merge extends JFrame{
    private JButton mergeButton;
    private JButton openButton;
    private JButton clearButton;
    private JButton destination;
    private JButton load;
    private JButton clearLogs;
    private JTextField sourceTextField;
    private JFileChooser fileChooser;
    private ArrayList<String> paths = new ArrayList<String>();
    private int returnFileValue;
    private String destinationStr;
    

    
    JFrame frame= new JFrame("PDF Merger");
    JPanel panel = new JPanel();
    JMenuBar mBar = new JMenuBar();
    JMenu m1 = new JMenu("FILE");
    JTextArea log;
    

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Merge();
            }
        });
    }

    public Merge() {
        
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        
        log = new JTextArea();
        log.setAlignmentX(100);
        log.setEditable(false);
        frame.add(new JScrollPane(log), null);

        
        mBar.add(m1);
        openButton = new JButton("Open");
        openButton.setMargin(new Insets(4, 4, 4, 4));
        mergeButton = new JButton("Merge");
        mergeButton.setMargin(new Insets(4, 4, 4, 4));
        clearButton = new JButton("Clear Selection");
        clearButton.setMargin(new Insets(4, 4, 4, 4));
        destination = new JButton("Destination");
        destination.setMargin(new Insets(4, 4, 4, 4));
        load = new JButton("Load Files");
        load.setMargin(new Insets(4, 4, 4, 4));
        clearLogs = new JButton("Clear Logs");
        clearLogs.setMargin(new Insets(4, 4, 4, 4));
        
        m1.add(openButton);
        panel.add(mergeButton);
        panel.add(clearButton);
        panel.add(destination);
        panel.add(load);
        panel.add(clearLogs);

        openButton.addActionListener(new AddActionListener());
        clearButton.addActionListener(new ClearActionListener());
        mergeButton.addActionListener(new MergeActionListener());
        destination.addActionListener(new SetDirectoryActionListener());
        load.addActionListener(new LoadActionListener());
        clearLogs.addActionListener(new ClearLogsActionListener());
        
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mBar);
        frame.setVisible(true);

	}

    class AddActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            try {
                JFileChooser fileChooser;
                if(openButton.isRolloverEnabled()){
                    fileChooser = new JFileChooser();
                    fileChooser.setMultiSelectionEnabled(true);
                    returnFileValue = fileChooser.showOpenDialog(openButton);
                    File[] files = fileChooser.getSelectedFiles();
                    for(int i = 0; i < files.length; i++){
                        paths.add(files[i].getAbsolutePath());
                        log.append(files[i].getAbsolutePath() + " has been added\n");
                    }
                }
            } catch (NullPointerException exception) {
                JOptionPane.showMessageDialog(null, "No File Was Selected.");
            }
            
        }
    }

    class ClearActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(openButton.isRolloverEnabled()){
                if(paths.isEmpty()){
                    log.append("All sources cleared." + "\n");
                    JOptionPane.showMessageDialog(null, "No Slected Files.");
                }else{
                    paths.clear();
                    log.append("All sources cleared" + "\n");
                    JOptionPane.showMessageDialog(null, "Selected Files Cleared.");
                }  
            }
        }
    }

    class MergeActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            PDFMergerUtility ut = new PDFMergerUtility();
            while(destinationStr == "" || destinationStr == null) {
            	JOptionPane.showMessageDialog(null, "Select a Destination Folder.");
            	SetDirectoryActionListener dirk = new SetDirectoryActionListener();
            	dirk.actionPerformed(e);
            }
            if(paths.size() == 0) {
            	JOptionPane.showMessageDialog(null, "No Files Selected.");
            }
            for(String path : paths) {
            	try {
					ut.addSource(path);
					log.append(path + "\n");
					
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "FileNotFoundException.");
				}
            }
            ut.setDestinationFileName(destinationStr);
            try {
				ut.mergeDocuments(null);
				File merged_file = new File(destinationStr);
	            Desktop desktop = Desktop.getDesktop();
	            desktop.open(merged_file);
	            JOptionPane.showMessageDialog(null, "All Files Succefully Merged");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
    }

    class SetDirectoryActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
        	try {
        		JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setDialogTitle("Select a Folder.");
                chooser.setAcceptAllFileFilterUsed(false);
                int returnFolderValue = chooser.showOpenDialog(destination); 
                File folderName = chooser.getSelectedFile();
                destinationStr = folderName.getAbsolutePath()  + "\\merged_file.pdf";
                log.append("Destination folder set to " + destinationStr + "\n");    
        	}
        	catch (Exception ex) {
        		if(destinationStr == "" || destinationStr == null) {
        			JOptionPane.showMessageDialog(null, "No Destination Folder Selected.");
        		}
        	}     
        }
    }
    
    class LoadActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(load.isRolloverEnabled()) {
                paths.sort(String::compareToIgnoreCase);
				if(paths.size() == 0) {
					JOptionPane.showMessageDialog(null, "No Files Selected.");
				}
				else {
					for(String path : paths) {
						log.append(path + "\n");
					}
				}
			}
		}
    	
    }

    class ClearLogsActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            log.setText(null);
            
        }
    }
}




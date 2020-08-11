package pcd.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import pcd.shared.Message;
import pcd.shared.Message.MsgType;
import pcd.shared.MyActionListener;

public class ClientGUI {

	private JFrame frame;
	private JList<String> list;
	private Client client;
	private JScrollPane pane;

	public ClientGUI(Client c) {
		client = c;
		frame = new JFrame("File Explorer");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addContent();
		frame.pack();
	}

	private void addContent() {
		list = new JList<String>(client.getDirectory().getDirectoryListing());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane = new JScrollPane(list);
		frame.add(pane, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton sizeButton = new JButton("Size");
		JButton exhibitButton = new JButton("Exhibit");
		JButton editButton = new JButton("Edit");
		JButton newButton = new JButton("New");
		JButton deleteButton = new JButton("Delete");
		sizeButton.addActionListener(new MyActionListener(MsgType.SIZE,this));
		exhibitButton.addActionListener(new MyActionListener(MsgType.READ,this));
		editButton.addActionListener(new MyActionListener(MsgType.WRITE,this));
		newButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = JOptionPane.showInputDialog("Insert the file name here");
				if(!fileName.endsWith(".txt"))
					fileName+=".txt";
				if(client.getDirectory().exists(fileName)) {
					JOptionPane.showMessageDialog(null,"The file you wanted to create already exists");
					return;
				}
				Message msgToCreate = new Message(MsgType.CREATE,fileName);
				sendMessageAndWaitToReceive(msgToCreate);
			}
		});
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Message msgToDelete = new Message(MsgType.DELETE,list.getSelectedValue());
				sendMessage(msgToDelete);				
			}

		});
		buttonPanel.add(sizeButton);
		buttonPanel.add(exhibitButton);
		buttonPanel.add(editButton);
		buttonPanel.add(newButton);
		buttonPanel.add(deleteButton);
		frame.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void exhibitWindow(String fileText,String fileName) {
		frame.setVisible(false);
		JFrame exhibitFrame = new JFrame("Displaying: " + fileName);
		exhibitFrame.setLayout(new BorderLayout());
		exhibitFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		JTextArea textArea = new JTextArea(fileText);
		textArea.setEditable(false);
		JButton closeButton = new JButton("Close");
		exhibitFrame.add(textArea);
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Message msgToSend = new Message(MsgType.CLOSE,fileName);
				sendMessage(msgToSend);
				exhibitFrame.dispose();
				frame.setVisible(true);
			}
		});
		exhibitFrame.add(closeButton, BorderLayout.SOUTH);
		exhibitFrame.pack();
		exhibitFrame.setVisible(true);
	}

	public void editWindow(String fileText, String fileName) {
		frame.setVisible(false);
		JFrame editFrame = new JFrame("Editing: " + fileName);
		editFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		editFrame.setLayout(new BorderLayout());
		JTextArea textArea = new JTextArea(fileText);
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String stringToSend = textArea.getText();
				Message msgToSend = new Message(MsgType.SAVE,fileName);
				msgToSend.setMessage(stringToSend);
				sendMessage(msgToSend);
				editFrame.dispose();
				frame.setVisible(true);
			}
		});
		editFrame.add(textArea, BorderLayout.CENTER);
		editFrame.add(saveButton,BorderLayout.SOUTH);
		editFrame.pack();
		editFrame.setVisible(true);

	}

	public void open() {
		frame.setVisible(true);
	}

	public void sendMessage(Message msgToSend) {
		client.sendMessage(msgToSend);

	}

	public JList<String> getList() {
		return list;
	}

	public void refreshFileList() {
		frame.remove(pane);
		list = new JList<String>(client.getDirectory().getDirectoryListing());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane = new JScrollPane(list);
		frame.add(pane, BorderLayout.CENTER);
		frame.pack();
	}

	public void sendMessageAndWaitToReceive(Message msgToSend) {
		client.sendMessageAndWaitToReceive(msgToSend);

	}
}	

package pcd.shared;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pcd.client.ClientGUI;
import pcd.shared.Message.MsgType;

public class MyActionListener implements ActionListener {

	private MsgType type;
	private ClientGUI gui;

	public MyActionListener(MsgType t, ClientGUI g) {
		type = t;
		gui = g;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(gui.getList().getSelectedValue() == null)
			return;
		Message msgToSend = new Message(type, gui.getList().getSelectedValue());
		gui.sendMessageAndWaitToReceive(msgToSend);

	}

}

package com.trendmicro.filesystem.ui;

import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import com.trendmicro.filesystem.handler.CommandHandler;

public class EditUi {

	Frame fr = new Frame();
	TextArea ta = new TextArea();

	CommandHandler commandHandler;
	String filename;
	TextField textfield;

	public EditUi(String fname, CommandHandler cHandler, TextField tf) {
		this.commandHandler = cHandler;
		this.filename = fname;
		this.textfield = tf;

		fr.setTitle(commandHandler.getPwd() + fname);
		fr.setBounds(200, 200, 600, 600);
		fr.setVisible(true);
		fr.setLayout(null);
		fr.setResizable(false);
		ta.setBounds(30, 30, 550, 500);
		fr.add(ta);

		ta.setText(commandHandler.getFileContent(filename));
		textfield.setEditable(false);

		fr.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int choose = JOptionPane.showConfirmDialog(fr, "save file?", "", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if(choose == JOptionPane.YES_OPTION) {
					commandHandler.editFile(filename, ta.getText().getBytes());
				}
				textfield.setEditable(true);
				fr.dispose();
			}
		});
	}
}

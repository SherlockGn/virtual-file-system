package com.trendmicro.filesystem.ui;

import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import com.trendmicro.filesystem.handler.CommandHandler;

public class MainUi {
	Frame fr = new Frame();
	TextArea log = new TextArea();
	TextField input = new TextField(), pwd = new TextField();

	private CommandHandler commandHandler = new CommandHandler();

	public MainUi(String init) {
		fr.setBounds(200, 200, 600, 600);
		fr.setVisible(true);
		fr.setLayout(null);
		fr.setResizable(false);

		pwd.setEditable(false);

		fr.add(log);
		fr.add(pwd);
		fr.add(input);

		log.setBounds(30, 30, 550, 500);
		pwd.setBounds(30, 540, 90, 30);
		input.setBounds(120, 540, 430, 30);

		fr.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				fr.dispose();
			}
		});

		input.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String back = commandHandler.exec(input.getText());

					if (!back.startsWith("ui-command")) {
						if (!back.equals(""))
							append(back);
					} else {
						String[] cmds = back.split("\\+");
						String cmd = cmds[1].trim();
						if (cmd.equals("exit window"))
							fr.dispose();
						else if (cmd.equals("reay to edit file")) {
							String name = cmds[2].trim();
							new EditUi(name, commandHandler, input);
						} else if (cmd.equals("delete")) {
							String name = cmds[2].trim();
							int choose = JOptionPane.showConfirmDialog(fr,
									"delete file or directory? yes=file, no=directory", "", JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE);
							if (choose == JOptionPane.YES_OPTION) {
								commandHandler.delete(name, true);
							} else
								commandHandler.delete(name, false);
						} else if (cmd.equals("clear")) {
							log.setText(null);
						}
					}
					input.setText("");
					pwd.setText(commandHandler.getPwd());
				}
			}
		});

		// start
		String msg = commandHandler.init(init);
		append(msg);
		pwd.setText(commandHandler.getPwd());
	}

	public void append(String str) {
		log.append(str + "\n\n");
	}

	public static void main(String[] args) {
		new MainUi(null);
	}
}

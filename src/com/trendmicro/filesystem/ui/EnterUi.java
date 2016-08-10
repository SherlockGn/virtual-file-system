package com.trendmicro.filesystem.ui;

import java.awt.Button;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EnterUi {
	private Frame fr = new Frame();
	private Button btn1 = new Button("new file system");
	private Button btn2 = new Button("open from file");
	private Button btnc = new Button("...");

	public EnterUi() {
		fr.setBounds(400, 400, 200, 200);
		fr.setVisible(true);
		fr.setLayout(null);
		fr.setResizable(false);

		fr.add(btn1);
		fr.add(btn2);

		btn1.setBounds(50, 60, 100, 30);
		btn2.setBounds(50, 120, 100, 30);
		btnc.setBounds(150, 120, 30, 30);

		fr.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				fr.dispose();
			}
		});

		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MainUi(null);
				fr.dispose();
			}
		});

		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(fr, "", FileDialog.LOAD);
				fd.setVisible(true);
				if (fd.getDirectory() != null && fd.getFile() != null) {
					new MainUi(fd.getDirectory() + fd.getFile());
					fr.dispose();
				}
			}
		});
	}

	public static void main(String[] args) {
		new EnterUi();
	}
}

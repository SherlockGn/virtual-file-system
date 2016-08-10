package com.trendmicro.filesystem.model;

import java.util.List;

public class File extends TreeNode {

	private static final long serialVersionUID = 1L;

	private byte[] contents;

	public File() {
		super();
		contents = new byte[0];
	}

	public File(String name, List<TreeNode> children) {
		super(true, name, children);
	}

	public File(String name) {
		super(true, name);
	}

	public byte[] getContents() {
		return contents;
	}

	public void setContents(byte[] contents) {
		this.contents = contents;
	}

	public void appendContents(byte[] contents) {
		byte[] tp = new byte[this.contents.length + contents.length];
		int cnt = 0;
		for (int i = 0; i < this.contents.length; i++)
			tp[cnt++] = this.contents[i];
		for (int i = 0; i < contents.length; i++)
			tp[cnt++] = contents[i];
		this.contents = tp;
	}
}

package com.trendmicro.filesystem.model;

import java.util.List;

public class Directory extends TreeNode {

	private static final long serialVersionUID = 1L;

	public Directory() {
		super();
	}

	public Directory(String name, List<TreeNode> children) {
		super(false, name, children);
	}
	
	public Directory(String name) {
		super(false, name);
	}
}

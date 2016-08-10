package com.trendmicro.filesystem.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public abstract class TreeNode implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean isFile;
	private String name;

	private List<TreeNode> children;
	private TreeNode father;

	public TreeNode() {
		isFile = true;
		name = "default";
		children = null;
	}

	public TreeNode(boolean isFile, String name, List<TreeNode> children) {
		this.isFile = isFile;
		this.name = name;
		this.children = children;
	}

	public TreeNode(boolean isFile, String name) {
		this.isFile = isFile;
		this.name = name;
		this.children = new LinkedList<TreeNode>();
	}

	public boolean isFile() {
		return isFile;
	}

	public boolean getIsFile() {
		return isFile;
	}

	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public void addChild(TreeNode treeNode) {
		children.add(treeNode);
	}

	public TreeNode getChild(int index) {
		return children.get(index);
	}

	public TreeNode getFather() {
		return father;
	}

	public void setFather(TreeNode father) {
		this.father = father;
	}

	public static void main(String[] args) {
	}
}

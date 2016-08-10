package com.trendmicro.filesystem.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class VirtualFileSystem implements Serializable {

	private static final long serialVersionUID = 1214292122674025876L;

	private TreeNode root;
	private String mapFile;
	private transient TreeNode pointer;

	private VirtualFileSystem() {
	}

	public static VirtualFileSystem init() {
		VirtualFileSystem vfs = new VirtualFileSystem();
		vfs.root = new Directory("root");
		vfs.root.setFather(null);
		vfs.mapFile = null;
		vfs.pointer = vfs.root;
		return vfs;
	}

	public static VirtualFileSystem initByFile(String path) {
		java.io.File file = new java.io.File(path);
		if (!file.exists())
			return null;
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			ObjectInputStream ois = new ObjectInputStream(new DataInputStream(bis));
			Object obj = ois.readObject();
			VirtualFileSystem vfs = (VirtualFileSystem) obj;
			ois.close();
			vfs.pointer = vfs.root;
			return vfs;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public boolean save(String path) {
		java.io.File file = new java.io.File(path);
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			ObjectOutputStream oos = new ObjectOutputStream(new DataOutputStream(bos));
			oos.writeObject(this);
			oos.flush();
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public String traverse() {
		StringBuffer buf = new StringBuffer();
		boolean first = true;
		for (TreeNode child : pointer.getChildren()) {
			if (!first)
				buf.append("\n");
			buf.append((child.isFile() ? "[f] " : "[d] ") + child.getName());
			first = false;
		}
		return buf.toString();
	}

	public boolean enterBack() {
		if (pointer.getFather() != null) {
			pointer = pointer.getFather();
			return true;
		}
		return false;
	}

	public boolean enterDirectory(String dname) {
		for (TreeNode child : pointer.getChildren()) {
			if (child.isFile() == false && child.getName().equals(dname)) {
				pointer = child;
				return true;
			}
		}
		return false;
	}

	public boolean addDir(String dname) {
		for (TreeNode child : pointer.getChildren()) {
			if (!child.isFile() && child.getName().equals(dname)) {
				pointer = child;
				return false;
			}
		}
		Directory dir = new Directory(dname);
		dir.setFather(pointer);
		pointer.addChild(dir);
		return true;
	}

	public boolean addFile(String fname, byte[] contents) {
		for (TreeNode child : pointer.getChildren()) {
			if (child.isFile() && child.getName().equals(fname))
				return false;
		}
		File f = new File(fname);
		f.setContents(contents);
		f.setFather(pointer);
		pointer.addChild(f);
		return true;
	}

	public boolean addFile(String fname, String contents) {
		return addFile(fname, contents.getBytes());
	}

	public boolean addFile(String fname) {
		return addFile(fname, "");
	}

	public String viewTextFile(String fname) {
		TreeNode f = null;
		for (TreeNode child : pointer.getChildren()) {
			if (child.isFile() == true && child.getName().equals(fname)) {
				f = child;
				break;
			}
		}
		if (f == null)
			return null;
		byte[] contents = ((File) f).getContents();
		return new String(contents);
	}

	public String viewBinaryFile(String fname) {
		TreeNode f = null;
		for (TreeNode child : pointer.getChildren()) {
			if (child.isFile() == true && child.getName().equals(fname)) {
				f = child;
				break;
			}
		}
		if (f == null)
			return null;
		byte[] contents = ((File) f).getContents();
		StringBuffer buf = new StringBuffer();
		boolean first = true;
		for (byte b : contents) {
			if (!first)
				buf.append(" ");
			String hex = Integer.toHexString((b & 0x000000FF) | 0xFFFFFF00).substring(6);
			buf.append(hex);
			first = false;
		}
		return buf.toString();
	}

	public boolean checkFile(String fname) {
		TreeNode f = null;
		for (TreeNode child : pointer.getChildren()) {
			if (child.isFile() == true && child.getName().equals(fname)) {
				f = child;
				break;
			}
		}
		if (f == null)
			return false;
		return true;
	}

	public int check(String name) {
		TreeNode d = null, f = null;
		for (TreeNode child : pointer.getChildren()) {
			if (child.isFile() == true && child.getName().equals(name)) {
				f = child;
				if (d != null)
					break;
			}
			if (child.isFile() == false && child.getName().equals(name)) {
				d = child;
				if (f != null)
					break;
			}
		}
		// not exist: 0
		// one exist: 1
		// two exist: 2
		if (f == null && d == null)
			return 0;
		if (f != null && d != null)
			return 2;
		return 1;
	}

	public boolean editFile(String fname, byte[] contents) {
		TreeNode f = null;
		for (TreeNode child : pointer.getChildren()) {
			if (child.isFile() == true && child.getName().equals(fname)) {
				f = child;
				break;
			}
		}
		if (f == null)
			return false;
		((File) f).setContents(contents);
		return true;
	}

	public String printWorkingDirectory() {
		TreeNode tp = pointer;
		StringBuffer buf = new StringBuffer();
		while (tp.getFather() != null) {
			buf.insert(0, tp.getName() + "/");
			tp = tp.getFather();
		}
		buf.insert(0, "/");
		return buf.toString();
	}

	// if name exists & only one node is patterned, it will be deleted
	// no matter directory or file
	// if both directory and file named exist, isFile is used
	public boolean delete(String name, boolean isFile) {
		TreeNode d = null, f = null;
		for (TreeNode child : pointer.getChildren()) {
			if (child.isFile() == true && child.getName().equals(name)) {
				f = child;
				if (d != null)
					break;
			}
			if (child.isFile() == false && child.getName().equals(name)) {
				d = child;
				if (f != null)
					break;
			}
		}
		if (f == null && d == null)
			return false;
		if (f != null && d != null) {
			if (isFile)
				pointer.getChildren().remove(f);
			else
				pointer.getChildren().remove(d);
		} else if (f != null)
			pointer.getChildren().remove(f);
		else
			pointer.getChildren().remove(d);
		return true;
	}

	public String getMapFile() {
		return mapFile;
	}

	public static void main(String[] args) {

		VirtualFileSystem vfs = VirtualFileSystem.initByFile("D:\\vfs.txt");
		System.out.println(vfs.traverse());
		vfs.enterDirectory("dir");
		System.out.println(vfs.traverse());

		/*
		 * VirtualFileSystem vfs = VirtualFileSystem.init();
		 * System.out.println(vfs.printWorkingDirectory()); vfs.addDir("dir");
		 * vfs.addFile("file", "123"); vfs.enterDirectory("dir");
		 * vfs.addFile("file", "abc");
		 * System.out.println(vfs.printWorkingDirectory());
		 * System.out.println(vfs.viewTextFile("file"));
		 * System.out.println(vfs.traverse()); vfs.enterBack();
		 * System.out.println(vfs.viewTextFile("file"));
		 * System.out.println(vfs.viewBinaryFile("file"));
		 * System.out.println(vfs.traverse());
		 * 
		 * vfs.save("D:\\vfs.txt");
		 */
	}
}

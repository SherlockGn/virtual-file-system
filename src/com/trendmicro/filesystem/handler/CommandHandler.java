package com.trendmicro.filesystem.handler;

import java.util.Scanner;

import com.trendmicro.filesystem.model.VirtualFileSystem;

public class CommandHandler {

	private VirtualFileSystem vfs;

	public String init(String path) {
		if (path == null)
			vfs = VirtualFileSystem.init();
		else {
			vfs = VirtualFileSystem.initByFile(path);
			if (vfs == null)
				return "virtual file system cannot be initialed by file \"+path+\"";
		}
		return "virtual file system is initialed successly";
	}

	public String exec(String cmd) {
		String[] combine = CommandParser.parse(cmd);
		String command = combine[0];
		String arg = combine[1];

		if (vfs == null) {
			return "virtual file system hasn\'t been initialed";
		}

		if (command.equals("help")) {
			return "help: to show this message\n" + "ls/dir/ll: to list files and directories\n"
					+ "cd \"dir_name\": enter the directory\n" + "md/mkdir \"dir_name\": create a directory\n"
					+ "touch \"file_name\": create an empty file\n" + "edit \"file_name:\": edit a file\n"
					+ "cat/type \"file_name\": show content of the file\n"
					+ "rm/del \"name\": delete file or directory\n" + "cls/clear: clear the screen\n"
					+ "xxd \"file_name\": show content of the file (binary)\n" + "pwd: print the working directory\n"
					+ "save \"real_file_name\": save the file system to a file\n" + "exit/quit: quit the system";
		} else if (command.equals("ls") || command.equals("dir") || command.equals("ll")) {
			return vfs.printWorkingDirectory() + ":\n" + vfs.traverse();
		} else if (command.equals("cd")) {
			if (arg == null)
				return command + " needs an arg";
			if (arg.equals("..")) {
				vfs.enterBack();
				return "";
			} else if (arg.equals("."))
				return "";
			else {
				if (vfs.enterDirectory(arg))
					return "";
				else
					return "cannot find directory " + arg;
			}
		} else if (command.equals("md") || command.equals("mkdir")) {
			if (arg == null)
				return command + " needs an arg";
			if (vfs.addDir(arg))
				return "";
			else
				return "directory " + arg + " has already existed";
		} else if (command.equals("touch")) {
			if (arg == null)
				return command + " needs an arg";
			if (vfs.addFile(arg))
				return "";
			else
				return "file " + arg + " has already existed";
		} else if (command.equals("edit")) {
			if (arg == null)
				return command + " needs an arg";
			if (vfs.checkFile(arg))
				return "ui-command + reay to edit file + " + arg;
			else
				return "file " + arg + " doesn\'t exist";
		} else if (command.equals("cat") || command.equals("type")) {
			if (arg == null)
				return command + " needs an arg";
			String con = vfs.viewTextFile(arg);
			if (con == null)
				return "file " + arg + " doesn\'t exist";
			return con;
		} else if (command.equals("xxd")) {
			if (arg == null)
				return command + " needs an arg";
			String con = vfs.viewBinaryFile(arg);
			if (con == null)
				return "file " + arg + " doesn\'t exist";
			return con;
		} else if (command.equals("save")) {
			if (arg == null)
				return command + " needs an arg";
			if (vfs.save(arg))
				return "";
			return "writing file failure";
		} else if (command.equals("rm") || command.equals("del")) {
			if (arg == null)
				return command + " needs an arg";
			int ret = vfs.check(arg);
			if (ret == 0)
				return "no file/directory named " + arg;
			if (ret == 1) {
				vfs.delete(arg, true);
				return "";
			}
			// ret == 2
			return "ui-command + delete + " + arg;
		} else if (command.equals("cls") || command.equals("clear")) {
			return "ui-command + clear";
		} else if (command.equals("pwd")) {
			return vfs.printWorkingDirectory();
		} else if (command.equals("exit") || command.equals("quit")) {
			return "ui-command + exit window";
		}
		return "cannot find command " + command;
	}

	public String getPwd() {
		return vfs.printWorkingDirectory();
	}

	public String getFileContent(String fname) {
		return vfs.viewTextFile(fname);
	}

	public boolean editFile(String fname, byte[] contents) {
		return vfs.editFile(fname, contents);
	}

	public boolean delete(String name, boolean isFile) {
		return vfs.delete(name, isFile);
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		CommandHandler ch = new CommandHandler();
		ch.init(null);

		Scanner scan = new Scanner(System.in);
		while (true) {
			String cmd = scan.nextLine();
			System.out.println(ch.exec(cmd));
		}
	}
}

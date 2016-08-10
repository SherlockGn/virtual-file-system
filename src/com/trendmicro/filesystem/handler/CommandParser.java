package com.trendmicro.filesystem.handler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommandParser {
	public static String[] parse(String cmd) {
		cmd = cmd.trim();
		if (cmd.equals(""))
			return new String[] { null, null };

		boolean quoTag = false, countTag = false;
		StringBuffer buf = new StringBuffer();
		List<String> result = new LinkedList<String>();
		for (int i = 0; i < cmd.length(); i++) {
			char ch = cmd.charAt(i);
			if (ch == ' ' || ch == '\t') {
				if (quoTag == false && countTag == false)
					;
				else if (quoTag == false && countTag == true) {
					if (!buf.toString().trim().equals(""))
						result.add(buf.toString().trim());
					buf.delete(0, buf.length());
				} else
					buf.append(" ");
			} else if (ch == '\"') {
				if (quoTag == true) {
					quoTag = countTag = false;
					if (!buf.toString().trim().equals(""))
						result.add(buf.toString().trim());
					buf.delete(0, buf.length());
				} else {
					if (!buf.toString().trim().equals(""))
						result.add(buf.toString().trim());
					buf.delete(0, buf.length());
					quoTag = countTag = true;
				}
			} else {
				countTag = true;
				buf.append(ch);
			}
		}
		if (!buf.toString().trim().equals(""))
			result.add(buf.toString().trim());
		buf.delete(0, buf.length());

		if (result.size() == 0)
			return new String[] { null, null };
		else if (result.size() == 1)
			return new String[] { result.get(0), null };
		else
			return new String[] { result.get(0), result.get(1) };
	}

	public static void main(String[] args) {
		System.out.println(Arrays.asList(parse("mkdir")));
	}
}

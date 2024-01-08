/*
 * Copyright (c) 2022, Melxin <https://github.com/melxin>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.jagexlauncherinterceptor.jagexlauncheragent.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OSUtils
{
	private static final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

	public static final boolean isWindows = OS_NAME.contains("win");
	public static final boolean isMac = OS_NAME.contains("mac") || OS_NAME.contains("darwin");
	public static final boolean isLinux = OS_NAME.contains("linux");

	public static final boolean isUnix = OS_NAME.contains("nix")
			|| OS_NAME.contains("nux")
			|| OS_NAME.contains("ix")
			|| OS_NAME.contains("bsd")
			|| OS_NAME.contains("hp-ux");

	public static final boolean isSolaris = OS_NAME.contains("sunos") || OS_NAME.contains("solaris");

	private static final String OS_ARCH = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);
	private static final Map<String, Integer> archMap = new HashMap<String, Integer>();

	static
	{
		archMap.put("x86", 32);
		archMap.put("i386", 32);
		archMap.put("i486", 32);
		archMap.put("i586", 32);
		archMap.put("i686", 32);
		archMap.put("x86_64", 64);
		archMap.put("amd64", 64);
	}

	/**
	 * Get arch integer (32 or 64)
	 *
	 * @return architecture
	 */
	private static int getArch()
	{
		return archMap.get(OS_ARCH);
	}

	public static boolean isArch_x32 = getArch() == 32;
	public static boolean isArch_x64 = getArch() == 64;

	public static final boolean isWindows_x32 = isWindows && isArch_x32;
	public static final boolean isWindows_x64 = isWindows && isArch_x64;

	public static final boolean isMac_x32 = isMac && isArch_x32;
	public static final boolean isMac_x64 = isMac && isArch_x64;

	public static final boolean isLinux_x32 = isLinux && isArch_x32;
	public static final boolean isLinux_x64 = isLinux && isArch_x64;

	public static final boolean isUnix_x32 = isUnix && isArch_x32;
	public static final boolean isUnix_x64 = isUnix && isArch_x64;

	public static final boolean isSolaris_x32 = isSolaris && isArch_x32;
	public static final boolean isSolaris_x64 = isSolaris && isArch_x64;
}

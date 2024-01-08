/*
 * Copyright (c) 2024, melxin
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
package com.jagexlauncherinterceptor;

import com.alee.laf.LookAndFeelException;
import com.alee.laf.WebLookAndFeel;
import com.jagexlauncherinterceptor.ui.UI;
import com.jagexlauncherinterceptor.utils.ResourceLoader;
import com.mageddo.jvm.attach.JvmAttach;
import java.lang.reflect.InvocationTargetException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import lombok.extern.slf4j.Slf4j;
import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.ProcessInfo;

/**
 * Sources: <https://github.com/mageddo-projects/jvm-attach/>
 * <https://github.com/liuzhengyang/javaagent-example/>
 */
@Slf4j
public class Main
{
	private static final UI ui = UI.getInstance();
	private static ProcessInfo targetProcess = null;

	public static void main(String[] args)
	{
		/**
		 * UI
		 */
		if (!ui.isVisible())
		{
			try
			{
				java.awt.EventQueue.invokeAndWait(() ->
				{
					log.debug("Creating UI...");
					try
					{
						WebLookAndFeel.install(com.alee.skin.dark.WebDarkSkin.class);
					}
					catch (LookAndFeelException e)
					{
						log.error("", e);
						try
						{
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						}
						catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
						{
							log.error("", ex);
						}
					}

					ui.initComponents();
					ui.setVisible(true);
					ui.toFront();
					ui.requestFocus();
					ui.validate();
				});
			}
			catch (InterruptedException | InvocationTargetException ex)
			{
				log.error("", ex);
			}
		}

		/**
		 * Attachment
		 */
		log.info("Waiting for target process..");

		while (targetProcess == null)
		{
			JProcesses.getProcessList().stream()
					.parallel()
					.filter(p -> p.getCommand().contains("RuneLite.jar"))
					.findAny()
					.ifPresent(p -> targetProcess = p);

			try
			{
				Thread.sleep(300);
			}
			catch (InterruptedException ex)
			{
				log.error("", ex);
			}
		}

		if (targetProcess != null)
		{
			log.info("Attaching agent to: {}", targetProcess.toString());
			JvmAttach.loadJar(Integer.parseInt(targetProcess.getPid()), Main.class.getResourceAsStream(ResourceLoader.JAGEX_LAUNCHER_AGENT));

			// Continue waiting for another process
			targetProcess = null;
			main(args);
		}
	}
}

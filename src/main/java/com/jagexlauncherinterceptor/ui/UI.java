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
package com.jagexlauncherinterceptor.ui;

import com.jagexlauncherinterceptor.utils.ResourceLoader;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;

public class UI extends javax.swing.JFrame
{
	private javax.swing.JPanel panel;
	private javax.swing.JScrollPane scrollPane;
	private javax.swing.JTextArea textArea;

	private static UI instance;

	public static UI getInstance()
	{
		if (instance == null)
		{
			instance = new UI();
		}
		return instance;
	}

	/**
	 * Private constructor
	 */
	private UI()
	{
	}

	/**
	 * This should be called after WebLookAndFeel.install
	 */
	public void initComponents()
	{
		panel = new javax.swing.JPanel();
		scrollPane = new javax.swing.JScrollPane();
		textArea = new javax.swing.JTextArea();

		setTitle("Jagex Launcher Interceptor");
		setIconImage(ResourceLoader.TITLE_BAR_ICON.getImage());
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		// Text area settings
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.GREEN);
		textArea.setEditable(false);
		textArea.setColumns(20);
		textArea.setRows(5);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scrollPane.setViewportView(textArea);

		// Set print stream and redirect system.out & err
		PrintStream stdOut = new PrintStream(new MultiOutputStream(System.out, textArea));
		PrintStream stdErr = new PrintStream(new MultiOutputStream(System.err, textArea));

		System.setOut(stdOut);
		System.setErr(stdErr);

		// Layout
		javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
		panel.setLayout(panelLayout);
		panelLayout.setHorizontalGroup(
				panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(scrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)
		);
		panelLayout.setVerticalGroup(
				panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
		);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		pack();
		setLocationRelativeTo(null);
	}

	/**
	 * Multi outputStream
	 */
	private class MultiOutputStream extends OutputStream
	{
		private final OutputStream outputStream;
		private final JTextArea textArea;

		public MultiOutputStream(OutputStream outputStream, JTextArea textArea)
		{
			this.outputStream = outputStream;
			this.textArea = textArea;
		}

		@Override
		public void write(int b) throws IOException
		{
			// Write to outputStream
			outputStream.write(b);

			// Clear text from textArea if too many characters
			if (textArea.getText().length() > 100000)
			{
				textArea.setText(String.valueOf((char) b));
				return;
			}

			// redirects data to the text area
			textArea.append(String.valueOf((char) b));

			// scrolls the text area to the end of data
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
	}
}

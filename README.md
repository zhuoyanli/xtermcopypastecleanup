# xtermcopypastecleanup
Java Swing GUI tool to clean up text between Copy&amp;Paste of Windows XTerm clients.

What this small Java Swing tool tries to solve is the following scenario: Windows XTerm clients would typically break long 
lines into multiple lines (line-wrapping) so that those long lines are readable for Terminal windows. However, when
users later on try to copy those long lines, mouse-highlighting-then-copying would usually return multiple-lines for single
long lines. This would cause troubles when users want to paste the content: Users typically want to paste single line, e.g. 
as complete shell commands, but they will get multiple lines that become invalid command fragments for shell.

The processing of this tool is very simple: Remove all the line-breaks except the last one. It also accounts for scenarios
where text are copied from Emacs: In Emacs, where the editor wraps long lines into multiple lines, symbols of backward slashes, '\',
will be added to each wrapped line to show that that line has been wrapped. When text are copied from the terminal
windows the trailing backward slashes, '\', will also be copied as part of the lines. This tool does a very simple check: If all
the lines, except the last one, end with backward slash symbols, it would then assume the text have been copied from Emacs and
also strip those slashes.

The source code of the main Swing application class, XTermCopyPasteCleanup, does not look like the typical Swing application
which have all sorts of Swing component initialization and setup codes. This app has been developped with IntelliJ GUI
designer and most of the Swing setup have been leveraged with the designer codes.

The tool is expected to be used in Windows only, though it should also work in X-window. The IntelliJ project has been
setup to build Windows executable Jars (JRE installation would still be necessary). The artifacts are found in
out/artifacts/ExecutableJars.


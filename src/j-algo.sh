#!/usr/bin/env bash
# this is UN*X startscript for JAlgo
# Stephan

CLASSPATH="runtime:extlibs/jface.jar:extlibs/boot.jar:extlibs/runtime.jar:\
extlibs/draw2d.jar"
MAINCLASS="org.jalgo.main.Jalgo"

UNAME=$(uname -s)

if [ "$UNAME" = "Linux" ]; then
	CLASSPATH="$CLASSPATH:extlibs/linux/swt.jar:extlibs/linux/swt-pi.jar"
	SYSTEM_PROPERTIES="java.library.path=extlibs/linux"
elif [ "$UNAME" = "SunOS" ] ; then
	CLASSPATH="$CLASSPATH:extlibs/solaris/swt.jar"
	SYSTEM_PROPERTIES="java.library.path=extlibs/solaris"
fi
	
JAVAVERSION=$(java -version 2>&1 | head -n 1 | cut -d' ' -f3 | \
	sed -e 's/^\"\([0-9]\+\)\.\([0-9]\+\)\.\([0-9]\+\)_.*\"$/\1\2\3/')
# this regex is ugly but it is the only portable solution

if [ "$JAVAVERSION" -lt "140" ] ; then
	echo "You need Java 1.4.0 or higher" > /dev/stderr
	exit 1
fi

exec java -D$SYSTEM_PROPERTIES -classpath "$CLASSPATH" $MAINCLASS "$@"


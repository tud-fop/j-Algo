#!/bin/sh
OLDPWD=`pwd`
cd `dirname $0`
exec java -Djava.library.path=extlibs -Djava.ext.dirs=runtime/modules -jar runtime/j-Algo.jar $*
cd $OLDPWD
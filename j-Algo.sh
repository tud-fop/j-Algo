#!/bin/sh
exec java -Djava.library.path=extlibs -Djava.ext.dirs=runtime/modules -jar runtime/j-Algo.jar $*

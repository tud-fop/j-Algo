#!/bin/sh
exec java -Djava.library.path=extlibs/linux -Djava.ext.dirs=runtime/modules -jar runtime/jalgo.jar $*

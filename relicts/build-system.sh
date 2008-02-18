#!/bin/sh

if [ $# -ne 1 ]; then
    echo "Usage: $0 SYSTEM"
    exit 1
fi

SYSTEM=$1
TARGETDIR=jalgo-$SYSTEM
TMP=`pwd`/tmp
MANIFEST=manifest.tmp

echo "Cleaning up..."
rm -dRf $TMP
mkdir $TMP
rm -dRf $TARGETDIR
mkdir $TARGETDIR

echo "Compiling..."

( cd .. ; ant -q $SYSTEM ) || exit 1

echo "Populating tmp directory..."
cp -R ../bin/* $TMP

echo "Copying third party stuff into $TARGETDIR..."
mkdir $TARGETDIR/extlibs
cp ../extlibs/*.jar ../extlibs/$SYSTEM/* $TARGETDIR/extlibs
cp -R ../pix $TARGETDIR/
cp -R ../res $TARGETDIR/


echo "Removing CVS directories..."
find $TMP $TARGETDIR -type d -and -name CVS -print0 | xargs -0 rm -dR

echo "Creating Manifest..."
echo "Manifest-Version: 1.0" > $MANIFEST
echo "Main-Class: org.jalgo.main.Jalgo" >> $MANIFEST
echo -n "Class-Path:" >> $MANIFEST
for JAR in `( cd $TARGETDIR ; find extlibs -name "*.jar")` ; do
    echo -n " $JAR" >> $MANIFEST
done
echo "" >> $MANIFEST

echo "Creating JAR..."
jar cfm $TARGETDIR/jalgo.jar $MANIFEST -C $TMP/ .

echo "Writing start script..."
if [ $SYSTEM = windows ]; then
    START=$TARGETDIR/start.bat
    echo 'javaw -Djava.library.path=extlibs -jar jalgo.jar' > $START
    echo "Creating ZIP archive for winy windows users..."
    zip -r $TARGETDIR $TARGETDIR
else
    START=$TARGETDIR/start.sh
    echo '#!/bin/sh' > $START
    echo 'java -Djava.library.path=extlibs/ -jar jalgo.jar $*' >> $START
    chmod +x $START
    echo "Creating tarball..."
    tar czf $TARGETDIR.tgz $TARGETDIR
fi


echo "Cleaning up (again)..."
rm -dRf $TMP
rm $MANIFEST


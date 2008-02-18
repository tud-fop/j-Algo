#!/bin/sh
# $Id: build-all.sh,v 1.1 2005/06/23 09:22:58 jalgosequoia Exp $

for SYSTEM in freebsd linux windows macosx; do
    echo "Building for $SYSTEM..."
    ./build-system.sh $SYSTEM
    rm -dR jalgo-$SYSTEM
    echo "Building complete for $SYSTEM"
    echo ""
done

echo "Computing checksums..."
openssl sha1 *.tgz *.zip > CHECKSUMS


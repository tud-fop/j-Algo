#!/bin/bash
find ./svg -iname "*.svg" | while read file;
	do
		inkscape -f $file -e ${file%%.svg}.png;
		inkscape -f $file -e ${file%%.svg}16.png -h 16;
		mv ./svg/*.png .;
	done;

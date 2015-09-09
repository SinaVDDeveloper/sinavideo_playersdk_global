#!/bin/sh
#synchronize the dev and trunk content 
if [ ! -d "../sinavideo_playersdk_v2.0/" ]; then
	echo "trunk dir must be ../sinavideo_playersdk_v2.0/"
	exit
fi
find .|grep -v ".svn"|grep -v "bin"|grep -v "gen" |grep -v "local.properties"|grep -v "project.properties"|grep -E ".java|.png|.xml|.docx|.properties"|awk '{print "cp -rf "$0" ../sinavideo_playersdk_v2.0/"$0}'|sh

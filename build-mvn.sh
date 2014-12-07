


# load build_utils.sh script
if [ -e build_utils.sh ]; then
	source ./build_utils.sh
elif [ -e /usr/local/bin/pxn/build_utils.sh ]; then
	source /usr/local/bin/pxn/build_utils.sh
else
	wget https://raw.githubusercontent.com/PoiXson/shellscripts/master/pxn/build_utils.sh \
		|| exit 1
	source ./build_utils.sh
fi



# replace version in pom files
sedresult=0
sedVersion "${PWD}/pom.xml"           || sedresult=1
sedVersion "${PWD}/java/pom.xml"      || sedresult=1
sedVersion "${PWD}/www/pom.xml"       || sedresult=1
sedVersion "${PWD}/ItemPacks/pom.xml" || sedresult=1
sedVersion "${PWD}/java/plugin.yml"   || sedresult=1
sedVersion "${PWD}/www/WebInterface/index.php" || sedresult=1



if [ $sedresult != 0 ]; then
	echo "Failed to sed the pom files!"
else
	# build
	mvn clean install
	buildresult=$?
fi



# return the pom files
mvresult=0
restoreSed "${PWD}/pom.xml"           || mvresult=1
restoreSed "${PWD}/java/pom.xml"      || mvresult=1
restoreSed "${PWD}/www/pom.xml"       || mvresult=1
restoreSed "${PWD}/ItemPacks/pom.xml" || mvresult=1
restoreSed "${PWD}/java/plugin.yml"   || mvresult=1
restoreSed "${PWD}/www/WebInterface/index.php" || mvresult=1



# results
if [ $sedresult != 0 ]; then
	exit 1
fi
if [ $buildresult != 0 ]; then
	echo "Build has failed!"
	exit 1
fi
if [ $mvresult != 0 ]; then
	echo "Failed to return an original pom.xml file!"
	exit 1
fi



cp -fv "${PWD}/target/WebAuctionPlus-"*.zip  "${PWD}"



newline
ls -lh "${PWD}/WebAuctionPlus-"*.zip
newline


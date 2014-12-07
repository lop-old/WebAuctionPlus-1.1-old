# (cd ${WORKSPACE}/; sh build-ci.sh)



echo "Pre-build Cleanup.."
rm -fv "${WORKSPACE}/WebAuctionPlus-"*.zip



echo "Build.."
( cd "${WORKSPACE}/" && sh build-mvn.sh --build-number ${BUILD_NUMBER} ) || exit 1



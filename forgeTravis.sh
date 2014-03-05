#!/bin/bash
case "$1" in
setup)
  wget http://mirrors.kernel.org/ubuntu/pool/universe/m/mozjs17/libmozjs-17.0-bin_17.0.0-0ubuntu1_amd64.deb
  wget http://security.ubuntu.com/ubuntu/pool/main/n/nspr/libnspr4_4.9.5-1ubuntu1.1_amd64.deb
  sudo dpkg -i libnspr4_4.9.5-1ubuntu1.1_amd64.deb
  sudo dpkg -i libmozjs-17.0-bin_17.0.0-0ubuntu1_amd64.deb
  sudo ln -sT /usr/bin/js17 /usr/bin/js
  curl -L http://github.com/micha/jsawk/raw/master/jsawk > jsawk
  sudo mv jsawk /usr/bin
  sudo chmod +x /usr/bin/jsawk
  ;;
build)
  if [ "$TRAVIS" = true ] ; then
    mc_version=$TRAVIS_BRANCH
  else
    mc_version=`git rev-parse --abbrev-ref HEAD`
    forge_release=latest
  fi
  forge_version="$mc_version-$forge_release"
  wget 'http://files.minecraftforge.net/maven/net/minecraftforge/forge/json'
  forge_build_num=$(cat json | jsawk "return this.promos" | sed -e "s/\"//g" -e "s/{//" -e "s/}//" -e "s/,/\n/g" | grep $forge_version | cut -d':' -f2)
  forge_version_number=$(cat json | jsawk "return this.number" | grep -oh "\"version\":\"[0-9]*.[0-9]*.[0-9]*.[0-9]*" | sed -e "s/\"//g" | cut -d':' -f2 | grep $forge_build_num)
  wget http://files.minecraftforge.net/maven/net/minecraftforge/forge/${mc_version}-${forge_version_number}/forge-${mc_version}-${forge_version_number}-src.zip
  unzip forge-${mc_version}-${forge_version_number}-src.zip -x "src/*"
  if [ "$TRAVIS" = true ] ; then
    sed -e 's/@@MOD_NAME@@/'"${MOD_NAME}"'/' -e 's/@@MOD_VERSION@@/'"${MOD_VERSION}"'/' -e 's/@@MOD_BUILD_NUM@@/'"${TRAVIS_BUILD_NUMBER}"'/' -e 's/@@MC_VERSION@@/'"${mc_version}"'/' Manifest.txt.in > Manifest.txt
    sed -e 's/@@MOD_NAME@@/'"${MOD_NAME}"'/' -e 's/@@MOD_VERSION@@/'"${MOD_VERSION}"'/' -e 's/@@MOD_BUILD_NUM@@/'"${TRAVIS_BUILD_NUMBER}"'/' -e 's/@@MC_VERSION@@/'"${mc_version}"'/' mcmod.info.in > src/main/resources/mcmod.info
  fi
  gradle build
  jar ufm build/libs/modid-1.0.jar Manifest.txt
  ;;
esac

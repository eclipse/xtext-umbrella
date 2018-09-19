#!/bin/bash
xargs_sed_inplace() {
	if [[ "$OSTYPE" == "darwin"* ]]; then
		xargs sed -i '' "$@"
	else
		xargs sed -i "$@" 
	fi	
}
from=2.15.0
to=2.16.0

# TODO make this work on mac too. needs sed -i '' ......... (e.g. use sed_inplace from above)

find ../ -type f -name "MANIFEST.MF" | xargs_sed_inplace -e "s/${from}.qualifier/${to}.qualifier/g"
find ../ -type f -name "MANIFEST.MF" | xargs_sed_inplace -e "s/;version=\"${from}\"/;version=\"${to}\"/g"
find ../ -type f -name "MANIFEST.MF" | xargs_sed_inplace -e "s/org.eclipse.xtext.xbase.lib;bundle-version=\"${from}\"/org.eclipse.xtext.xbase.lib;bundle-version=\"${to}\"/g"
find ../ -type f -name "MANIFEST.MF" | xargs_sed_inplace -e "s/org.eclipse.xtend.lib;bundle-version=\"${from}\"/org.eclipse.xtend.lib;bundle-version=\"${to}\"/g"
find ../ -type f -name "MANIFEST.MF_gen" | xargs_sed_inplace -e "s/${from}.qualifier/${to}.qualifier/g"
find ../ -type f -name "pom.xml" | xargs_sed_inplace -e "s/${from}-SNAPSHOT/${to}-SNAPSHOT/g"
find ../ -type f -name "maven-pom.xml" | xargs_sed_inplace -e "s/${from}-SNAPSHOT/${to}-SNAPSHOT/g"
find ../ -type f -name "tycho-pom.xml" | xargs_sed_inplace -e "s/${from}-SNAPSHOT/${to}-SNAPSHOT/g"
find ../ -type f -name "versions.gradle" | xargs_sed_inplace -e "s/version = '${from}-SNAPSHOT'/version = '${to}-SNAPSHOT'/g"
find ../ -type f -name "feature.xml" | xargs_sed_inplace -e "s/version=\"${from}.qualifier\"/version=\"${to}.qualifier\"/g"
find ../ -type f -name "feature.xml" | xargs_sed_inplace -e "s/version=\"${from}\" match=\"equivalent\"/version=\"${to}\" match=\"equivalent\"/g"
find ../ -type f -name "category.xml" | xargs_sed_inplace -e "s/version=\"${from}.qualifier\"/version=\"${to}.qualifier\"/g"
find ../ -type f -name "plugin.xml" | xargs_sed_inplace -e "s/<version>${from}-SNAPSHOT<\/version>/<version>${to}-SNAPSHOT<\/version>/g"
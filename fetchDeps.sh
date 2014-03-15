#!/bin/bash
hg clone https://bitbucket.org/shedar/modstats
mv modstats/org src/main/java/org
rm -rf modstats
patch -p0 < ModsUpdateEvent.patch

#!/bin/bash

# Below has been commented as showing some errors while executing the tests. Needs to be uncommented
#./run_format_and_deps.sh

sbt Gatling/test -DrunLocal=true

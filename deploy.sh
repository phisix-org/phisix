#!/bin/bash
set -ev
if [ "${TRAVIS_PULL_REQUEST}" = "false" ]; then
  gcloud auth activate-service-account --key-file phisix-api4-66cbb1afd51b.json
  mvn clean package appengine:deploy -Pphisix-4

  gcloud auth activate-service-account --key-file phisix-api3-eebcc9b5960c.json
  mvn clean package appengine:deploy -Pphisix-3

  gcloud auth activate-service-account --key-file phisix-api2-0a8be5d4b178.json
  mvn clean package appengine:deploy -Pphisix-2

  gcloud auth activate-service-account --key-file phisix-api-f6d7427c49af.json
  mvn clean package appengine:deploy
fi

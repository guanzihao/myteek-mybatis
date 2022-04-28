#!/usr/bin/env bash

source /etc/profile

mvn clean install package org.apache.maven.plugins:maven-deploy-plugin:2.8:deploy -DskipTests --settings ~/.m2/aliyun.xml



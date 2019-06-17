#!/usr/bin/env bash

export BACKEND=https://web-news-backend.herokuapp.com
rm -rf dist/
parcel build index.html

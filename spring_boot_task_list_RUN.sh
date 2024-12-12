#!/bin/bash

REPO_URL="https://github.com/Aleksandr595959/Spring-Boot-TaskList"
CLONE_DIR="Spring-Boot-TaskList"

echo "Cloning repository from $REPO_URL..."
git clone $REPO_URL $CLONE_DIR

cd $CLONE_DIR

echo "Building and starting Docker containers..."
docker-compose up --build -d

echo "Removing the cloned repository..."
cd ..
rm -rf $CLONE_DIR

echo "Done."


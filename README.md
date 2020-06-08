# gRPC-Chat
![](https://travis-ci.com/DmiitriyJarosh/gRPC-Chat.svg?branch=master)
## Description
It is a small console peer-to-peer chat written with gRPC framework.
To run you can use command `java -jar RPCChat.jar <port> <ip>`. IP should be provided only for client mode.
## Build
To build chat from source run `gradle wrapper && ./gradlew installDist` in RPCChat directory.
## Example
![](https://github.com/DmiitriyJarosh/gRPC-Chat/raw/master/example.png)

## Docker
You can run chat inside Docker using provided Docker file.
For build Docker image: `sudo docker build . --tag chat:1.0`
For run Docker image: `sudo docker run -it -e ip=<ip for client mode> -e port=<port_number> -p <port_number>:<port_number> chat:1.0`

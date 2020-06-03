FROM ubuntu:18.04
EXPOSE 8000 
RUN apt-get update
RUN apt-get -y install default-jdk 
RUN apt-get -y install git
RUN git clone https://github.com/DmiitriyJarosh/gRPC-Chat.git
ENV ip ""
ENV port 8000
CMD cd gRPC-Chat && java -jar 1.jar ${port} ${ip} 

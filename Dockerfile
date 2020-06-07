FROM gradle:latest
RUN git clone https://github.com/DmiitriyJarosh/gRPC-Chat.git
ENV ip ""
ENV port 8000
RUN cd gRPC-Chat && gradle wrapper && ./gradlew installDist
CMD cd gRPC-Chat/build/libs && java -jar RPCChat.jar ${port} ${ip} 

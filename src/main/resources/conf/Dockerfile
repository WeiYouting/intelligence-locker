# 基础镜像使用java
FROM java:8
# 作者
MAINTAINER wyt
# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /tmp
# 将jar包添加到容器中并更名为mayikt.jar
ADD intelligence-locker-0.0.1-SNAPSHOT.jar wyt.jar
# 运行jar包
RUN bash -c 'touch /wyt.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/wyt.jar"]
##暴露8080端口
#EXPOSE 8080

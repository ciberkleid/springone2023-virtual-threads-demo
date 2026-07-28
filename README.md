# Java Virtual Threads using Spring Boot and Web MVC

This project showcases Spring Boot 3.2 support for Java virtual threads in Web MVC applications.
This demo uses Tomcat web server, but the same support is available for Jetty as well.

This application makes a blocking call to an endpoint that adds a delay before responding.

> Bonus: The demo also showcases another feature new in Spring Boot 3.2:
> [RestClient](com/example/demo/DemoController.java).

### Pre-requisites

1. Docker - for running the backend (httpbin) service
2. Java 21 - for virtual thread support in the JVM
3. `hey` CLI, or an alternative load test utility

### Instructions

Run the demo three times, using the three configuration options below.
You will observe improved performance and hardware utilization using virtual threads as compared to using platform threads in an imperative code base.

##### Test 1: Use traditional platform threads
- Open the file [application.properties](src/main/resources/application.properties).
- Make sure `spring.threads.virtual.enabled` is commented out or set to `false`.
- Set `server.tomcat.threads.max` to the number of cores on your machine.

##### Test 2: Use virtual threads
- Open the file [application.properties](src/main/resources/application.properties).
- You can ignore or comment out `server.tomcat.threads.max`.
- Set `spring.threads.virtual.enabled=true`.
- Open the file [DemoApplication.java](com/example/demo/DemoApplication.java).
- Set `jdk.virtualThreadScheduler.maxPoolSize` to the number of cores on your machine.

##### Test 3: Use virtual threads with less hardware
- Use the settings from Test 2 above.
- Set `jdk.virtualThreadScheduler.maxPoolSize` to `1`.

### Start the backend (httpbin)

Start the backend service in Docker, mapped to port 9091 (so it doesn't collide with the demo app's port 9090):
```shell
docker run -d --rm --name httpbin -p 9091:8080 mccutchen/go-httpbin
```

### Start demo app

```shell
./mvnw spring-boot:run
```

### Run a load test

Try a number of requests with delays (see options for `hey` to configure load test).
For example, simulate 20 clients sending a total of 60 requests, with a 3-second delay from the backend:
```shell
hey -c 20 -n 60 http://localhost:9090/block/3
```

The logs should show if you are using platform threads (e.g. `Thread[#19,http-nio-9090-exec-1,5,main]`)
or virtual threads (e.g. `VirtualThread[#36,tomcat-handler-0]/runnable@ForkJoinPool-1-worker-1`).

In either case, the `exec` or `worker` thread number should not exceed the limits you set in the configuration (i.e. the number of cores on your machine, or `1` in Test 3).

Compare the output of the load test.
You should observe that performance is significantly better with virtual threads, even if the application is provided less hardware.
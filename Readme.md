# Spring Boot + Jetty + Virtual Threads demo

## Benchmark

To run simple benchmark:

`wrk -c 200 -t 100 -d 60s --latency http://localhost:8080/horoscopes/weekly`

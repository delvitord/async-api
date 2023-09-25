# async-api
Repository ini menampung hasil pengerjaan untuk menyelesaikan study case async API. Dibuat oleh kelompok 2 kelas 3B mata kuliah Pengembangan Web, JTK Polban. 

# Case Study Description :: Asynchronous API

text

![Technology - Spring Boot](https://img.shields.io/badge/Technology-Spring_Boot-blue)
![Tracing Difficulty - None](https://img.shields.io/badge/Tracing_Difficulty-None-blue)
![Implementation Difficulty - Medium Hard](https://img.shields.io/badge/Implementation_Difficulty-Medium_Hard-orange)

## The Condition

You are developing an application where there is long running process on some endpoints. The process could take up to minutes or even hours.

## The Problem

Due to the long processing time, there is a possibility that a timeout or memory leak might occur within the application, or on the client side.

text
Process Request
    |
x minutes/hours   x-------x timeout
    |
Finished Response


## The Objective

You need to implement a mechanism, where the process could run in parallel, and the client is not required to wait for the response, but could fetch it another time.

text
Process Request ----> Result Request ----> Result Request
    |                       |                    |
x seconds               x seconds            x seconds
    |                       |                    |
Accepted Response   Unfinished Response  Finished Response
                        (repeat)


You could also implements Signaling or PubSub mechanism to achieve this.

text
Process Request
    |
x seconds
    |
Accepted Response

--- --- ---

Finished Signal -------------> Result Request
                                     |
                                 x seconds
                                     |
                              Finished Response


## The Expected Result

The long running API main call is now could responds within seconds, and the response could be fetched another time in the future.


# Yang Telah Kelompok Kami Lakukan

Blabla
cmake_minimum_required(VERSION 3.26)
project(VIS)

set(CMAKE_CXX_STANDARD 14)

# Shortcuts
set(PREP socket/cpp/Hello/src/main)
set(PrimC socket/cpp/Primitive_SocketClient/src/main)
set(PrimS socket/cpp/Primitive_SocketServer/src/main)
set(DemoS socket/cpp/Demo_SocketServer/src/main)
#set(EcoS socket/cpp/Echo_SocketServer/src/main)
#set(UdpC socket/cpp/Udp_SocketClient/src/main)
#set(UdpS socket/cpp/Udp_SocketServer/src/main)
#set(Ipv6C socket/cpp/Ipv6_SocketClient/src/main)
#set(Ipv6S socket/cpp/Ipv6_SocketServer/src/main)
#set(EnvS socket/cpp/Environment_SocketServer/src/main)
#set(ResS socket/cpp/Resource_SocketServer/src/main)
#set(ResC socket/cpp/Resource_SocketClient/src/main)

# Executable inkl. beteiligte Sourcen
add_executable(Hello
        ${PREP}/cpp/main.cpp
        ${PREP}/cpp/Foo.cpp
        ${PREP}/headers/Foo.h)

# Exercise 1.1 a, b, c
add_executable(Primitive_SocketServer
        ${PrimS}/cpp/Primitive_SocketServer.cpp)
add_executable(Primitive_SocketClient
        ${PrimC}/cpp/Primitive_SocketClient.cpp)

add_executable(Demo_SocketServer
        ${DemoS}/cpp/Demo_SocketServer.cpp)

# Exercise 1.2 a
#add_executable(Echo_SocketServer
#        ${EcoS}/cpp/Echo_SocketServer.cpp
#        ${EcoS}/headers/Echo_SocketServer.h)

# Exercise 1.2 b
#add_executable(Udp_SocketServer
#        ${UdpS}/cpp/Udp_SocketServer.cpp
#        ${UdpS}/headers/Udp_SocketServer.h)
#add_executable(Udp_SocketClient
#        ${UdpC}/cpp/Udp_SocketClient.cpp
#        ${UdpC}/headers/Udp_SocketClient.h)

# Exercise 1.2 c
#add_executable(Ipv6_SocketServer
#        ${Ipv6S}/cpp/Ipv6_SocketServer.cpp
#        ${Ipv6S}/headers/Ipv6_SocketServer.h)
#add_executable(Ipv6_SocketClient
#        ${Ipv6C}/cpp/Ipv6_SocketClient.cpp
#        ${Ipv6C}/headers/Ipv6_SocketClient.h)

# Exercise 1.2 d and 1.3 a
#add_executable(Environment_SocketServer
#        ${EnvS}/cpp/Environment_SocketServer.cpp
#        ${EnvS}/cpp/EnvironmentService.cpp
#        ${EnvS}/headers/Environment_SocketServer.h
#        ${EnvS}/headers/EnvironmentService.h)

# Exercise 1.4 -- BONUS --
#add_executable(Resource_SocketServer
#        ${ResS}/cpp/Resource_SocketServer.cpp
#        ${ResS}/cpp/SharedResource_Polling.cpp
#        ${ResS}/cpp/SharedResource_Semaphore.cpp
#        ${ResS}/cpp/SharedResource_Signal.cpp
#        ${ResS}/headers/Resource_SocketServer.h
#        ${ResS}/headers/SharedResource_Polling.h
#        ${ResS}/headers/SharedResource_Semaphore.h
#        ${ResS}/headers/SharedResource_Signal.h
#        ${ResS}/headers/ISharedResource.h)
#add_executable(Resource_SocketClient
#        ${ResC}/cpp/Resource_SocketClient.cpp
#        ${ResC}/headers/Resource_SocketClient.h)

# Header Files announce
target_include_directories(Hello PRIVATE ${PREP}/headers)
#target_include_directories(Echo_SocketServer PRIVATE ${EcoS}/headers)
#target_include_directories(Udp_SocketServer PRIVATE ${UdpS}/headers)
#target_include_directories(Udp_SocketClient PRIVATE ${UdpC}/headers)
#target_include_directories(Ipv6_SocketServer PRIVATE ${Ipv6S}/headers)
#target_include_directories(Ipv6_SocketClient PRIVATE ${Ipv6C}/headers)
#target_include_directories(Environment_SocketServer PRIVATE ${EnvS}/headers)
#target_include_directories(Resource_SocketServer PRIVATE ${ResS}/headers)
#target_include_directories(Resource_SocketClient PRIVATE ${ResC}/headers)


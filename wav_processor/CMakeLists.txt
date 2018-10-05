cmake_minimum_required(VERSION 2.8)
project(wav_processor)



add_definitions(-D__STDC_LIMIT_MACROS)
add_definitions(-D__STDC_CONSTANT_MACROS)

cmake_policy(SET CMP0015 NEW)

set(CMAKE_CXX_COMPILER "/usr/bin/g++")
set(CMAKE_C_COMPILER "/usr/bin/gcc")
set(CMAKE_CXX_FLAGS "-ggdb -std=c++11")


include_directories(
    ./
    src
    include
)



file(GLOB WAV_PROCESSOR_TEST_SRC *.cpp)

add_subdirectory(src)


#find_package(Threads REQUIRED)

find_library(SCTP sctp)
find_library(RT rt)


set(WAV_PROCESSOR_TEST_OBJ_LIBS $<TARGET_OBJECTS:src>)



set(SO_LIBS ${CMAKE_THREAD_LIBS_INIT}
    ${SCTP}
    ${RT})

add_executable(${PROJECT_NAME} ${WAV_PROCESSOR_TEST_SRC} ${WAV_PROCESSOR_TEST_OBJ_LIBS})
target_link_libraries(${PROJECT_NAME} ${SO_LIBS})

set(LIB src)

include_directories(
    ../include
)

file(GLOB SRC *.cpp)

add_library(${LIB} OBJECT ${SRC})

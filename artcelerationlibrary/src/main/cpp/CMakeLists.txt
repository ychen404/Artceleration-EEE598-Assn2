cmake_minimum_required(VERSION 3.4.1)

# build cpufeatures as a static lib
add_library(cpufeatures STATIC
            ${ANDROID_NDK}/sources/android/cpufeatures/cpu-features.c)

# build app's shared lib

# set up neon build flag for file using intrinsics
# name: helloneon-intrinsics.c (It is named EXACTLY as this on disk,
#                              just like a normal source file)
# then set up neon flag for neon files
# [This example only build for armeabi-v7a, x86 could be done the same way]
#
if (${ANDROID_ABI} STREQUAL "armeabi-v7a")
  # make a list of neon files and add neon compiling flags to them
  set(neon_SRCS helloneon-intrinsics.c)

  set_property(SOURCE ${neon_SRCS}
               APPEND_STRING PROPERTY COMPILE_FLAGS " -mfpu=neon")
  add_definitions("-DHAVE_NEON=1")
else ()
  set(neon_SRCS)
endif ()

#add_library(hello-neon SHARED
#            helloneon.c
#            ${neon_SRCS})

add_library(my-native-lib SHARED
            helloneon.c
            native-lib.cpp
            ${neon_SRCS})

find_library( # Sets the name of the path variable.
              log-lib
                # Specifies the name of the NDK library that
                # you want CMake to locate.
               log )

target_include_directories(my-native-lib PRIVATE
                           ${ANDROID_NDK}/sources/android/cpufeatures)

target_link_libraries(my-native-lib
                      android
                      cpufeatures
                      log
                      ${log-lib})
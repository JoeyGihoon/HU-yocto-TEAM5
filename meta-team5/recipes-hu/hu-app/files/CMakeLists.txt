cmake_minimum_required(VERSION 3.16)

project(TEAM5_HU VERSION 0.1 LANGUAGES CXX)

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON) 
set(CMAKE_AUTOUIC ON)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# 1) Qt5 컴포넌트 찾기 (필요한 모듈 추가 가능)
find_package(Qt5 5.15 REQUIRED COMPONENTS
  Core
  Gui
  Qml
  Quick
  QuickControls2
  WebEngine
  WebChannel
)

# ===== CommonAPI / vsomeip libs =====
find_package(PkgConfig REQUIRED)

pkg_check_modules(COMMONAPI REQUIRED CommonAPI)
pkg_check_modules(COMMONAPI_SOMEIP REQUIRED CommonAPI-SomeIP)
pkg_check_modules(VSOMEIP REQUIRED vsomeip3)

# ===== Generated sources (정적 포함 핵심) =====
set(COMMUNICATION_SRCGEN_DIR "${CMAKE_SOURCE_DIR}/src-gen")

file(GLOB_RECURSE GEN_CORE_SRCS
    "${COMMUNICATION_SRCGEN_DIR}/core/v0/commonapi/*.cpp"
)

file(GLOB_RECURSE GEN_SOMEIP_SRCS
    "${COMMUNICATION_SRCGEN_DIR}/someip/v0/commonapi/*.cpp"
)

# 2) 실행 파일
add_executable(appTEAM5_HU
    main.cpp
    mapController.h
    mapcontroller.cpp
    weather.cpp
    weather.h
    youtubeController.cpp
    youtubeController.h
    imageExtractor.cpp
    imageExtractor.h
    usbscanner.cpp
    usbscanner.h
    GearController.h
    GearController.cpp
    qml.qrc

    ${GEN_CORE_SRCS}
    ${GEN_SOMEIP_SRCS}
)

# 3) VSOME/IP 관련 설정
set(COMMUNICATION_SRCGEN_DIR "${CMAKE_SOURCE_DIR}/src-gen")
target_include_directories(appTEAM5_HU
  PRIVATE
    ${COMMUNICATION_SRCGEN_DIR}/core
    ${COMMUNICATION_SRCGEN_DIR}/someip
    ${COMMONAPI_INCLUDE_DIRS}
    ${COMMONAPI_SOMEIP_INCLUDE_DIRS}
    ${VSOMEIP_INCLUDE_DIRS}
)


# pkg-config가 준 컴파일 옵션도 반영(경고/define 등)
target_compile_options(appTEAM5_HU PRIVATE
    ${COMMONAPI_CFLAGS_OTHER}
    ${COMMONAPI_SOMEIP_CFLAGS_OTHER}
    ${VSOMEIP_CFLAGS_OTHER}
)


# 4) 링크
target_link_libraries(appTEAM5_HU
    PRIVATE
      Qt5::Core
      Qt5::Gui
      Qt5::Qml
      Qt5::Quick
      Qt5::QuickControls2
      Qt5::WebEngine
      Qt5::WebChannel

      ${COMMONAPI_LIBRARIES}
      ${COMMONAPI_SOMEIP_LIBRARIES}
      ${VSOMEIP_LIBRARIES}
      pthread
)

# 5) 설치 규칙 (옵션)
include(GNUInstallDirs)
install(TARGETS appTEAM5_HU
    BUNDLE DESTINATION .
    LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
    RUNTIME DESTINATION ${CMAKE_INSTALL_BINDIR}
)

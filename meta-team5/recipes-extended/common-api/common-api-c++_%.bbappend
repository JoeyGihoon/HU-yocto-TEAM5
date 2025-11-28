# patch 대신 sed로 include <string> 삽입 (patch-fuzz 회피)

do_configure:append() {
    TYPES_HPP="${S}/include/CommonAPI/Types.hpp"

    # 이미 <string> 있으면 스킵
    if ! grep -q "^#include <string>" ${TYPES_HPP}; then
        # 헤더 가드 바로 아래에 넣는 방식(아주 안전)
        # 첫 번째 #define 줄 다음에 삽입
        sed -i '/^#define COMMONAPI_TYPES_HPP_/a #include <string>' ${TYPES_HPP}
    fi
}

# (선택) 구버전 + 최신 GCC 경고 대비
EXTRA_OECMAKE:append = " -DCMAKE_CXX_STANDARD=14 -DCMAKE_CXX_STANDARD_REQUIRED=ON "
CXXFLAGS:append = " -Wno-error=deprecated-declarations -Wno-error=deprecated "
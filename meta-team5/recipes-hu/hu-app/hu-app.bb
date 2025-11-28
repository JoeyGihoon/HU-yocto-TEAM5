SUMMARY = "Head Unit Qt App"
LICENSE = "CLOSED"

SRC_URI = "file://."
S = "${WORKDIR}"

inherit cmake_qt5
# Qt5 예시 DEPENDS:
DEPENDS += "qtbase qtbase-native qtdeclarative qtmultimedia qtquickcontrols2 qtwayland qtwebengine qtgraphicaleffects"

EXTRA_OECMAKE += " \
    -DCMAKE_AUTOMOC=ON -DCMAKE_AUTORCC=ON -DCMAKE_AUTOUIC=ON \
"

DEPENDS += " common-api-c++ common-api-c++-someip vsomeip "
DEPENDS += " pkgconfig-native "
RDEPENDS:${PN} += " common-api-c++ common-api-c++-someip vsomeip hu-config"

FILES:${PN} += "/lib/systemd/system/hu-app.service \
                ${bindir}/appTEAM5_HU \
                "
RDEPENDS:${PN} += " \
  qtbase qtdeclarative qtmultimedia qtquickcontrols2 qtwayland qtwebengine qtgraphicaleffects\
"

# systemd
inherit systemd
SRC_URI += "file://hu-app.service"
SYSTEMD_SERVICE:${PN} = "hu-app.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/appTEAM5_HU ${D}${bindir}/

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/hu-app.service ${D}${systemd_system_unitdir}/
}

do_configure:append() {
    if [ -d ${S}/src-gen ]; then

        # 2) CommonAPI/ICProxyBase.hpp → CommonAPI/SomeIP/ProxyBase.hpp 로 치환
        find ${S}/src-gen -type f \( -name "*.hpp" -o -name "*.h" \) \
            -exec sed -i 's#<CommonAPI/ICProxyBase.hpp>#<CommonAPI/SomeIP/ProxyBase.hpp>#g' {} \;

        # 3) 혹시 일부 generator는 .h일 수도 있으므로 확장 치환
        find ${S}/src-gen -type f \( -name "*.hpp" -o -name "*.h" \) \
            -exec sed -i 's#\"CommonAPI/ICProxyBase.hpp\"#\"CommonAPI/SomeIP/ProxyBase.hpp\"#g' {} \;
    fi
}
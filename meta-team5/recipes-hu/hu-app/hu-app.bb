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

FILES:${PN} += "/lib/systemd/system/hu-app.service \
                ${bindir}/appTEAM5_HU"
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

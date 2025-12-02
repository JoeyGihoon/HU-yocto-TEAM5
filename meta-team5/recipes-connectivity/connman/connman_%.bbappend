FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

inherit systemd

SYSTEMD_SERVICE:${PN} = "connman.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

# 우리가 만든 main.conf 를 소스에 추가
SRC_URI:append = " file://main.conf"

do_install:append() {
    # /etc/connman/main.conf 설치
    install -d ${D}${sysconfdir}/connman
    install -m 0644 ${WORKDIR}/main.conf ${D}${sysconfdir}/connman/main.conf
}

SUMMARY = "Team5 setup script and service"
LICENSE = "CLOSED"

SRC_URI = " \
    file://team5-setup.service \
    file://team5-setup.sh \
"

inherit systemd

SYSTEMD_SERVICE:${PN} = "team5-setup.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install() {
    # systemd 서비스
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/team5-setup.service ${D}${systemd_system_unitdir}

    # 셸 스크립트 -> /usr/sbin (Yocto에서는 ${sbindir})
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/team5-setup.sh ${D}${sbindir}/team5-setup.sh
}

# 아래 줄은 **있으면** 그대로 두고, 없으면 굳이 안 써도 됨
# (기본 FILES:${PN}에 sbindir는 포함이라 사실 없어도 됨)
FILES:${PN} += " \
    ${systemd_system_unitdir}/team5-setup.service \
    ${sbindir}/team5-setup.sh \
"
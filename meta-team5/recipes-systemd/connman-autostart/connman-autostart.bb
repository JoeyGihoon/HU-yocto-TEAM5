SUMMARY = "Force ConnMan to start at boot"
DESCRIPTION = "Ensures that connman.service is explicitly started during boot, \
even if other systemd targets do not depend on it."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
SRC_URI = "file://connman-autostart.service"

S = "${WORKDIR}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "connman-autostart.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

FILES:${PN} += "${systemd_system_unitdir}/connman-autostart.service"

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/connman-autostart.service ${D}${systemd_system_unitdir}/
}

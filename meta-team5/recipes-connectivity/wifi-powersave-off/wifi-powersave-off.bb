SUMMARY = "Disable Wi-Fi power saving on wlan0 at boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit systemd

SRC_URI = "file://wifi-powersave-off.service"

S = "${WORKDIR}"

SYSTEMD_SERVICE:${PN} = "wifi-powersave-off.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

FILES:${PN} += " ${systemd_system_unitdir}/wifi-powersave-off.service "

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/wifi-powersave-off.service ${D}${systemd_system_unitdir}/
}

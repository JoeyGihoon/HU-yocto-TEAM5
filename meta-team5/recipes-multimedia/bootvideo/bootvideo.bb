SUMMARY = "Play boot video before HU app"
LICENSE = "CLOSED"

SRC_URI = " \
    file://videoplay.service \
    file://videoplay.mp4 \
"

S = "${WORKDIR}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "videoplay.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/videoplay.service ${D}${systemd_system_unitdir}/

    install -d ${D}/usr/share/bootvideo
    install -m 0644 ${WORKDIR}/videoplay.mp4 ${D}/usr/share/bootvideo/
}

FILES:${PN} += "${systemd_system_unitdir}/videoplay.service /usr/share/bootvideo/videoplay.mp4"
RDEPENDS:${PN} += " \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-bad \
    gstreamer1.0-libav \
    gst-player \
"

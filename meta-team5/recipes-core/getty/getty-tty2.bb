SUMMARY = "Enable getty on tty2"
LICENSE = "CLOSED"

inherit allarch

RDEPENDS:${PN} += "systemd"

do_install() {
    # /etc/systemd/system/getty.target.wants 디렉토리 생성
    install -d ${D}${sysconfdir}/systemd/system/getty.target.wants

    # getty@tty2.service → /lib/systemd/system/getty@.service 로 symlink 생성
    ln -sf /lib/systemd/system/getty@.service \
        ${D}${sysconfdir}/systemd/system/getty.target.wants/getty@tty2.service
}

FILES:${PN} += "${sysconfdir}/systemd/system/getty.target.wants/getty@tty2.service"
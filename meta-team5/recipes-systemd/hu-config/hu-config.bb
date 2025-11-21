SUMMARY = "VSOMEIP + CommonAPI config files"
LICENSE = "MIT"

SRC_URI += " \
    file://vsomeip.json \
    file://commonapi-someip.ini \
"

S = "${WORKDIR}"

do_install() {
    # vsomeip 설정
    install -d ${D}/etc/vsomeip
    install -m 0644 ${WORKDIR}/vsomeip.json ${D}/etc/vsomeip/vsomeip.json

    # commonapi 설정
    install -m 0644 ${WORKDIR}/commonapi-someip.ini ${D}/etc/commonapi-someip.ini
}

FILES:${PN} += " \
    /etc/vsomeip/vsomeip.json \
    /etc/commonapi-someip.ini \
"
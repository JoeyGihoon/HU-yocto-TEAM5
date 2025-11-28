SUMMARY = "VSOMEIP + CommonAPI config files"
LICENSE = "CLOSED"

SRC_URI += " \
    file://HU.json \
    file://commonapi-someip.ini \
"

S = "${WORKDIR}"

do_install() {
    # SOME/IP 설정
    install -d ${D}/opt/HU_someip/json
    install -m 0644 ${WORKDIR}/HU.json \
        ${D}/opt/HU_someip/json/HU.json

    # CommonAPI 설정
    install -d ${D}/opt/HU_someip
    install -m 0644 ${WORKDIR}/commonapi-someip.ini \
        ${D}/opt/HU_someip/commonapi-someip.ini
}

FILES:${PN} += " \
    /opt/HU_someip/ \
"
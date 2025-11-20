inherit systemd

SYSTEMD_SERVICE:${PN} = "connman.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

SUMMARY = "Team5 HU image (Raspberry Pi 4)"
LICENSE = "CLOSED"

inherit core-image

require recipes-graphics/images/core-image-weston.bb

IMAGE_INSTALL:append = " \
  qtbase qtdeclarative qtmultimedia qtquickcontrols2 qtwayland \
  gstreamer1.0 gstreamer1.0-plugins-base \
  gstreamer1.0-plugins-good gstreamer1.0-plugins-bad \
  gstreamer1.0-libav gst-player \
  weston weston-init \
  wpa-supplicant connman connman-client linux-firmware \
  hu-app bootvideo getty-tty2 \
  linux-firmware-bcm43430 linux-firmware-bcm43455 wpa-supplicant iw dhcpcd \
  team5-wifi-config \
  openssl ca-certificates \
  connman-autostart \
  hu-config tzdata team5-setup\
"

IMAGE_INSTALL:remove = " wpa-supplicant"

IMAGE_FEATURES += "splash ssh-server-openssh weston"

# 루트FS 만들어진 다음에 resolv.conf를 connman용으로 덮어쓰기
ROOTFS_POSTPROCESS_COMMAND:append = " fix_resolv_conf;"

fix_resolv_conf () {
    # ${sysconfdir} 는 보통 /etc
    rm -f ${IMAGE_ROOTFS}${sysconfdir}/resolv.conf
    ln -s /run/connman/resolv.conf ${IMAGE_ROOTFS}${sysconfdir}/resolv.conf
}

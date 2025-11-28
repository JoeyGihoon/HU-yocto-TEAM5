SUMMARY = "Provision ConnMan with pre-defined Wi-Fi credentials"
DESCRIPTION = "Installs a provisioning file in /var/lib/connman so that ConnMan \
automatically connects to the configured Wi-Fi network on first boot."
LICENSE = "MIT"

inherit allarch

TEAM5_WIFI_CONFIG_NAME ?= "team5-wifi"
TEAM5_WIFI_SSID ?= "SEA:ME WiFi Access"
TEAM5_WIFI_PASSPHRASE ?= "1fy0u534m3"
TEAM5_WIFI_SECURITY ?= "psk"
TEAM5_WIFI_AUTOCONNECT ?= "true"
TEAM5_WIFI_HIDDEN ?= "false"
TEAM5_WIFI_DEVICE ?= "wlan0"
TEAM5_WIFI_IPV4 ?= "dhcp"
TEAM5_WIFI_IPV6 ?= "auto"
# Extra lines appended verbatim to the generated provisioning file
TEAM5_WIFI_EXTRA ?= ""

do_install[dirs] = "${D}/var/lib/connman"

python do_install () {
    import os

    dvar = d.getVar
    ssid = (dvar('TEAM5_WIFI_SSID') or '').strip()
    passwd = (dvar('TEAM5_WIFI_PASSPHRASE') or '').strip()

    if not ssid or not passwd:
        bb.warn('TEAM5_WIFI_SSID / TEAM5_WIFI_PASSPHRASE not set; skipping Wi-Fi provisioning file')
        return

    config_dir = os.path.join(dvar('D'), 'var', 'lib', 'connman')
    bb.utils.mkdirhier(config_dir)

    config_name = (dvar('TEAM5_WIFI_CONFIG_NAME') or 'team5-wifi').strip()
    config_path = os.path.join(config_dir, "%s.config" % config_name)

    contents = """[service_{name}]
Type = wifi
Name = {ssid}
Security = {security}
Passphrase = {passphrase}
Favorite = true
AutoConnect = {autoconnect}
Hidden = {hidden}
DeviceName = {device}
IPv4 = {ipv4}
IPv6 = {ipv6}
""".format(
        name=config_name,
        ssid=ssid,
        security=dvar('TEAM5_WIFI_SECURITY') or 'psk',
        passphrase=passwd,
        autoconnect=dvar('TEAM5_WIFI_AUTOCONNECT') or 'true',
        hidden=dvar('TEAM5_WIFI_HIDDEN') or 'false',
        device=dvar('TEAM5_WIFI_DEVICE') or 'wlan0',
        ipv4=dvar('TEAM5_WIFI_IPV4') or 'dhcp',
        ipv6=dvar('TEAM5_WIFI_IPV6') or 'auto',
    )

    extra = (dvar('TEAM5_WIFI_EXTRA') or '').strip()
    if extra:
        contents = contents + extra + "\n"

    with open(config_path, 'w', encoding='utf-8') as config_file:
        config_file.write(contents)

    os.chmod(config_path, 0o600)
}

FILES:${PN} = "/var/lib/connman/*.config"
ALLOW_EMPTY:${PN} = "1"

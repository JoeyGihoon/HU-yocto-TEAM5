#!/bin/sh
set -e

echo "[team5-setup] rfkill unblock all"
/usr/sbin/rfkill unblock all || true

echo "[team5-setup] set timezone"
/usr/bin/timedatectl set-timezone Europe/Berlin || true

echo "[team5-setup] configure eth0 static ip"
/sbin/ip addr flush dev eth0 || true
/sbin/ip addr add 192.168.10.2/24 dev eth0 || true
/sbin/ip link set eth0 up || true

echo "[team5-setup] ensure connman is running"
if ! /bin/systemctl is-active --quiet connman.service; then
    /bin/systemctl start connman.service || true
fi

# connman이 뜰 시간을 약간 준다 (최대 5초 대기)
for i in 1 2 3 4 5; do
    if /bin/systemctl is-active --quiet connman.service; then
        break
    fi
    sleep 1
done

echo "[team5-setup] enable wifi via connmanctl (idempotent)"
if command -v /usr/bin/connmanctl >/dev/null 2>&1; then
    /usr/bin/connmanctl enable wifi || true
fi

echo "[team5-setup] restart connman to apply wifi"
/bin/systemctl restart connman.service || true

echo "[team5-setup] done."
exit 0

# SEA-ME-Head-Unit

## 1. Project Summary

SEA-ME Head Unit is a Qt/QML-based infotainment system.  
This repository integrates both the Head Unit application and the embedded Yocto build environment, enabling the system to run on embedded hardware such as the Raspberry Pi.

The project provides core in-vehicle infotainment features—including map display, media playback, weather information, USB content scanning, and image extraction—through a modular C++/QML architecture.

In this version, the previous **Instrument Cluster (IC)** project has been **rebuilt on a Yocto-based embedded Linux environment** and unified with the Head Unit system, allowing both components to share the same build pipeline and operate cohesively within the SEA-ME platform.

Overall, the goal of this project is to offer a unified development environment for building, testing, and deploying an automotive-grade Head Unit UI that can seamlessly integrate with the Instrument Cluster and vehicle data systems in the SEA-ME platform.

## 2. Architecture

<img width="1280" height="800" alt="System_Architecture2" src="https://github.com/user-attachments/assets/479ad51e-015f-44c6-aec5-2846765896cc" />


## 3. Build & Installation
This system must be built on an Ubuntu 22 environment.
```bash
git clone https://github.com/JoeyGihoon/SEA-ME-Head-Unit.git
```

### 3.1 Common Setup

Before building images, configure your Wi-Fi settings in wpa_supplicant.conf-sane:

```
network={
    ssid="Your Wifi"
    psk="Your password"
    key_mgmt=Your Authentication
}
```

File location:
SEA-ME-Head-Unit/IC_team5/yocto/poky/meta/recipes-connectivity/wpa-supplicant/wpa-supplicant/wpa_supplicant.conf-sane

## 3. Function
<p align="center">
  <img src="./assets/main.png" width="23%">
  <img src="./assets/youtube.png" width="23%">
  <img src="./assets/weather.png" width="23%">
  <img src="./assets/map.png" width="23%">
</p>




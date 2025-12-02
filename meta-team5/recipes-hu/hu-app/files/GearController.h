// #pragma once
// #include <QObject>
// #include <QString>
// #include <memory>
// #include <string>
// #include <iostream>

// #include <CommonAPI/CommonAPI.hpp>
// #include <v0/commonapi/ICProxy.hpp>

// class GearController : public QObject {
//     Q_OBJECT

// public:
//     explicit GearController(std::shared_ptr<v0::commonapi::ICProxy<>> proxy,
//                             QObject* parent=nullptr);

//     // HU -> IC: 기어 변경 요청
//     Q_INVOKABLE void setGear(const QString& gear);

// signals:
//     void gearChanged(const QString& gear);

// private:
//     std::shared_ptr<v0::commonapi::ICProxy<>> proxy_;
// };


#pragma once
#include <QObject>
#include <QUdpSocket>

class GearController : public QObject {
    Q_OBJECT
public:
    explicit GearController(QObject* parent = nullptr);

    // QML에서 호출할 함수
    Q_INVOKABLE void setGear(const QString& gear);

    // 필요하면 런타임에 IP/포트 변경
    Q_INVOKABLE void setPeer(const QString& ip, int huListenPort, int icListenPort);

signals:
    // IC -> HU : IC에서 온 기어 상태를 QML에 알려주기 위한 신호
    void gearReceived(const QString& gear);

private:
    QUdpSocket socket_;
    // 상대(IC)의 리슨 포트 (HU -> IC 용)
    QString icIp_      = "192.168.10.1"; // IC IP
    quint16 icPort_    = 4000;           // IC 리슨 포트

    // 내(HU)의 리슨 포트 (IC -> HU 용)
    quint16 huPort_    = 5000;           // HU 리슨 포트

    void handleReadyRead();
};
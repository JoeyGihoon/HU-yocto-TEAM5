// #include "GearController.h"

// GearController::GearController(std::shared_ptr<v0::commonapi::ICProxy<>> proxy,
//                                QObject* parent)
//     : QObject(parent)
//     , proxy_(std::move(proxy)) {

//     if (proxy_) {
//         // IC -> HU : 기어 상태 변경 브로드캐스트 구독
//         proxy_->getGearStatusChangedEvent().subscribe(
//             [this](const std::string& gearValue) {

//                 // CommonAPI 스레드에서 호출될 수 있으니 Qt 스레드로 넘겨줌
//                 QMetaObject::invokeMethod(
//                     this,
//                     [this, gearValue]() {
//                         QString qGear = QString::fromStdString(gearValue);
//                         // currentGear_ = qGear;
//                         std::cout << "[HU] gearStatusChanged from IC: "
//                                   << gearValue << std::endl;
//                         emit gearChanged(qGear);
//                     },
//                     Qt::QueuedConnection
//                 );
//             }
//         );
//     }
// }

// void GearController::setGear(const QString& gear) {
//     if (!proxy_) {
//         std::cout << "[HU] setGear called but proxy is null" << std::endl;
//         return;
//     }

//     std::string g = gear.toStdString();

//     // HU -> IC : setGear 메소드 호출
//     proxy_->setGearAsync(
//         g,
//         [](const CommonAPI::CallStatus& status, const int32_t& result) {
//             std::cout << "[HU] setGearAsync status="
//                       << static_cast<int>(status)
//                       << " result=" << result
//                       << std::endl;
//         }
//     );
// }

#include "GearController.h"
#include <QDebug>
#include <QHostAddress>

GearController::GearController(QObject* parent)
    : QObject(parent)
{
    // HU 리슨 포트(5000)에 바인드 (IC -> HU 수신용)
    if (!socket_.bind(QHostAddress::Any, huPort_)) {
        qWarning() << "[HU][UDP] Failed to bind on port" << huPort_
                   << ":" << socket_.errorString();
    } else {
        qDebug() << "[HU][UDP] Listening on port" << huPort_;
    }

    connect(&socket_, &QUdpSocket::readyRead,
            this, &GearController::handleReadyRead);
}

void GearController::setPeer(const QString& ip, int huListenPort, int icListenPort) {
    icIp_   = ip;
    huPort_ = static_cast<quint16>(huListenPort);
    icPort_ = static_cast<quint16>(icListenPort);

    // 다시 바인드 (포트를 바꿨다면)
    socket_.close();
    if (!socket_.bind(QHostAddress::Any, huPort_)) {
        qWarning() << "[HU][UDP] Re-bind failed on port" << huPort_
                   << ":" << socket_.errorString();
    } else {
        qDebug() << "[HU][UDP] Re-bind success. Now listening on port" << huPort_;
    }
}

void GearController::setGear(const QString& gear) {
    if (gear.isEmpty()) {
        qWarning() << "[HU][UDP] Gear is empty. Not sending.";
        return;
    }

    QByteArray data = gear.toUtf8();

    qint64 sent = socket_.writeDatagram(
        data,
        QHostAddress(icIp_),
        icPort_
    );

    if (sent == -1) {
        qWarning() << "[HU][UDP] Failed to send gear datagram:"
                   << socket_.errorString();
    } else {
        qDebug() << "[HU][UDP] Sent gear:" << gear
                 << "to" << icIp_ << ":" << icPort_;
    }
}

void GearController::handleReadyRead() {
    while (socket_.hasPendingDatagrams()) {
        QByteArray buffer;
        buffer.resize(int(socket_.pendingDatagramSize()));
        QHostAddress sender;
        quint16 senderPort = 0;

        socket_.readDatagram(buffer.data(), buffer.size(), &sender, &senderPort);

        QString gear = QString::fromUtf8(buffer).trimmed();
        qDebug() << "[HU][UDP] Received datagram from"
                 << sender.toString() << ":" << senderPort
                 << "gear =" << gear;

        if (!gear.isEmpty()) {
            emit gearReceived(gear);
        }
    }
}
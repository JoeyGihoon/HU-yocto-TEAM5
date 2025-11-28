#include "GearController.h"

#include <QMetaObject>

GearController::GearController(std::shared_ptr<v0::commonapi::ICProxy<>> proxy,
                               QObject* parent)
    : QObject(parent)
    , proxy_(std::move(proxy)) {

    if (proxy_) {
        // IC -> HU : 기어 상태 변경 브로드캐스트 구독
        proxy_->getGearStatusChangedEvent().subscribe(
            [this](const std::string& gearValue) {

                // CommonAPI 스레드에서 호출될 수 있으니 Qt 스레드로 넘겨줌
                QMetaObject::invokeMethod(
                    this,
                    [this, gearValue]() {
                        QString qGear = QString::fromStdString(gearValue);
                        // currentGear_ = qGear;
                        std::cout << "[HU] gearStatusChanged from IC: "
                                  << gearValue << std::endl;
                        emit gearChanged(qGear);
                    },
                    Qt::QueuedConnection
                );
            }
        );
    }
}

void GearController::setGear(const QString& gear) {
    if (!proxy_) {
        std::cout << "[HU] setGear called but proxy is null" << std::endl;
        return;
    }

    std::string g = gear.toStdString();

    // HU -> IC : setGear 메소드 호출
    proxy_->setGearAsync(
        g,
        [](const CommonAPI::CallStatus& status, const int32_t& result) {
            std::cout << "[HU] setGearAsync status="
                      << static_cast<int>(status)
                      << " result=" << result
                      << std::endl;
        }
    );

    // 여기서 currentGear_를 바로 바꿀 수도 있지만,
    // IC에서 실제로 기어 수행 후 broadcast를 보내주므로
    // 그 이벤트를 기준으로만 화면을 바꾸는 게 더 정합성이 좋음.
}

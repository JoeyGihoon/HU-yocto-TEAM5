#include "GearController.h"

GearController::GearController(std::shared_ptr<v0::commonapi::ICProxy<>> proxy,
                               QObject* parent)
    : QObject(parent)
    , proxy_(std::move(proxy)) {

    if (!proxy_) {
        std::cout << "[HU] GearController created with null proxy!" << std::endl;
    } else {
        std::cout << "[HU] GearController created. Proxy is valid." << std::endl;
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
}

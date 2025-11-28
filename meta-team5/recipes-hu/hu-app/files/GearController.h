#pragma once
#include <QObject>
#include <QString>
#include <memory>
#include <string>
#include <iostream>

#include <CommonAPI/CommonAPI.hpp>
#include <v0/commonapi/ICProxy.hpp>

class GearController : public QObject {
    Q_OBJECT
    //Q_PROPERTY(QString currentGear READ currentGear NOTIFY gearChanged)

public:
    explicit GearController(std::shared_ptr<v0::commonapi::ICProxy<>> proxy,
                            QObject* parent=nullptr);

    // HU -> IC: 기어 변경 요청
    Q_INVOKABLE void setGear(const QString& gear);

    //QString currentGear() const { return currentGear_; }

signals:
    void gearChanged(const QString& gear);

private:
    std::shared_ptr<v0::commonapi::ICProxy<>> proxy_;
    //QString currentGear_ { "" };
};

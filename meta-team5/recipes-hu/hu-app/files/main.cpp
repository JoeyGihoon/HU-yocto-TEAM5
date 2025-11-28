#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QQmlContext>
#include <QtWebEngine/QtWebEngine>
#include <QtNetwork/QSslSocket>
#include <CommonAPI/CommonAPI.hpp>

#include <v0/commonapi/ICProxy.hpp> // Generated CommonAPI Proxy 헤더 파일
#include "mapController.h"
#include "weather.h"
#include "youtubeController.h"
#include "usbscanner.h"
#include "imageExtractor.h"
#include "GearController.h"

std::shared_ptr<v0::commonapi::ICProxy<>> g_icProxy;

static void initSomeipProxy() {
    auto runtime = CommonAPI::Runtime::get();

    std::string domain = "local";                // ini의 domain
    std::string instance = "commonapi.IC_service"; // ini의 instance

    auto context = std::make_shared<CommonAPI::MainLoopContext>();

    // g_icProxy = runtime->buildProxy<v0::commonapi::ICProxy<>>(domain, instance, connId);
    g_icProxy = runtime->buildProxy<v0::commonapi::ICProxy>(domain, instance, context);

    if (!g_icProxy) {
        std::cerr << "[HU] Failed to create IC proxy. Starting HU without SOME/IP" << std::endl;
        g_icProxy = nullptr;
        return;
    }

    g_icProxy->getProxyStatusEvent().subscribe([](CommonAPI::AvailabilityStatus s){
        std::cout << "IC Proxy status: "
                  << static_cast<int>(s) << std::endl;
    });
}

int main(int argc, char *argv[])
{
    qputenv("QTWEBENGINE_DISABLE_SANDBOX", "1");
    qputenv("QTWEBENGINE_CHROMIUM_FLAGS", "--no-sandbox --disable-gpu --disable-gpu-compositing");

    QCoreApplication::setAttribute(Qt::AA_EnableHighDpiScaling);
    QCoreApplication::setAttribute(Qt::AA_ShareOpenGLContexts);

    initSomeipProxy();
    
    QGuiApplication app(argc, argv);
    QQmlApplicationEngine engine;

    GearController gearController(g_icProxy);
    engine.rootContext()->setContextProperty("gearController", &gearController);

    MapController mapController;
    engine.rootContext()->setContextProperty("mapController", &mapController);

    Weather weather;
    engine.rootContext()->setContextProperty("weather", &weather);

    YoutubeController YoutubeController;
    engine.rootContext()->setContextProperty("youtubeController", &YoutubeController);

    UsbScanner usbScanner;
    engine.rootContext()->setContextProperty("usbScanner", &usbScanner);

    ImageExtract imageExtractor;
    engine.rootContext()->setContextProperty("imageExtract", &imageExtractor);

    usbScanner.rescanMountedUsb();
    imageExtractor.setExtractorCommand("python3", {"/usr/bin/extract_cover.py", "--in", "%IN%", "--out", "%OUT%"});

    const QUrl url(QStringLiteral("qrc:/qml/pages/Main.qml"));
    QObject::connect(&engine, &QQmlApplicationEngine::objectCreated,
                     &app, [url](QObject *obj, const QUrl &objUrl) {
                         if (!obj && url == objUrl) QCoreApplication::exit(-1);
                     }, Qt::QueuedConnection);

    engine.load(url);
    return app.exec();
}

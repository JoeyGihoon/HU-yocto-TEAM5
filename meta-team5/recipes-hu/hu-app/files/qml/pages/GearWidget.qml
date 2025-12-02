// GearWidget.qml
import QtQuick 2.12
import QtGraphicalEffects 1.0

AppWidget {
    id: root

    widthData: 74
    heightData: 300

    property alias currentGear: gearPanel.currentGear
    property alias gears:       gearPanel.gears
    property alias activeColor: gearPanel.activeColor
    property alias inactiveColor: gearPanel.inactiveColor
    property alias activeBg:    gearPanel.activeBg

    signal gearChanged(string gear)

    // 🔹 외부(IC)에서 들어온 기어를 세팅할 때, 다시 IC로 보내지 않도록 플래그
    property bool _suppressSend: false

    // 🔹 외부에서 호출할 함수 (IC -> HU 반영용)
    function applyExternalGear(gear) {
        _suppressSend = true
        gearPanel.currentGear = gear
        _suppressSend = false
    }

    // 기어 변경 시 외부로 알려주기
    onCurrentGearChanged: {
        if (_suppressSend) {
            // 외부에서 설정한 경우 -> 다시 보내지 않음
            return
        }

        gearChanged(currentGear)

        // C++ main.cpp에서 setContextProperty로 등록한 gearController 호출
        if (typeof gearController !== "undefined" && gearController) {
            gearController.setGear(currentGear)
        } else {
            console.warn("gearController is not available")
        }
    }

    // AppWidget이 제공하는 contentItem 슬롯에 실제 UI를 꽂는다
    contentItem: Rectangle {
        id: gearPanel
        anchors.fill: parent
        radius: 12
        color: "transparent"
        border.color: "#314055"
        border.width: 2

        anchors.centerIn: parent

        property int panelPadding: 8
        property int itemSpacing: 4
        property var gears: ["P","R","N","D"]
        property string currentGear: "P"

        property color activeColor:   "#35c2b1"
        property color inactiveColor: "#a9b4c7"
        property color activeBg:      "#1f2a33"

        property int cellH: Math.floor((height - panelPadding*2 - itemSpacing*(gears.length-1)) / gears.length)

        Column {
            id: col
            anchors.fill: parent
            anchors.margins: gearPanel.panelPadding
            spacing: gearPanel.itemSpacing

            Repeater {
                model: gearPanel.gears
                delegate: Item {
                    width: col.width
                    height: gearPanel.cellH
                    property bool active: gearPanel.currentGear === modelData

                    // 활성 줄 배경 하이라이트
                    Rectangle {
                        anchors.fill: parent
                        radius: 8
                        color: active ? gearPanel.activeBg : "transparent"
                        Behavior on color { ColorAnimation { duration: 160 } }
                    }

                    // 기어 문자
                    Text {
                        id: label
                        anchors.centerIn: parent
                        text: modelData
                        font.pixelSize: 28
                        font.weight: active ? Font.DemiBold : Font.Normal
                        color: active ? gearPanel.activeColor : gearPanel.inactiveColor
                        opacity: active ? 1.0 : 0.8
                        scale: active ? 1.08 : 1.0

                        Behavior on color   { ColorAnimation { duration: 140 } }
                        Behavior on opacity { NumberAnimation { duration: 140 } }
                        Behavior on scale   { NumberAnimation { duration: 140; easing.type: Easing.OutQuad } }
                    }

                    // 빛나는 효과(바깥 글로우)
                    DropShadow {
                        anchors.fill: label
                        source: label
                        horizontalOffset: 0
                        verticalOffset: 0
                        radius: active ? 16 : 0
                        samples: 25
                        color: gearPanel.activeColor
                        transparentBorder: true
                        opacity: active ? 0.9 : 0.0
                        visible: active || opacity > 0
                        Behavior on radius  { NumberAnimation { duration: 160 } }
                        Behavior on opacity { NumberAnimation { duration: 160 } }
                    }

                    // 마우스로도 기어 변경
                    MouseArea {
                        anchors.fill: parent
                        onClicked: {
                            if (gearPanel.currentGear !== modelData) {
                                gearPanel.currentGear = modelData
                            }
                        }
                        hoverEnabled: true
                        cursorShape: Qt.PointingHandCursor
                    }
                }
            }
        }
    }
}

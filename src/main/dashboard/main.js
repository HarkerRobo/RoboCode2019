const updateToggleModes = () => {
    let isCargoShipEnabled = NetworkTables.getValue("/SmartDashboard/Is scoring on cargo ship?");
    let isHatchEnabled = NetworkTables.getValue("/SmartDashboard/Has hatch?");
    let isWristEnabled = NetworkTables.getValue("/SmartDashboard/Has wrist manual control?");

    if(isCargoShipEnabled) {
        document.getElementById("cargoship").style.display = "inline-block";
        document.getElementById("rocket").style.display = "none";
    } else {
        document.getElementById("cargoship").style.display = "none";
        document.getElementById("rocket").style.display = "inline-block";
    }

    if(isHatchEnabled) {
        document.getElementById("hatch").style.display = "inline-block";
        document.getElementById("cargo").style.display = "none";
    } else {
        document.getElementById("hatch").style.display = "none";
        document.getElementById("cargo").style.display = "inline-block";
    }

    if (isWristEnabled) {
        document.getElementById("wrist").style.display = "inline-block";
        document.getElementById("align").style.display = "none";
    }
    else {
        document.getElementById("wrist").style.display = "none";
        document.getElementById("align").style.display = "inline-block";
    }
} 

const updateSmartDashFields = () => {
    let elevatorPosition = NetworkTables.getValue("/SmartDashboard/Elevator Position");
    let wristPosition = NetworkTables.getValue("/SmartDashboard/Wrist Position");

    document.getElementById("elevatorPos").innerHTML = elevatorPosition;
    document.getElementById("wristPos").innerHTML = Math.round(wristPosition * 10) / 10 + " degrees";
}

const interval = setInterval(() => {
    updateToggleModes();
    updateSmartDashFields();
}, 100);
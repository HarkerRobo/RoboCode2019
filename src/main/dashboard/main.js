const ge = (element) => {
    return document.getElementById(element);
}

const updateText = (cargoEnabled, hatchEnabled) => {
    if(cargoEnabled) {
        ge("cargoship").style.display = "inline-block";
        ge("rocket").style.display = "none";
    } else {
        ge("rocket").style.display = "inline-block";
        ge("cargoship").style.display = "none";
    }
    if(hatchEnabled) {
        ge("hatch").style.display = "inline-block";
        ge("cargo").style.display = "none";
    } else {
        ge("cargo").style.display = "inline-block";
        ge("hatch").style.display = "none";
    }
} 

const interval = setInterval(() => {
    let isCargoShipEnabled = NetworkTables.getValue("/SmartDashboard/Is scoring on cargo ship?");
    let isHatchEnabled = NetworkTables.getValue("/SmartDashboard/Has hatch?");
    let elevatorPosition = NetworkTables.getValue("/SmartDashboard/Elevator Position");
    let wristPosition = NetworkTables.getValue("/SmartDashboard/Wrist Position");
    updateText(isCargoShipEnabled, isHatchEnabled);
}, 100);
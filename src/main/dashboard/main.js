const ge = (element) => {
    return document.getElementById(element);
}

const updateText = (cargoEnabled, hatchEnabled) => {
    ge("cshipm").innerHTML = `
        <img src=${cargoEnabled ? "cargo_ship.png" : "rocket.png"} style="height: 30vh"/>
        <span class="big">${cargoEnabled ? "Cargo ship" : "Rocket"} mode</span><br /><br /><br /><br /><br/><br/><br /><br/>
        <img src=${hatchEnabled ? "hatchpanel.png" : "cargo.png"} style="height: 30vh"/>
        <span class="big">Has ${hatchEnabled ? "hatch panel" : "cargo"}</span>`;
    //ge("cshipm").innerHTML = `${cargoEnabled ? "Cargo ship" : "Rocket"} mode`;
    //if(document.getElementById("cshipm").classList.contains("green") && !cargoEnabled)
    //    document.getElementById("cshipm").classList.toggle("green");
    //else if(!document.getElementById("cshipm").classList.contains("green") && cargoEnabled)
    //    document.getElementById("cshipm").classList.toggle("green");
    
} 

const interval = setInterval(() => {
    let isCargoShipEnabled = NetworkTables.getValue("/SmartDashboard/Is scoring on cargo ship?");
    let isHatchEnabled = NetworkTables.getValue("/SmartDashboard/Has hatch?");
    updateText(isCargoShipEnabled, isHatchEnabled);
}, 500);
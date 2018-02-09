"use strict";

/***************************************************************/
var LOGGING = {};
// 0 = off, 1 = debug, 2 = trace (freezes)
LOGGING.level = 1;
if(LOGGING.level >= 1) console.log("LOGGING.level = " + LOGGING.level);
/***************************************************************/

function scaleSize(maxW, maxH, currW, currH){
    var ratio = currH / currW;
    if(currW >= maxW && ratio <= 1){
        currW = maxW;
        currH = currW * ratio;
    }
    if(currH >= maxH){
        currH = maxH;
        currW = currH / ratio;
    }
    return [currW, currH];
}

// Returns a random integer between min (included) and max (excluded)
// Using Math.round() will give you a non-uniform distribution!
function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min)) + min;
}

function calculateRatio(){
    // finally query the various pixel ratios
    var devicePixelRatio = window.devicePixelRatio || 1;
    var backingStoreRatio = canvasContext.webkitBackingStorePixelRatio ||
                            canvasContext.mozBackingStorePixelRatio ||
                            canvasContext.msBackingStorePixelRatio ||
                            canvasContext.oBackingStorePixelRatio ||
                            canvasContext.backingStorePixelRatio || 1;
    ratio = devicePixelRatio / backingStoreRatio;
}

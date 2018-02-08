"use strict";

var imagesStillLoading = 0;

function loadImage(imageName){
    var image = new Image();
    image.src = imageName;
    imagesStillLoading+=1;
    image.onload = function () {
        imagesStillLoading -= 1;
    };
    return image;
}

function assetLoadingLoop() {
    if (imagesStillLoading > 0)
        window.setTimeout(assetLoadingLoop, 1000 / 60);
    else {
        mainLoop();
    }
}

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
"use strict";
/* When a script is interpreted in strict mode, it isn’t allowed to use
   variables without declaring them first. */

var drawableFolder = "res/drawable/";

function addCanvas() {
    var canvas = document.createElement("CANVAS");
    canvas.id = "myCanvas";
    canvas.style.position = "absolute";
    canvas.style.border = "1px solid";
    var body = document.getElementById("myBody");
    while (body.hasChildNodes()) {
        body.removeChild(body.firstChild);
    }
    body.appendChild(canvas);
    setCanvasFillScreen();
}

function setCanvasFillScreen(){
    var canvas = document.getElementById("myCanvas");
    var context = canvas.getContext("2d");
    context.canvas.width  = window.innerWidth;
    context.canvas.height = window.innerHeight;
}

function init(options){
    addCanvas();
    startTheGame({ numberOfCards: options.numberOfCards }); // --> GameLoop.js
}
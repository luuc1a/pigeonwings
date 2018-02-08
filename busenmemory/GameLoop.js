/* GAME LOOP */  //     if(LogON) console.log("CurrentMove.js - checkIfPair(): picA="+this._picA+" picB="+this._picB+"  ----"+(logNR++));

"use strict";
/* When a script is interpreted in strict mode, it isn’t allowed to use
   variables without declaring them first. */

var canvas = undefined;
var canvasContext = undefined;
var screenWidth = undefined;
var screenHeight = undefined;
var ratio = undefined;

var run = false;

/**************** STUFF USED FOR MY GAME ****************/
var myPlayingField = undefined;
var myCurrentMove = undefined;
var numberOfCards = undefined;
var pairsFoundCounter = undefined;
var myAnimation = undefined;
var showCardsTime = 0;
var myCelebration = undefined;
/********************************************************/

function startTheGame(options) {

    numberOfCards = options.numberOfCards;

    canvas = document.getElementById("myCanvas");
    canvasContext = canvas.getContext("2d");
    screenWidth = canvas.width;
    screenHeight = canvas.height;

    canvas.addEventListener("mousedown",handleMouseDown,false);
    canvas.addEventListener("mouseup",handleMouseUp,false);

    calculateRatio();
    initializeGameObjects();

    window.requestAnimationFrame = window.requestAnimationFrame ||
        window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame ||
        window.oRequestAnimationFrame || window.msRequestAnimationFrame ||
        function (callback) {
            window.setTimeout(callback, 1000 / 60);
        };
    assetLoadingLoop();
}

function initializeGameObjects(){
    myPlayingField = new PlayingField({numberOfCards:numberOfCards});
    myPlayingField.spawnCards();
    myCurrentMove = new CurrentMove();
    pairsFoundCounter=0;
    myAnimation = new Animation();
    myCelebration = new Celebration();
}

function clearCanvas () {
    canvasContext.clearRect(0, 0, canvas.width, canvas.height);
}

function gameIsOver() {
    return (pairsFoundCounter == (numberOfCards/2) );
}

function update () {
    /* In JavaScript, you can use the following two instructions to get the current system time:*/
    var d = new Date();
    var currentTime = d.getTime();

    if(!myAnimation.isRunning() && myCurrentMove.isDone()){
        Mouse.clickable = false;
        myAnimation.start({a:myCurrentMove._picA, b:myCurrentMove._picB});
    }

    if(myAnimation.isRunning()) {
        myAnimation.update();

        if(myAnimation.isDone()){
            myAnimation.hide();
            if(showCardsTime == 0)
                showCardsTime = currentTime;
            Mouse.clickable = true;

            if(myCurrentMove.checkIfPair()){
                pairsFoundCounter++;
                myPlayingField.removeCards({a:myCurrentMove._picA, b:myCurrentMove._picB});
                myCurrentMove._next=true;
            }

            if(gameIsOver() && !myCelebration.isRunning()){
                showCardsTime=0;
                myCurrentMove._next=true;
                myCelebration.start();
            }

            if((currentTime-showCardsTime)>2000){
                showCardsTime=0;
                myCurrentMove._next=true;
            }

            if(myCurrentMove.isNext()){
                showCardsTime=0;
                myPlayingField.flipCards({a:myCurrentMove._picA, b:myCurrentMove._picB});
                myAnimation.reset();
                myCurrentMove.next();
            }
        }
    }

    if(myCelebration.isRunning()){
        Mouse.clickable = false;
        myCelebration.update();

        if(myCelebration.isDone()){
            myCelebration.reset();
            location.reload();
        }
    }
}

function render () {
    myPlayingField.render();
    myAnimation.render();
    myCelebration.render();
}

function mainLoop () {
    if(!run){
        canvasContext = canvas.getContext("2d");
        clearCanvas();
        update();
        render();
        window.requestAnimationFrame(mainLoop);
   }
}
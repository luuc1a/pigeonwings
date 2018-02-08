"use strict";
/* When a script is interpreted in strict mode, it isn’t allowed to use
   variables without declaring them first. */
// if(LogON) console.log("PlayingField.js - new Card: imageID="+i+", posID="+posID+", X="+this._grid.getX(posID)+", Y="+this._grid.getY(posID)+"  ...." +(logNR++));

   var screenCase = 0;
   var headerSize = [0,0];
   var offsetY = 0;

function PlayingField(options){

    if(screenWidth<screenHeight){
        screenCase = 1; // PORTRAIT
        if( screenHeight/screenWidth >= 1.7)
            screenCase = 3; // LONG-PORTRAIT
        this._header = loadImage(drawableFolder+"headerPortrait.jpg");
        this._footer = loadImage(drawableFolder+"footerPortrait.jpg");
    } else {
       screenCase = 2; // LANDSCAPE
       if( screenWidth/screenHeight >= 2)
           screenCase = 4; // LONG-PORTRAIT
           this._header = loadImage(drawableFolder+"headerLandscape.jpg");
           this._footer = loadImage(drawableFolder+"footerLandscape.jpg");
    }

    this._numberOfCards = options.numberOfCards;
    this._grid = new Grid({numberOfCards:this._numberOfCards, screenCase:screenCase });
    this._grid.generate();
    this._card = [];

}

PlayingField.prototype.spawnCards = function spawnCards(){

    var posUsed = [];

    for (var i = 0; i < this._numberOfCards; i++) {
        var posID = 0;
        do {
            posID = getRandomInt(0, this._numberOfCards);
        } while ( isElementOf(posID, posUsed) );
        var n = posUsed.length+1;
        posUsed[n] = posID;

        this._card[i] = new Card( {
            imageBmp : loadImage(drawableFolder+"back.jpg"),
            imageBmpFront : loadImage(drawableFolder+(i+1)+".jpg"),  //TODO change name of pics
            imageWidth : this._grid.getCardWidth(),
            imageID : i,
            positionID : posID,
            x : this._grid.getX(posID),
            y : this._grid.getY(posID)
        });
    }
}

function isElementOf(pID, pUsed){
    for (var i=0; i< pUsed.length; i++){
        if( pID == pUsed[i] )
            return true;
    }
    return false;
}

PlayingField.prototype.isTouched = function isTouched(options){
    var xT = options.x;
    var yT = options.y;

    for (var i = 0; i < this._numberOfCards; i++) {
        if(this._card[i].isTouched({x:xT,y:yT})){
            this._card[i].show();
            return this._card[i].getImageID();
        }
    }
    return undefined;
}

PlayingField.prototype.removeCards = function removeCards(options){
    this._card[options.a].remove();
    this._card[options.b].remove();
}

PlayingField.prototype.flipCards = function flipCards(options){
    this._card[options.a].hide();
    this._card[options.b].hide();
}

PlayingField.prototype.update = function update(){

}


PlayingField.prototype.render = function render(){
    canvasContext.save();
    canvasContext.drawImage(this._header,(screenWidth-headerSize[0])/2, (offsetY-headerSize[1])/2, headerSize[0], headerSize[1]);
    canvasContext.drawImage(this._footer,(screenWidth-headerSize[0])/2,screenHeight-(offsetY+headerSize[1])/2, headerSize[0], headerSize[1]);
    canvasContext.restore();
    for (var i = 0; i < this._card.length; i++) {
        this._card[i].render();
    }
}

function Grid(options){
    this._numberOfCards = options.numberOfCards;
    this._x = [];
    this._y = [];
    this._cardWidth = undefined;
}

Grid.prototype.generate = function generate(){

    var cols = 1;
    var rows = 1;

    var borderLeft=screenWidth/30;
    var borderRight=screenWidth/30;
    var borderTop = screenHeight/10;
    var borderBottom = screenHeight/10;

    switch(this._numberOfCards){
        case 90:
        switch(screenCase){
                case 1: // PORTRAIT
                    cols=10;
                    rows=9;
                break;
                case 4: // LONG-LANDSCAPE
                    cols=6;
                    rows=15;
                break;
                case 3: // LONG-PORTRAIT
                    cols=15;
                    rows=6;
                break;
                case 2: // LANDSCAPE
                    cols=9;
                    rows=10;
                break;
                default:
            }
        break;
        case 60:
        switch(screenCase){
                case 1: // PORTRAIT
                    cols=10;
                    rows=6;
                break;
                case 4: // LONG-LANDSCAPE
                    cols=5;
                    rows=12;
                break;
                case 3: // LONG-PORTRAIT
                    cols=12;
                    rows=5;
                break;
                case 2: // LANDSCAPE
                    cols=6;
                    rows=10;
                break;
                default:
            }
        break;
        case 30:
        switch(screenCase){
                case 1: // PORTRAIT
                    cols=6;
                    rows=5;
                break;
                case 4: // LONG-LANDSCAPE
                    cols=5;
                    rows=6;
                break;
                case 3: // LONG-PORTRAIT
                    cols=6;
                    rows=5;
                break;
                case 2: // LANDSCAPE
                    cols=5;
                    rows=6;
                break;
                default:
            }
        break;
    }

    var gridWidth = (screenWidth-borderLeft-borderRight);
    var gridHeight = (screenHeight-borderTop-borderBottom);

    this._cardWidth = Math.min(Math.round(gridWidth/rows),Math.round(gridHeight/cols));

    var posID = 0;
    var offsetX = (screenWidth- this._cardWidth*rows )/2;
    offsetY = (screenHeight- this._cardWidth*cols )/2;

    for (var i = 0; i < cols; i++) {
        for (var j=0; j < rows ; j++){
            this._x[posID] = offsetX+ j*this._cardWidth;
            this._y[posID] = offsetY+ i*this._cardWidth;
            posID++;
        }
    }

    switch(screenCase){
        case 1:headerSize=scaleSize(screenWidth, offsetY, 800, 200);break; // portrait
        case 2:headerSize=scaleSize(screenWidth, offsetY, 1600, 80);break; // landscape
        case 3:headerSize=scaleSize(screenWidth, offsetY, 800, 200);break; // long portrait
        case 4:headerSize=scaleSize(screenWidth, offsetY, 1600, 80);break; // long landscape
    }
}

Grid.prototype.getX = function getX(pos){ return this._x[pos]; }
Grid.prototype.getY = function getY(pos){ return this._y[pos]; }
Grid.prototype.getCardWidth = function getCardWidth(){ return this._cardWidth; }
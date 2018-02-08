"use strict";
/* When a script is interpreted in strict mode, it isn’t allowed to use
   variables without declaring them first. */

function Card(options){
    this._positionID = options.positionID;
    this._imageID = options.imageID;
    this._imageBmp = options.imageBmp;
    this._imageBmpFront = options.imageBmpFront;
    this._imageWidth = options.imageWidth;
    this._x = options.x;
    this._y = options.y;
    this._visible = false;
    this._gone = false;
}

Card.prototype.render = function render(){

    if(!this._gone){
        canvasContext.save();
        if(this._visible)
            canvasContext.drawImage(this._imageBmpFront,this._x,this._y,this._imageWidth,this._imageWidth);
        else canvasContext.drawImage(this._imageBmp,this._x,this._y,this._imageWidth,this._imageWidth);

        canvasContext.restore();
    }
}

Card.prototype.update = function update(){

}

Card.prototype.show = function show(){
    this._visible = true;
}

Card.prototype.hide = function hide(){
    this._visible = false;
}

Card.prototype.remove = function remove(){
    this._gone = true;
}

Card.prototype.getImageID = function getImageID(){
    return this._imageID;
}

Card.prototype.isTouched = function isTouched(options){
    return !( this._gone ||
              this._visible ||
             options.x > (this._x + this._imageWidth) ||
             options.x < (this._x) ||
             options.y < (this._y) ||
             options.y > (this._y+this._imageWidth) );
}


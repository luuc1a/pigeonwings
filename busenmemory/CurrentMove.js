
function CurrentMove(){  // the constructor for CurrentMove.prototype

    this._counter = 0;
    this._picA = undefined;
    this._picB = undefined;
    this._next = true;
}

CurrentMove.prototype.isDone = function isDone(){
    if(this._picA != undefined && this._picB != undefined)
        return true;
    else return false;

}

CurrentMove.prototype.next = function next(){
    this._picA = undefined;
    this._picB = undefined;
    this._counter++;
}

CurrentMove.prototype.setPicID = function setPicID(options){
    this._next = false;
    if (this._picA == undefined) this._picA = options.picID;
    else    if (this._picB == undefined) this._picB = options.picID;
}

CurrentMove.prototype.checkIfPair = function checkIfPair(){
    if(  ( (this._picA-this._picB)*(this._picA-this._picB) == 1 ) && (Math.min(this._picA, this._picB)%2 == 0) )
        return true;
    else return false;
}

CurrentMove.prototype.isNext = function isNext(){
    return this._next;
}
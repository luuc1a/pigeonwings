
function Animation(){
    this._step=0;
    this._timer=0;
    this._on=false;
    this._done=false;

    this._stepMax=(80*ratio); // TODO initial distance of the pics
    this._imageWidth = Math.min(screenHeight,screenWidth)*0.3;
    if(this._imageWidth>300) this._imageWidth=300;

    this._picA = undefined;
    this._posA = screenWidth/2-this._stepMax-this._imageWidth;
    this._picB = undefined;
    this._posB = screenWidth/2+this._stepMax;
    this._posY = (screenHeight-this._imageWidth)/2;
}

Animation.prototype.start = function start(options){

    if ( options.a%2 == 0 ){
        this._picA = loadImage(drawableFolder+(options.a+1)+".jpg");
        this._picB = loadImage(drawableFolder+(options.b+1)+".jpg");
    }
    else {
        this._picA = loadImage(drawableFolder+(options.b+1)+".jpg");
        this._picB = loadImage(drawableFolder+(options.a+1)+".jpg");
    }

    this._on=true;
}

Animation.prototype.update = function update(){

    if(this._step < this._stepMax){
        this._step+=(4*ratio);
    } else {
        if(this._timer === 0){
            this._step=this._stepMax;
            var d = new Date();
            this._timer=d.getTime();
        } else {
            var d = new Date();
            var currentTime = d.getTime();
            if( ( currentTime - this._timer ) > 1500 ){
                this._done=true;
            }
        }
    }
}

Animation.prototype.isDone = function isDone(){
    return this._done;
}

Animation.prototype.hide = function hide(){
    this._on=false;
    this._done=true;
}

Animation.prototype.reset = function reset(){
    this._step=0;
    this._timer=0;
    this._on=false;
    this._done=false;
}

Animation.prototype.render = function render(){
    if(this._on && !this._done){
        canvasContext.save();
        canvasContext.drawImage(this._picA, this._posA + this._step, this._posY, this._imageWidth,this._imageWidth);
        canvasContext.drawImage(this._picB, this._posB - this._step, this._posY, this._imageWidth,this._imageWidth);

        canvasContext.restore();

    }
}

Animation.prototype.isRunning = function isRunning(){
    return this._on;
}


function Celebration(){
    this._on = false;
    this._done = false;
    this._celebrationTime = 0;
}

Celebration.prototype.start = function start(options){
    this._on=true;
    var d = new Date();
    this._celebrationTime = d.getTime();

}

Celebration.prototype.update = function update(){
    var d = new Date();
    var currentTime = d.getTime();
    if((currentTime - this._celebrationTime)>4000){
        this._done=true;
    }
}

Celebration.prototype.isDone = function isDone(){
    return this._done;
}

Celebration.prototype.render = function render(){
    if(this._on && !this._done){
        canvasContext.save();
        canvasContext.fillStyle = "rgb("+getRandomInt(0, 255)+","+getRandomInt(0, 255)+","+getRandomInt(0, 255)+")";
        canvasContext.fillRect(0, 0, canvas.width, canvas.height);
        canvasContext.fillStyle = "black";
        canvasContext.font = "50pt verdana";
        canvasContext.fillText("BUSENMEMORY", (screenWidth/2), (screenHeight/2));
        canvasContext.restore();
    }
}

Celebration.prototype.isRunning = function isRunning(){
    return this._on;
}

Celebration.prototype.reset = function reset(){
    this._on=false;
    this._done=false;
    this._celebrationTime = 0;
}

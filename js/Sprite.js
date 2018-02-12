function Sprite (imagePath, frameCountX, frameCountY) {
	if(LOGGING.level >= 1) console.log("called SPRITE constructor");
	
	this.spriteSheet = GAME.loadImage(imagePath);
	
	this.frameTicker = 0;
	this.framePeriod = 160;
	
	this.maxFrameX = frameCountX;
	this.maxFrameY = frameCountY;
	this.currentFrameX = 0;
	this.currentFrameY = 0;
		
	//	this.spriteWidth =	this.spriteSheet.width/frameCountX;
	//	this.spriteHeight =	this.spriteSheet.height/frameCountY;
	this.spriteWidth = 100;
	this.spriteHeight = 85;
	if(LOGGING.level >= 1) console.log("this.spriteWidth="+this.spriteWidth+" , this.spriteHeight="+this.spriteHeight);
		
	this.srcX = 0;
	this.srcY = 0;
	this.destX = 0;
	this.destY = 0;
};

Sprite.prototype.incrementYFrame = function(){
	if(LOGGING.level >= 1) console.log("called SPRITE.incrementYFrame");
	if( (this.currentFrameY + 1) < this.maxFrameY ){
		this.currentFrameY = this.currentFrameY + 1;
	}
	this.srcY = this.currentFrameY * this.spriteHeight;
}

Sprite.prototype.incrementXFrame = function(){
	this.currentFrameX = (this.currentFrameX + 1) % this.maxFrameX;
	this.srcX = this.currentFrameX * this.spriteWidth;
}

Sprite.prototype.updatePosition = function(x,y){
	if(LOGGING.level >= 1) console.log("called SPRITE.updatePosition");
	this.destX = x;
	this.destY = y;
}

Sprite.prototype.updateFrame = function(gameTime){
	if(LOGGING.level >= 2) console.log("called SPRITE.update");

	if(gameTime > this.frameTicker + this.framePeriod){
		this.frameTicker = gameTime;
		this.incrementXFrame();
	}
}

Sprite.prototype.render = function(){
	if(LOGGING.level >= 2) console.log("called SPRITE.render");
	
	GAME.ctx.save();
	//void ctx.drawImage(image, sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight);
	// s* .. source rectangle from image, d* .. destination on canvas
	
	GAME.ctx.drawImage(
		this.spriteSheet, 
		//source rectangle
		this.srcX, this.srcY, this.spriteWidth, this.spriteHeight,
		//desitnation rect
		this.destX, this.destY, this.spriteWidth, this.spriteHeight
	);
	
	GAME.ctx.restore();
}

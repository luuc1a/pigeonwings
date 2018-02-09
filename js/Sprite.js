function Sprite (imagePath, width, height) {
	if(LOGGING.level >= 1) console.log("called SPRITE constructor");
	
	this.x_pos = 0;
	this.y_pos = 0;
	this.spriteWidth = width;
	this.spriteHeight = height;

	this._spriteSheet = GAME.loadImage(imagePath);
};

Sprite.prototype.update = function(){
	if(LOGGING.level >= 2) console.log("called SPRITE.update");

}

Sprite.prototype.render = function(){
	if(LOGGING.level >= 2) console.log("called SPRITE.render");
	
	GAME.ctx.save();
	//void ctx.drawImage(image, sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight);
	// s* .. source rectangle from image, d* .. destination on canvas
	
	GAME.ctx.drawImage(
		this._spriteSheet, 
		//source rectangle
		0,0,100,85,
		//desitnation rect
		this.x_pos, this.y_pos, this.x_pos + this.spriteWidth, this.y_pos + this.spriteHeight
	);
	
	GAME.ctx.restore();
}

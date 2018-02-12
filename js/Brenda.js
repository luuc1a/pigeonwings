"use strict";

function Brenda() {
	if(LOGGING.level >= 1) console.log("called Brenda()");
	this.type = 'brenda';
	this.sprite = new Sprite("/home/lechner/Development/pigeonwings/res/brenda_sprite.png", 8, 5);
	this.x_pos = 7;
	this.y_pos = GAME.HEIGHT-150;
};

Brenda.prototype.init = function(){
	if(LOGGING.level >= 1) console.log("called BRENDA.init");
	this.sprite.updatePosition(this.x_pos,this.y_pos);
};

Brenda.prototype.update = function(gameTime){
	if(LOGGING.level >= 2) console.log("called BRENDA.update");
	this.sprite.updateFrame(gameTime);
	
};

Brenda.prototype.render = function(){
	if(LOGGING.level >= 2) console.log("called BRENDA.render");		
	this.sprite.render();
};

Brenda.prototype.hitByShit = function(){
	if(LOGGING.level >= 1) console.log("called BRENDA.hitByShit");
	this.sprite.incrementYFrame();
};

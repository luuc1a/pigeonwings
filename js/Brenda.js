"use strict";

function Brenda() {
	if(LOGGING.level >= 1) console.log("called Brenda()");
	this.sprite = new Sprite("/home/lechner/Development/pigeonwings/res/brenda_sprite.png",100,85);
	this.x_pos = 7;
	this.y_pos = 400;
};

Brenda.prototype.update = function(){
	if(LOGGING.level >= 2) console.log("called BRENDA.update");
	this.sprite.update();
};

Brenda.prototype.render = function(){
	if(LOGGING.level >= 2) console.log("called BRENDA.render");		
	this.sprite.render();
};

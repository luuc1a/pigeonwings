"use strict";


STAGE.Brenda = function() {

    this.img = new Image();
    this.img.src = "../res/brenda_sprite.png";

    this.spriteWidth=100;
    this.spriteHeight=85;

    this.x_pos = 7; // current x position
    this.y_pos = 400; // y position - will never change as brenda can't jump

    this.update = function(){};
    this.render = function(){

        //void ctx.drawImage(image, sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight);
        // s* .. source rectangle from image, d* .. destination on canvas
        STAGE.ctx.drawImage(
            this.img, 
            //source rectangle
            0,0,100,85,
            //desitnation rect
            this.x_pos, this.y_pos, this.x_pos + this.spriteWidth, this.y_pos + this.spriteHeight );

    };

}

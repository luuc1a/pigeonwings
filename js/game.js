

var POP = {

canvas: null,
ctx: null,
img: null,
init: function(){

POP.canvas = document.getElementsByTagName('canvas')[0];
POP.ctx = POP.canvas.getContext('2d');

POP.img = new Image();
POP.img.src = "slogan_combo.png";

try{
  POP.ctx.drawImage(POP.img,0,0);
} catch (err)  {
  console.info(err);
}
}

}

window.addEventListener('load', POP.init, false); 

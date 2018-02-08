// code should be executed in strict mode, e.g. no use of undeclared variables
"use strict";

// http://paulirish.com/2011/requestanimationframe-for-smart-animating
// shim layer with setTimeout fallback
window.requestAnimFrame = (function(){
  return  window.requestAnimationFrame       || 
          window.webkitRequestAnimationFrame || 
          window.mozRequestAnimationFrame    || 
          window.oRequestAnimationFrame      || 
          window.msRequestAnimationFrame     || 
          function( callback ){
            window.setTimeout(callback, 1000 / 60);
          };
})();

// namespace for the game
var STAGE = {

    // set up some inital values
    WIDTH: 320, 
    HEIGHT:  480, 
    scale:  1,
    // the position of the canvas
    // in relation to the screen
    offset: {top: 0, left: 0},
    // store all game-elements as well as gestures
    entities: [],
    // for tracking player's progress
    score: {
        taps: 0,
        hit: 0,
        escaped: 0,
        accuracy: 0
    },
    // we'll set the rest of these
    // in the init function
    RATIO:  null,
    currentWidth:  null,
    currentHeight:  null,
    canvas: null,
    ctx:  null,
    ua:  null,
    android: null,
    ios:  null,

    img_brenda: null,

    init: function() {
   
        // the proportion of width to height
        STAGE.RATIO = STAGE.WIDTH / STAGE.HEIGHT;
        // these will change when the screen is resize
        STAGE.currentWidth = STAGE.WIDTH;
        STAGE.currentHeight = STAGE.HEIGHT;
        // this is our canvas element
        STAGE.canvas = document.getElementsByTagName('canvas')[0];
        // it's important to set this
        // otherwise the browser will
        // default to 320x200
        STAGE.canvas.width = STAGE.WIDTH;
        STAGE.canvas.height = STAGE.HEIGHT;
        // the canvas context allows us to 
        // interact with the canvas api
        STAGE.ctx = STAGE.canvas.getContext('2d');
        // we need to sniff out android & ios
        // so we can hide the address bar in
        // our resize function
        //STAGE.ua = navigator.userAgent.toLowerCase();
        //STAGE.android = STAGE.ua.indexOf('android') > -1 ? true : false;
        //STAGE.ios = ( STAGE.ua.indexOf('iphone') > -1 || STAGE.ua.indexOf('ipad') > -1  ) ? true : false;

        STAGE.img_brenda = new Image();
        STAGE.img_brenda.src = "../res/brenda_sprite.png";

        STAGE.entities.push(new STAGE.Brenda());

        // listen for clicks
        window.addEventListener('click', function(e) {
            e.preventDefault();
            STAGE.Input.set(e);
        }, false);

        // listen for touches
        window.addEventListener('touchstart', function(e) {
            e.preventDefault();
            // the event object has an array
            // called touches, we just want
            // the first touch
            STAGE.Input.set(e.touches[0]);
        }, false);
        window.addEventListener('touchmove', function(e) {
            // we're not interested in this
            // but prevent default behaviour
            // so the screen doesn't scroll
            // or zoom
            e.preventDefault();
        }, false);
        window.addEventListener('touchend', function(e) {
            // as above
            e.preventDefault();
        }, false);

        // we're ready to resize
        STAGE.resize();

        STAGE.loop();

    },


    resize: function() {
    
        STAGE.currentHeight = window.innerHeight;
        // resize the width in proportion
        // to the new height
        STAGE.currentWidth = STAGE.currentHeight * STAGE.RATIO;

        // this will create some extra space on the
        // page, allowing us to scroll pass
        // the address bar, and thus hide it.
        //if (STAGE.android || STAGE.ios) {
        //    document.body.style.height = (window.innerHeight + 50) + 'px';
        //}

        // set the new canvas style width & height
        // note: our canvas is still 320x480 but
        // we're essentially scaling it with CSS
        STAGE.canvas.style.width = STAGE.currentWidth + 'px';
        STAGE.canvas.style.height = STAGE.currentHeight + 'px';

        // the amount by which the css resized canvas
        // is different to the actual (480x320) size.
        STAGE.scale = STAGE.currentWidth / STAGE.WIDTH;
        // position of canvas in relation to
        // the screen
        STAGE.offset.top = STAGE.canvas.offsetTop;
        STAGE.offset.left = STAGE.canvas.offsetLeft;

        // we use a timeout here as some mobile
        // browsers won't scroll if there is not
        // a small delay
        //window.setTimeout(function() {
        //        window.scrollTo(0,1);
        //}, 1);
    },

    // this is where all entities will be moved
    // and checked for collisions etc
    update: function() {

        // if the user has tapped the screen
        if (STAGE.Input.tapped) {
            console.debug("user tapped screen")
            // keep track of taps; needed to 
            // calculate accuracy
            STAGE.score.taps += 1;
            // add a new touch
            STAGE.entities.push(new STAGE.Touch(STAGE.Input.x, STAGE.Input.y));
            // set tapped back to false
            // to avoid spawning a new touch
            // in the next cycle
            STAGE.Input.tapped = false;
        }

        // cycle through all entities and update as necessary
        for (var i = 0; i < STAGE.entities.length; i += 1) {
            STAGE.entities[i].update();
            // delete from array if remove property
            // flag is set to true
            if (STAGE.entities[i].remove) {
                STAGE.entities.splice(i, 1);
            }
        }

    },


    // this is where we draw all the entities
    render: function() {

        // draw the background
        STAGE.Draw.rect(0, 0, STAGE.WIDTH, STAGE.HEIGHT, '#036');
        STAGE.Draw.circle(
                        STAGE.WIDTH/2, 
                        STAGE.HEIGHT/2,
                        10, 
                        '#fff'); 

        // cycle through all entities and render to canvas
        for (var i = 0; i < STAGE.entities.length; i += 1) {
            STAGE.entities[i].render();
        }

        // display scores
        STAGE.Draw.text('Hit: ' + STAGE.score.hit, 20, 30, 14, '#fff');
        STAGE.Draw.text('Escaped: ' + STAGE.score.escaped, 20, 50, 14, '#fff');
        STAGE.Draw.text('Accuracy: ' + STAGE.score.accuracy + '%', 20, 70, 14, '#fff');

    },


    // the actual loop
    // requests animation frame
    // then proceeds to update
    // and render
    loop: function() {
        
        requestAnimFrame( STAGE.loop );

        STAGE.update();
        STAGE.render();

    }

};


// abstracts various canvas operations into
// standalone functions
STAGE.Draw = {

    clear: function() {
        STAGE.ctx.clearRect(0, 0, STAGE.WIDTH, STAGE.HEIGHT);
    },


    rect: function(x, y, w, h, col) {
        STAGE.ctx.fillStyle = col;
        STAGE.ctx.fillRect(x, y, w, h);
    },

    circle: function(x, y, r, col) {
        STAGE.ctx.fillStyle = col;
        STAGE.ctx.beginPath();
        STAGE.ctx.arc(x + 5, y + 5, r, 0,  Math.PI * 2, true);
        STAGE.ctx.closePath();
        STAGE.ctx.fill();
    },


    text: function(string, x, y, size, col) {
        STAGE.ctx.font = 'bold '+size+'px Monospace';
        STAGE.ctx.fillStyle = col;
        STAGE.ctx.fillText(string, x, y);
    },
    
    image: function( sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight) {
        STAGE.ctx.drawImage(
            STAGE.img_brenda, 
            //source rectangle
            sx, sy, sWidth, sHeight,
            //desitnation rect
            dx, dy, dWidth, dHeight );
    }

};



STAGE.Input = {

    x: 0,
    y: 0,
    tapped :false,

    set: function(data) {
        this.x = (data.pageX - STAGE.offset.left) / STAGE.scale;
        this.y = (data.pageY - STAGE.offset.top) / STAGE.scale;
        this.tapped = true;

    }

};

STAGE.Touch = function(x, y) {

    console.log("touch " + x + " " + y );

    this.type = 'touch';    // we'll need this later
    this.x = x;             // the x coordinate
    this.y = y;             // the y coordinate
    this.r = 5;             // the radius
    this.opacity = 1;       // inital opacity. the dot will fade out
    this.fade = 0.05;       // amount by which to fade on each game tick
    // this.remove = false;    // flag for removing this entity. POP.update
                            // will take care of this

    this.update = function() {
        // reduct the opacity accordingly
        this.opacity -= this.fade; 
        // if opacity if 0 or less, flag for removal
        this.remove = (this.opacity < 0) ? true : false;
    };

    this.render = function() {
        STAGE.Draw.circle(this.x, this.y, this.r, 'rgba(255,0,0,'+this.opacity+')');
    };

};

STAGE.Brenda = function() {

console.log("called Brenda");

    this.spriteWidth=100;
    this.spriteHeight=85;

    this.x_pos = 7; // current x position
    this.y_pos = 400; // y position - will never change as brenda can't jump

    this.update = function(){};
    this.render = function(){

console.log("called Brenda.render");

        //void ctx.drawImage(image, sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight);
        // s* .. source rectangle from image, d* .. destination on canvas
        STAGE.Draw.image(
            
            //source rectangle
            0,0,100,85,
            //desitnation rect
            this.x_pos, this.y_pos, this.x_pos + this.spriteWidth, this.y_pos + this.spriteHeight );

    };

};

window.addEventListener('load', STAGE.init, false);
window.addEventListener('resize', STAGE.resize, false);

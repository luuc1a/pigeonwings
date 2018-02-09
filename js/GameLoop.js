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
var GAME = {
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

    imagesStillLoading : 0,

    init: function() {
		if(LOGGING.level >= 1) console.log("called GAME.init()");
		   
        // the proportion of width to height
        GAME.RATIO = GAME.WIDTH / GAME.HEIGHT;
        // these will change when the screen is resize
        GAME.currentWidth = GAME.WIDTH;
        GAME.currentHeight = GAME.HEIGHT;
        // this is our canvas element
        GAME.canvas = document.getElementsByTagName('canvas')[0];
        // it's important to set this
        // otherwise the browser will
        // default to 320x200
        GAME.canvas.width = GAME.WIDTH;
        GAME.canvas.height = GAME.HEIGHT;
        // the canvas context allows us to 
        // interact with the canvas api
        GAME.ctx = GAME.canvas.getContext('2d');
        // we need to sniff out android & ios
        // so we can hide the address bar in
        // our resize function
        //GAME.ua = navigator.userAgent.toLowerCase();
        //GAME.android = GAME.ua.indexOf('android') > -1 ? true : false;
        //GAME.ios = ( GAME.ua.indexOf('iphone') > -1 || GAME.ua.indexOf('ipad') > -1  ) ? true : false;

        GAME.entities.push(new GAME.Brenda());

        // listen for clicks
        window.addEventListener('click', function(e) {
            e.preventDefault();
            GAME.Input.set(e);
        }, false);

        // listen for touches
        window.addEventListener('touchstart', function(e) {
            e.preventDefault();
            // the event object has an array
            // called touches, we just want
            // the first touch
            GAME.Input.set(e.touches[0]);
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
        GAME.resize();

		// loop asset loading
		GAME.assetLoadingLoop();
		
    },

	loadImage : function(imagePath) {
		if(LOGGING.level >= 1) console.log("called GAME.loadImage( " + imagePath + " )");
		
		var image = new Image();
		GAME.imagesStillLoading += 1;
    
		image.onload = function() {
			GAME.imagesStillLoading -= 1;
		};
		
		image.src = imagePath;
		    
		return image;
	},

	assetLoadingLoop : function() {
		if(LOGGING.level >= 1) console.log("called GAME.assetLoadingLoop");

		if (GAME.imagesStillLoading > 0){
			if(LOGGING.level >= 1) console.log("GAME.imagesStillLoading = " + GAME.imagesStillLoading +" > 0");
			window.setTimeout(GAME.assetLoadingLoop, 1000 / 60);
		}
		else {
			GAME.loop();
		}
	},

    resize: function() {
  		if(LOGGING.level >= 1) console.log("called GAME.resize()");

        GAME.currentHeight = window.innerHeight;
        // resize the width in proportion
        // to the new height
        GAME.currentWidth = GAME.currentHeight * GAME.RATIO;

        // this will create some extra space on the
        // page, allowing us to scroll pass
        // the address bar, and thus hide it.
        //if (GAME.android || GAME.ios) {
        //    document.body.style.height = (window.innerHeight + 50) + 'px';
        //}

        // set the new canvas style width & height
        // note: our canvas is still 320x480 but
        // we're essentially scaling it with CSS
        GAME.canvas.style.width = GAME.currentWidth + 'px';
        GAME.canvas.style.height = GAME.currentHeight + 'px';

        // the amount by which the css resized canvas
        // is different to the actual (480x320) size.
        GAME.scale = GAME.currentWidth / GAME.WIDTH;
        // position of canvas in relation to
        // the screen
        GAME.offset.top = GAME.canvas.offsetTop;
        GAME.offset.left = GAME.canvas.offsetLeft;

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
		if(LOGGING.level >= 2) console.log("called GAME.update()");
        // if the user has tapped the screen
        if (GAME.Input.tapped) {
            if(LOGGING.level >= 2) console.debug("GAME.Input.tapped is true")
            // keep track of taps; needed to 
            // calculate accuracy
            GAME.score.taps += 1;
            // add a new touch
            GAME.entities.push(new GAME.Touch(GAME.Input.x, GAME.Input.y));
            // set tapped back to false
            // to avoid spawning a new touch
            // in the next cycle
            GAME.Input.tapped = false;
        }

        // cycle through all entities and update as necessary
        for (var i = 0; i < GAME.entities.length; i += 1) {
            GAME.entities[i].update();
            // delete from array if remove property
            // flag is set to true
            if (GAME.entities[i].remove) {
                GAME.entities.splice(i, 1);
            }
        }

    },


    // this is where we draw all the entities
    render: function() {
		if(LOGGING.level >= 2) console.log("called GAME.render()");
        // draw the background
        GAME.Draw.rect(0, 0, GAME.WIDTH, GAME.HEIGHT, '#036');
        GAME.Draw.circle(
                        GAME.WIDTH/2, 
                        GAME.HEIGHT/2,
                        10, 
                        '#fff'); 

        // cycle through all entities and render to canvas
        for (var i = 0; i < GAME.entities.length; i += 1) {
            GAME.entities[i].render();
        }

        // display scores
        GAME.Draw.text('Hit: ' + GAME.score.hit, 20, 30, 14, '#fff');
        GAME.Draw.text('Escaped: ' + GAME.score.escaped, 20, 50, 14, '#fff');
        GAME.Draw.text('Accuracy: ' + GAME.score.accuracy + '%', 20, 70, 14, '#fff');

    },


    // the actual loop
    // requests animation frame
    // then proceeds to update
    // and render
    loop: function() {
		if(LOGGING.level >= 2) console.log("called GAME.loop()");

        requestAnimFrame( GAME.loop );

        GAME.update();
        GAME.render();

    }

};


// abstracts various canvas operations into
// standalone functions
GAME.Draw = {

    clear: function() {
        GAME.ctx.clearRect(0, 0, GAME.WIDTH, GAME.HEIGHT);
    },


    rect: function(x, y, w, h, col) {
        GAME.ctx.fillStyle = col;
        GAME.ctx.fillRect(x, y, w, h);
    },

    circle: function(x, y, r, col) {
        GAME.ctx.fillStyle = col;
        GAME.ctx.beginPath();
        GAME.ctx.arc(x + 5, y + 5, r, 0,  Math.PI * 2, true);
        GAME.ctx.closePath();
        GAME.ctx.fill();
    },


    text: function(string, x, y, size, col) {
        GAME.ctx.font = 'bold '+size+'px Monospace';
        GAME.ctx.fillStyle = col;
        GAME.ctx.fillText(string, x, y);
    },
    
    image: function( sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight) {
        GAME.ctx.drawImage(
            GAME.img_brenda, 
            //source rectangle
            sx, sy, sWidth, sHeight,
            //desitnation rect
            dx, dy, dWidth, dHeight );
    }

};

GAME.Input = {

    x: 0,
    y: 0,
    tapped :false,

    set: function(data) {
        this.x = (data.pageX - GAME.offset.left) / GAME.scale;
        this.y = (data.pageY - GAME.offset.top) / GAME.scale;
        this.tapped = true;

    }

};

GAME.Brenda = function(){ 

	this.brenda = new Brenda();
	this.update = function(){this.brenda.update();};
	this.render = function(){this.brenda.render();};
		
};

GAME.Touch = function(x, y) {

    if(LOGGING.level >= 1) console.log("called GAME.Touch: x = " + x + ", y = " + y );

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
        GAME.Draw.circle(this.x, this.y, this.r, 'rgba(255,0,0,'+this.opacity+')');
    };

};

window.addEventListener('load', GAME.init, false);
window.addEventListener('resize', GAME.resize, false);


var Mouse = {
    clickable : true,
    position : { x : 0, y : 0 },
    leftDown : false
};

function handleMouseDown(evt) {
    if (Mouse.clickable && evt.which === 1) {
        Mouse.leftDown = true;
        Mouse.position.x=evt.clientX;
        Mouse.position.y=evt.clientY;
        if(myCurrentMove.isDone()) myCurrentMove._next=true;
        else myCurrentMove.setPicID({picID: myPlayingField.isTouched({x:Mouse.position.x, y:Mouse.position.y}) });;
    }
}

function handleMouseUp(evt) {
    if (evt.which === 1)
        Mouse.leftDown = false;
}
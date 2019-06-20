function rgb(r, g, b){
	return 'rgb(' + Math.floor(r) + ', '
		+ Math.floor(g) + ', '
		+ Math.floor(b) + ')';
}

function rgba(r, g, b, a) {
    return 'rgba(' + Math.floor(r) + ', '
        + Math.floor(g) + ', '
        + Math.floor(b) + ', '
        + a + ')';
}

class Board {
	constructor(size){
		this.size = size;
		this.score = 0;
        var l = new Array(size);
        for (var x = 0; x < this.size; ++x) {
            l[x] = 0;
        }
        this.board = [];
        for (var x = 0; x < this.size; ++x) {
            this.board.push(l.slice(0));
        }
    }

    operationDown() {
        var operated = false;
        for (var i = 0; i < this.size; ++i) {
            var visit = [];
            for (var j = this.size - 2; j > -1; --j) {
                for (var k = j; k < this.size - 1; ++k) {
                    if (this.board[i][k + 1] == 0) {
                        if (this.board[i][k] != 0) {
                            operated = true;
                            this.board[i][k + 1] = this.board[i][k];
                            this.board[i][k] = 0;
                        }
                    } else if (this.board[i][k + 1] == this.board[i][k] &&
                        visit.indexOf(k + 1) == -1 && visit.indexOf(k) == -1) {
                        ++this.board[i][k + 1];
                        this.score += 1 << this.board[i][k + 1];
                        this.board[i][k] = 0;
                        visit.push(k + 1);
                        operated = true;
                    } else {
                        break;
                    }
                }
            }
        }
		document.getElementById('last-move').textContent = 'Down';
        return operated;
    }

    operationUp() {
        var operated = false;
        for (var i = 0; i < this.size; ++i) {
            var visit = [];
            for (var j = 1; j < this.size; ++j) {
                for (var k = j; k > 0; --k) {
                    if (this.board[i][k - 1] == 0) {
                        if (this.board[i][k] != 0) {
                            operated = true;
                            this.board[i][k - 1] = this.board[i][k];
                            this.board[i][k] = 0;
                        }
                    } else if (this.board[i][k - 1] == this.board[i][k] &&
                        visit.indexOf(k - 1) == -1 && visit.indexOf(k) == -1) {
                        ++this.board[i][k - 1];
                        this.score += 1 << this.board[i][k - 1];
                        this.board[i][k] = 0;
                        visit.push(k - 1);
                        operated = true;
                    } else {
                        break;
                    }
                }
            }
        }
		document.getElementById('last-move').textContent = 'Up';
        return operated;
    }

    operationRight() {
        var operated = false;
        for (var j = 0; j < this.size; ++j) {
            var visit = [];
            for (var i = this.size - 2; i > -1; --i) {
                for (var k = i; k < this.size - 1; ++k) {
                    if (this.board[k + 1][j] == 0) {
                        if (this.board[k][j] != 0) {
                            operated = true;
                            this.board[k + 1][j] = this.board[k][j];
                            this.board[k][j] = 0;
                        }
                    } else if (this.board[k + 1][j] == this.board[k][j] &&
                        visit.indexOf(k + 1) == -1 && visit.indexOf(k) == -1) {
                        ++this.board[k + 1][j];
                        this.score += 1 << this.board[k + 1][j];
                        this.board[k][j] = 0;
                        visit.push(k + 1);
                        operated = true;
                    } else {
                        break;
                    }
                }
            }
        }
		document.getElementById('last-move').textContent = 'Right';
        return operated;
    }

    operationLeft() {
        var operated = false;
        for (var j = 0; j < this.size; ++j) {
            var visit = [];
            for (var i = 1; i < this.size; ++i) {
                for (var k = i; k > 0; --k) {
                    if (this.board[k - 1][j] == 0) {
                        if (this.board[k][j] != 0) {
                            operated = true;
                            this.board[k - 1][j] = this.board[k][j];
                            this.board[k][j] = 0;
                        }
                    } else if (this.board[k - 1][j] == this.board[k][j] &&
                        visit.indexOf(k - 1) == -1 && visit.indexOf(k) == -1) {
                        ++this.board[k - 1][j];
                        this.score += 1 << this.board[k - 1][j];
                        this.board[k][j] = 0;
                        visit.push(k + 1);
                        operated = true;
                    } else {
                        break;
                    }
                }
            }
        }
		document.getElementById('last-move').textContent = 'Left';
        return operated;
    }

    isContinue() {
        for (var i = 0; i < this.size; ++i) {
            for (var j = 0; j < this.size; ++j) {
                if (j + 1 >= 0 && j + 1 < this.size &&
                    this.board[i][j] == this.board[i][j + 1] ||
                    this.board[i][j] == 0 ||
                    i + 1 >= 0 && i + 1 < this.size &&
                    this.board[i + 1][j] == this.board[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    createNewNum() {
        var zeroOrOne = Math.floor(Math.random() * 4);
        var newNum = 1;
        if (zeroOrOne == 0) {
            newNum = 2;
        }
        var zeroNum = 0;
        for (var i = 0; i < this.size; ++i) {
            for (var j = 0; j < this.size; ++j) {
                if (this.board[i][j] == 0) {
                    ++zeroNum;
                }
            }
        }
        var index = Math.floor(Math.random() * (zeroNum - 1));
        for (var i = 0; i < this.size; ++i) {
            for (var j = 0; j < this.size; ++j) {
                if (this.board[i][j] == 0 && index == 0) {
                    this.board[i][j] = newNum;
                    return;
                } else if (this.board[i][j] == 0) {
                    --index;
                }
            }
        }
    }

    getPoints() { // No use
        var point = 0;
        for (var i = 0; i < this.size; ++i) {
            for (var j = 0; j < this.size; ++j) {
                if (this.board[i][j] != 0) {
                    point += 1 << this.board[i][j];
                }
            }
        }
        return point;
    }
}

class Display {
    constructor(name = 'null', width = 400, height = 600, size = 4,
        initMap = [], converted = false) {
        this.canvas = document.getElementById('game-board');
        Display.makeColor();
        this.ctx = this.canvas.getContext('2d');
        this.blockFont = Math.floor(width / size * 0.32) + 'px SimHei';
        //block font
        //insturction font
        //title font
        this.canvas.width = this.canvas.height = width;
        this.width = width; // ?
        this.height = height; // ?
        this.gapWidth = width * 0.09753 / size;
        this.rectWidth = (width - this.gapWidth * (size + 1)) / size;
        this.mapSize = size;
        if (converted) {
            this.rects = initMap;
        } else {
            this.rects = Display.convertMap(initMap);
        }
        this.name = name;
        this.moving = false;
        this.score = 0;
        this.instruction = '';
        this.radius = 5;
        this.speed = 1 / Display.step;
        if (size >= 6) {
            this.speed *= 2;
        }
        this.newBlocks = [];
        this.frameRate = 50;
        //console.log(this.rects);//TEST
    }

    update(state = 0, direction = 0, nextMap = null) {
        // setInterval...
        /*if (nextMap == null) {
            nextMap = Display.toInteger(this.rects);
        }*/
        nextMap = Display.convertMap(nextMap);
        this.eventControl();
        this.drawBackground();
        this.drawBlocks();
        //...
        if (state == 1 && !this.moving) {
            this.moving = true;
        }
        if (this.moving) {
            if (!this.move(direction)) {
                this.moving = false;
                this.newBlocks = [];
                this.syncMap(nextMap);
            }
        }
    }

    updateScore(score) {
        this.score = score;
    }

    move(direction) {
        var haveToMove = false;
        if (direction == 0) {
            return;
        }
        if (direction == 1) {
            this.rects.sort((r1, r2) => r1[1] <= r2[1]);
            for (var i = 0; i < this.rects.length; ++i) {
                if (this.up(this.rects[i]) && this.newBlocks.indexOf(this.rects[i]) == -1) {
                    haveToMove = true;
                    this.rects[i][1] -= this.speed;
                }
            }
        } else if (direction == 2) {
            this.rects.sort((r1, r2) => r1[1] >= r2[1]);
            for (var i = 0; i < this.rects.length; ++i) {
                if (this.down(this.rects[i]) && this.newBlocks.indexOf(this.rects[i]) == -1) {
                    haveToMove = true;
                    this.rects[i][1] += this.speed;
                }
            }
        } else if (direction == 3) {
            this.rects.sort((r1, r2) => r1[0] <= r2[0]);
            for (var i = 0; i < this.rects.length; ++i) {
                if (this.left(this.rects[i]) && this.newBlocks.indexOf(this.rects[i]) == -1) {
                    haveToMove = true;
                    this.rects[i][0] -= this.speed;
                }
            }
        } else if (direction == 4) {
            this.rects.sort((r1, r2) => r1[0] >= r2[0]);
            for (var i = 0; i < this.rects.length; ++i) {
                if (this.right(this.rects[i]) && this.newBlocks.indexOf(this.rects[i]) == -1) {
                    haveToMove = true;
                    this.rects[i][0] += this.speed;
                }
            }
        }
        var addRects = [];
        for (var i = 0; i < this.rects.length; ++i) {
            var rect = this.rects[i];
            if (this.countOverlap(rect) == 2) {
                addRects.push([Math.round(rect[0]),
                    Math.round(rect[1]),
                    rect[2] + 1
                ]);
                this.newBlocks.push([Math.round(rect[0]),
                    Math.round(rect[1]),
                    rect[2] + 1
                ]);
                var newRects = [];
                for (var j = 0; j < this.rects.length; ++j) {
                    var r = this.rects[j];
                    if (!this.overlap(r, rect)) {
                        newRects.push([
                            Math.round(r[0]),
                            Math.round(r[1]),
                            r[2]
                        ]);
                    }
                }
                this.rects = newRects;
            }
        }
        this.rects = this.rects.concat(addRects);
        return haveToMove;
    }

    syncMap(nextMap) {
        this.rects = nextMap;
    }

    setGameOverDisplay() {
        //TODO
    }

    countOverlap(rect) {
        var count = 0;
        for (var i = 0; i < this.rects.length; ++i) {
            var r = this.rects[i];
            if (this.overlap(r, rect)) {
                ++count;
            }
        }
        return count;
    }

    overlap(r1, r2) {
        return Math.abs(r1[0] - r2[0]) < this.speed &&
            Math.abs(r1[1] - r2[1]) < this.speed;
    }

    eventControl() {
        //TODO
    }

    drawBackground() {
        this.ctx.lineWidth = 0;
        Display.drawRoundRect(this.ctx, Display.canvasColor, 0, 0, this.canvas.width, this.canvas.height, this.radius);
        for (var h = 0; h < this.mapSize; ++h) {
            for (var w = 0; w < this.mapSize; ++w) {
                var top = this.heightCoor(h);
                var left = this.widthCoor(w);
                Display.drawRoundRect(this.ctx, Display.strokeColor, top, left, this.rectWidth, this.rectWidth, this.radius);
            }
        }
    }

    drawBlocks() {
        for (var i = 0; i < this.rects.length; ++i) {
            this.drawABlock(this.rects[i], this.gapWidth / 2);
        }
    }

    drawABlock(rect, z = 5, textUsed = 0) {
        var x1 = this.heightCoor(rect[0]);
        var y1 = this.widthCoor(rect[1]);
        var currentColor = rgba(255, 255, 255, 100 / 255);
        if (rect[2] <= Display.colors.length) {
            currentColor = Display.colors[rect[2] - 1];
        }
        var currentText = (1 << rect[2]).toString();
        if (rect[2] <= Display.texts[textUsed].length) {
            currentText = Display.texts[textUsed][rect[2] - 1];
        }
        Display.drawRoundRect(this.ctx, Display.darker(currentColor, 20), x1, y1,
            this.rectWidth, this.rectWidth, this.radius);
        Display.drawRoundRect(this.ctx, currentColor, x1, y1 - z,
            this.rectWidth, this.rectWidth, this.radius);
        //console.log("x1=" + x1 + ", y1=" + y1 + ", color=" + currentColor); //TEST
        //console.log("x1=" + x1 + ", y1=" + y1 + ", shadow=" + Display.darker(currentColor, 20)); //TEST
        this.ctx.textAlign = 'center';
        this.ctx.textBaseline = 'middle';
        this.ctx.font = this.blockFont;
        if (rect[2] <= 2 || rect[2] > Display.colors.length) {
            this.ctx.fillStyle = Display.btColor1;
        } else {
            this.ctx.fillStyle = Display.btColor2;
        }
        this.ctx.fillText(currentText, x1 + this.rectWidth / 2,
            y1 + this.rectWidth / 2 - z);
    }

    heightCoor(h) {
        return (h + 1) * this.gapWidth + h * this.rectWidth;
    }
    widthCoor(w) {
        return (w + 1) * this.gapWidth + w * this.rectWidth;
    }

    up(rect) {
        return rect[1] > 0 && this.noObstacle(rect[0], rect[1] - 1, rect[2]);
    }

    down(rect) {
        return rect[1] < this.mapSize - 1 && this.noObstacle(rect[0], rect[1] + 1, rect[2]);
    }

    left(rect) {
        return rect[0] > 0 && this.noObstacle(rect[0] - 1, rect[1], rect[2]);
    }

    right(rect) {
        return rect[0] < this.mapSize - 1 && this.noObstacle(rect[0] + 1, rect[1], rect[2]);
    }

    noObstacle(x, y, z) {
        var count = 0;
        for (var i = 0; i < this.rects.length; ++i) {
            var r = this.rects[i];
            if (this.overlap([x, y, z], r) &&
                (this.newBlocks.indexOf(r) != -1 ||
                    z != r[2])) {
                ++count;
            }
        }
        return count == 0;
    }

    exit() {
        //?
    }
}
Display.texts = [
    [
        '炼气', // 2
        '筑基', // 4
        '开光', // 8
        '胎息', // 16
        '辟谷', // 32
        '金丹', // 64
        '元婴', // 128
        '出窍', // 256
        '分神', // 512
        '合体', // 1024
        '大乘', // 2048
        '渡劫' // 4096
    ],
    [
        '2',
        '4',
        '8',
        '16',
        '32',
        '64',
        '128',
        '256',
        '512',
        '1024',
        '2048',
        '4096',
    ],
];
Display.backgroundColor = rgb(250, 248, 239);
Display.canvasColor = rgb(187, 173, 160); //rgba(250, 150, 100, 20/255)
Display.strokeColor = rgb(205, 193, 180); //rgba(255, 250, 150, 1)
Display.textColor = rgb(119, 110, 101);
Display.scoreCanvasColor = rgb(187, 173, 160);
Display.scorePreColor = rgb(231, 228, 218);
Display.scoreColor = rgb(255, 255, 255);
Display.btColor1 = rgb(147, 137, 128);
Display.btColor2 = rgb(249, 246, 242);
Display.step = 8;
Display.makeColor = function () {
    Display.colors = [
        rgb(238, 228, 218), // 2
        rgb(237, 224, 220), // 4
        rgb(242, 177, 121), // 8
        rgb(245, 149, 99), // 16
        rgb(246, 124, 95), // 32
        rgb(246, 94, 59), // 64
        rgb(237, 207, 114), // 128
        rgb(237, 204, 97), // 256
        rgb(237, 200, 80), // 512
        rgb(228, 185, 60), // 1024
        rgb(237, 197, 40), // 2048
        rgb(230, 224, 20), // 4096
    ]
}
Display.darker = function (color, value) {
    var colorArray = color.split(' ');
    //console.log(colorArray);//TEST
    if (colorArray.length == 3) {
        var r = parseInt(colorArray[0].substring(4, colorArray[0].length - 1));
        var g = parseInt(colorArray[1].substring(0, colorArray[1].length - 1));
        var b = parseInt(colorArray[2].substring(0, colorArray[2].length - 1));
        r = Math.max(r - value, 0);
        g = Math.max(g - value, 0);
        b = Math.max(b - value, 0);
        return rgb(r, g, b);
    } else if (colorArray.length == 4) {
        var r = parseInt(colorArray[0].substring(5, colorArray[0].length - 1));
        var g = parseInt(colorArray[1].substring(0, colorArray[1].length - 1));
        var b = parseInt(colorArray[2].substring(0, colorArray[2].length - 1));
        var a = parseInt(colorArray[3].substring(0, colorArray[3].length - 1));
        r = Math.max(r - value, 0);
        g = Math.max(g - value, 0);
        b = Math.max(b - value, 0);
        a = Math.min(a + value / 255, 1);
        return rgba(r, g, b, a);
    }
}
Display.convertMap = function (initMap) {
    var ans = [];
    for (var x = 0; x < initMap.length; ++x) {
        for (var y = 0; y < initMap[x].length; ++y) {
            if (initMap[x][y] > 0) {
                ans.push([x, y, initMap[x][y]]);
            }
        }
    }
    return ans;
}
Display.drawRoundRect = function (ctx, color, left, top, width, height, radius = 5) {
    ctx.fillStyle = color;
    ctx.beginPath();
    ctx.moveTo(left, top + radius);
    ctx.arcTo(left, top, left + radius, top, radius);
    ctx.lineTo(left + width - radius, top);
    ctx.arcTo(left + width, top, left + width, top + radius, radius);
    ctx.lineTo(left + width, top + height - radius);
    ctx.arcTo(left + width, top + height, left + width - radius, top + height, radius);
    ctx.lineTo(left + radius, top + height);
    ctx.arcTo(left, top + height, left, top + height - radius, radius);
    ctx.lineTo(left, top + radius);
    ctx.fill();
}

class Game {
    constructor(name = 'null', width = 400, height = 600, size = 4) {
        this.name = name;
        this.board = new Board(size);
        this.board.createNewNum();
        this.board.createNewNum();
        this.blocks = this.board.board;
        this.display = new Display(name, width, height, size, this.blocks);
        this.activated = true;
        this.state = 1; // 0:moving, 1:can move, 2:game over
        this.mapSize = size;
        console.log("size:" + this.mapSize.toString());
    }

    *run() {
        console.log('Running');//TEST
        var direction = 0;
        var keyReleased = true;
        while (this.activated) {
            //event?
            if (this.state == 2) {
                yield {
                    score: this.display.score,
                    continue : this.board.isContinue,
                };
                continue;
            }
            this.state = 0;
            var key = this.keyCode;
            if (!this.display.moving && keyReleased) {
                keyReleased = false;
                this.state = 1;
                direction = this.checkDirection(key);
                if (direction == 0) {
                    this.state = 0;
                    keyReleased = true;
                }
                this.display.updateScore(this.board.score);
                this.move(direction);
            }
            if (!this.display.moving && this.checkDirection(key) == 0) {
                keyReleased = true;
            }
            this.display.update(this.state, direction, this.board.board);
            if (!this.board.isContinue() && this.dialog == null && direction == 0) {
                this.over();
            }
            yield {
                score: this.display.score,
                continue : this.board.isContinue,
            };
        }
    }

    move(direction) {
        if (direction == 4 && this.board.operationRight() ||
            direction == 3 && this.board.operationLeft() ||
            direction == 2 && this.board.operationDown() ||
            direction == 1 && this.board.operationUp()) {
            this.board.createNewNum();
            document.getElementById('score').textContent = this.board.score.toString();
            //console.log(this.board.board);//TEST
        }
    }

    checkDirection(key) {
        if (key == 38) {
            return 1;//UP
        } else if (key == 40) {
            return 2;//DOWN
        } else if (key == 37) {
            return 3;//LEFT
        } else if (key == 39) {
            return 4;//RIGHT
        } else {
            return 0;
        }
    }

    over() {
    	console.log('Game over');
        this.state = 2;
        document.getElementById('score').textContent = this.board.score.toString();
		document.getElementById('game-over').textContent = ',(game over)';
		document.getElementById('submit-score').removeAttribute('hidden');
		if(confirm('Game Over, score = ' + this.board.score.toString())){
			console.log("Confirm ok");
			this.submitFunc();
		}
		console.log("After confirm");
    }
    

    submitFunc() {
    	myAjaxPost({
    		url: 'submitScore.jsp',
    		data: {
    			'size': document.getElementById('size').textContent,
    			'score': document.getElementById('score').textContent,
    		},
    		success: function (data){
                try {
                    document.getElementById('submit-msg').textContent = data.responseText;
                    document.getElementById('btn-submit').setAttribute('hidden', 'true');
                }
                catch (exception) {
                    document.getElementById('submit-msg').textContent = exception;
                }
    		},
    		fail: function (data){
    			console.log(data);
    			alert('Error, fail to submit.', 'Error', 'OK');
    		}
    	});
    }
}


function myAjaxPost(obj){
	var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4) {
            if (xmlhttp.status >= 200 && xmlhttp.status < 300 || xmlhttp.status == 304) {
                obj.success(xmlhttp);
            } else {
                obj.fail(xmlhttp);
            }
        };
    };
    var param;
    xmlhttp.open("post", obj.url, true);
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); 
    var first = true;
    for (var i in obj.data) {
        if (first) {
            param = i + '=' + obj.data[i];
            first = false;
        } else {
            param += '&' + i + '=' + obj.data[i];
        }
    }
    xmlhttp.send(param);
}


window.onload = function() {

    document.body.style.backgroundColor = Display.backgroundColor;
    var scoreDom = document.getElementById('score');
    var game = new Game('2048', 400, 600, parseInt(document.getElementById('size').textContent));

    window.onkeydown = function(event) {
        //console.log('Key down:' + event.keyCode.toString());//TEST
        game.keyCode = event.keyCode;
    }
    
    window.onkeyup = function (event) {
        //console.log('Key up:' + event.keyCode.toString());//TEST
        game.keyCode = null;
    }
    
    document.getElementById('btn-submit').onclick = game.submitFunc;

    var runIter = game.run();
    function setScore() {
        var result = runIter.next();
    }
    var interval = window.setInterval(setScore, 1 / game.frameRate);
}

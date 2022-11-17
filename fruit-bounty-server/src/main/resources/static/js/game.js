"use strict";

var TIMER_INTERVAL = 90;
var NEXT_QUEST_IDLE = 5000;
var OUTPUT_AFTER_HINT_SCROLL = 143;

var imgGameScreen = new Image();
var imgDefeat = new Image();
var imgVictory = new Image();
var imgBtnNext = new Image();
var imgBtnClose = new Image();

var timerId;
var game;
var oldGame;
var currentIntroPage;
var maxTimerProgressWidth;
var gameWindowMayBeClosed;
var nextQuestEnableTimerId;
var gameFinishedWithWin = false;

function initGameUi() {
  maxTimerProgressWidth = $('#time-progress').width();

  $('#player-surrender').on("click", surrenderClicked);
  $('#subwindow-btn-next').on("click", onSubwindowClose);
  $('#subwindow-close').on("click", hideGameResultSubwindow);
  $('#showEduVideoBtn').on("click", onShowEduVideo);
  $('#closeEduVideoWindowBtn').on("click", onCloseEduVideo);
  $('#closeGame').on("click", onSubwindowClose);

  $('#firstPage').on("click", firstPage);
  $('#prevPage').on("click", prevPage);
  $('#nextPage').on("click", nextPage);
  $('#lastPage').on("click", lastPage);

  $('#next-quest').on("click", onNextQuest);
  $('body').on('click', '.answerOption', onAnswerClicked);

  preloadGameSecondImages();
  setImagesOnTags();
}

function processGameStartedOperation(newGame) {
  showAdds();

  window.game = newGame;
  $('.background-img').attr('src', imgGameScreen.src);

  resetGameInfo();
  resetGameRequestUi();
  switchToGameWindow();
  $('#output').scrollTop(0);

  resetProps(newGame);
  setAnswerClicks(newGame);
  setHookClicks(newGame);
  setUi(newGame);
  processGameChangedOperation(newGame);

  if (game.showTables) {
    paintStatusTables(newGame);
    turnProgress();
  } else {
    turnNotepad();
  }
}

function resetGameInfo() {
  gameWindowMayBeClosed = false;
  $('#subwindow-background').hide();
  $('#closeGame').hide();
  $('#see-solution').hide();
  showEduVideoBtn();
  hideConfirmWindow();

  if (COMPOSITE_GAMES.includes(game.questType)) {
    $('#rewindPages').show();
    $('#next-quest').show();
  } else {
    $('#rewindPages').hide();
    $('#next-quest').hide();
  }
}

function showEduVideoBtn() {
  $('#showEduVideoBtn').hide();
  if (getVideoRefFull() !== undefined) {
    $('#showEduVideoBtn').show();
  }
}

function resetGameRequestUi() {
  isWaitingForOpponent = false;
  updatePlayButtonText();
  $("#play-loader").hide();
  stopDisplayTips();
}

function switchToGameWindow() {
  hidePlayQuests();
  $("#lobby-window").hide();
  $("#game-window").show();
}

function resetProps(game) {
  currentIntroPage = 0;
}

function setAnswerClicks(game) {
  $('#answers').html("");
  for (var i = 0; i < game.answerOptions.length; i++) {
    var answerOption = game.answerOptions[i];
    $('#answers').append("<a class='answerOption dropdown-item' href='#' >" + answerOption + "</a>");
    if (i < game.answerOptions.length - 1) {
      $('#answers').append('<div class="dropdown-divider"></div>');
    }
  }
}

function setHookClicks(game) {
  if (game.hooks === null) {
    return;
  }

  $('#hooks').html("");
  for (var i = 0; i < game.hooks.length; i++) {
    var hookName = game.hooks[i].name;
    $('#hooks').append("<a class='dropdown-item' href='#' onclick='onHookClicked(\"" + hookName + "\")'>" + hookName + "</a><br>");
  }
}

function setUi(game) {
  if (game.solution) {
    $('#see-solution').show();
  }
}

function preloadGameSecondImages() {
  imgGameScreen.src = '../img/components/game-background.png';
  imgDefeat.src = '../img/components/window_defeat.' + browserLocale + '.png';
  imgVictory.src = '../img/components/window_victory.' + browserLocale + '.png';
  imgBtnNext.src = '../img/components/button_next.' + browserLocale + '.png';
  imgBtnClose.src = '../img/components/close_btn.png';
}

function setImagesOnTags() {
  $('#subwindow-btn-next').css("background-image", "url(" + imgBtnNext.src + ")");
}

function processGameChangedOperation(newGame) {
  oldGame = window.game;
  window.game = newGame;
  killGameTimer();

  newGame.incomingTime = Date.now();

  paintGame(newGame);
  paintOutput(newGame);

  timerId = setInterval(
    function() {
      paintGame(newGame);
    },
    TIMER_INTERVAL);
}

function paintStatusTables(game) {
  var v = '';
  var firstTable = 1;
  var secondTable = 0;

  var list = '<select name="hero">' +
    '          <option value="s0" selected></option>' +
    '          <option value="s1">➖</option>' +
    '          <option value="s2">➕</option>' +
    '        </select>';

  for (var i = 0; i < game.tables.length; i++) {
    v += "<table class='status-table table table-bordered table-condensed table-sm table-content-centered background-colored rounded-borders'>";
    v += '<tr>';
    v += '<td></td>';

    var table = game.tables[i];
    for (var j = 0; j < table[firstTable].length; j++) {
      v += '<td>' + wrapStatusTd(table[firstTable][j]) + '</td>';
    }
    v += '</tr>';

    for (var j = 0; j < table[secondTable].length; j++) {
      v += '<tr>';
      v += '<td>' + wrapStatusTd(table[secondTable][j]) + '</td>';
      for (var k = 0; k < table[firstTable].length; k++) {
        v += '<td>' + wrapStatusTd(list) + '</td>';
      }
      v += '</tr>';
    }

    v += "</table>";
  }

  $('#helpTables').html(v);
}

function wrapStatusTd(td) {
  return "<span class='status-td background-colored'>" + td + "</span>";
}

function showConfirmWindow(yesFunction, noFunction, text) {
  $("#warnwindow-text").text(text);

  $("#warnwindow-yes").on("click", yesFunction);
  $("#warnwindow-no").on("click", noFunction);

  $("#warnwindow-background").show();
}

function hideConfirmWindow() {
  $("#warnwindow-background").hide();
}

function onShowEduVideo(e) {
  $("#eduVideoWindowIframeContent").html(getVideoRefFull());
  $("#eduVideoWindow").show();
}

function getVideoRefFull() {
  if (game.questType === "MISSION" && userInfo.mission === 1) {
    return getVideoRef('456239022&hash=c33b0d7792014236');
  } else if (game.questType === "MISSION" && userInfo.mission === 2) {
    return getVideoRef('456239023&hash=311a7a5a3df212a4');
  } else if (game.questType === "BRIEF_1") {
    return getVideoRef('456239024&hash=cf4e87aac6034480');
  } else if (game.questType === "MISSION" && userInfo.mission === 8) {
    return getVideoRef('456239025&hash=2dabd585f8464cc3');
  } else if (game.questType === "MISSION" && userInfo.mission === 9) {
    return getVideoRef('456239026&hash=e38c86a231519634');
  } else if (game.questType === "BRIEF_3") {
    return getVideoRef('456239027&hash=997a23cf5fb7a103');
  }
}

function getVideoRef(videRef) {
  return '<iframe src="https://vk.com/video_ext.php?oid=-217243459&id=\n' +
      videRef +
      '&hd=2&autoplay=1" width="360" height="570" allow="autoplay; encrypted-media; fullscreen; picture-in-picture;" frameborder="0" allowfullscreen></iframe>';
}

function onCloseEduVideo(e) {
  $("#eduVideoWindow").hide();
  $("#eduVideoWindowIframeContent").html('');
}

function onSubwindowClose(e) {
  if (!gameFinishedWithWin && getRandomInt(1, 101) <= 75) {
    showAdds();
  }

  $('.background-img').attr('src', imgLobbyScreen.src);

  $("#game-window").hide();
  $("#lobby-window").show();
  $('#helpTables').html("");

  if (SHOW_FEEDBACK_FOR_LEVELS.includes(userInfo.mission)) {
    turnFeedback();
  } else {
    turnChat();
  }
}

function hideGameResultSubwindow(e) {
  $('#closeGame').show();
  $('#subwindow-background').hide();
}

function surrenderClicked() {
  showConfirmWindow(onSurrender, hideConfirmWindow, localize('concede-confirmation'));
}

function firstPage() {
  currentIntroPage = 0;
  showCurrentIntroPage();
}

function lastPage() {
  currentIntroPage = game.pages.length - 1;
  showCurrentIntroPage();
}

function prevPage() {
  if (currentIntroPage > 0) {
    currentIntroPage--;
    showCurrentIntroPage();
  }
}

function nextPage() {
  if (game === undefined) {
    return;
  }

  if (currentIntroPage < game.pages.length - 1) {
    currentIntroPage++;
    showCurrentIntroPage();
  }
}

function hintBtn() {
  setTimeout(function() {
    $('#output').scrollTop($('#output').scrollTop() + OUTPUT_AFTER_HINT_SCROLL);
  }, 100);
}

function onNextQuest() {
  if (game === undefined) {
    return;
  }

  $('#next-quest').attr("disabled", true);

  var payload = {
    type: NEXT_QUEST_ACTION
  };
  sendGameAction(payload);

  nextQuestEnableTimerId = window.setTimeout(enableNextQuest, NEXT_QUEST_IDLE);
}

function enableNextQuest() {
  window.clearTimeout(nextQuestEnableTimerId);
  $('#next-quest').removeAttr("disabled");
}

function showCurrentIntroPage() {
  $('#output').html(game.pages[currentIntroPage]);
  $('#pageNumber').html((currentIntroPage + 1) + " страница");
}

function onSurrender() {
  var surrenderPayload = {
    type: SURRENDER_GAME_ACTION
  };
  sendGameAction(surrenderPayload);

  hideConfirmWindow();
}

function killGameTimer() {
  if (timerId != null) {
    clearInterval(timerId);
  }
}

function onAnswerClicked(e) {
  e.preventDefault();

  var answerOption = $(this).text();
  var payload = {
    type: ANSWER_GAME_ACTION,
    answer: answerOption
  };
  sendGameAction(payload);
}

function onHookClicked(hookName) {
  var payload = {
    type: HOOK_GAME_ACTION,
    hook: hookName
  };
  sendGameAction(payload);
}

function sendGameAction(movePayload) {
  sendOperation(SEND_GAME_ACTION, movePayload);
}



function paintGame(game) {
  if (game == null) {
    return;
  }

  if (game.finished) {
    killGameTimer();
    gameWindowMayBeClosed = true;
    hideConfirmWindow();
  }

  if (!game.finished) {
    paintTimer(game);
  }
  paintWinner(game);
}

function paintTimer(game) {
  var moveTimeLeft = game.clientCurrentMoveTimeLeft - (Date.now() - game.incomingTime);
  var timerProgressWidth = maxTimerProgressWidth * moveTimeLeft / game.timePerMoveMs;
  $('#time-progress').width(timerProgressWidth);
}

function paintOutput(game) {
  if (game.pages.length !== oldGame.pages.length) {
    currentIntroPage = game.pages.length - 1;
  }

  showCurrentIntroPage();
}


function paintWinner(game) {
  if (!game.finished) {
    return;
  }

  var gameResult;
  if (game.win) {
    gameResult = 'win';
  } else {
    gameResult = 'defeat';
  }

  // Game points
  var player = findSelfPlayer();
  var addedScore = player.addedScore;
  if (addedScore > -1) {
    addedScore = "+" + addedScore;
  }
  var text = localize('score') + ": " + addedScore;

  // Player level
  if (addedScore > 0 && game.questType === "MISSION") {
    text += "<br> Уровень: +1";  //todo: not hardcode
    $('#subwindow-points').css("top", "42%");
  } else {
    $('#subwindow-points').css("top", "38%");
  }

  $('#subwindow-points').html(text);

  // Set background image
  if (gameResult === "win") {
    $('#subwindow-container').css("background-image", "url(" + imgVictory.src + ")");
    $('#subwindow-title').text(localize('win_caps'));
    gameFinishedWithWin = true;
  } else if (gameResult === "defeat") {
    $('#subwindow-container').css("background-image", "url(" + imgDefeat.src + ")");
    $('#subwindow-title').text(localize('defeat_caps'));
    gameFinishedWithWin = false;
  } else {
    // $('#subwindow-container').css("background-image", "url(" + imgDraw.src + ")");
    $('#subwindow-title').text('Error');
    gameFinishedWithWin = false;
  }

  $('#subwindow-background').show();
}


function findOpponentId() {
  // Works only for 2 players in game.
  if (game.players[0].id === userInfo.id) {
    return game.players[1].id;
  } else {
    return  game.players[0].id;
  }
}

function findSelfPlayer() {
  // Works only for 2 players in game.
  if (game.players[0].id === userInfo.id) {
    return game.players[0];
  } else {
    return  game.players[1];
  }
}

function getCanvasWidth() {
  var chatOutputScrolling = document.getElementById("chat-output-scrolling");
  return chatOutputScrolling.offsetWidth;
}

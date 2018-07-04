<?php

require "guest.php";
require "guide.php";
require "reserve.php";
require "message.php";
require "estimate.php";

$command = $_POST["command"];

if (strcmp($command, "createGuide") == 0) {
  createGuide();
} else if (strcmp($command, "uploadGuideImage") == 0) {
  uploadGuideImage();
} else if (strcmp($command, "updateGuide") == 0) {
  updateGuide();
} else if (strcmp($command, "getGuide") == 0) {
  getGuide();
} else if (strcmp($command, "createGuest") == 0) {
  createGuest();
 } else if (strcmp($command, "uploadGuestImage") == 0) {
  uploadGuestImage();
} else if (strcmp($command, "updateGuest") == 0) {
  updateGuest();
} else if (strcmp($command, "getGuest") == 0) {
  getGuest();
} else if (strcmp($command, "login") == 0) {
  login();
} else if (strcmp($command, "reserve") == 0) {
  reserve();
} else if (strcmp($command, "getMessage") == 0) {
  getMessage();
} else if (strcmp($command, "postMessage") == 0) {
  postMessage();
} else if (strcmp($command, "getEstimate") == 0) {
  getEstimate();
} else if (strcmp($command, "postEstimate") == 0) {
  postEstimate();
}
else {
  echo("unknown");
}

function createGuide() {

  $name = $_POST["name"];
  $nationality = $_POST["nationality"];
  $language = $_POST["language"];
  $specialty = $_POST["specialty"];
  $category = $_POST["category"];
  $message = $_POST["message"];
  $timeZone = $_POST["timeZone"];
  $applicableNumber = $_POST["applicableNumber"];
  $fee = $_POST["fee"];
  $notes = $_POST["notes"];

  $guideId = Guide::create($name, $nationality, $language, $specialty, $category, $message, $timeZone, $applicableNumber, $fee, $notes);
  if (is_null($guideId)) {
    echo(json_encode(Array("result" => "1")));
  } else {
    echo(json_encode(Array("result" => "0", "guideId" => $guideId)));
  }
}

function uploadGuideImage() {
	echo(json_encode(Array("result" => "0")));
}

function updateGuide() {

  $id = $_POST["id"];
  $name = $_POST["name"];
  $nationality = $_POST["nationality"];
  $language = $_POST["language"];
  $specialty = $_POST["specialty"];
  $category = $_POST["category"];
  $message = $_POST["message"];
  $timeZone = $_POST["timeZone"];
  $applicableNumber = $_POST["applicableNumber"];
  $fee = $_POST["fee"];
  $notes = $_POST["notes"];
  $schedules = $_POST["schedules"];

  if (Guide::update($id, $name, $nationality, $language, $specialty, $category, $message, $timeZone, $applicableNumber, $fee, $notes, $schedules)) {
    echo(json_encode(Array("result" => "0")));
  } else {
    echo(json_encode(Array("result" => "1")));
  }
}

function getGuide() {

  $ret = Array("result" => "0",
                "guides" => Guide::getGuideArray());
  echo(json_encode($ret));
}

function createGuest() {

  $name = $_POST["name"];
  $nationality = $_POST["nationality"];

  $guestId = Guest::create($name, $nationality);
  if (is_null($guestId)) {
    echo(json_encode(Array("result" => "1")));
  } else {
    echo(json_encode(Array("result" => "0", "guestId" => $guestId)));
  }
}

function uploadGuestImage() {
	echo(json_encode(Array("result" => "0")));
}

function updateGuest() {

  $id = $_POST["id"];
  $name = $_POST["name"];
  $nationality = $_POST["nationality"];

  if (Guest::update($id, $name, $nationality)) {
    echo(json_encode(Array("result" => "0")));
  } else {
    echo(json_encode(Array("result" => "1")));
  }
}

function getGuest() {

  $ret = Array("result" => "0",
                "guests" => Guest::getGuestArray());
  echo(json_encode($ret));
}

function login() {

  $id = $_POST["id"];
  if (Guide::login($id)) {
    echo(json_encode(Array("result" => "0")));
  } else {
    echo(json_encode(Array("result" => "1")));
  }
}

function reserve() {

  date_default_timezone_set('Asia/Tokyo');

  $reserveData = new ReserveData();
  $reserveData->id = $_GET["id"];
  $reserveData->requesterId = $_GET["requesterId"];
  $reserveData->guideId = $_GET["guideId"];
  $reserveData->area = $_GET["area"];
  $reserveData->date = date("YmdHis");

  if (Reserve::append($reserveData)) {
    echo(json_encode(Array("result" => "0")));
  } else {
    echo(json_encode(Array("result" => "1")));
  }
}

function getMessage() {

  $userId = $_POST["userId"];
  $data = [];
  $messageList = Message::readMessage($userId);
  foreach ($messageList as $message) {
    $data[] = Array(
        "messageId" => $message->messageId,
        "senderId" => $message->senderId,
        "receiverId" => $message->receiverId,
        "message" => $message->message,
        "date" => $message->date
    );
  }
  echo(json_encode(Array("result" => "0",
                        "messages" => $data)));
}

function postMessage() {

  date_default_timezone_set('Asia/Tokyo');

  $messageData = new MessageData();
  $messageData->messageId = $_POST["messageId"];
  $messageData->senderId = $_POST["senderId"];
  $messageData->receiverId = $_POST["receiverId"];
  $messageData->date = date('YmdHis');
  $messageData->message = $_POST["message"];
  Message::append($messageData);
  echo(json_encode(Array("result" => "0")));
}


function getEstimate() {

  $data = [];
  $estimateList = Estimate::readAll();
  foreach ($estimateList as $estimateData) {
    $data[] = Array("requesterId" => $estimateData->requesterId,
                    "guideId" => $estimateData->guideId,
                    "score" => $estimateData->score,
                    "comment" => $estimateData->comment);
  }
  $ret = Array("result" => "0", "estimates" => $data);
  echo(json_encode($ret));
}

function postEstimate() {

  $estimateData = new EstimateData();
  $estimateData->requesterId = $_GET["requesterId"];
  $estimateData->guideId = $_GET["guideId"];
  $estimateData->score = $_GET["score"];
  $estimateData->comment = $_GET["comment"];

  if (Estimate::append($estimateData)) {
    echo(json_encode(Array("result" => "0")));
  } else {
    echo(json_encode(Array("result" => "1")));
  }
}


function DebugSave($str){

	date_default_timezone_set('Asia/Tokyo');

	$d = date('Y-m-d H:i:s');
	$s = $d . " " . $str . "\r\n";
	file_put_contents("debug.txt", $s, FILE_APPEND);
}

?>

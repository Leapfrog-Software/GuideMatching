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
} else if (strcmp($command, "uploadTourImage") == 0) {
  uploadTourImage();
} else if (strcmp($command, "login") == 0) {
  login();
} else if (strcmp($command, "createReserve") == 0) {
  createReserve();
} else if (strcmp($command, "getReserve") == 0) {
  getReserve();
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

  $email = $_POST["email"];
  $name = $_POST["name"];
  $nationality = $_POST["nationality"];
  $language = $_POST["language"];
  $area = $_POST["area"];
  $keyword = $_POST["keyword"];
  $category = $_POST["category"];
  $message = $_POST["message"];
  $applicableNumber = $_POST["applicableNumber"];
  $fee = $_POST["fee"];
  $notes = $_POST["notes"];

  $guideId = Guide::create($email, $name, $nationality, $language, $area, $keyword, $category, $message, $applicableNumber, $fee, $notes);
  if (is_null($guideId)) {
    echo(json_encode(Array("result" => "1")));
  } else {
    echo(json_encode(Array("result" => "0", "guideId" => $guideId)));
  }
}

function uploadGuideImage() {

  $guideId = $_POST["guideId"];
  $suffix = $_POST["suffix"];
  $file = $_FILES['image']['tmp_name'];
  if (Guide::uploadImage($guideId, $suffix, $file)) {
    echo(json_encode(Array("result" => "0")));
  } else {
    echo(json_encode(Array("result" => "1")));
  }
}

function updateGuide() {

  $id = $_POST["id"];
  $name = $_POST["name"];
  $nationality = $_POST["nationality"];
  $language = $_POST["language"];
  $area = $_POST["area"];
  $keyword = $_POST["keyword"];
  $category = $_POST["category"];
  $message = $_POST["message"];
  $applicableNumber = $_POST["applicableNumber"];
  $fee = $_POST["fee"];
  $notes = $_POST["notes"];
  $schedules = $_POST["schedules"];
  $tours = $_POST["tours"];
  $stripeAccountId = $_POST["stripeAccountId"];
  $bankAccount = $_POST["bankAccount"];

  if (Guide::update($id, $name, $nationality, $language, $area, $keyword, $category, $message, $applicableNumber, $fee, $notes, $schedules, $tours, $stripeAccountId, $bankAccount)) {
    echo(json_encode(Array("result" => "0")));
  } else {
    echo(json_encode(Array("result" => "1")));
  }
}

function getGuide() {

  $guides = [];
  $guideList = Guide::readAll();

  foreach ($guideList as $guideData) {
    $guides[] = Array("id" => $guideData->id,
                      "email" => $guideData->email,
                      "name" => $guideData->name,
                      "nationality" => $guideData->nationality,
                      "language" => $guideData->language,
                      "area" => $guideData->area,
                      "keyword" => $guideData->keyword,
                      "category" => $guideData->category,
                      "message" => $guideData->message,
                      "applicableNumber" => $guideData->applicableNumber,
                      "fee" => $guideData->fee,
                      "notes" => $guideData->notes,
                      "loginDate" => $guideData->loginDate,
                      "schedules" => $guideData->schedules,
                      "tours" => $guideData->tours,
                      "stripeAccountId" => $guideData->stripeAccountId,
                      "bankAccount" => $guideData->bankAccount);
  }
  $ret = Array("result" => "0", "guides" => $guides);
  echo(json_encode($ret));
}

function createGuest() {

  $email = $_POST["email"];
  $name = $_POST["name"];
  $nationality = $_POST["nationality"];

  $guestId = Guest::create($email, $name, $nationality);
  if (is_null($guestId)) {
    echo(json_encode(Array("result" => "1")));
  } else {
    echo(json_encode(Array("result" => "0", "guestId" => $guestId)));
  }
}

function uploadGuestImage() {
  
  $guestId = $_POST["guestId"];
  $suffix = $_POST["suffix"];
  $file = $_FILES['image']['tmp_name'];
  if (Guest::uploadImage($guestId, $suffix, $file)) {
    echo(json_encode(Array("result" => "0")));
  } else {
    echo(json_encode(Array("result" => "1")));
  }
}

function updateGuest() {

  $id = $_POST["id"];
  $name = $_POST["name"];
  $nationality = $_POST["nationality"];
  $stripeCustomerId = $_POST["stripeCustomerId"];

  if (Guest::update($id, $name, $nationality, $stripeCustomerId)) {
    echo(json_encode(Array("result" => "0")));
  } else {
    echo(json_encode(Array("result" => "1")));
  }
}

function getGuest() {

  $guests = [];
  $guestList = Guest::readAll();
  foreach ($guestList as $guestData) {
    $guests[] = Array("id" => $guestData->id,
                      "email" => $guestData->email,
                      "name" => $guestData->name,
                      "nationality" => $guestData->nationality,
                      "stripeCustomerId" => $guestData->stripeCustomerId);
  }
  $ret = Array("result" => "0",
                "guests" => $guests);
  echo(json_encode($ret));
}

function uploadTourImage() {

  $tourId = $_POST["tourId"];
  $suffix = $_POST["suffix"];
  $file = $_FILES['image']['tmp_name'];
  if (Guide::uploadTourImage($tourId, $suffix, $file)) {
    echo(json_encode(Array("result" => "0")));
  } else {
    echo(json_encode(Array("result" => "1")));
  }
}

function login() {

  $id = $_POST["id"];
  if (Guide::login($id)) {
    echo(json_encode(Array("result" => "0")));
  } else {
    echo(json_encode(Array("result" => "1")));
  }
}

function createReserve() {

  $guestId = $_POST["guestId"];
  $guideId = $_POST["guideId"];
  $meetingPlace = $_POST["meetingPlace"];
  $day = $_POST["day"];
  $startTime = $_POST["startTime"];
  $endTime = $_POST["endTime"];

  if (Reserve::create($guestId, $guideId, $meetingPlace, $day, $startTime, $endTime)) {
    echo(json_encode(Array("result" => "0")));
  } else {
    echo(json_encode(Array("result" => "1")));
  }
}

function getReserve() {

  $data = [];
  $reserveList = Reserve::readAll();
  foreach ($reserveList as $reserveData) {
    $data[] = Array("id" => $reserveData->id,
                    "guestId" => $reserveData->guestId,
                    "guideId" => $reserveData->guideId,
                    "meetingPlace" => $reserveData->meetingPlace,
                    "day" => $reserveData->day,
                    "startTime" => $reserveData->startTime,
                    "endTime" => $reserveData->endTime,
                    "reserveDate" => $reserveData->reserveDate);
  }
  $ret = Array("result" => "0", "reserves" => $data);
  echo(json_encode($ret));
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
    $data[] = Array("reserveId" => $estimateData->reserveId,
                    "guestId" => $estimateData->guestId,
                    "guideId" => $estimateData->guideId,
                    "score" => $estimateData->score,
                    "comment" => $estimateData->comment);
  }
  $ret = Array("result" => "0", "estimates" => $data);
  echo(json_encode($ret));
}

function postEstimate() {

  $estimateData = new EstimateData();
  $estimateData->reserveId = $_POST["reserveId"];
  $estimateData->guestId = $_POST["guestId"];
  $estimateData->guideId = $_POST["guideId"];
  $estimateData->score = $_POST["score"];
  $estimateData->comment = $_POST["comment"];

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

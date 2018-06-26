<?php

class MessageData {
  public $messageId;
  public $senderId;
  public $receiverId;
  public $date;
  public $message;

  static function initFromFileString($line) {
    $datas = explode(",", $line);
    if (count($datas) == 5) {
      $messageData = new MessageData();
      $messageData->messageId = $datas[0];
      $messageData->senderId = $datas[1];
      $messageData->receiverId = $datas[2];
      $messageData->date = $datas[3];
      $messageData->message = $datas[4];
      return $messageData;
    }
    return null;
  }

  function toFileString() {
    return $this->messageId . ","
          . $this->senderId . ","
          . $this->receiverId . ","
          . $this->date . ","
          . $this->message . "\n";
  }
}

class Message {

  const DIRECTORY_NAME = "data/message/";

  static function listUpFile($userId) {

    $fileNames = [];
    foreach(glob(Message::DIRECTORY_NAME . "*", GLOB_BRACE) as $file){
      if (is_file($file)) {
        $removed1 = str_replace(".txt", "", $file);
        $removed2 = str_replace(Message::DIRECTORY_NAME, "", $removed1);
        $userIds = explode("-", $removed2);
        if (count($userIds) == 2) {
          if ((strcmp($userIds[0], $userId) == 0) || (strcmp($userIds[1], $userId) == 0)) {
            $fileNames[] = $file;
          }
        }
      }
    }
    return $fileNames;
  }

  static function readFile($fileName) {
    DebugSave($fileName);
    if (file_exists($fileName)) {
      $fileData = file_get_contents($fileName);
      DebugSave($fileData);
      if ($fileData !== false) {
        $messageList = [];
        $lines = explode("\n", $fileData);
        for ($i = 0; $i < count($lines); $i++) {
          DebugSave("for");
          $messageData = MessageData::initFromFileString($lines[$i]);
          if (!is_null($messageData)) {
            $messageList[] = $messageData;
          }
        }
        return $messageList;
      }
    }
    return [];
  }

  static function readMessage($userId) {
    $messages = [];
    $files = Message::listUpFile($userId);
    foreach ($files as $file) {
      $messages += Message::readFile($file);
    }
    return $messages;
  }

  static function append($messageData) {
    $fileName1 = Message::DIRECTORY_NAME . $messageData->senderId . "-" . $messageData->receiverId . ".txt";
    $fileName2 = Message::DIRECTORY_NAME . $messageData->receiverId . "-" . $messageData->senderId . ".txt";
    $fileName = "";
    if (file_exists($fileName1)) {
      $fileName = $fileName1;
    } else {
      $fileName = $fileName2;
    }
    file_put_contents($fileName, $messageData->toFileString(), FILE_APPEND);
  }
}

?>

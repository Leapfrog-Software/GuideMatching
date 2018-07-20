<?php

class GuestData {
  public $id;
  public $email;
  public $name;
  public $nationality;
  public $stripeCustomerId;

  static function initFromFileString($line) {
    $datas = explode(",", $line);
    if (count($datas) == 5) {
      $guestData = new GuestData();
      $guestData->id = $datas[0];
      $guestData->email = $datas[1];
      $guestData->name = $datas[2];
      $guestData->nationality = $datas[3];
      $guestData->stripeCustomerId = $datas[4];
      return $guestData;
    }
    return null;
  }

  function toFileString() {
    $str = "";
    $str .= $this->id;
    $str .= ",";
    $str .= $this->email;
    $str .= ",";
    $str .= $this->name;
    $str .= ",";
    $str .= $this->nationality;
    $str .= ",";
    $str .= $this->stripeCustomerId;
    $str .= "\n";
    return $str;
  }
}

class Guest {

  const FILE_NAME = "data/guest.txt";

  static function readAll() {
    if (file_exists(Guest::FILE_NAME)) {
      $fileData = file_get_contents(Guest::FILE_NAME);
      if ($fileData !== false) {
        $dataList = [];
        $lines = explode("\n", $fileData);
        for ($i = 0; $i < count($lines); $i++) {
          $data = GuestData::initFromFileString($lines[$i]);
          if (!is_null($data)) {
            $dataList[] = $data;
          }
        }
        return $dataList;
      }
    }
    return [];
  }

  static function create($email, $name, $nationality) {

    $maxGuestId = -1;

    $guestList = Guest::readAll();
    foreach ($guestList as $guestData) {
      $guestId = intval(substr($guestData->id, 6, strlen($guestData->id) - 6));
      if ($guestId > $maxGuestId) {
        $maxGuestId = $guestId;
      }
    }

    $nextGuestId = strval($maxGuestId + 1);

    $guestData = new GuestData();
    $guestData->id = "guest_" . $nextGuestId;
    $guestData->email = $email;
    $guestData->name = $name;
    $guestData->nationality = $nationality;

    if (file_put_contents(Guest::FILE_NAME, $guestData->toFileString(), FILE_APPEND) !== FALSE) {
      return $guestData->id;
    } else {
      return null;
    }
  }

  static function update($id, $name, $nationality, $stripeCustomerId) {

    $guestList = Guest::readAll();
    $find = false;

    foreach ($guestList as &$guestData) {
      if (strcmp($guestData->id, $id) == 0) {
        $newGuestData = new GuestData();
        $newGuestData->id = $id;
        $newGuestData->email = $guestData->email;
        $newGuestData->name = $name;
        $newGuestData->nationality = $nationality;
        $newGuestData->stripeCustomerId = $stripeCustomerId;
        $guestData = $newGuestData;

        $find = true;
        break;
      }
    }
    if (!$find) {
      return false;
    }

    $str = "";
    foreach($guestList as $data) {
      $str .= $data->toFileString();
    }
    return file_put_contents(Guest::FILE_NAME, $str) !== false;
  }
}

?>

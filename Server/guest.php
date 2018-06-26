<?php

class GuestData {
  public $id;
  public $name;
  public $nationality;

  static function initFromFileString($line) {
    $datas = explode(",", $line);
    if (count($datas) == 3) {
      $guestData = new GuestData();
      $guestData->id = $datas[0];
      $guestData->name = $datas[1];
      $guestData->nationality = $datas[2];
      return $guestData;
    }
    return null;
  }

  function toFileString() {
    $str = "";
    $str .= $this->id;
    $str .= ",";
    $str .= $this->name;
    $str .= ",";
    $str .= $this->nationality;
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

  static function create($name, $nationality) {

    $maxGuestId = -1;

    $guestList = Guest::readAll();
    foreach ($guestList as $guestData) {
      $guestId = intval($guestData->id);
      if ($guestId > $maxGuestId) {
        $maxGuestId = $guestId;
      }
    }

    $nextGuestId = strval($maxGuestId + 1);

    $guestData = new GuestData();
    $guestData->id = $nextGuestId;
    $guestData->name = $name;
    $guestData->nationality = $nationality;

    if (file_put_contents(Guest::FILE_NAME, $guestData->toFileString(), FILE_APPEND) !== FALSE) {
      return $nextGuestId;
    } else {
      return null;
    }
  }

  static function update($id, $name, $nationality) {

    $guestList = Guest::readAll();
    $find = false;

    foreach ($guestList as &$guestData) {
      if (strcmp($guestData->id, $id) == 0) {
        $guestData = new GuestData();
        $guestData->id = $id;
        $guestData->name = $name;
        $guestData->nationality = $nationality;

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

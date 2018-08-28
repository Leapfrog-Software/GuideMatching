<?php

class GuideData {
  public $id;
  public $email;
	public $name;
  public $nationality;
  public $language;
  public $area;
  public $keyword;
  public $category;
  public $message;
  public $applicableNumber;
  public $fee;
  public $notes;
  public $schedules;
  public $loginDate;
  public $stripeAccountId;
  public $bankAccount;

	static function initFromFileString($line) {
		$datas = explode(",", $line);
		if (count($datas) == 16) {
      $guideData = new GuideData();
      $guideData->id = $datas[0];
      $guideData->email = $datas[1];
			$guideData->name = $datas[2];
      $guideData->nationality = $datas[3];
      $guideData->language = $datas[4];
      $guideData->area = $datas[5];
      $guideData->keyword = $datas[6];
      $guideData->category = $datas[7];
      $guideData->message = $datas[8];
      $guideData->applicableNumber = $datas[9];
      $guideData->fee = $datas[10];
      $guideData->notes = $datas[11];
      $guideData->schedules = $datas[12];
      $guideData->loginDate = $datas[13];
      $guideData->stripeAccountId = $datas[14];
      $guideData->bankAccount = $datas[15];
			return $guideData;
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
    $str .= $this->language;
    $str .= ",";
    $str .= $this->area;
    $str .= ",";
    $str .= $this->keyword;
    $str .= ",";
    $str .= $this->category;
    $str .= ",";
    $str .= $this->message;
    $str .= ",";
    $str .= $this->applicableNumber;
    $str .= ",";
    $str .= $this->fee;
    $str .= ",";
    $str .= $this->notes;
    $str .= ",";
    $str .= $this->schedules;
    $str .= ",";
    $str .= $this->loginDate;
    $str .= ",";
    $str .= $this->stripeAccountId;
    $str .= ",";
    $str .= $this->bankAccount;
    $str .= "\n";
    return $str;
  }
}

class Guide {

  const FILE_NAME = "data/guide.txt";
  const IMAGE_DIRECTORY = "data/image/guide/";

	static function readAll() {
		if (file_exists(Guide::FILE_NAME)) {
			$fileData = file_get_contents(Guide::FILE_NAME);
			if ($fileData !== false) {
				$dataList = [];
				$lines = explode("\n", $fileData);
				for ($i = 0; $i < count($lines); $i++) {
					$data = GuideData::initFromFileString($lines[$i]);
					if (!is_null($data)) {
						$dataList[] = $data;
					}
				}
				return $dataList;
			}
		}
		return [];
	}

  static function create($email, $name, $nationality, $language, $area, $keyword, $category, $message, $applicableNumber, $fee, $notes) {

    $maxGuideId = -1;

    $guideList = Guide::readAll();
    foreach ($guideList as $guideData) {
      $guideId = intval(substr($guideData->id, 6, strlen($guideData->id) - 6));
      if ($guideId > $maxGuideId) {
        $maxGuideId = $guideId;
      }
    }

    $nextGuideId = strval($maxGuideId + 1);

    date_default_timezone_set('Asia/Tokyo');

    $guideData = new GuideData();
    $guideData->id = "guide_" . $nextGuideId;
    $guideData->email = $email;
    $guideData->name = $name;
    $guideData->nationality = $nationality;
    $guideData->language = $language;
    $guideData->area = $area;
    $guideData->keyword = $keyword;
    $guideData->category = $category;
    $guideData->message = $message;
    $guideData->applicableNumber = $applicableNumber;
    $guideData->fee = $fee;
    $guideData->notes = $notes;
    $guideData->schedules = "";
    $guideData->loginDate = date("YmdHis");
    $guideData->stripeAccountId = "";

    if (file_put_contents(Guide::FILE_NAME, $guideData->toFileString(), FILE_APPEND) !== FALSE) {
      return $guideData->id;
    } else {
      return null;
    }
  }

  static function update($id, $name, $nationality, $language, $area, $keyword, $category, $message, $applicableNumber, $fee, $notes, $schedules, $stripeAccountId, $bankAccount) {

    $guideList = Guide::readAll();
    $find = false;

    date_default_timezone_set('Asia/Tokyo');

    foreach ($guideList as &$guideData) {
      if (strcmp($guideData->id, $id) == 0) {
        $newGuideData = new GuideData();
        $newGuideData->id = $id;
        $newGuideData->email = $guideData->email;
        $newGuideData->name = $name;
        $newGuideData->nationality = $nationality;
        $newGuideData->language = $language;
        $newGuideData->area = $area;
        $newGuideData->keyword = $keyword;
        $newGuideData->category = $category;
        $newGuideData->message = $message;
        $newGuideData->applicableNumber = $applicableNumber;
        $newGuideData->fee = $fee;
        $newGuideData->notes = $notes;
        $newGuideData->schedules = $schedules;
        $newGuideData->loginDate = date("YmdHis");
        $newGuideData->stripeAccountId = $stripeAccountId;
        $newGuideData->bankAccount = $bankAccount;
        $guideData = $newGuideData;

        $find = true;
        break;
      }
    }
    if (!$find) {
      return false;
    }

    $str = "";
    foreach($guideList as $data) {
      $str .= $data->toFileString();
    }
    return file_put_contents(Guide::FILE_NAME, $str) !== false;
  }

  static function login() {

    $guideList = Guide::readAll();
    $find = false;

    foreach ($guideList as &$guideData) {
      if (strcmp($guideData->id, $id) == 0) {
        date_default_timezone_set('Asia/Tokyo');
        $guideData->loginDate = date("YmdHis");
        $find = true;
        break;
      }
    }
    if (!$find) {
      return false;
    }

    $str = "";
    foreach($guideList as $data) {
      $str .= $data->toFileString();
    }
    return file_put_contents(Guide::FILE_NAME, $str) !== false;
  }

  static function uploadImage($guideId, $suffix, $file) {

    $fileName = Guide::IMAGE_DIRECTORY . $guideId . "-" . $suffix;
    return move_uploaded_file($file, $fileName);
  }
}

?>

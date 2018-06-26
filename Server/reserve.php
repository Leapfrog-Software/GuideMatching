<?php

class ReserveData {
  public $id;
	public $requesterId;
  public $guideId;
  public $area;
  public $date;

	static function initFromFileString($line) {
		$datas = explode(",", $line);
		if (count($datas) == 5) {
      $reserveData = new ReserveData();
      $reserveData->id = $datas[0];
			$reserveData->requesterId = $datas[1];
      $reserveData->guideId = $datas[2];
      $reserveData->area = $datas[3];
      $reserveData->date = $datas[4];
			return $reserveData;
		}
		return null;
	}

  function toFileString() {
    $str = "";
    $str .= $this->id;
    $str .= ",";
    $str .= $this->requesterId;
    $str .= ",";
    $str .= $this->guideId;
    $str .= ",";
    $str .= $this->area;
    $str .= ",";
    $str .= $this->date;
    $str .= "\n";
    return $str;
  }
}

class Reserve {

  const FILE_NAME = "data/reserve.txt";

	static function readAll() {
		if (file_exists(Reserve::FILE_NAME)) {
			$fileData = file_get_contents(Reserve::FILE_NAME);
			if ($fileData !== false) {
				$reserveList = [];
				$lines = explode("\n", $fileData);
				for ($i = 0; $i < count($lines); $i++) {
					$reserveData = ReserveData::initFromFileString($lines[$i]);
					if (!is_null($reserveData)) {
						$reserveList[] = $reserveData;
					}
				}
				return $reserveList;
			}
		}
		return [];
	}

  static function append($reserveData) {

    $str = $reserveData->toFileString();
    return file_put_contents(Reserve::FILE_NAME, $str, FILE_APPEND) !== false;
  }
}

?>

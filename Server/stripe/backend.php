<?php

require('stripe-php-6.10.4/init.php');

\Stripe\Stripe::setApiKey("sk_test_LcKqhnYm6KRh4Jmk5ANNos0C");

$command = $_POST["command"];

if (strcmp($command, "createCustomer") == 0) {
  createCustomer();
} else if (strcmp($command, "createEphemeralKey") == 0) {
  createEphemeralKey();
} else if (strcmp($command, "createCharge") == 0) {
  createCharge();
} else {
  echo("unknown");
}

function createCustomer() {

  $email = $_POST["email"];
  if (!isValidEmail($email)) {
    echo(json_encode(Array("result" => "1")));
    return;
  }

  $customer = \Stripe\Customer::create(
    Array(
      "email" => $email
    )
  );
  $decoded = json_decode($customer->__toJSON());
  if (is_null($decoded)) {
    echo(json_encode(Array("result" => "2")));
    return;
  }
   
  $customerId = $decoded->id;
  if (is_null($customerId)) {
    echo(json_encode(Array("result" => "2")));
    return;
  }
  if (strlen($customerId) == 0) {
    echo(json_encode(Array("result" => "2")));
    return;
  }
  echo(json_encode(Array("result" => "0", "customerId" => $customerId)));
}

function createEphemeralKey() {

  $apiVersion = $_POST["apiVersion"];
  $customerId = $_POST["customerId"];

  if ((!isset($apiVersion)) || (!isset($customerId))) {
    echo(json_encode(Array("result" => "1")));
    return;
  }

  try {
    $key = \Stripe\EphemeralKey::create(
      Array("customer" => $customerId),
      Array("stripe_version" => $apiVersion)
    );
    echo(json_encode(Array("result" => "0", "key" => $key)));
  } catch (Exception $e) {
    echo(json_encode(Array("result" => "2")));
  }
}

function createCharge() {

  $customerId = $_POST["customerId"];
  $cardId = $_POST["cardId"];
  $amount = $_POST["amount"];  
  $applicationFee = $_POST["applicationFee"];
  $destination = $_POST["destination"];

  if ((!isset($customerId)) || (!isset($cardId)) || (!isset($destination))) {
    echo(json_encode(Array("result" => "1")));
    return;
  }
  $amountInt = intval($amount);
  $applicationFeeInt = intval($applicationFee);
  if (($amountInt == 0) || ($applicationFee == 0)) {
    echo(json_encode(Array("result" => "1")));
    return;
  }

  $charge = \Stripe\Charge::create(
    Array(
      "customer" => $customerId,
      "amount" => $amountInt,
      "currency" => "jpy",
      "source" => $cardId,
      "application_fee" => $applicationFeeInt,
      "destination" => Array(
        "account" => $destination
      )
    )
  );

  echo(json_encode(Array("result" => "0")));
}

function isValidEmail($email) {

  if (strlen($email) == 0) {
    return false;
  }
  $exploded = explode("@", $email);
  if (count($exploded) != 2) {
    return false;
  }
  if (strlen($exploded[0]) == 0) {
    return false;
  }
  if (count(explode(".", $exploded[1])) <= 1) {
    return false;
  }

  return true;
}

function DebugSave($str){

	date_default_timezone_set('Asia/Tokyo');

	$d = date('Y-m-d H:i:s');
	$s = $d . " " . $str . "\r\n";
	file_put_contents("debug.txt", $s, FILE_APPEND);
}

?>

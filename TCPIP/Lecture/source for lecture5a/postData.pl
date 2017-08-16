use LWP::UserAgent;

$url = "http://002-kangaroo.jamesooi.com/robot-postdata.php";

$name = "Ng Lu Lu";
$idOrPassport = "950131076622";
$car = "PMA311";

my $ua = LWP::UserAgent->new;
$ua->agent("UEEN3123Demo/1.1");

my $res = $ua->post(
	$url,
	[
		'name' => $name,
		'idOrPassport' => $idOrPassport,
		'car' => $car,
	],
);

if ($res->is_success) {
	print $res->content;
}
else {
	print $res->status_line, "\n";
}
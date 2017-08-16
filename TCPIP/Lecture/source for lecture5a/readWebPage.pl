# Init
my $num_args = $#ARGV + 1;
if ($num_args != 1) {
    print "\nUsage: readWebPage.pl url\n";
    exit;
}

my $url = $ARGV[0];

# Create a user agent object
use LWP::UserAgent;
my $ua = LWP::UserAgent->new;
$ua->agent("UEEN3123Demo/1.1");

# Create a request
my $req = HTTP::Request->new(GET => $url);

# Pass request to the user agent and get a response back
my $res = $ua->request($req);

# Check the outcome of the response
if ($res->is_success) {
	print $res->content;
}
else {
	print $res->status_line, "\n";
}
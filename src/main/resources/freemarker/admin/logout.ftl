<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<meta name="google-signin-client_id" content="${googleApiClientId}">
	<title>Logout</title>
</head>

<body>
	<div style="position: absolute; top: 50%; left: 50%; transform: translateX(-50%) translateY(-50%); font-family: sans-serif">Uz redzēšanos!</div>

	<script>
		function signOut() {
			gapi.load('auth2', function() {
				gapi.auth2.init().then(function() {
					gapi.auth2.getAuthInstance().signOut().then(function() {
						window.location.href = "${loginPath}";
					});
				});
			});
		}

	</script>
	<script src="https://apis.google.com/js/platform.js?onload=signOut" async defer></script>

</body>

</html>

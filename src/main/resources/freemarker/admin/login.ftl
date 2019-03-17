<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<meta name="google-signin-client_id" content="${googleApiClientId}">
	<title>Login</title>
</head>

<body>
	<div class="g-signin2" data-onsuccess="onSignIn" style="position: absolute; top: 50%; left: 50%; transform: translateX(-50%) translateY(-50%);"></div>

	<form method="post" name="form">
		<input type="hidden" name="token" />
	</form>

	<script>
		function onSignIn(user) {
			document.form.token.value = user.getAuthResponse().id_token;
			document.form.submit();
		}
	</script>
	<script src="https://apis.google.com/js/platform.js" async defer></script>

</body>

</html>

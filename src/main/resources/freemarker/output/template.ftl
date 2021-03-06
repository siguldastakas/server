<#macro html>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="main.css?ts=${.now?long?c}">
</head>

<body>

<header>
    <nav>
        <img src="logo-1x.png" srcset="logo-2x.png 2x">
    </nav>
</header>
<main class="width-limit">
<#nested/>
</main>
</body>
</html>
</#macro>

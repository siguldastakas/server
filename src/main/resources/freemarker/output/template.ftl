<#macro html>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="main.css">
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

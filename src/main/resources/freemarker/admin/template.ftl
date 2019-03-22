<#macro html>
<!DOCTYPE html>
<html>
<head>
    <title>Siguldas Takas</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="${context}/main.css">
</head>

<body>

<header>
    <nav class="width-limit">
        <img src="${context}/logo-1x.png" srcset="${context}/logo-2x.png 2x">
        <ul class="pull-right">
            <li>${user.name}</li>
            <li><a href="${logout}">Iziet</a></li>
        </ul>
    </nav>
</header>
<main class="width-limit">
<#nested/>
</main>
</body>
</html>
</#macro>

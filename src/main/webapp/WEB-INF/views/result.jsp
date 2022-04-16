<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Botted - Spot Reddit Bots</title>
	<meta name="description" content="botted app">
	<meta name="author" content="">
	<link rel="apple-touch-icon" sizes="180x180" href="/images/favicon/apple-touch-icon.png">
	<link rel="icon" type="image/png" sizes="32x32" href="/images/favicon/favicon-32x32.png">
	<link rel="icon" type="image/png" sizes="16x16" href="/images/favicon/favicon-16x16.png">
	<link rel="manifest" href="/images/favicon/site.webmanifest">
	<link rel="mask-icon" href="/images/favicon/safari-pinned-tab.svg" color="#5bbad5">
	<meta name="msapplication-TileColor" content="#da532c">
	<meta name="theme-color" content="#ffffff">
	<link rel="stylesheet" href="/css/stylesheet.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js" defer></script>
	<script src="/js/botted.js" defer></script>
</head>
<body>
	<div class="top"><img src="/images/botted.png" width="140px"></div>
	<div class="content"><img src="/images/bot.png" width="200px" align="center"><br>
	<form action="/" method="post">
        <input type="text" name="u" class="u" placeholder="user/comment/post/url"><br><br>
        <input type="submit" name="submit" class="submit" value="Bot or Not?"><br>
	</form>
	<div class="loading"><p th:utext="${isBot}"><br></div>
	<input type="button" name="showdata" class="showdata" value="show data"><br>
	</div>
	<div class="results">
        <table class="layout">
            <tr>
                <td width="33%" style="vertical-align: text-top;"><div class="result"><p th:utext="${user}"></div></td>
                <td width="33%" style="vertical-align: text-top;"><div class="result"><p th:utext="${comments}"></div></td>
                <td width="33%" style="vertical-align: text-top;"><div class="result"><p th:utext="${submissions}"></div></td>
                </tr>
        </table>
    </div>
</body>
</html>

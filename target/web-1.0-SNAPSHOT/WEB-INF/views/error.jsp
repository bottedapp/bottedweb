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
	<div class="top"><a href="https://botted.app"><img src="/images/bottedlogo-dark.png"></a>
			<div class="rand">
                <form action="/" method="post">
                    <input type="hidden" name="random" class="random" value="random">
                    <input type="submit" name="random" class="random" value="random">
                </form>
            </div>
		<div class="hamburger" id="hamburger_menu">
			<div class="line"></div>
			<div class="line"></div>
			<div class="line"></div>
		</div>
		<section class="menu_body" id="menubody">
			<div class="menu_body_item_wrapper">
				<ul class="menu_list">
					<li><a href="https://botted.app">Home</a></li>
					<li><a href="https://botted.app/about">About</a></li>
					<li><a href="http://github.com/bottedapp/botted">Github</a></li>
					<li><a href="mailto:bottedapp@gmail.com">Contact Us</a></li>
				</ul>
			</div>
		</section>
	</div>
	<div class="content">
	<div class="error"><span style="color:#d7dadc;font-size:20px;font-family:system-ui;" th:utext="${status}"></span>&nbsp;<span style="color:#d7dadc;font-size:20px;font-family:system-ui;" th:utext="${error}"></span></div>
	<img src="/images/botted.png">
		<br>
		<form action="/" method="post">
			<input type="text" name="u" class="u" placeholder="user/submission/comment">
			<br>
			<br>
			<input type="submit" name="submit" class="submit" value="Bot or Not?">
			<br>
			<br> </form>
	</div>
	</div>
	<div class="loading"></div>
	<div class="usage">
                <b>Usage:</b>
                <br>
                <b># username</b><br>
                <font color="#eb5528">&lt;username&gt;</font> | u/<font color="#eb5528">&lt;username&gt;</font> | reddit.com/u/<font color="#eb5528">&lt;username&gt;</font> | reddit.com/user/<font color="#eb5528">&lt;username&gt;</font><br>
                https://www.reddit.com/user/bottedapp<br>
                <br>
                <b># submission</b><br>
                reddit.com/r/<font color="#eb5528">&lt;subreddit&gt;</font>/comments/<font color="#eb5528">&lt;id&gt;</font>/<font color="#eb5528">&lt;title&gt;</font><br>
                https://www.reddit.com/r/test/comments/u8fq2o/comment/i5kycps/<br>
                <br>
                <b># comment</b><br>
                reddit.com/r/<font color="#eb5528">&lt;subreddit&gt;</font>/comments/<font color="#eb5528">&lt;id&gt;</font>/comment/<font color="#eb5528">&lt;id&gt;</font><br>
                https://www.reddit.com/r/test/comments/u8fq2o/comment/i5kycps/<br>
    </div>
</body>
</html>
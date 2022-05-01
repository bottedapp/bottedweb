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
	<link rel="mask-icon" href="/images/favicon/safari-pinned-tab.svg" color="#eb5528">
	<meta name="msapplication-TileColor" content="#eb5528">
	<meta name="theme-color" content="#1a1a1b">
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
	<div class="content"><img src="/images/botted.png"><br>
	<div class="about">
		<br>
		<h1>About</h1>
	    Botted is a Reddit API application that is written in Java and deployed using Spring Boot and Heroku. It was developed as a final project for Computer Science II: Object-Oriented Programming.
		<p>
		<h2>Developed by</h2>
		<a href="https://github.com/leemoriarity">github.com/leemoriarity</a><br>
		<a href="https://github.com/chrismben">github.com/chrismben</a><br>
		<a href="https://github.com/kalisparrow">github.com/kalisparrow</a>
		</div>
	</div>
</body>
</html>
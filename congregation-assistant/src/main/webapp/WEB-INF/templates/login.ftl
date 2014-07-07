<html>
<head>
    <title>Frameset Vaadin demo</title>
    <link rel="stylesheet" type="text/css" href="../static/css/login.css"/>
</head>
<body>
<div class="login">
	<div class="top clearfix">
	   <div class="logo">
	   </div>
	    <div class="title">Frameset demo</div>
	</div>
	<form action="../j_spring_security_check" method="post">
	<div class="form">
	    <input type="hidden" name="_csrf_token" value="{{ csrf_token }}" />
	    <div>
	    <label for="username">Login</label>
	    <input type="text" id="username" name="j_username" required="required" placeholder="login"/><br>
	    </div>
	    <div>
	    <label for="password">Hasło</label>
	    <input type="password" id="password" name="j_password" required="required" placeholder="hasło" />
	    </div>
		<div class="checkbox">
			<input type="checkbox" id="remember_me" name="_spring_security_remember_me" value="on" /> Zapamiętaj mnie
       </div>
       <div style="text-align: right;padding-right:40px;">
	    <input type="submit" id="_submit" name="_submit" value="Zaloguj" class="zaloguj"/>
	   </div>
	</div>
	</form>

        <#if isError?? && isError?string == "true">
                <div class="error">Nie znaleziono użytkownika</div>
                <#if Session.SPRING_SECURITY_LAST_EXCEPTION?? && Session.SPRING_SECURITY_LAST_EXCEPTION.message?has_content>
                <div>powód: ${Session.SPRING_SECURITY_LAST_EXCEPTION.message}</div>  
                </#if>
        </#if>
</div>
</body>
</html>
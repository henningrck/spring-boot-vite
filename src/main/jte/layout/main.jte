@import com.henningstorck.springbootvite.common.page.Page
@import gg.jte.Content

@param Page page
@param Content content

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Hello, World!</title>
    <meta charset="utf-8"/>
    @for(String stylesheet : page.stylesheets())
        <link rel="stylesheet" href="${stylesheet}"/>
    @endfor
    @for(String script : page.scripts())
        <script type="module" src="${script}"></script>
    @endfor
</head>
<body>
${content}
</body>
</html>

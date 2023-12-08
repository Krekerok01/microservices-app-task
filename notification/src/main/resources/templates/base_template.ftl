<#macro baseTemplateEn title="">
    <!DOCTYPE html>
    <html>
    <head>
        <link href='https://fonts.googleapis.com/css?family=Open Sans' rel='stylesheet'>
        <meta charset="utf-8">
        <title>Email Template</title>
        <style>
            body {
                font-family: 'Open Sans', sans-serif;
                font-size: 20px;
                line-height: 1.5;
                background-color: #f2f2f2;
                margin: 0;
                padding: 0;
            }

            h1 {
                font-size: 24px;
                font-weight: bold;
                text-align: center;
                margin-top: 30px;
                margin-bottom: 20px;
            }

            h2 {
                font-size: 20px;
                font-weight: bold;
                margin-top: 30px;
                margin-bottom: 10px;
            }

            ul {
                list-style: none;
                padding: 0;
                margin: 0;
            }

            li {
                margin-bottom: 10px;
                font-size: 18px;
            }

            .footer-text {
                margin-top: 50px;
                padding: 20px;
                background-color: #ffffff;
                text-align: left;
                font-size: 14px;
                color: #555555;
            }

            img {
                pointer-events: none;
            }
        </style>
    </head>
    <body>
    <table>
        <tr>
            <td>
                <table width="600" border="0" cellspacing="0" cellpadding="0" bgcolor="#ffffff">
                    <tr>
                        <td style="padding: 20px;">
                            <img src="https://cdn.websites.hibu.com/97df2096307b4f18a0f85bf1ed3dceb8/dms3rep/multi/blog-c341ba1a.png" alt="Logo" style="max-width: 100%; height: auto;">
                        </td>
                    </tr>
                    <tr>
                        <td style="padding: 20px;">
                            <#nested>
                            <p>Best regards, Blog-application team</p>
                        </td>
                    </tr>
                    <tr>
                        <td class="footer-text">
                            <p>Contact information:</p>
                            <p>Address: Minsk, Belarus</p>
                            <p>Phone: +375-25-333-55-99</p>
                            <p>Email: specificgroupmicroservicestask@gmail.com</p>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    </body>
    </html>
</#macro>

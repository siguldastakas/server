<#include 'template.ftl'>
<@html>
    <section>
        <h1>Rezultāti</h1>
        <table class="index">
            <tbody>
                <#list series as item>
                    <tr>
                        <td><a href="${item.path}/index.html">${item.name}</a></td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </section>
</@>
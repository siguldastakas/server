<#include 'template.ftl'>
<@html>
    <section>
        <h1>${name}</h1>
        <table class="index">
            <tbody>
                <#if overall>
                    <tr>
                        <td colspan="3"><a href="overall.html">Kopvērtējums</a></td>
                    </tr>
                </#if>
                <#list events as item>
                    <#if results[item?index]>
                        <tr>
                            <td><a href="${item.number}.html">${item.number}</a></td>
                            <td><a href="${item.number}.html">${item.date}</a></td>
                            <td><a href="${item.number}.html">${item.name}</a></td>
                        </tr>
                    <#else>
                        <tr>
                            <td>${item.number}</td>
                            <td>${item.date}</td>
                            <td>${item.name}</td>
                        </tr>
                    </#if>
                </#list>
            </tbody>
        </table>
    </section>
</@>
<#include '../template.ftl'>
<@html>
    <section>
        <h1>${series.name}</h1>
    </section>
    <section>
        <table>
            <thead>
                <tr>
                    <th class="number">Nr.</th>
                    <th class="left">Kārta</th>
                </tr>
            </thead>
            <tbody>
                <#if series.events??>
                    <#list series.events as item>
                        <tr>
                            <td class="number"><a href="${link}/${item.number}">${item.number}</a></td>
                            <td><a href="${link}/${item.number}">${item.name}</a></td>
                        </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
    </section>
    <section>
        <form action="${update}" method="post">
            <input type="submit" value="Ģenerēt lapu">
        </form>
    </section>
</@>
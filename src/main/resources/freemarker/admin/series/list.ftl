<#include '../template.ftl'>
<@html>
    <section>
        <table>
            <thead>
                <tr>
                    <th class="left">Seriāls</th>
                </tr>
            </thead>
            <tbody>
                <#list series as item>
                    <tr>
                        <td><a href="${link}/${item.path}">${item.name}</a></td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </section>
</@>
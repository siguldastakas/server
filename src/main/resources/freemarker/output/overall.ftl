<#include 'template.ftl'>
<@html>
    <section>
        <h3>${prefix}</h3>
        <h1>Kopvērtējums</h1>
        <#list classes as class>
            <section class="results">
                <h2>${class.name}</h2>
                <table>
                    <tbody>
                    <tr>
                        <th>&nbsp;</th>
                        <th class="name">Vārds, uzvārds</th>
                        <th class="club">Klubs</th>
                        <th class="points">Punkti</th>
                        <#list events as event><th class="points hidden-600">${event.number}. kārta</th></#list>
                    </tr>
                    <#list class.runners as runner>
                        <tr>
                            <td class="position"><#if runner.points gt 0>${runner.position}<#else>&nbsp;</#if></td>
                            <td class="name">${runner.name}</td>
                            <td class="club">${runner.club}</td>
                            <td class="points"><#if runner.points gt 0>${runner.points}<#else>&nbsp;</#if></td>
                            <#list runner.eventPoints as points>
                                <#if points??>
                                    <td class="points details hidden-600<#if !points.used> small</#if>">${points.points}</td>
                                <#else>
                                    <td class="hidden-600">&nbsp;</td>
                                </#if>
                            </#list>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </section>
        </#list>
    </section>
</@>